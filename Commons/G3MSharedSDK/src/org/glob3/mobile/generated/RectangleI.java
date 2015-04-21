package org.glob3.mobile.generated; 
//
//  RectangleI.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/21/12.
//
//



public class RectangleI
{
  public final int _x;
  public final int _y;
  public final int _width;
  public final int _height;

  public RectangleI(int x, int y, int width, int height)
  {
     _x = x;
     _y = y;
     _width = width;
     _height = height;
  }

  public RectangleI(RectangleI that)
  {
     _x = that._x;
     _y = that._y;
     _width = that._width;
     _height = that._height;
  }

  public void dispose()
  {
  }

  public final boolean equalTo(RectangleI that)
  {
    return (_x == that._x) && (_y == that._y) && (_width == that._width) && (_height == that._height);
  }

  public final boolean contains(int x, int y)
  {
    return (x >= _x) && (y >= _y) && (x <= (_x + _width)) && (y <= (_y + _height));
  }

}