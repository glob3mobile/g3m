package org.glob3.mobile.generated; 
//
//  BufferTerrainElevationGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  BufferTerrainElevationGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




public abstract class BufferTerrainElevationGrid extends TerrainElevationGrid
{
  protected final int _bufferSize;
  protected final double _deltaHeight;

  protected abstract double getValueInBufferAt(int index);

  public void dispose()
  {
    super.dispose();
  }

  protected BufferTerrainElevationGrid(Sector sector, Vector2I extent, int bufferSize, double deltaHeight)
  {
     super(sector, extent);
     _bufferSize = bufferSize;
     _deltaHeight = deltaHeight;
  
  }


  public final double getElevationAt(int x, int y)
  {
    final int index = ((_extent._y-1-y) * _extent._x) + x;
    return getValueInBufferAt(index) + _deltaHeight;
  }

}