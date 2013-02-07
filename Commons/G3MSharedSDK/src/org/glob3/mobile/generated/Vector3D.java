package org.glob3.mobile.generated; 
//
//  Vector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Vector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//class MutableVector3D;

public class Vector3D
{

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D operator =(Vector3D that);

  public final double _x;
  public final double _y;
  public final double _z;


  public Vector3D(double x, double y, double z)
  {
     _x = x;
     _y = y;
     _z = z;

  }

  public void dispose()
  {
  }

  public Vector3D(Vector3D v)
  {
     _x = v._x;
     _y = v._y;
     _z = v._z;

  }

  public static Vector3D nan()
  {
    return new Vector3D(IMathUtils.instance().NanD(), IMathUtils.instance().NanD(), IMathUtils.instance().NanD());
  }

  public static Vector3D zero()
  {
    return new Vector3D(0, 0, 0);
  }

  public static Vector3D upX()
  {
    return new Vector3D(1, 0, 0);
  }

  public static Vector3D downX()
  {
    return new Vector3D(-1, 0, 0);
  }

  public static Vector3D upY()
  {
    return new Vector3D(0, 1, 0);
  }

  public static Vector3D downY()
  {
    return new Vector3D(0, -1, 0);
  }

  public static Vector3D upZ()
  {
    return new Vector3D(0, 0, 1);
  }

  public static Vector3D downZ()
  {
    return new Vector3D(0, 0, -1);
  }

  public final boolean isNan()
  {
    return (IMathUtils.instance().isNan(_x) || IMathUtils.instance().isNan(_y) || IMathUtils.instance().isNan(_z));
  }

  public final boolean isZero()
  {
    return (_x == 0) && (_y == 0) && (_z == 0);
  }

  public final Vector3D normalized()
  {
    double d = length();
    return new Vector3D(_x / d, _y /d, _z / d);
  }

  public final double length()
  {
    return IMathUtils.instance().sqrt(squaredLength());
  }

  public final double squaredLength()
  {
    return _x * _x + _y * _y + _z * _z;
  }

  public final double dot(Vector3D v)
  {
    return _x * v._x + _y * v._y + _z * v._z;
  }

  public final Vector3D add(Vector3D v)
  {
    return new Vector3D(_x + v._x, _y + v._y, _z + v._z);
  }

  public final Vector3D sub(Vector3D v)
  {
    return new Vector3D(_x - v._x, _y - v._y, _z - v._z);
  }

  public final Vector3D times(Vector3D v)
  {
    return new Vector3D(_x * v._x, _y * v._y, _z * v._z);
  }

  public final Vector3D times(double magnitude)
  {
    return new Vector3D(_x * magnitude, _y * magnitude, _z * magnitude);
  }

  public final Vector3D div(Vector3D v)
  {
    return new Vector3D(_x / v._x, _y / v._y, _z / v._z);
  }

  public final Vector3D div(double v)
  {
    return new Vector3D(_x / v, _y / v, _z / v);
  }

  public final Vector3D cross(Vector3D other)
  {
    return new Vector3D(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
  }

  public final Angle angleBetween(Vector3D other)
  {
    final Vector3D v1 = normalized();
    final Vector3D v2 = other.normalized();
  
    double c = v1.dot(v2);
    if (c > 1.0)
       c = 1.0;
    else if (c < -1.0)
       c = -1.0;
  
    return Angle.fromRadians(IMathUtils.instance().acos(c));
  }
  public final Angle signedAngleBetween(Vector3D other, Vector3D up)
  {
    final Angle angle = angleBetween(other);
    if (cross(other).dot(up) > 0)
    {
      return angle;
    }
  
    return angle.times(-1);
  }

  public final Vector3D rotateAroundAxis(Vector3D axis, Angle theta)
  {
    final double u = axis._x;
    final double v = axis._y;
    final double w = axis._z;
  
    final double cosTheta = theta.cosinus();
    final double sinTheta = theta.sinus();
  
    final double ms = axis.squaredLength();
    final double m = IMathUtils.instance().sqrt(ms);
  
    return new Vector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
  }

  //  double x() const {
  //    return _x;
  //  }
  //
  //  double y() const {
  //    return _y;
  //  }
  //
  //  double z() const {
  //    return _z;
  //  }

  public final Vector3D transformedBy(MutableMatrix44D m, double homogeneus)
  {
    int __TODO_move_to_matrix;
    return new Vector3D(_x * m.get(0) + _y * m.get(4) + _z * m.get(8) + homogeneus * m.get(12), _x * m.get(1) + _y * m.get(5) + _z * m.get(9) + homogeneus * m.get(13), _x * m.get(2) + _y * m.get(6) + _z * m.get(10) + homogeneus * m.get(14));
  }

  public final MutableVector3D asMutableVector3D()
  {
    return new MutableVector3D(_x, _y, _z);
  }

  public final double maxAxis()
  {
    if (_x >= _y && _x >= _z)
    {
      return _x;
    }
    else if (_y >= _z)
    {
      return _y;
    }
    else
    {
      return _z;
    }
  }

  public final Vector3D projectionInPlane(Vector3D normal)
  {
    Vector3D axis = normal.cross(this);
    MutableMatrix44D m = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(90), axis);
    Vector3D projected = normal.transformedBy(m, 0).normalized();
    return projected.times(this.length());
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(V3D ");
    isb.addDouble(_x);
    isb.addString(", ");
    isb.addDouble(_y);
    isb.addString(", ");
    isb.addDouble(_z);
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}