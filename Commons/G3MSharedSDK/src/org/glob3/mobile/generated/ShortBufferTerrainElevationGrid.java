package org.glob3.mobile.generated; 
//
//  ShortBufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  ShortBufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




public class ShortBufferTerrainElevationGrid extends BufferTerrainElevationGrid
{
  private short[] _buffer;
  private final short _noDataValue;

  protected final double getValueInBufferAt(int index)
  {
    final short value = _buffer[index];
    return (value == _noDataValue) ? java.lang.Double.NaN : value;
  }



  public ShortBufferTerrainElevationGrid(Sector sector,
                                         Vector2I extent,
                                         short[] buffer,
                                         int bufferSize,
                                         double deltaHeight,
                                         short noDataValue) {
    super(sector, extent, bufferSize, deltaHeight);
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

  public final Vector3D getMinMaxAverageElevations()
  {
    final IMathUtils mu = IMathUtils.instance();
    short minHeight = mu.maxInt16();
    short maxHeight = mu.minInt16();
    double sumHeight = 0.0;
  
    for (int i = 0; i < _bufferSize; i++)
    {
      short height = _buffer[i];
      if (height == _noDataValue)
      {
        continue;
      }
      height += _deltaHeight;
      if (height < minHeight)
      {
        minHeight = height;
      }
      if (height > maxHeight)
      {
        maxHeight = height;
      }
      sumHeight += height;
    }
  
    if (minHeight == mu.maxInt16())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minInt16())
    {
      maxHeight = 0;
    }
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_extent._x * _extent._y));
  }

}