//
//  DEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "DEMGrid.hpp"

#include "Projection.hpp"


DEMGrid::DEMGrid(const Projection* projection,
                 const Sector&     sector,
                 const Vector2I&   extent) :
_projection(projection),
_sector(sector),
_extent(extent),
_resolution(sector._deltaLatitude.div(extent._y),
            sector._deltaLongitude.div(extent._x))
{
  _projection->_retain();
}

DEMGrid::~DEMGrid() {
  _projection->_release();
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
