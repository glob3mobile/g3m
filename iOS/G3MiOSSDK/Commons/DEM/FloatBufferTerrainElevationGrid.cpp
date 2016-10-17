//
//  FloatBufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

#include "FloatBufferTerrainElevationGrid.hpp"


#include "ErrorHandling.hpp"


#ifdef C_CODE
FloatBufferTerrainElevationGrid::FloatBufferTerrainElevationGrid(const Sector& sector,
                                                                 const Vector2I& extent,
                                                                 float* buffer,
                                                                 int bufferSize,
                                                                 double deltaHeight) :
BufferTerrainElevationGrid(sector, extent, bufferSize, deltaHeight),
_buffer(buffer)
{
  if (_bufferSize != (_extent._x * _extent._y)) {
    THROW_EXCEPTION("Invalid bufferSize");
  }
}
#endif
#ifdef JAVA_CODE
public FloatBufferTerrainElevationGrid(Sector sector,
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

FloatBufferTerrainElevationGrid::~FloatBufferTerrainElevationGrid() {
  delete [] _buffer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

double FloatBufferTerrainElevationGrid::getValueInBufferAt(int index) const {
  return _buffer[index];
}

Vector3D FloatBufferTerrainElevationGrid::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  float minHeight = mu->maxFloat();
  float maxHeight = mu->minFloat();
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

  if (minHeight == mu->maxFloat()) {
    minHeight = 0;
  }
  if (maxHeight == mu->minFloat()) {
    maxHeight = 0;
  }

  return Vector3D(minHeight,
                  maxHeight,
                  sumHeight / (_extent._x * _extent._y));
}
