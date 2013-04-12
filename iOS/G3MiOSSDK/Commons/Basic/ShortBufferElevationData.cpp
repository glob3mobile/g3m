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
#include "Vector3D.hpp"

ShortBufferElevationData::ShortBufferElevationData(const Sector& sector,
                                                   const Vector2I& resolution,
                                                   short noDataValue,
                                                   IShortBuffer* buffer) :
BufferElevationData(sector, resolution, buffer->size()),
_buffer(buffer),
_noDataValue(noDataValue)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }
  
  int size = buffer->size();
  _hasNoData = false;
  for (int i = 0; i < size; i++) {
    if (buffer->get(i) == noDataValue){
      _hasNoData = true;
      break;
    }
  }
}

ShortBufferElevationData::~ShortBufferElevationData() {
  delete _buffer;
}

double ShortBufferElevationData::getValueInBufferAt(int index) const {
  short s = _buffer->get(index);
  if (s == _noDataValue){
    return IMathUtils::instance()->NanD();
  } else{
    return s;
  }
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

Vector3D ShortBufferElevationData::getMinMaxAverageHeights() const {
  const IMathUtils* mu = IMathUtils::instance();
  short minHeight = mu->maxInt16();
  short maxHeight = mu->minInt16();
  double sumHeight = 0.0;
  
  const int bufferSize = _buffer->size();
  for (int i = 0; i < bufferSize; i++) {
    const short height = _buffer->get(i);
    if (height != _noDataValue) {
      if (height < minHeight) {
        minHeight = height;
      }
      if (height > maxHeight) {
        maxHeight = height;
      }
      sumHeight += height;
    }
  }
  
  if (minHeight == mu->maxInt16()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minInt16()) {
    maxHeight = 0;
  }
  
  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_width * _height));
}
