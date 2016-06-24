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

public final <<<<<<< HEAD Vector2F add(Vector2F that)
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

//C++ TO JAVA CONVERTER TODO TASK: The following line could not be converted:
  MutableVector2F asMutableVector2F() const;
  {
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
    return MutableVector2F(_x, _y);
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
  ======= Vector2F Vector2F.clampLength(float min, float max) const
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
  >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
  }
  

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
======= const double squaredDistanceTo(float x, float y) const;


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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2F clampLength(float min, float max);

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
}

public final double Vector2F.squaredDistanceTo(float x, float y)
{
  final double dx = _x - x;
  final double dy = _y - y;
  return (dx * dx) + (dy * dy);
}