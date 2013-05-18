//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "ElevationData.hpp"

#include "Vector2I.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"

#include "BilinearInterpolator.hpp"

ElevationData::ElevationData(const Sector& sector,
                             const Vector2I& extent) :
_sector(sector),
_width(extent._x),
_height(extent._y),
_resolution(sector.getDeltaLatitude().div(extent._y),
            sector.getDeltaLongitude().div(extent._x)),
_interpolator(NULL)
{
}

ElevationData::~ElevationData() {
  if (_interpolator != NULL) {
    delete _interpolator;
  }
}

double ElevationData::getElevationAt(const Vector2I& position) const {
  return getElevationAt(position._x, position._y);
}

const Vector2I ElevationData::getExtent() const {
  return Vector2I(_width, _height);
}

Mesh* ElevationData::createMesh(const Ellipsoid* ellipsoid,
                                float verticalExaggeration,
                                const Geodetic3D& positionOffset,
                                float pointSize) const {

  const Vector3D minMaxAverageHeights = getMinMaxAverageHeights();
  const double minHeight     = minMaxAverageHeights._x;
  const double maxHeight     = minMaxAverageHeights._y;
  const double deltaHeight   = maxHeight - minHeight;
  const double averageHeight = minMaxAverageHeights._z;

  ILogger::instance()->logInfo("averageHeight=%f, minHeight=%f maxHeight=%f delta=%f",
                               averageHeight, minHeight, maxHeight, deltaHeight);


  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          ellipsoid,
                                          Vector3D::zero());
  FloatBufferBuilderFromColor colors;

  const IMathUtils* mu = IMathUtils::instance();
  for (int x = 0; x < _width; x++) {
    const double u = (double) x / (_width  - 1);

    for (int y = 0; y < _height; y++) {
      const double height = getElevationAt(x, y);
      if (mu->isNan(height)) {
        continue;
      }

      const float alpha = (float) ((height - minHeight) / deltaHeight);
      const float r = alpha;
      const float g = alpha;
      const float b = alpha;
      colors.add(r, g, b, 1);

      const double v = 1.0 - ( (double) y / (_height - 1) );

      const Geodetic2D position = _sector.getInnerPoint(u, v).add(positionOffset.asGeodetic2D());

      vertices.add(position,
                   positionOffset.height() + (height * verticalExaggeration));

    }
  }

  const float lineWidth = 1;
  Color* flatColor = NULL;

  return new DirectMesh(GLPrimitive::points(),
                        //GLPrimitive::lineStrip(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        pointSize,
                        flatColor,
                        colors.create());
}

Interpolator* ElevationData::getInterpolator() const {
  if (_interpolator == NULL) {
    _interpolator = new BilinearInterpolator();
  }
  return _interpolator;
}

double ElevationData::getElevationAt(const Angle& latitude,
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
      if (mu->isNan(valueNW)) {
        return nanD;
      }
      const double valueNE = getElevationAt(nextX, y);
      if (mu->isNan(valueNE)) {
        return nanD;
      }
      const double valueSE = getElevationAt(nextX, nextY);
      if (mu->isNan(valueSE)) {
        return nanD;
      }
      const double valueSW = getElevationAt(x, nextY);
      if (mu->isNan(valueSW)) {
        return nanD;
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
