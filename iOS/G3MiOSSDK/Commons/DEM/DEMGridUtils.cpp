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
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "IMathUtils.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Geodetic3D.hpp"
#include "SubsetDEMGrid.hpp"
#include "Vector2S.hpp"
#include "DecimatedDEMGrid.hpp"
#include "InterpolatedDEMGrid.hpp"
#include "Projection.hpp"


const Vector3D DEMGridUtils::getMinMaxAverageElevations(const DEMGrid* grid) {
  const IMathUtils* mu = IMathUtils::instance();
  double minElevation = mu->maxDouble();
  double maxElevation = mu->minDouble();
  double sumElevation = 0.0;

  const int width  = grid->getExtent()._x;
  const int height = grid->getExtent()._y;

  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      const double elevation = grid->getElevation(x, y);
      if (!ISNAN(elevation)) {
        if (elevation < minElevation) {
          minElevation = elevation;
        }
        if (elevation > maxElevation) {
          maxElevation = elevation;
        }
        sumElevation += elevation;
      }
    }
  }

  return Vector3D(minElevation,
                  maxElevation,
                  sumElevation / (width * height));
}

Mesh* DEMGridUtils::createDebugMesh(const DEMGrid* grid,
                                    const Planet* planet,
                                    float verticalExaggeration,
                                    const Geodetic3D& offset,
                                    const double minElevation,
                                    const double maxElevation,
                                    float pointSize) {
  const double deltaElevation = maxElevation - minElevation;

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  FloatBufferBuilderFromColor colors;

  const Projection* projection = grid->getProjection();
  const Vector2I extent = grid->getExtent();
  const Sector   sector = grid->getSector();

  for (int x = 0; x < extent._x; x++) {
    const double u = (double) x / (extent._x  - 1);
    const Angle longitude = projection->getInnerPointLongitude(sector, u).add(offset._longitude);

    for (int y = 0; y < extent._y; y++) {
      const double elevation = grid->getElevation(x, y);
      if (!ISNAN(elevation)) {
        const double v = 1.0 - ( (double) y / (extent._y - 1) );
        const Angle latitude = projection->getInnerPointLatitude(sector, v).add(offset._latitude);

        const double height = (elevation + offset._height) * verticalExaggeration;

        vertices->add(latitude, longitude, height);

        const float gray = (float) ((elevation - minElevation) / deltaElevation);
        colors.add(gray, gray, gray, 1);
      }
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
                                true                  // depthTest
                                );

  delete vertices;

  return result;
}


Mesh* DEMGridUtils::createDebugMesh(const DEMGrid* grid,
                                    const Planet* planet,
                                    float verticalExaggeration,
                                    const Geodetic3D& offset,
                                    float pointSize) {
  const Vector3D minMaxAverageElevations = getMinMaxAverageElevations(grid);
  const double minElevation = minMaxAverageElevations._x;
  const double maxElevation = minMaxAverageElevations._y;

  return createDebugMesh(grid,
                         planet,
                         verticalExaggeration,
                         offset,
                         minElevation,
                         maxElevation,
                         pointSize);
}

DEMGrid* DEMGridUtils::bestGridFor(DEMGrid*        grid,
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

  grid->_retain();
  DEMGrid* subsetGrid = SubsetDEMGrid::create(grid, sector);
  const Vector2I subsetGridExtent = subsetGrid->getExtent();
  if (subsetGridExtent.isEquals(extent)) {
    return subsetGrid;
  }
  else if ((subsetGridExtent._x > extent._x) || (subsetGridExtent._y > extent._y)) {
    return DecimatedDEMGrid::create(subsetGrid, extent);
  }
  else {
    return InterpolatedDEMGrid::create(subsetGrid, extent);
  }
}
