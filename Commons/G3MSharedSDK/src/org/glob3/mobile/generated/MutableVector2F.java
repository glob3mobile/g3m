package org.glob3.mobile.generated; 
//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/14.
//
//

//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/14.
//
//


//class Vector2F;


public class MutableVector2F
{
  private float _x;
  private float _y;

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

  public final float x()
  {
    return _x;
  }

  public final float y()
  {
    return _y;
  }

  public final Vector2F asVector2F()
  {
    return new Vector2F(_x, _y);
  }
}