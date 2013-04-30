//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#include "BufferElevationData.hpp"

#include "BilinearInterpolator.hpp"
#include "Vector2I.hpp"

Interpolator* BufferElevationData::getInterpolator() const {
  if (_interpolator == NULL) {
    _interpolator = new BilinearInterpolator();
  }
  return _interpolator;
}

BufferElevationData::BufferElevationData(const Sector& sector,
                                         const Vector2I& extent,
                                         int bufferSize) :
ElevationData(sector, extent),
_bufferSize(bufferSize),
_interpolator(NULL),
_resolution(sector.getDeltaLatitude().div(extent._y),
            sector.getDeltaLongitude().div(extent._x))
{

}

BufferElevationData::~BufferElevationData() {
  delete _interpolator;
}

double BufferElevationData::getElevationAt(int x,
                                           int y,
                                           double valueForNoData) const {
  const int index = ((_height-1-y) * _width) + x;
  //  const int index = ((_width-1-x) * _height) + y;

  if ( (index < 0) || (index >= _bufferSize) ) {
    printf("break point on me\n");
    return valueForNoData;
  }

  const double elevation = getValueInBufferAt( index );
  if ( IMathUtils::instance()->isNan(elevation) ) {
    return valueForNoData;
  }
  else {
    return elevation;
  }
}


double BufferElevationData::getElevationAt(const Angle& latitude,
                                           const Angle& longitude,
                                           double valueForNoData) const {


  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return valueForNoData;
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


  double result;
  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      result = getElevationAt(x, y, valueForNoData);
    }
    else {
      // linear on Y
      const double heightY = getElevationAt(x, y, valueForNoData);
      if (mu->isNan(heightY)) {
        return valueForNoData;
      }

      const double heightNextY = getElevationAt(x, nextY, valueForNoData);
      if (mu->isNan(heightNextY)) {
        return valueForNoData;
      }

      result = mu->linearInterpolation(heightNextY, heightY, alphaY);
    }
  }
  else {
    if (y == dY) {
      // linear on X
      const double heightX = getElevationAt(x, y, valueForNoData);
      if (mu->isNan(heightX)) {
        return valueForNoData;
      }
      const double heightNextX = getElevationAt(nextX, y, valueForNoData);
      if (mu->isNan(heightNextX)) {
        return valueForNoData;
      }

      result = mu->linearInterpolation(heightX, heightNextX, alphaX);
    }
    else {
      // bilinear
      const double valueNW = getElevationAt(x, y, valueForNoData);
      if (mu->isNan(valueNW)){
        return valueForNoData;
      }
      const double valueNE = getElevationAt(nextX, y, valueForNoData);
      if (mu->isNan(valueNE)){
        return valueForNoData;
      }
      const double valueSE = getElevationAt(nextX, nextY, valueForNoData);
      if (mu->isNan(valueSE)){
        return valueForNoData;
      }
      const double valueSW = getElevationAt(x, nextY, valueForNoData);
      if (mu->isNan(valueSW)){
        return valueForNoData;
      }

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
