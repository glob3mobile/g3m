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
#include "Vector3D.hpp"

const float FloatBufferElevationData::NO_DATA_VALUE = NANF;


FloatBufferElevationData::FloatBufferElevationData(const Sector& sector,
                                                   const Vector2I& extent,
                                                   const Sector& realSector,
                                                   const Vector2I& realExtent,
                                                   IFloatBuffer* buffer,
                                                   double deltaHeight) :
BufferElevationData(sector, extent, realSector, realExtent, buffer->size(), deltaHeight),
_buffer(buffer)
{
  if (_buffer->size() != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }

  const int size = buffer->size();
  _hasNoData = false;
  for (int i = 0; i < size; i++) {
    if (buffer->get(i) == NO_DATA_VALUE) {
      _hasNoData = true;
      break;
    }
  }
}

FloatBufferElevationData::~FloatBufferElevationData() {
  delete _buffer;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

double FloatBufferElevationData::getValueInBufferAt(int index) const {
  const float value = _buffer->get(index);
  if (value == NO_DATA_VALUE) {
    return NAND;
  }
  return value;
}

const std::string FloatBufferElevationData::description(bool detailed) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(FloatBufferElevationData extent=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(" sector=");
  isb->addString( _sector.description() );
  if (detailed) {
    isb->addString("\n");
    for (int row = 0; row < _width; row++) {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++) {
        isb->addDouble( getElevationAt(col, row) );
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

Vector3D FloatBufferElevationData::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  float minHeight = mu->maxFloat();
  float maxHeight = mu->minFloat();
  double sumHeight = 0.0;

  const int bufferSize = _buffer->size();
  for (int i = 0; i < bufferSize; i++) {
    const float height = _buffer->get(i);
    if (height != NO_DATA_VALUE) {
      if (height < minHeight) {
        minHeight = height;
      }
      if (height > maxHeight) {
        maxHeight = height;
      }
      sumHeight += height;
    }
  }

  if (minHeight == mu->maxFloat()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minFloat()) {
    maxHeight = 0;
  }

  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_width * _height));
}
