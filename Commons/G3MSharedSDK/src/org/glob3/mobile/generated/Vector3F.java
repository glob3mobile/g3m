package org.glob3.mobile.generated; 
//
//  Vector3F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//















public class Vector3F
{
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
   public float dot(Vector3F v)
   {
     return ((_x * v._x) + (_y * v._y) + (_z * v._z));
   }
   public double length()
   {
     return IMathUtils.instance().sqrt(squaredLength());
   }
   public double squaredLength()
   {
     return _x * _x + _y * _y + _z * _z;
   }
   public Vector3F normalized()
   {
     final double d = length();
     return new Vector3F((float)(_x / d), (float)(_y / d), (float)(_z / d));
   }
   public Vector3F sub(Vector3F that)
   {
     return new Vector3F(_x - that._x, _y - that._y, _z - that._z);
   }
   public Vector3F cross(Vector3F other)
   {
     return new Vector3F(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
   }
}