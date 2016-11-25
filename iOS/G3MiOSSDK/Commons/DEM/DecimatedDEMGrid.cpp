//
//  DecimatedDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

#include "DecimatedDEMGrid.hpp"

#include "Vector2S.hpp"
#include "IMathUtils.hpp"


DecimatedDEMGrid* DecimatedDEMGrid::create(const DEMGrid*  grid,
                                           const Vector2S& extent) {
  return new DecimatedDEMGrid(grid,
                              grid->getSector(),
                              Vector2I(extent._x, extent._y));
}

DecimatedDEMGrid::DecimatedDEMGrid(const DEMGrid*  grid,
                                   const Sector&   sector,
                                   const Vector2I& extent) :
DecoratorDEMGrid(grid, sector, extent)
{
}

double DecimatedDEMGrid::getElevationAt(int x, int y) const {
  const double ratioX = (double) _extent._x / _grid->getExtent()._x;
  const double ratioY = (double) _extent._y / _grid->getExtent()._y;

  const double u0 =  x    * (1.0 / ratioX);
  const double v0 =  y    * (1.0 / ratioY);
  const double u1 = (x+1) * (1.0 / ratioX);
  const double v1 = (y+1) * (1.0 / ratioY);

  return getElevationBoxAt(u0, v0,
                           u1, v1);
}

double DecimatedDEMGrid::getElevationBoxAt(double x0, double y0,
                                           double x1, double y1) const {
  const IMathUtils* mu = IMathUtils::instance();

  const double floorY0 = mu->floor(y0);
  const double ceilY1  = mu->ceil(y1);
  const double floorX0 = mu->floor(x0);
  const double ceilX1  = mu->ceil(x1);

  const int parentHeight = _grid->getExtent()._y;
  const int parentWidth  = _grid->getExtent()._x;

  if ((floorY0 < 0) || (ceilY1 > parentHeight)) {
    return 0;
  }
  if ((floorX0 < 0) || (ceilX1 > parentWidth)) {
    return 0;
  }

  double elevationSum = 0;
  double area = 0;

  const double maxX = parentWidth  - 1;
  const double maxY = parentHeight - 1;

  for (double y = floorY0; y <= ceilY1; y++) {
    double ysize = 1.0;
    if (y < y0) {
      ysize *= (1.0 - (y0-y));
    }
    if (y > y1) {
      ysize *= (1.0 - (y-y1));
    }

    const int yy = (int) mu->min(y, maxY);

    for (double x = floorX0; x <= ceilX1; x++) {
      const double elevation = _grid->getElevationAt((int) mu->min(x, maxX),
                                                     yy);

      if (!ISNAN(elevation)) {
        double size = ysize;
        if (x < x0) {
          size *= (1.0 - (x0-x));
        }
        if (x > x1) {
          size *= (1.0 - (x-x1));
        }

        elevationSum += elevation * size;
        area += size;
      }
    }
  }
  
  return elevationSum/area;
}
