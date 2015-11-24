package org.glob3.mobile.generated; 
//
//  MutableVector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  MutableVector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//class Vector3D;

public class MutableVector3D
{
  private double _x;
  private double _y;
  private double _z;

//  MutableVector3D& operator=(const MutableVector3D& that);



  public MutableVector3D()
  {
     _x = 0;
     _y = 0;
     _z = 0;
  }

  public MutableVector3D(double x, double y, double z)
  {
     _x = x;
     _y = y;
     _z = z;
  }

  public MutableVector3D(MutableVector3D v)
  {
     _x = v._x;
     _y = v._y;
     _z = v._z;
  }

  public MutableVector3D(Vector3D v)
  {
     _x = v._x;
     _y = v._y;
     _z = v._z;
  }

  public final void set(double x, double y, double z)
  {
    _x = x;
    _y = y;
    _z = z;
  }

  public final void copyFrom(MutableVector3D that)
  {
    _x = that._x;
    _y = that._y;
    _z = that._z;
  }

  public final void copyFrom(Vector3D that)
  {
    _x = that._x;
    _y = that._y;
    _z = that._z;
  }

  public final MutableVector3D normalized()
  {
    final double d = length();
    return new MutableVector3D(_x / d, _y /d, _z / d);
  }
  public final void normalize()
  {
      final double d = length();
      _x /= d;
      _y /= d;
      _z /= d;
  }

  public static MutableVector3D nan()
  {
    return new MutableVector3D(java.lang.Double.NaN, java.lang.Double.NaN, java.lang.Double.NaN);
  }

  public final boolean equalTo(MutableVector3D v)
  {
    return (v._x == _x && v._y == _y && v._z == _z);
  }

  public final boolean isNan()
  {
    return ((_x != _x) || (_y != _y) || (_z != _z));
  }

  public final boolean isZero()
  {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y + _z * _z;
  }

  public final double dot(MutableVector3D v)
  {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  public final MutableVector3D add(MutableVector3D v)
  {
    return new MutableVector3D(_x + v._x, _y + v._y, _z + v._z);
  }

  public final void addInPlace(MutableVector3D that)
  {
    _x += that._x;
    _y += that._y;
    _z += that._z;
  }

  public final void addInPlace(Vector3D that)
  {
    _x += that._x;
    _y += that._y;
    _z += that._z;
  }

  public final MutableVector3D sub(MutableVector3D v)
  {
    return new MutableVector3D(_x - v._x, _y - v._y, _z - v._z);
  }

  public final Vector3D sub(Vector3D v)
  {
    return new Vector3D(_x - v._x, _y - v._y, _z - v._z);
  }

  public final MutableVector3D times(MutableVector3D v)
  {
    return new MutableVector3D(_x * v._x, _y * v._y, _z * v._z);
  }

  public final MutableVector3D times(Vector3D v)
  {
    return new MutableVector3D(_x * v._x, _y * v._y, _z * v._z);
  }

  public final MutableVector3D times(double magnitude)
  {
    return new MutableVector3D(_x * magnitude, _y * magnitude, _z * magnitude);
  }

  public final MutableVector3D div(MutableVector3D v)
  {
    return new MutableVector3D(_x / v._x, _y / v._y, _z / v._z);
  }

  public final MutableVector3D div(double v)
  {
    return new MutableVector3D(_x / v, _y / v, _z / v);
  }

  public final MutableVector3D cross(MutableVector3D other)
  {
    return new MutableVector3D(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
  }

  public final Angle angleBetween(MutableVector3D other)
  {
    final MutableVector3D v1 = normalized();
    final MutableVector3D v2 = other.normalized();
  
    double c = v1.dot(v2);
    if (c > 1.0)
       c = 1.0;
    else if (c < -1.0)
       c = -1.0;
  
    return Angle.fromRadians(IMathUtils.instance().acos(c));
  }

  public final MutableVector3D rotatedAroundAxis(MutableVector3D axis, Angle theta)
  {
    final double u = axis.x();
    final double v = axis.y();
    final double w = axis.z();
  
  //  const double cosTheta = theta.cosinus();
  //  const double sinTheta = theta.sinus();
    final double cosTheta = java.lang.Math.cos(theta._radians);
    final double sinTheta = java.lang.Math.sin(theta._radians);
  
    final double ms = axis.squaredLength();
    final double m = IMathUtils.instance().sqrt(ms);
  
    return new MutableVector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
  }

  public final double x()
  {
    return _x;
  }

  public final double y()
  {
    return _y;
  }

  public final double z()
  {
    return _z;
  }

  public final MutableVector3D transformedBy(MutableMatrix44D m, double homogeneus)
  {
    return new MutableVector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(), _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(), _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
  }

  public final Vector3D asVector3D()
  {
    return new Vector3D(_x, _y, _z);
  }

  public final void putSub(MutableVector3D a, Vector3D b)
  {
    _x = a._x - b._x;
    _y = a._y - b._y;
    _z = a._z - b._z;
  }

  public static double normalizedDot(MutableVector3D a, MutableVector3D b)
  {
    final double aLength = a.length();
    final double a_x = a._x / aLength;
    final double a_y = a._y / aLength;
    final double a_z = a._z / aLength;
  
    final double bLength = b.length();
    final double b_x = b._x / bLength;
    final double b_y = b._y / bLength;
    final double b_z = b._z / bLength;
  
    return ((a_x * b_x) + (a_y * b_y) + (a_z * b_z));
  }

  public static double angleInRadiansBetween(MutableVector3D a, MutableVector3D b)
  {
    double c = MutableVector3D.normalizedDot(a, b);
    if (c > 1.0)
    {
      c = 1.0;
    }
    else if (c < -1.0)
    {
      c = -1.0;
    }
    return IMathUtils.instance().acos(c);
  }

  public final MutableVector3D rotateAroundAxis(MutableVector3D axis, Angle theta)
  {
    final double u = axis._x;
    final double v = axis._y;
    final double w = axis._z;
  
    final double cosTheta = java.lang.Math.cos(theta._radians);
    final double sinTheta = java.lang.Math.sin(theta._radians);
  
    final double ms = axis.squaredLength();
    final double m = IMathUtils.instance().sqrt(ms);
  
    return new MutableVector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
  }

  public final double squaredDistanceTo(Vector3D that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    final double dz = _z - that._z;
    return (dx * dx) + (dy * dy) + (dz * dz);
  }

  public final double squaredDistanceTo(MutableVector3D that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    final double dz = _z - that._z;
    return (dx * dx) + (dy * dy) + (dz * dz);
  }

}