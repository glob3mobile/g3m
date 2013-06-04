package org.glob3.mobile.generated; 
//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

//
//  BufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//



public abstract class BufferElevationData extends ElevationData
{
  private final int _bufferSize;

  private final Geodetic2D _realResolution ;

  protected abstract double getValueInBufferAt(int index);

  public BufferElevationData(Sector sector, Vector2I extent, Sector realSector, Vector2I realExtent, int bufferSize)
  {
     super(sector, extent);
     _bufferSize = bufferSize;
     _realResolution = new Geodetic2D(realSector.getDeltaLatitude().div(realExtent._y), realSector.getDeltaLongitude().div(realExtent._x));
  
  }

  public void dispose()
  {

  }

  public final Geodetic2D getRealResolution()
  {
    return _realResolution;
  }

  public final double getElevationAt(int x, int y)
  {
    final int index = ((_height-1-y) * _width) + x;
    //const int index = ((_width-1-x) * _height) + y;
  
    if ((index < 0) || (index >= _bufferSize))
    {
      System.out.print("break point on me\n");
      return IMathUtils.instance().NanD();
    }
  
    return getValueInBufferAt(index);
  }

}