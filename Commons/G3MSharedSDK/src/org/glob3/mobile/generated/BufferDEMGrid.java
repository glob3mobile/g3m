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

  protected final Projection _projection;

  protected final int _bufferSize;
  protected final double _deltaHeight;

  protected BufferDEMGrid(Projection projection, Sector sector, Vector2I extent, int bufferSize, double deltaHeight)
  {
     super(sector, extent);
     _projection = projection;
     _bufferSize = bufferSize;
     _deltaHeight = deltaHeight;
    _projection._retain();
  }

  public void dispose()
  {
    _projection._release();
    super.dispose();
  }

  protected abstract double getValueInBufferAt(int index);



  public final Projection getProjection()
  {
    return _projection;
  }

  public final double getElevationAt(int x, int y)
  {
    final int index = ((_extent._y-1-y) * _extent._x) + x;
    return getValueInBufferAt(index) + _deltaHeight;
  }

}
