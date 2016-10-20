package org.glob3.mobile.generated; 
//
//  BufferDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//

//
//  BufferDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/5/16.
//
//




public abstract class BufferDEMGrid extends DEMGrid
{

  protected final int _bufferSize;
  protected final double _deltaHeight;

  protected BufferDEMGrid(Sector sector, Vector2I extent, int bufferSize, double deltaHeight)
  {
     super(sector, extent);
     _bufferSize = bufferSize;
     _deltaHeight = deltaHeight;
  
  }

  public void dispose()
  {
    super.dispose();
  }

  protected abstract double getValueInBufferAt(int index);



  public final double getElevationAt(int x, int y)
  {
    final int index = ((_extent._y-1-y) * _extent._x) + x;
    return getValueInBufferAt(index) + _deltaHeight;
  }

}