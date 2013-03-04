//
//  FloatBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

#include "FloatBufferElevationData.hpp"

#include "IFloatBuffer.hpp"
#include "IStringBuilder.hpp"

FloatBufferElevationData::FloatBufferElevationData(const Sector& sector,
                                                   const Vector2I& resolution,
                                                   double noDataValue,
                                                   IFloatBuffer* buffer) :
BufferElevationData(sector, resolution, noDataValue, buffer->size()),
_buffer(buffer)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }
}

FloatBufferElevationData::~FloatBufferElevationData() {
  delete _buffer;
}

double FloatBufferElevationData::getValueInBufferAt(int index) const {
  return _buffer->get(index);
}

const std::string FloatBufferElevationData::description(bool detailed) const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(FloatBufferElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  int type;
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row, &type) );
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
