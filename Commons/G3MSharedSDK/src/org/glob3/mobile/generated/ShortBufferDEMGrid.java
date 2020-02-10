package org.glob3.mobile.generated;
//
//  ShortBufferDEMGrid.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  ShortBufferDEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




public class ShortBufferDEMGrid extends BufferDEMGrid
{
  private short[] _buffer;
  private final short _noDataValue;

  protected final double getValueInBufferAt(int index)
  {
    final short value = _buffer[index];
    return (value == _noDataValue) ? Double.NaN : value;
  }


  public ShortBufferDEMGrid(Projection projection,
                            Sector sector,
                            Vector2I extent,
                            short[] buffer,
                            int bufferSize,
                            double deltaHeight,
                            short noDataValue) {
    super(projection, sector, extent, bufferSize, deltaHeight);
    _buffer = buffer;
    _noDataValue = noDataValue;
    if (_bufferSize != (_extent._x * _extent._y)) {
      throw new RuntimeException("Invalid bufferSize");
    }
  }
  
  public void dispose()
  {
    _buffer = null;
    super.dispose();
  }


}
