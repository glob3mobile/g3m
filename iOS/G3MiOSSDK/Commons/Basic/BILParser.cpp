//
//  BILParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

#include "BILParser.hpp"

#include "IByteBuffer.hpp"
#include "ByteBufferIterator.hpp"
#include "IShortBuffer.hpp"
#include "ShortBufferElevationData.hpp"
#include "Vector2I.hpp"
#include "ShortBufferDEMGrid.hpp"



ShortBufferElevationData* BILParser::oldParseBIL16(const Sector&      sector,
                                                   const Vector2I&    extent,
                                                   const IByteBuffer* buffer,
                                                   const double       deltaHeight) {

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

    if ((height == -9999) || (height == minValue)) {
      height = ShortBufferElevationData::NO_DATA_VALUE;
    }

    shortBuffer[i] = height;
  }

  return new ShortBufferElevationData(sector,
                                      extent,
                                      shortBuffer,
                                      size,
                                      deltaHeight);
}

ShortBufferDEMGrid* BILParser::parseBIL16(const Sector&      sector,
                                          const Vector2I&    extent,
                                          const IByteBuffer* buffer,
                                          const short        noDataValue,
                                          const double       deltaHeight) {
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

    if ((height == -9999) || (height == minValue)) {
      height = noDataValue;
    }

    shortBuffer[i] = height;
  }

  return new ShortBufferDEMGrid(sector,
                                extent,
                                shortBuffer,
                                size,
                                deltaHeight,
                                noDataValue);
}
