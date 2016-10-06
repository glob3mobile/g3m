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
#include "ShortBufferTerrainElevationGrid.hpp"


short* BILParser::pvtParse(const int          size,
                           const IByteBuffer* buffer,
                           const short        noDataValue) {

  const int expectedSizeInBytes = size * 2;
  if (buffer->size() != expectedSizeInBytes) {
    ILogger::instance()->logError("Invalid buffer size, expected %d bytes, but got %d",
                                  expectedSizeInBytes,
                                  buffer->size());
    return NULL;
  }

  ByteBufferIterator iterator(buffer);

  const short minValue = IMathUtils::instance()->minInt16();

  short* result = new short[size];
  for (int i = 0; i < size; i++) {
    short height = iterator.nextInt16();

    if (height == -9999) {
      height = noDataValue;
    }
    else if (height == minValue) {
      height = noDataValue;
    }

    result[i] = height;
  }

  return result;
}


ShortBufferElevationData* BILParser::oldParseBIL16(const Sector&      sector,
                                                   const Vector2I&    extent,
                                                   const IByteBuffer* buffer,
                                                   const double       deltaHeight) {

  const int size = extent._x * extent._y;

  short* shortBuffer = pvtParse(size, buffer, ShortBufferElevationData::NO_DATA_VALUE);
  if (shortBuffer == NULL) {
    return NULL;
  }

  return new ShortBufferElevationData(sector,
                                      extent,
                                      shortBuffer,
                                      size,
                                      deltaHeight);
}

ShortBufferTerrainElevationGrid* BILParser::parseBIL16(const Sector&      sector,
                                                       const Vector2I&    extent,
                                                       const IByteBuffer* buffer,
                                                       const short        noDataValue,
                                                       const double       deltaHeight) {
  const int size = extent._x * extent._y;

  short* shortBuffer = pvtParse(size, buffer, noDataValue);
  if (shortBuffer == NULL) {
    return NULL;
  }

  return new ShortBufferTerrainElevationGrid(sector,
                                             extent,
                                             shortBuffer,
                                             size,
                                             deltaHeight,
                                             noDataValue);
}
