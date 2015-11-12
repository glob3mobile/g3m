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

  public final double squaredDistanceTo(float x, float y)
  {
    final double dx = _x - x;
    final double dy = _y - y;
    return (dx * dx) + (dy * dy);
  }


  public final Vector2F add(Vector2F v)
  {
    return new Vector2F(_x + v._x, _y + v._y);
  }

  public final boolean isNan()
  {
    return (_x != _x) || (_y != _y);
  }

  public final Vector2F sub(Vector2F v)
  {
    return new Vector2F(_x - v._x, _y - v._y);
  }

  public final Vector2F times(float magnitude)
  {
    return new Vector2F(_x * magnitude, _y * magnitude);
  }

  public final Vector2F div(float v)
  {
    return new Vector2F(_x / v, _y / v);
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y;
  }

  public final Vector2F clampLength(float min, float max)
  {
    float length = (float) this.length();
    if (length < min)
    {
      return this.times(min / length);
    }
    if (length > max)
    {
      return this.times(max / length);
    }
    return this;
  }

}