//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "ElevationData.hpp"

#include "IFloatBuffer.hpp"
#include "Vector2I.hpp"
#include "IStringBuilder.hpp"

ElevationData::ElevationData(const Vector2I& extent,
                             IFloatBuffer* buffer):
_width(extent._x),
_height(extent._y),
_buffer(buffer)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }
}

float ElevationData::getElevationAt(int x, int y) const {
  return _buffer->get( (x * _width) + y );
//  return _buffer->get( (y * _height) + x );
}

Vector2I ElevationData::getExtent() const {
  return Vector2I(_width, _height);
}

const std::string ElevationData::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(ElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString("\n");
  for (int row = 0; row < _width; row++) {
    isb->addString("   ");
    for (int col = 0; col < _height; col++) {
      isb->addFloat( getElevationAt(col, row) );
      isb->addString(" ");
    }
    isb->addString("\n");
  }
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
