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
#include "IFactory.hpp"
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
