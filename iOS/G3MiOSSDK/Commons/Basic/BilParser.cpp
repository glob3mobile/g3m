//
//  BilParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#include "BilParser.hpp"

#include "IByteBuffer.hpp"
#include "ByteBufferIterator.hpp"
#include "IShortBuffer.hpp"
#include "ShortBufferElevationData.hpp"
#include "Vector2I.hpp"


ShortBufferElevationData* BilParser::parseBil16(const Sector& sector,
                                                const Vector2I& extent,
                                                const IByteBuffer* buffer,
                                                double deltaHeight) {

  const int size = extent._x * extent._y;

  const int expectedSizeInBytes = size * 2;
  if (buffer->size() != expectedSizeInBytes) {
    ILogger::instance()->logError("Invalid buffer size, expected %d bytes, but got %d",
                                  expectedSizeInBytes,
                                  buffer->size());
    return NULL;
  }

  ByteBufferIterator iterator(buffer);

  const short minValue = IMathUtils::instance()->minInt16();

  short* shortBuffer = new short[size];
  for (int i = 0; i < size; i++) {
    short height = iterator.nextInt16();

    if (height == -9999) {
      height = ShortBufferElevationData::NO_DATA_VALUE;
    }
    else if (height == minValue) {
      height = ShortBufferElevationData::NO_DATA_VALUE;
    }

    shortBuffer[i] = height;
  }

  return new ShortBufferElevationData(sector,
                                      extent,
                                      sector,
                                      extent,
                                      shortBuffer,
                                      size,
                                      deltaHeight);
}

ShortBufferElevationData* BilParser::parseBil16Redim (const Sector& sector, IByteBuffer *buffer, double deltaHeight)
{
#warning Chano at work here!
    
    ByteBufferIterator *iterator = new ByteBufferIterator(buffer);
    
    const short size = iterator->nextInt16();
    
    const int expectedSizeInBytes = (size * size * 2) + 10;
    if (buffer->size() != expectedSizeInBytes)
    {
        ILogger::instance()->logError("Invalid buffer size, expected %d bytes, but got %d", expectedSizeInBytes, buffer->size());
        return NULL;
    }
    
    const short minValue = IMathUtils::instance()->minInt16();
    
    short* shortBuffer = new short[size*size];
    for (int i = 0; i < size*size; i++)
    {
        short height = iterator->nextInt16();
        
        if (height == 15000) //Our own NODATA, since -9999 is a valid height.
        {
            height = ShortBufferElevationData::NO_DATA_VALUE;
        }
        else if (height == minValue)
        {
            height = ShortBufferElevationData::NO_DATA_VALUE;
        }
        
        shortBuffer[i] = height;
    }
    
    short max = iterator->nextInt16();
    short min = iterator->nextInt16();
    short children = iterator->nextInt16();
    short similarity = iterator->nextInt16();
    
    
    Vector2I extent = Vector2I(size,size);
    
    return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer,
                                        size*size, deltaHeight,max,min,children,similarity);
}
