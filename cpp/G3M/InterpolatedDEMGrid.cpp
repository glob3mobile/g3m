//
//  InterpolatedDEMGrid.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

#include "InterpolatedDEMGrid.hpp"

#include "Vector2S.hpp"
#include "IMathUtils.hpp"
#include "Vector2D.hpp"


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

  return ((alphaSW * valueSW) +
          (alphaSE * valueSE) +
          (alphaNE * valueNE) +
          (alphaNW * valueNW));
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
DecoratorDEMGrid(grid, sector, extent)
{

}

double InterpolatedDEMGrid::getElevation(int x, int y) const {
  const double u = (double) x / _extent._x;
  const double v = (double) y / _extent._y;

  return getElevationAt(_grid, u, v);
}

double InterpolatedDEMGrid::getElevationAt(const DEMGrid* grid,
                                           double u,
                                           double v) {
  if ((u < 0) || (u > 1) ||
      (v < 0) || (v > 1)) {
    return NAND;
  }

  const Vector2I gridExtent = grid->getExtent();
  const double dX = u * (gridExtent._x - 1);
  const double dY = v * (gridExtent._y - 1);

  const int x = (int) dX;
  const int y = (int) dY;
  const int nextX = x + 1;
  const int nextY = y + 1;
  const double alphaY = dY - y;
  const double alphaX = dX - x;

  if (x == dX) {
    if (y == dY) {
      // exact on grid point
      return grid->getElevation(x, y);
    }

    // linear on Y
    const double heightY     = grid->getElevation(x,     y);
    const double heightNextY = grid->getElevation(x, nextY);
    return linearInterpolation(heightY, heightNextY, alphaY);
  }

  if (y == dY) {
    // linear on X
    const double heightX     = grid->getElevation(    x, y);
    const double heightNextX = grid->getElevation(nextX, y);
    return linearInterpolation(heightX, heightNextX, alphaX);
  }

  // bilinear
  const double valueNW = grid->getElevation(    x,     y);
  const double valueNE = grid->getElevation(nextX,     y);
  const double valueSE = grid->getElevation(nextX, nextY);
  const double valueSW = grid->getElevation(    x, nextY);
  return bilinearInterpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
}

double InterpolatedDEMGrid::getElevation(const Angle& latitude,
                                         const Angle& longitude) const {
  // const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
  // const double u = uv._x;
  // const double v = 1.0 - uv._y;

  const double u = _sector.getUCoordinate(longitude);
  const double v = 1.0 - _sector.getVCoordinate(latitude);

  return getElevationAt(this,
                        u,
                        v);
}
