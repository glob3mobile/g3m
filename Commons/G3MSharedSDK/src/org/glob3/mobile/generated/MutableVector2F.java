package org.glob3.mobile.generated; 
//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//

//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//


//class Vector2F;

public class MutableVector2F
{
  public float _x;
  public float _y;

  public MutableVector2F()
  {
     _x = 0F;
     _y = 0F;
  }

  public MutableVector2F(float x, float y)
  {
     _x = x;
     _y = y;
  }

  public MutableVector2F(MutableVector2F that)
  {
     _x = that._x;
     _y = that._y;
  }

  public MutableVector2F(Vector2F that)
  {
     _x = that._x;
     _y = that._y;
  
  }

  public final void set(float x, float y)
  {
    _x = x;
    _y = y;
  }

  public final void add(float x, float y)
  {
    _x += x;
    _y += y;
  }

  public final void times(float k)
  {
    _x *= k;
    _y *= k;
  }

  public final MutableVector2F copyFrom(MutableVector2F that)
  {
    _x = that._x;
    _y = that._y;
    return this;
  }

  public static MutableVector2F zero()
  {
    return new MutableVector2F(0, 0);
  }

  public static MutableVector2F nan()
  {
    return new MutableVector2F(java.lang.Float.NaN, java.lang.Float.NaN);
  }

  public final Vector2F asVector2F()
  {
    return new Vector2F(_x, _y);
  }

  public final boolean isNan()
  {
    return ((_x != _x) || (_y != _y));
  }

}