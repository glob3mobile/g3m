//
//  ShortBufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "ShortBufferDEMGrid.hpp"

#include "ErrorHandling.hpp"
#include "IMathUtils.hpp"


#ifdef C_CODE
ShortBufferDEMGrid::ShortBufferDEMGrid(const Projection* projection,
                                       const Sector& sector,
                                       const Vector2I& extent,
                                       short* buffer,
                                       int bufferSize,
                                       double deltaHeight,
                                       short noDataValue) :
BufferDEMGrid(projection, sector, extent, bufferSize, deltaHeight),
_buffer(buffer),
_noDataValue(noDataValue)
{
  if (_bufferSize != (_extent._x * _extent._y)) {
    THROW_EXCEPTION("Invalid bufferSize");
  }
}
#endif
#ifdef JAVA_CODE
public ShortBufferDEMGrid(Projection projection,
                          Sector sector,
                          Vector2I extent,
                          short[] buffer,
                          int bufferSize,
                          double deltaHeight,
                          short noDataValue) {
  super(projection, sector, extent, bufferSize, deltaHeight);
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
