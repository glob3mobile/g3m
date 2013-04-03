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

double BufferElevationData::getElevationAt(int x, int y,
                                           int *type) const {
  const int index = ((_height-1-y) * _width) + x;
//  const int index = ((_width-1-x) * _height) + y;

  if ( (index < 0) || (index >= _bufferSize) ) {
    printf("break point on me\n");
    *type = 0;
    return _noDataValue;
  }
  *type = 1;
  return getValueInBufferAt( index );
}


double BufferElevationData::getElevationAt(const Angle& latitude,
                                           const Angle& longitude,
                                           int *type) const {


  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return _noDataValue;
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

  int unsedType = -1;
  double result;
  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      result = getElevationAt(x, y, type);
    }
    else {
      // linear on Y
      const double heightY     = getElevationAt(x, y,     &unsedType);
      const double heightNextY = getElevationAt(x, nextY, &unsedType);
      *type = 2;
      result = mu->linearInterpolation(heightNextY, heightY, alphaY);
    }
  }
  else {
    if (y == dY) {
      // linear on X
      const double heightX     = getElevationAt(x,     y, &unsedType);
      const double heightNextX = getElevationAt(nextX, y, &unsedType);
      *type = 3;
      result = mu->linearInterpolation(heightX, heightNextX, alphaX);
    }
    else {
      // bilinear
      const double valueNW = getElevationAt(x,     y,     &unsedType);
      const double valueNE = getElevationAt(nextX, y,     &unsedType);
      const double valueSE = getElevationAt(nextX, nextY, &unsedType);
      const double valueSW = getElevationAt(x,     nextY, &unsedType);

      *type = 4;
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
