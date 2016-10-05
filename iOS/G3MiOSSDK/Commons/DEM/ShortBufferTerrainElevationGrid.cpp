//
//  ShortBufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#include "ShortBufferTerrainElevationGrid.hpp"

#include "ErrorHandling.hpp"


ShortBufferTerrainElevationGrid::ShortBufferTerrainElevationGrid(const Sector&   sector,
                                                                 const Vector2I& extent,
                                                                 short*          buffer,
                                                                 int             bufferSize,
                                                                 double          deltaHeight,
                                                                 short           noDataValue) :
BufferTerrainElevationGrid(sector, extent, bufferSize, deltaHeight),
_buffer(buffer),
_noDataValue(noDataValue)
{
  if (_bufferSize != (_extent._x * _extent._y)) {
    THROW_EXCEPTION("Invalid bufferSize");
  }
}

ShortBufferTerrainElevationGrid::~ShortBufferTerrainElevationGrid() {
  delete [] _buffer;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

double ShortBufferTerrainElevationGrid::getValueInBufferAt(int index) const {
  const short value = _buffer[index];
  return (value == _noDataValue) ? NAND : value;
}

Vector3D ShortBufferTerrainElevationGrid::getMinMaxAverageElevations() const {
  const IMathUtils* mu = IMathUtils::instance();
  short minHeight = mu->maxInt16();
  short maxHeight = mu->minInt16();
  double sumHeight = 0.0;

  for (size_t i = 0; i < _bufferSize; i++) {
    const short height = _buffer[i];
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
                  sumHeight / (_extent._x * _extent._y));
}
