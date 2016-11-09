//
//  DEMGridUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/7/16.
//
//

#include "DEMGridUtils.hpp"

#include "Vector3D.hpp"
#include "DEMGrid.hpp"
#include "ILogger.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "IMathUtils.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Geodetic3D.hpp"
#include "ErrorHandling.hpp"


Mesh* DEMGridUtils::createDebugMesh(const DEMGrid* grid,
                                    const Planet* planet,
                                    float verticalExaggeration,
                                    const Geodetic3D& offset,
                                    float pointSize) {

  const Vector3D minMaxAverageElevations = grid->getMinMaxAverageElevations();
  const double minElevation     = minMaxAverageElevations._x;
  const double maxElevation     = minMaxAverageElevations._y;
  const double averageElevation = minMaxAverageElevations._z;
  const double deltaElevation   = maxElevation - minElevation;

  ILogger::instance()->logInfo("Elevations: average=%f, min=%f max=%f delta=%f",
                               averageElevation, minElevation, maxElevation, deltaElevation);

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  FloatBufferBuilderFromColor colors;

  const Vector2I extent = grid->getExtent();
  const Sector   sector = grid->getSector();

  for (int x = 0; x < extent._x; x++) {
    const double u = (double) x / (extent._x  - 1);
    const Angle longitude = sector.getInnerPointLongitude(u).add(offset._longitude);

    for (int y = 0; y < extent._y; y++) {
      const double elevation = grid->getElevationAt(x, y);
      if (ISNAN(elevation)) {
        continue;
      }

      const double v = 1.0 - ( (double) y / (extent._y - 1) );
      const Angle latitude = sector.getInnerPointLatitude(v).add(offset._latitude);

      const double height = (elevation + offset._height) * verticalExaggeration;

      vertices->add(latitude, longitude, height);

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

const DEMGrid* DEMGridUtils::bestGridFor(const DEMGrid*  grid,
                                         const Sector&   sector,
                                         const Vector2S& extent) {
  if (grid == NULL) {
    return NULL;
  }

  const Sector   gridSector = grid->getSector();
  const Vector2I gridExtent = grid->getExtent();

  if (gridSector.isEquals(sector) &&
      gridExtent.isEquals(extent)) {
    grid->_retain();
    return grid;
  }

  if (!gridSector.touchesWith(sector)) {
    return NULL;
  }

  THROW_EXCEPTION("Diego at work!");
}
