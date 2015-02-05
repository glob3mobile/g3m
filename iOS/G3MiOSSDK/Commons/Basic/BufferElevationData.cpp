//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#include "BufferElevationData.hpp"

//#include "Vector2I.hpp"

BufferElevationData::BufferElevationData(const Sector& sector,
                                         const Vector2I& extent,
                                         const Sector& realSector,
                                         const Vector2I& realExtent,
                                         int bufferSize,
                                         double deltaHeight) :
ElevationData(sector, extent),
_bufferSize(bufferSize),
_deltaHeight(deltaHeight)
{

}

double BufferElevationData::getElevationAt(int x,
                                           int y) const {
  const int index = ((_height-1-y) * _width) + x;

//  if ( (index < 0) || (index >= _bufferSize) ) {
//    printf("break point on me\n");
//    return IMathUtils::instance()->NanD();
//  }

  return getValueInBufferAt( index ) + _deltaHeight;
}
