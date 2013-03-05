//
//  ShortBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#include "ShortBufferElevationData.hpp"

#include "IShortBuffer.hpp"
#include "IStringBuilder.hpp"

ShortBufferElevationData::ShortBufferElevationData(const Sector& sector,
                                                   const Vector2I& resolution,
                                                   double noDataValue,
                                                   IShortBuffer* buffer) :
BufferElevationData(sector, resolution, noDataValue, buffer->size()),
_buffer(buffer)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }
}

ShortBufferElevationData::~ShortBufferElevationData() {
  delete _buffer;
}

double ShortBufferElevationData::getValueInBufferAt(int index) const {
  return _buffer->get(index);
}

const std::string ShortBufferElevationData::description(bool detailed) const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(ShortBufferElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  int unusedType = -1;
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row, &unusedType) );
        isb->addString(",");
      }
      isb->addString("\n");
    }
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
