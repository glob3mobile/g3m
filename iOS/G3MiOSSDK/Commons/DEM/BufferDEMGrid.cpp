//
//  BufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "BufferDEMGrid.hpp"


BufferDEMGrid::~BufferDEMGrid() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

BufferDEMGrid::BufferDEMGrid(const Sector& sector,
                             const Vector2I& extent,
                             size_t bufferSize,
                             double deltaHeight) :
DEMGrid(sector, extent),
_bufferSize(bufferSize),
_deltaHeight(deltaHeight)
{

}

double BufferDEMGrid::getElevationAt(int x,
                                     int y) const {
  const int index = ((_extent._y-1-y) * _extent._x) + x;
  return getValueInBufferAt( index ) + _deltaHeight;
}
