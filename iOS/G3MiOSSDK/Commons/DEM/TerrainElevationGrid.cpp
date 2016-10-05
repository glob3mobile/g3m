//
//  DEM.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "TerrainElevationGrid.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"


TerrainElevationGrid::TerrainElevationGrid(const Sector&   sector,
                                           const Vector2I& extent) :
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x))
{
}

TerrainElevationGrid::~TerrainElevationGrid() {
#ifdef JAVA
  super.dispose();
#endif
}

const Vector2I TerrainElevationGrid::getExtent() const {
  return _extent;
}

const Geodetic2D TerrainElevationGrid::getResolution() const {
  return _resolution;
}

Mesh* TerrainElevationGrid::createMesh(const Planet* planet,
                                       float verticalExaggeration,
                                       const Geodetic3D& positionOffset,
                                       float pointSize) const {

  const Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
  const double minElevation     = minMaxAverageElevations._x;
  const double maxElevation     = minMaxAverageElevations._y;
  const double deltaElevation   = maxElevation - minElevation;
  const double averageElevation = minMaxAverageElevations._z;

  ILogger::instance()->logInfo("Elevations: average=%f, min=%f max=%f delta=%f",
                               averageElevation, minElevation, maxElevation, deltaElevation);

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  FloatBufferBuilderFromColor colors;

  for (int x = 0; x < _extent._x; x++) {
    const double u = (double) x / (_extent._x  - 1);
    const Angle longitude = _sector.getInnerPointLongitude(u).add(positionOffset._longitude);

    for (int y = 0; y < _extent._y; y++) {
      const double elevation = getElevationAt(x, y);
      if (ISNAN(elevation)) {
        continue;
      }

      const double v = 1.0 - ( (double) y / (_extent._y - 1) );
      const Angle latitude = _sector.getInnerPointLatitude(v).add(positionOffset._latitude);

      const double height = (elevation * verticalExaggeration) + positionOffset._height;

      vertices->add(longitude, latitude, height);

      const float gray = (float) ((elevation - minElevation) / deltaElevation);
      colors.add(gray, gray, gray, 1);
    }
  }

  Mesh* result = new DirectMesh(GLPrimitive::points(),
                                true,
                                vertices->getCenter(),
                                vertices->create(),
                                1,                    // lineWidth
                                pointSize,
                                NULL,                 // flatColor
                                colors.create(),
                                0,                    // colorsIntensity
                                false                 // depthTest
                                );

  delete vertices;
  
  return result;
}
