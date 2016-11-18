//
//  FloatBufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

#include "FloatBufferDEMGrid.hpp"

#include "ErrorHandling.hpp"
#include "IMathUtils.hpp"


#ifdef C_CODE
FloatBufferDEMGrid::FloatBufferDEMGrid(const Projection* projection,
                                       const Sector& sector,
                                       const Vector2I& extent,
                                       float* buffer,
                                       int bufferSize,
                                       double deltaHeight) :
BufferDEMGrid(projection, sector, extent, bufferSize, deltaHeight),
_buffer(buffer)
{
  if (_bufferSize != (_extent._x * _extent._y)) {
    THROW_EXCEPTION("Invalid bufferSize");
  }
}
#endif
#ifdef JAVA_CODE
public FloatBufferDEMGrid(Sector sector,
                          Vector2I extent,
                          float[] buffer,
                          int bufferSize,
                          double deltaHeight) {
  super(sector, extent, bufferSize, deltaHeight);
  _buffer = buffer;
  if (_bufferSize != (_extent._x * _extent._y)) {
    throw new RuntimeException("Invalid bufferSize");
  }
}
#endif

FloatBufferDEMGrid::~FloatBufferDEMGrid() {
  delete [] _buffer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

double FloatBufferDEMGrid::getValueInBufferAt(int index) const {
  return _buffer[index];
}
