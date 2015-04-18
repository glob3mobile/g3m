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

const short ShortBufferElevationData::NO_DATA_VALUE = -32768;


ShortBufferElevationData::ShortBufferElevationData(const Sector& sector,
                                                   const Vector2I& extent,
                                                   const Sector& realSector,
                                                   const Vector2I& realExtent,
                                                   short* buffer,
                                                   int bufferSize,
                                                   double deltaHeight) :
BufferElevationData(sector, extent, realSector, realExtent, bufferSize, deltaHeight),
_buffer(buffer)
{
  if (_bufferSize != (_width * _height) ) {
    ILogger::instance()->logError("Invalid buffer size");
  }

  const int size = _bufferSize;
  _hasNoData = false;
  for (int i = 0; i < size; i++) {
    if (buffer[i] == NO_DATA_VALUE) {
      _hasNoData = true;
      break;
    }
  }
}

ShortBufferElevationData::~ShortBufferElevationData() {
  delete [] _buffer;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

double ShortBufferElevationData::getValueInBufferAt(int index) const {
  const short value = _buffer[index];
  if (value == NO_DATA_VALUE) {
    return NAND;
  }
  return value;
}

const std::string ShortBufferElevationData::description(bool detailed) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("(ShortBufferElevationData extent=");
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

Vector3D ShortBufferElevationData::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  short minHeight = mu->maxInt16();
  short maxHeight = mu->minInt16();
  double sumHeight = 0.0;

  const int bufferSize = _bufferSize;
  for (int i = 0; i < bufferSize; i++) {
    const short height = _buffer[i];
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
