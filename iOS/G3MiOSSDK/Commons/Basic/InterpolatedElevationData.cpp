//
//  InterpolatedElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/13.
//
//

#include "InterpolatedElevationData.hpp"

#include "Vector2I.hpp"
#include "Interpolator.hpp"

InterpolatedElevationData::InterpolatedElevationData(const ElevationData* elevationData,
                                                     bool deleteElevationData,
                                                     const Interpolator* interpolator,
                                                     bool deleteInterpolator) :
ElevationData( elevationData->getSector(), elevationData->getExtent() ),
_elevationData(elevationData),
_deleteElevationData(deleteElevationData),
_interpolator(interpolator),
_deleteInterpolator(deleteInterpolator)
{

}

InterpolatedElevationData::~InterpolatedElevationData() {
  if (_deleteElevationData) {
    delete _elevationData;
  }
  if (_deleteInterpolator) {
    delete _interpolator;
  }
}

double InterpolatedElevationData::getElevationAt(int x,
                                                 int y) const {
  return _elevationData->getElevationAt(x, y);
}

double InterpolatedElevationData::getElevationAt(const Angle& latitude,
                                                 const Angle& longitude) const {

  const IMathUtils* mu = IMathUtils::instance();

  const double nanD = mu->NanD();

  if (!_sector.contains(latitude, longitude)) {
    //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
    //                                  _sector.description().c_str(),
    //                                  latitude.description().c_str(),
    //                                  longitude.description().c_str());
    return nanD;
  }


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
      result = getElevationAt(x, y);
    }
    else {
      // linear on Y
      const double heightY = getElevationAt(x, y);
      if (mu->isNan(heightY)) {
        return nanD;
      }

      const double heightNextY = getElevationAt(x, nextY);
      if (mu->isNan(heightNextY)) {
        return nanD;
      }

      result = mu->linearInterpolation(heightNextY, heightY, alphaY);
    }
  }
  else {
    if (y == dY) {
      // linear on X
      const double heightX = getElevationAt(x, y);
      if (mu->isNan(heightX)) {
        return nanD;
      }
      const double heightNextX = getElevationAt(nextX, y);
      if (mu->isNan(heightNextX)) {
        return nanD;
      }

      result = mu->linearInterpolation(heightX, heightNextX, alphaX);
    }
    else {
      // bilinear
      const double valueNW = getElevationAt(x, y);
      if (mu->isNan(valueNW)){
        return nanD;
      }
      const double valueNE = getElevationAt(nextX, y);
      if (mu->isNan(valueNE)){
        return nanD;
      }
      const double valueSE = getElevationAt(nextX, nextY);
      if (mu->isNan(valueSE)){
        return nanD;
      }
      const double valueSW = getElevationAt(x, nextY);
      if (mu->isNan(valueSW)){
        return nanD;
      }

      result = _interpolator->interpolation(valueSW,
                                            valueSE,
                                            valueNE,
                                            valueNW,
                                            alphaX,
                                            alphaY);
    }
  }

  return result;
}
