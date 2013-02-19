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
#include "IFloatBuffer.hpp"
#include "IFactory.hpp"
#include "ElevationData.hpp"


ElevationData* BilParser::parseBil16(const IByteBuffer* buffer,
                                     const Vector2I& extent) {

  const int size = extent._x * extent._y;

  const int expectedSizeInBytes = size * 2;
  if (buffer->size() != expectedSizeInBytes) {
    ILogger::instance()->logError("Invalid buffer size, expected %d bytes, but got %d",
                                  expectedSizeInBytes,
                                  buffer->size());
    return NULL;
  }

  ByteBufferIterator iterator(buffer);

  IFloatBuffer* floatBuffer = IFactory::instance()->createFloatBuffer(size);
  for (int i = 0; i < size; i++) {
    short height = iterator.nextInt16();
    if (height == -9999) {
      height = 0;
    }
    //    if (height < 0) {
    //      height = 0;
    //    }
    floatBuffer->rawPut(i, (float) height);
  }

  return new ElevationData(extent,
                           floatBuffer);
}
