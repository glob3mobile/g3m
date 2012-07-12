package org.glob3.mobile.client.generated; 
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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector3D;

public class Vector3D
{
  private final double _x;
  private final double _y;
  private final double _z;


  public Vector3D(double x, double y, double z)
  {
	  _x = x;
	  _y = y;
	  _z = z;

  }

  public Vector3D(Vector3D v)
  {
	  _x = v._x;
	  _y = v._y;
	  _z = v._z;

  }

  public static Vector3D nan()
  {
	return new Vector3D(Double.NaN, Double.NaN, Double.NaN);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return Double.isNaN(_x *_y *_z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D normalized() const
  public final Vector3D normalized()
  {
	  double d = length();
	  return new Vector3D(_x / d, _y /d, _z / d);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double length() const
  public final double length()
  {
	return Math.sqrt(squaredLength());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredLength() const
  public final double squaredLength()
  {
	return _x * _x + _y * _y + _z * _z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double dot(const Vector3D& v) const
  public final double dot(Vector3D v)
  {
	return _x * v._x + _y * v._y + _z * v._z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D add(const Vector3D& v) const
  public final Vector3D add(Vector3D v)
  {
	return new Vector3D(_x + v._x, _y + v._y, _z + v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D sub(const Vector3D& v) const
  public final Vector3D sub(Vector3D v)
  {
	return new Vector3D(_x - v._x, _y - v._y, _z - v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D times(const Vector3D& v) const
  public final Vector3D times(Vector3D v)
  {
	return new Vector3D(_x * v._x, _y * v._y, _z * v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D times(const double magnitude) const
  public final Vector3D times(double magnitude)
  {
	return new Vector3D(_x * magnitude, _y * magnitude, _z * magnitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D div(const Vector3D& v) const
  public final Vector3D div(Vector3D v)
  {
	return new Vector3D(_x / v._x, _y / v._y, _z / v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D div(const double v) const
  public final Vector3D div(double v)
  {
	return new Vector3D(_x / v, _y / v, _z / v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D cross(const Vector3D& other) const
  public final Vector3D cross(Vector3D other)
  {
	return new Vector3D(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angleBetween(const Vector3D& other) const
  public final Angle angleBetween(Vector3D other)
  {
	Vector3D v1 = normalized();
	Vector3D v2 = other.normalized();
	double c = v1.dot(v2);
	if (c > 1.0)
		c = 1.0;
	else if (c < -1.0)
		c = -1.0;
  
	Angle a = Angle.fromRadians(Math.acos(c));
  
	return a;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D rotateAroundAxis(const Vector3D& axis, double theta) const
  public final Vector3D rotateAroundAxis(Vector3D axis, double theta)
  {
	final double u = axis.x();
	final double v = axis.y();
	final double w = axis.z();
  
	final double cosTheta = Math.cos(theta);
	final double sinTheta = Math.sin(theta);
  
	final double ms = axis.squaredLength();
	final double m = Math.sqrt(ms);
  
	return new Vector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double x() const
  public final double x()
  {
	return _x;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double y() const
  public final double y()
  {
	return _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double z() const
  public final double z()
  {
	return _z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D applyTransform(const MutableMatrix44D &m) const
  public final Vector3D applyTransform(MutableMatrix44D m)
  {
	//const double * M = m.getMatrix();

	Vector3D v = new Vector3D(_x * m.get(0) + _y * m.get(4) + _z * m.get(8) + m.get(12), _x * m.get(1) + _y * m.get(5) + _z * m.get(9) + m.get(13), _x * m.get(2) + _y * m.get(6) + _z * m.get(10) + m.get(14));

	return v;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D asMutableVector3D() const
  public final MutableVector3D asMutableVector3D()
  {
	return new MutableVector3D(_x, _y, _z);
  }

}