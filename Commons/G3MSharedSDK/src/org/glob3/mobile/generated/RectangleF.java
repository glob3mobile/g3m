package org.glob3.mobile.generated; 
//
//  RectangleF.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 19/04/13.
//
//



public class RectangleF
{
  public final float _x;
  public final float _y;
  public final float _width;
  public final float _height;

  public RectangleF(float x, float y, float width, float height)
  {
     _x = x;
     _y = y;
     _width = width;
     _height = height;
  }

  public RectangleF(RectangleF that)
  {
     _x = that._x;
     _y = that._y;
     _width = that._width;
     _height = that._height;
  }

  public final boolean equalTo(RectangleF that)
  {
    return (_x == that._x) && (_y == that._y) && (_width == that._width) && (_height == that._height);
  }

}