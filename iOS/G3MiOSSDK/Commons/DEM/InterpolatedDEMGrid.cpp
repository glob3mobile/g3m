//
//  InterpolatedDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

#include "InterpolatedDEMGrid.hpp"

#include "Vector2S.hpp"


#include "ErrorHandling.hpp"


const InterpolatedDEMGrid* InterpolatedDEMGrid::create(const DEMGrid*  grid,
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

double InterpolatedDEMGrid::getElevationAt(int x, int y) const {
//#error Diego at work!
  THROW_EXCEPTION("Diego at work!");
}
