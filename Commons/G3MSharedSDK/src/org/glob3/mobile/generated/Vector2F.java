package org.glob3.mobile.generated; 
//
//  Vector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

//
//  Vector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//



//class Vector2I;
//class MutableVector2F;

public class Vector2F
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2F operator =(Vector2F v);


  public static Vector2F zero()
  {
    return new Vector2F(0, 0);
  }

  public static Vector2F nan()
  {
    return new Vector2F(java.lang.Float.NaN, java.lang.Float.NaN);
  }

  public final float _x;
  public final float _y;


  public Vector2F(float x, float y)
  {
     _x = x;
     _y = y;
  }

  public Vector2F(Vector2F v)
  {
     _x = v._x;
     _y = v._y;
  }

  public void dispose()
  {
  }

  public final double squaredDistanceTo(Vector2I that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    return (dx * dx) + (dy * dy);
  }

  public final double squaredDistanceTo(Vector2F that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    return (dx * dx) + (dy * dy);
  }

  public final Vector2F add(Vector2F that)
  {
    return new Vector2F(_x + that._x, _y + that._y);
  }

  public final Vector2F sub(Vector2F that)
  {
    return new Vector2F(_x - that._x, _y - that._y);
  }

  public final Vector2F div(double d)
  {
    return new Vector2F((float)(_x / d), (float)(_y / d));
  }

  public final double squaredLength()
  {
    return (double) _x * _x + _y * _y;
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final MutableVector2F asMutableVector2F()
  {
    return new MutableVector2F(_x, _y);
  }

}