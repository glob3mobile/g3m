//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#include "BufferElevationData.hpp"

//
//  FloatBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "BufferElevationData.hpp"

#include "BilinearInterpolator.hpp"

Interpolator* BufferElevationData::getInterpolator() const {
  if (_interpolator == NULL) {
    _interpolator = new BilinearInterpolator();
  }
  return _interpolator;
}

BufferElevationData::BufferElevationData(const Sector& sector,
                                         const Vector2I& resolution,
                                         double noDataValue,
                                         int bufferSize) :
ElevationData(sector, resolution, noDataValue),
_bufferSize(bufferSize),
_interpolator(NULL)
{

}

BufferElevationData::~BufferElevationData() {
  delete _interpolator;
}

double BufferElevationData::getElevationAt(int x, int y) const {
  const int index = ((_height-1-y) * _width) + x;
  if ( (index < 0) || (index >= _bufferSize) ) {
    printf("break point on me\n");
    return _noDataValue;
  }
  return getValueInBufferAt( index );
}


double BufferElevationData::getElevationAt(const Angle& latitude,
                                           const Angle& longitude) const {


  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return _noDataValue;
  }

  IMathUtils* mu = IMathUtils::instance();

  const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
  const double u = mu->clamp(uv._x, 0, 1);
  const double v = mu->clamp(uv._y, 0, 1);
  const double dX = u * (_width - 1);
  const double dY = (1.0 - v) * (_height - 1);

  const int x = (int) dX;
  const int y = (int) dY;
  const int nextX = (int) (dX + 1.0);
  const int nextY = (int) (dY + 1.0);
  const double alphaY = dY - y;
  const double alphaX = dX - x;

  double result;
  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      result = getElevationAt(x, y);
    }
    else {
      // linear on Y
      const double heightY     = getElevationAt(x, y);
      const double heightNextY = getElevationAt(x, nextY);
      result = mu->lerp(heightY, heightNextY, alphaY);
    }
  }
  else {
    if (y == dY) {
      // linear on X
      const double heightX     = getElevationAt(x,     y);
      const double heightNextX = getElevationAt(nextX, y);
      result = mu->lerp(heightX, heightNextX, alphaX);
    }
    else {
      // bilinear
      const double valueSW = getElevationAt(x,     y);
      const double valueSE = getElevationAt(nextX, y);
      const double valueNE = getElevationAt(nextX, nextY);
      const double valueNW = getElevationAt(x,     nextY);

      result = getInterpolator()->interpolate(valueSW,
                                              valueSE,
                                              valueNE,
                                              valueNW,
                                              alphaY,
                                              alphaX);
    }
  }
  
  return result;
}
