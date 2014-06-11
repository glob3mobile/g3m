package org.glob3.mobile.generated; 
//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

//
//  Vector3F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//




public class Vector3F
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3F operator =(Vector3F that);

  public static Vector3F zero()
  {
    return new Vector3F(0, 0, 0);
  }

  public final float _x;
  public final float _y;
  public final float _z;

  public Vector3F(float x, float y, float z)
  {
     _x = x;
     _y = y;
     _z = z;
  }

  public Vector3F(Vector3F that)
  {
     _x = that._x;
     _y = that._y;
     _z = that._z;
  }

  public void dispose()
  {
  }


  public final float dot(Vector3F v)
  {
    return ((_x * v._x) + (_y * v._y) + (_z * v._z));
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y + _z * _z;
  }

  public final Vector3F normalized()
  {
    final double d = length();
    return new Vector3F((float)(_x / d), (float)(_y / d), (float)(_z / d));
  }


  public final Vector3F sub(Vector3F that)
  {
    return new Vector3F(_x - that._x, _y - that._y, _z - that._z);
  }

  public final Vector3F cross(Vector3F that)
  {
    return new Vector3F(_y * that._z - _z * that._y, _z * that._x - _x * that._z, _x * that._y - _y * that._x);
  }

  public final boolean isZero()
  {
    return ((_x == 0) && (_y == 0) && (_z == 0));
  }

  public final boolean isNan()
  {
    return ((_x != _x) || (_y != _y) || (_z != _z));
  }

}