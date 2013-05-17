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
                             const Vector2I& resolution,
                             double noDataValue) :
_sector(sector),
_width(resolution._x),
_height(resolution._y),
_noDataValue(noDataValue)
{
}

Vector2I ElevationData::getExtent() const {
  return Vector2I(_width, _height);
}

Mesh* ElevationData::createMesh(const Planet* planet,
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
                                          planet,
                                          Vector3D::zero());
  FloatBufferBuilderFromColor colors;

  int type = -1;

  for (int x = 0; x < _width; x++) {
    const double u = (double) x / (_width  - 1);

    for (int y = 0; y < _height; y++) {
      const double height = getElevationAt(x, y, &type);

      const float alpha = (float) ((height - minHeight) / deltaHeight);

      float r = alpha;
      float g = alpha;
      float b = alpha;
      /*
      if (type == 1) {
        g = 1;
      }
      else if (type == 2) {
        r = 1;
        g = 1;
      }
      else if (type == 3) {
        r = 1;
        b = 1;
      }
      else if (type == 4) {
        r = 1;
      }
      */

      const double v = 1.0 - ( (double) y / (_height - 1) );

      const Geodetic2D position = _sector.getInnerPoint(u, v).add(positionOffset.asGeodetic2D());

      vertices.add(position,
                   positionOffset.height() + (height * verticalExaggeration));

      colors.add(r, g, b, 1);
    }
  }

  const float lineWidth = 1;
//  const float pointSize = 1;
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
