//
//  FloatBufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

#ifndef FloatBufferTerrainElevationGrid_hpp
#define FloatBufferTerrainElevationGrid_hpp

#include "BufferTerrainElevationGrid.hpp"


class FloatBufferTerrainElevationGrid : public BufferTerrainElevationGrid {
private:
  float* _buffer;

protected:
  double getValueInBufferAt(int index) const;

  virtual ~FloatBufferTerrainElevationGrid();

public:
#ifdef C_CODE
  FloatBufferTerrainElevationGrid(const Sector& sector,
                                  const Vector2I& extent,
                                  float* buffer,
                                  int bufferSize,
                                  double deltaHeight);
#endif

  Vector3D getMinMaxAverageElevations() const;
  
};


#endif
