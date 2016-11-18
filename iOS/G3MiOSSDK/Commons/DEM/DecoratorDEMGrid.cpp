//
//  DecoratorDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/16.
//
//

#include "DecoratorDEMGrid.hpp"


DecoratorDEMGrid::DecoratorDEMGrid(const DEMGrid*  grid,
                                   const Sector&   sector,
                                   const Vector2I& extent) :
DEMGrid(sector, extent),
_grid(grid)
{
  _grid->_retain();
}

DecoratorDEMGrid::~DecoratorDEMGrid() {
  _grid->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Projection* DecoratorDEMGrid::getProjection() const {
  return _grid->getProjection();
}
