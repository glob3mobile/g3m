//
//  ShortBufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef ShortBufferTerrainElevationGrid_hpp
#define ShortBufferTerrainElevationGrid_hpp

#include "BufferTerrainElevationGrid.hpp"


class ShortBufferTerrainElevationGrid : public BufferTerrainElevationGrid {
private:
  short*      _buffer;
  const short _noDataValue;

protected:
  double getValueInBufferAt(int index) const;

public:
  ShortBufferTerrainElevationGrid(const Sector&   sector,
                                  const Vector2I& extent,
                                  short*          buffer,
                                  int             bufferSize,
                                  double          deltaHeight,
                                  short           noDataValue);

  virtual ~ShortBufferTerrainElevationGrid();

  Vector3D getMinMaxAverageElevations() const;

};

#endif
