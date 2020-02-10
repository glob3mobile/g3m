//
//  DEMGrid.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "DEMGrid.hpp"


DEMGrid::DEMGrid(const Sector&   sector,
                 const Vector2I& extent) :
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x))
{
}

DEMGrid::~DEMGrid() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Sector DEMGrid::getSector() const {
  return _sector;
}

const Vector2I DEMGrid::getExtent() const {
  return _extent;
}

const Geodetic2D DEMGrid::getResolution() const {
  return _resolution;
}
