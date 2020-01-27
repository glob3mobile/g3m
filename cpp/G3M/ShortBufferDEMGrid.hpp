//
//  ShortBufferDEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

#ifndef ShortBufferDEMGrid_hpp
#define ShortBufferDEMGrid_hpp

#include "BufferDEMGrid.hpp"


class ShortBufferDEMGrid : public BufferDEMGrid {
private:
  short*      _buffer;
  const short _noDataValue;

protected:
  double getValueInBufferAt(int index) const;

  virtual ~ShortBufferDEMGrid();

public:
#ifdef C_CODE
  ShortBufferDEMGrid(const Projection* projection,
                     const Sector& sector,
                     const Vector2I& extent,
                     short* buffer,
                     int bufferSize,
                     double deltaHeight,
                     short noDataValue);
#endif

};

#endif
