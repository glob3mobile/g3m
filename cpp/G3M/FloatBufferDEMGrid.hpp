//
//  FloatBufferDEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

#ifndef FloatBufferDEMGrid_hpp
#define FloatBufferDEMGrid_hpp

#include "BufferDEMGrid.hpp"


class FloatBufferDEMGrid : public BufferDEMGrid {
private:
  float* _buffer;

protected:
  double getValueInBufferAt(int index) const;

  virtual ~FloatBufferDEMGrid();

public:
#ifdef C_CODE
  FloatBufferDEMGrid(const Projection* projection,
                     const Sector& sector,
                     const Vector2I& extent,
                     float* buffer,
                     int bufferSize,
                     double deltaHeight);
#endif

  Vector3D getMinMaxAverageElevations() const;

};

#endif
