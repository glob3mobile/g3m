package org.glob3.mobile.generated; 
//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//


//class IFloatBuffer;
//class Vector2I;


public class ElevationData
{
  private IFloatBuffer _buffer;
  private final int _width;
  private final int _height;



  public ElevationData(Vector2I extent, IFloatBuffer buffer)
  {
     _width = extent._x;
     _height = extent._y;
     _buffer = buffer;
    if (_buffer.size() != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  }

  public void dispose()
  {

  }


  public final float getElevationAt(int x, int y)
  {
    //return _buffer->get( (x * _width) + y );
    //return _buffer->get( (x * _height) + y );
  
    //        const double height = elevationData->getElevationAt(x, extent._y-1-y);
  
  //  const int a = (_height-1-(y+_margin)) * _width;
  //  const int a1 = _height-1-(y+_margin);
  
  //  const int index = ((_height-1-(y+_margin)) * _width) + (x+_margin);
    final int index = ((_height-1-y) * _width) + x;
    if ((index < 0) || (index >= _buffer.size()))
    {
      System.out.print("break point on me\n");
    }
    return _buffer.get(index);
  }

  public final Vector2I getExtent()
  {
    return new Vector2I(_width, _height);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(ElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString("\n");
    for (int row = 0; row < _width; row++)
    {
      //isb->addString("   ");
      for (int col = 0; col < _height; col++)
      {
        isb.addFloat(getElevationAt(col, row));
        isb.addString(",");
      }
      isb.addString("\n");
    }
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}