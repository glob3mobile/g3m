//
//  BufferDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef BufferDEMGrid_hpp
#define BufferDEMGrid_hpp

#include "DEMGrid.hpp"


class BufferDEMGrid : public DEMGrid {
protected:
  
  const size_t _bufferSize;
  const double _deltaHeight;

  BufferDEMGrid(const Projection* projection,
                const Sector& sector,
                const Vector2I& extent,
                size_t bufferSize,
                double deltaHeight);

  virtual ~BufferDEMGrid();

  virtual double getValueInBufferAt(int index) const = 0;


public:

  double getElevationAt(int x, int y) const;

};

#endif
