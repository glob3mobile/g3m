//
//  ShortBufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "ShortBufferDEMGrid.hpp"

#include "ErrorHandling.hpp"


#ifdef C_CODE
ShortBufferDEMGrid::ShortBufferDEMGrid(const Sector& sector,
                                       const Vector2I& extent,
                                       short* buffer,
                                       int bufferSize,
                                       double deltaHeight,
                                       short noDataValue) :
BufferDEMGrid(sector, extent, bufferSize, deltaHeight),
_buffer(buffer),
_noDataValue(noDataValue)
{
  if (_bufferSize != (_extent._x * _extent._y)) {
    THROW_EXCEPTION("Invalid bufferSize");
  }
}
#endif
#ifdef JAVA_CODE
public ShortBufferDEMGrid(Sector sector,
                          Vector2I extent,
                          short[] buffer,
                          int bufferSize,
                          double deltaHeight,
                          short noDataValue) {
  super(sector, extent, bufferSize, deltaHeight);
  _buffer = buffer;
  _noDataValue = noDataValue;
  if (_bufferSize != (_extent._x * _extent._y)) {
    throw new RuntimeException("Invalid bufferSize");
  }
}
#endif

ShortBufferDEMGrid::~ShortBufferDEMGrid() {
  delete [] _buffer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

double ShortBufferDEMGrid::getValueInBufferAt(int index) const {
  const short value = _buffer[index];
  return (value == _noDataValue) ? NAND : value;
}

Vector3D ShortBufferDEMGrid::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  short minHeight = mu->maxInt16();
  short maxHeight = mu->minInt16();
  double sumHeight = 0.0;

  for (size_t i = 0; i < _bufferSize; i++) {
    short height = _buffer[i];
    if (height == _noDataValue) {
      continue;
    }
    height += _deltaHeight;
    if (height < minHeight) {
      minHeight = height;
    }
    if (height > maxHeight) {
      maxHeight = height;
    }
    sumHeight += height;
  }

  if (minHeight == mu->maxInt16()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minInt16()) {
    maxHeight = 0;
  }

  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_extent._x * _extent._y));
}
