//
//  BufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "BufferTerrainElevationGrid.hpp"


BufferTerrainElevationGrid::~BufferTerrainElevationGrid() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

BufferTerrainElevationGrid::BufferTerrainElevationGrid(const Sector& sector,
                                                       const Vector2I& extent,
                                                       size_t bufferSize,
                                                       double deltaHeight) :
TerrainElevationGrid(sector, extent),
_bufferSize(bufferSize),
_deltaHeight(deltaHeight)
{

}

double BufferTerrainElevationGrid::getElevationAt(int x,
                                                  int y) const {
  const int index = ((_extent._y-1-y) * _extent._x) + x;
  return getValueInBufferAt( index ) + _deltaHeight;
}
