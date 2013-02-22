//
//  FloatBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "FloatBufferElevationData.hpp"

#include "IFloatBuffer.hpp"
#include "IStringBuilder.hpp"
#include "BilinearInterpolator.hpp"

Interpolator* FloatBufferElevationData::getInterpolator() const {
  if (_interpolator == NULL) {
    _interpolator = new BilinearInterpolator();
  }
  return _interpolator;
}

FloatBufferElevationData::FloatBufferElevationData(const Sector& sector,
                                                   const Vector2I& resolution,
                                                   IFloatBuffer* buffer) :
ElevationData(sector, resolution),
_buffer(buffer),
_interpolator(NULL)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }

}

FloatBufferElevationData::~FloatBufferElevationData() {
  delete _buffer;
  delete _interpolator;
}

double FloatBufferElevationData::getElevationAt(int x, int y) const {
  //return _buffer->get( (x * _width) + y );
  //return _buffer->get( (x * _height) + y );

  //        const double height = elevationData->getElevationAt(x, extent._y-1-y);

  //  const int a = (_height-1-(y+_margin)) * _width;
  //  const int a1 = _height-1-(y+_margin);

  //  const int index = ((_height-1-(y+_margin)) * _width) + (x+_margin);
  const int index = ((_height-1-y) * _width) + x;
  if ((index < 0) ||
      (index >= _buffer->size())) {
    printf("break point on me\n");
  }
  return _buffer->get( index );
}

double FloatBufferElevationData::getElevationAt(const Angle& latitude,
                                                const Angle& longitude,
                                                int* type) const {

  IMathUtils* mu = IMathUtils::instance();

  if (!_sector.contains(latitude, longitude)) {
    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
                                  _sector.description().c_str(),
                                  latitude.description().c_str(),
                                  longitude.description().c_str());

//    return mu->NanD();
    return -5000;
  }

//  const double dX = (longitude.radians() - _sector.lower().longitude().radians()) / _stepInLongitudeRadians;
//  const double dY = (latitude.radians()  - _sector.lower().latitude().radians()) / _stepInLatitudeRadians;

  const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
  const double dX = uv._x * _width;
  const double dY = (1.0 - uv._y) * _height;

  const int x = (int) dX;
  const int y = (int) dY;

  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      *type = 1;
      return getElevationAt(x, y);
    }
    else {
      // linear on Y
      const int nextY = (int) (dY + 1.0);

      const double heightY     = getElevationAt(x, y);
      const double heightNextY = getElevationAt(x, nextY);
      const double alphaY = dY - y;
      *type = 2;
      return mu->lerp(heightY, heightNextY, alphaY);
    }
  }
  else {
    if (y == dY) {
      // linear on X
      const int nextX = (int) (dX + 1.0);
      const double heightX     = getElevationAt(x,     y);
      const double heightNextX = getElevationAt(nextX, y);
      const double alphaX = dX - x;
      *type = 3;
      return mu->lerp(heightX, heightNextX, alphaX);
    }
    else {
      // bilinear
      int _WORKING;
      const int nextX = (int) (dX + 1.0);
      const int nextY = (int) (dY + 1.0);

      const double valueSW = getElevationAt(x,     y);
      const double valueSE = getElevationAt(nextX, y);
      const double valueNE = getElevationAt(nextX, nextY);
      const double valueNW = getElevationAt(x,     nextY);

      const double alphaY = dY - y;
      const double alphaX = dX - x;

      *type = 4;
      return getInterpolator()->interpolate(valueSW,
                                            valueSE,
                                            valueNE,
                                            valueNW,
                                            alphaY,
                                            alphaX);
//      return 0;
    }
  }
  
  //  return IMathUtils::instance()->NanD();
}

const std::string FloatBufferElevationData::description(bool detailed) const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(FloatBufferElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row) );
        isb->addString(",");
      }
      isb->addString("\n");
    }
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
