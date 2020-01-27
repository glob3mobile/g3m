//
//  BufferElevationData.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#include "BufferElevationData.hpp"


BufferElevationData::BufferElevationData(const Sector& sector,
                                         const Vector2I& extent,
                                         size_t bufferSize,
                                         double deltaHeight) :
ElevationData(sector, extent),
_bufferSize(bufferSize),
_deltaHeight(deltaHeight)
{

}

double BufferElevationData::getElevationAt(int x,
                                           int y) const {
  const int index = ((_height-1-y) * _width) + x;

  return getValueInBufferAt( index ) + _deltaHeight;
}
