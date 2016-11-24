//
//  InterpolatedDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

#include "InterpolatedDEMGrid.hpp"

#include "Vector2S.hpp"
#include "IMathUtils.hpp"


double InterpolatedDEMGrid::linearInterpolation(double from,
                                                double to,
                                                double alpha) {
  return from + ((to - from) * alpha);
}

double InterpolatedDEMGrid::bilinearInterpolation(double valueSW,
                                                  double valueSE,
                                                  double valueNE,
                                                  double valueNW,
                                                  double u,
                                                  double v) {
  const double alphaSW = (1.0 - u) * v;
  const double alphaSE = u         * v;
  const double alphaNE = u         * (1.0 - v);
  const double alphaNW = (1.0 - u) * (1.0 - v);

  return (alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW);
}

InterpolatedDEMGrid* InterpolatedDEMGrid::create(const DEMGrid*  grid,
                                                 const Vector2S& extent) {
  return new InterpolatedDEMGrid(grid,
                                 grid->getSector(),
                                 Vector2I(extent._x, extent._y));
}

InterpolatedDEMGrid::InterpolatedDEMGrid(const DEMGrid*  grid,
                                         const Sector&   sector,
                                         const Vector2I& extent) :
DecoratorDEMGrid(grid, sector, extent),
_ratioX( (double) _extent._x / grid->getExtent()._x ),
_ratioY( (double) _extent._y / grid->getExtent()._y )
{

}

double InterpolatedDEMGrid::getElevationAt(int x, int y) const {
  const double u = (double) x / _extent._x;
  const double v = (double) y / _extent._y;
//  const double v = 1.0 - ((double) y / _extent._y);

  return getElevationAt(_grid, u, v);
}

double InterpolatedDEMGrid::getElevationAt(const DEMGrid* grid,
                                           double u,
                                           double v) {
  if ((u < 0) || (u > 1) || (v < 0) || (v > 1)) {
    return NAND;
  }

  const double dX = u * (grid->getExtent()._x - 1);
  const double dY = v * (grid->getExtent()._y - 1);
//  const double dY = (1.0 - v) * (grid->getExtent()._y - 1);

  const int x = (int) dX;
  const int y = (int) dY;
  const int nextX = x + 1;
  const int nextY = y + 1;
  const double alphaY = dY - y;
  const double alphaX = dX - x;

  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      return grid->getElevationAt(x, y);
    }

    // linear on Y
    const double heightY     = grid->getElevationAt(x,     y);
    const double heightNextY = grid->getElevationAt(x, nextY);
    return linearInterpolation(heightY, heightNextY, alphaY);
  }

  if (y == dY) {
    // linear on X
    const double heightX     = grid->getElevationAt(    x, y);
    const double heightNextX = grid->getElevationAt(nextX, y);
    return linearInterpolation(heightX, heightNextX, alphaX);
  }

  // bilinear
  const double valueNW = grid->getElevationAt(    x,     y);
  const double valueNE = grid->getElevationAt(nextX,     y);
  const double valueSE = grid->getElevationAt(nextX, nextY);
  const double valueSW = grid->getElevationAt(    x, nextY);
  return bilinearInterpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
}
