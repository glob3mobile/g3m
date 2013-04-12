//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
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
                                         int bufferSize) :
ElevationData(sector, resolution),
_bufferSize(bufferSize),
_interpolator(NULL)
{

}

BufferElevationData::~BufferElevationData() {
  delete _interpolator;
}

double BufferElevationData::getElevationAt(int x, int y,
                                           int *type) const {
  const int index = ((_height-1-y) * _width) + x;
//  const int index = ((_width-1-x) * _height) + y;

  if ( (index < 0) || (index >= _bufferSize) ) {
    printf("break point on me\n");
    *type = 0;
    return IMathUtils::instance()->NanD();
  }
  *type = 1;
  
  double d = getValueInBufferAt( index );
  return d;
}


double BufferElevationData::getElevationAt(const Angle& latitude,
                                           const Angle& longitude,
                                           int *type) const {


  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return IMathUtils::instance()->NanD();
  }

  const IMathUtils* mu = IMathUtils::instance();

  const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
  const double u = mu->clamp(uv._x, 0, 1);
  const double v = mu->clamp(uv._y, 0, 1);
  const double dX = u * (_width - 1);
  //const double dY = (1.0 - v) * (_height - 1);
  const double dY = v * (_height - 1);

  const int x = (int) dX;
  const int y = (int) dY;
//  const int nextX = (int) (dX + 1.0);
//  const int nextY = (int) (dY + 1.0);
  const int nextX = x + 1;
  const int nextY = y + 1;
  const double alphaY = dY - y;
  const double alphaX = dX - x;

//  if (alphaX < 0 || alphaX > 1 ||
//      alphaY < 0 || alphaY > 1) {
//    printf("break point\n");
//  }

  
  IMathUtils *m = IMathUtils::instance();
  int unsedType = -1;
  double result;
  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      result = getElevationAt(x, y, type);
    }
    else {
      
      *type = 2;
      // linear on Y
      const double heightY     = getElevationAt(x, y,     &unsedType);
      if (m->isNan(heightY)){return m->NanD();}
      const double heightNextY = getElevationAt(x, nextY, &unsedType);
      if (m->isNan(heightNextY)){return m->NanD();}
      
      if (m->isNan(heightY) || m->isNan(heightNextY)){
        return m->NanD();
      }
      
      result = mu->linearInterpolation(heightNextY, heightY, alphaY);
    }
  }
  else {
    if (y == dY) {
      
      *type = 3;
      // linear on X
      const double heightX     = getElevationAt(x,     y, &unsedType);
      if (m->isNan(heightX)){return m->NanD();}
      const double heightNextX = getElevationAt(nextX, y, &unsedType);
      if (m->isNan(heightNextX)){return m->NanD();}
      
      result = mu->linearInterpolation(heightX, heightNextX, alphaX);
    }
    else {
      
      *type = 4;
      // bilinear
      const double valueNW = getElevationAt(x,     y,     &unsedType);
      if (m->isNan(valueNW)){return m->NanD();}
      const double valueNE = getElevationAt(nextX, y,     &unsedType);
      if (m->isNan(valueNE)){return m->NanD();}
      const double valueSE = getElevationAt(nextX, nextY, &unsedType);
      if (m->isNan(valueSE)){return m->NanD();}
      const double valueSW = getElevationAt(x,     nextY, &unsedType);
      if (m->isNan(valueSW)){return m->NanD();}

      result = getInterpolator()->interpolation(valueSW,
                                                valueSE,
                                                valueNE,
                                                valueNW,
                                                alphaX,
                                                alphaY);
    }
  }

  return result;
}
