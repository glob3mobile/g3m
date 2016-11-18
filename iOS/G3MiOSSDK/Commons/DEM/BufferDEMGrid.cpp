//
//  BufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "BufferDEMGrid.hpp"

#include "Projection.hpp"


BufferDEMGrid::BufferDEMGrid(const Projection* projection,
                             const Sector& sector,
                             const Vector2I& extent,
                             size_t bufferSize,
                             double deltaHeight) :
DEMGrid(sector, extent),
_projection(projection),
_bufferSize(bufferSize),
_deltaHeight(deltaHeight)
{
  _projection->_retain();
}

BufferDEMGrid::~BufferDEMGrid() {
  _projection->_release();
#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Projection* BufferDEMGrid::getProjection() const {
  return _projection;
}

double BufferDEMGrid::getElevationAt(int x, int y) const {
  const int index = ((_extent._y-1-y) * _extent._x) + x;
  return getValueInBufferAt( index ) + _deltaHeight;
}
