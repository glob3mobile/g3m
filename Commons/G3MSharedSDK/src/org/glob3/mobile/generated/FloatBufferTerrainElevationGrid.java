package org.glob3.mobile.generated; 
//
//  FloatBufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//

//
//  FloatBufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/16.
//
//




public class FloatBufferTerrainElevationGrid extends BufferTerrainElevationGrid
{
  private float[] _buffer;

  protected final double getValueInBufferAt(int index)
  {
    return _buffer[index];
  }


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
  
  public void dispose()
  {
    _buffer = null;
    super.dispose();
  }


  public final Vector3D getMinMaxAverageElevations()
  {
    final IMathUtils mu = IMathUtils.instance();
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
    double sumHeight = 0.0;
  
    for (int i = 0; i < _bufferSize; i++)
    {
      double height = _buffer[i];
      if ((height != height))
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
  
    if (minHeight == mu.maxDouble())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minDouble())
    {
      maxHeight = 0;
    }
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_extent._x * _extent._y));
  }

}