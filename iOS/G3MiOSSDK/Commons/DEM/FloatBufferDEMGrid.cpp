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
#include "Vector3D.hpp"


#ifdef C_CODE
FloatBufferDEMGrid::FloatBufferDEMGrid(const Sector& sector,
                                       const Vector2I& extent,
                                       float* buffer,
                                       int bufferSize,
                                       double deltaHeight) :
BufferDEMGrid(sector, extent, bufferSize, deltaHeight),
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

Vector3D FloatBufferDEMGrid::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  double minHeight = mu->maxDouble();
  double maxHeight = mu->minDouble();
  double sumHeight = 0.0;

  for (size_t i = 0; i < _bufferSize; i++) {
    double height = _buffer[i];
    if (ISNAN(height)) {
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

  if (minHeight == mu->maxDouble()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minDouble()) {
    maxHeight = 0;
  }

  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_extent._x * _extent._y));
}
