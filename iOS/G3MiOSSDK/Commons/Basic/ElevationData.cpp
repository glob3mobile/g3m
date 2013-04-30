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


ElevationData::ElevationData(const Sector& sector,
                             const Vector2I& extent) :
_sector(sector),
_width(extent._x),
_height(extent._y)
{
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
  const double nanD = mu->NanD();
  for (int x = 0; x < _width; x++) {
    const double u = (double) x / (_width  - 1);

    for (int y = 0; y < _height; y++) {
      const double height = getElevationAt(x, y, nanD);
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
