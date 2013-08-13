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

  public final float _x;
  public final float _y;
  public final float _z;

  public Vector3F(float x, float y, float z)
  {
     _x = x;
     _y = y;
     _z = z;

  }

  public void dispose()
  {
  super.dispose();

  }

  public Vector3F(Vector3F v)
  {
     _x = v._x;
     _y = v._y;
     _z = v._z;

  }

  public final float dot(Vector3D v)
  {
    return ((_x * (float) v._x) + (_y * (float) v._y) + (_z * (float) v._z));
  }

  public final float dot(Vector3F v)
  {
    return ((_x * v._x) + (_y * v._y) + (_z * v._z));
  }

  public final Vector3F normalized()
  {
    final double d = length();
    return new Vector3F((float)(_x / d), (float)(_y / d), (float)(_z / d));
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y + _z * _z;
  }

}