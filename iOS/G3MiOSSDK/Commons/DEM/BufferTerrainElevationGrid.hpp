//
//  BufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef BufferTerrainElevationGrid_hpp
#define BufferTerrainElevationGrid_hpp

#include "TerrainElevationGrid.hpp"


class BufferTerrainElevationGrid : public TerrainElevationGrid {
protected:
  const size_t _bufferSize;
  const double _deltaHeight;

  virtual double getValueInBufferAt(int index) const = 0;

  virtual ~BufferTerrainElevationGrid();

public:
  BufferTerrainElevationGrid(const Sector& sector,
                             const Vector2I& extent,
                             size_t bufferSize,
                             double deltaHeight);

  double getElevationAt(int x,
                        int y) const;
  
  
};

#endif
