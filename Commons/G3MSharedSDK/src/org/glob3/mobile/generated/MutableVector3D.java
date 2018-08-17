package org.glob3.mobile.generated;import java.util.*;

//
//  MutableVector3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//

//
//  MutableVector3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D normalized() const
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
//C++ TO JAVA CONVERTER TODO TASK: The #define macro NAND was defined in alternate ways and cannot be replaced in-line:
	return new MutableVector3D(NAND, NAND, NAND);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean equalTo(const MutableVector3D& v) const
  public final boolean equalTo(MutableVector3D v)
  {
	return (v._x == _x && v._y == _y && v._z == _z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return ((_x != _x) || (_y != _y) || (_z != _z));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return (_x == 0) && (_y == 0) && (_z == 0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double length() const
  public final double length()
  {
	return IMathUtils.instance().sqrt(squaredLength());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredLength() const
  public final double squaredLength()
  {
	return _x * _x + _y * _y + _z * _z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double dot(const MutableVector3D& v) const
  public final double dot(MutableVector3D v)
  {
	return _x * v._x + _y * v._y + _z * v._z;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D add(const MutableVector3D& v) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D sub(const MutableVector3D& v) const
  public final MutableVector3D sub(MutableVector3D v)
  {
	return new MutableVector3D(_x - v._x, _y - v._y, _z - v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D sub(const Vector3D& v) const
  public final Vector3D sub(Vector3D v)
  {
	return new Vector3D(_x - v._x, _y - v._y, _z - v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D times(const MutableVector3D& v) const
  public final MutableVector3D times(MutableVector3D v)
  {
	return new MutableVector3D(_x * v._x, _y * v._y, _z * v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D times(const Vector3D& v) const
  public final MutableVector3D times(Vector3D v)
  {
	return new MutableVector3D(_x * v._x, _y * v._y, _z * v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D times(const double magnitude) const
  public final MutableVector3D times(double magnitude)
  {
	return new MutableVector3D(_x * magnitude, _y * magnitude, _z * magnitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D div(const MutableVector3D& v) const
  public final MutableVector3D div(MutableVector3D v)
  {
	return new MutableVector3D(_x / v._x, _y / v._y, _z / v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D div(const double v) const
  public final MutableVector3D div(double v)
  {
	return new MutableVector3D(_x / v, _y / v, _z / v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D cross(const MutableVector3D& other) const
  public final MutableVector3D cross(MutableVector3D other)
  {
	return new MutableVector3D(_y * other._z - _z * other._y, _z * other._x - _x * other._z, _x * other._y - _y * other._x);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angleBetween(const MutableVector3D& other) const
  public final Angle angleBetween(MutableVector3D other)
  {
	final MutableVector3D v1 = normalized();
	final MutableVector3D v2 = other.normalized();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double c = v1.dot(v2);
	double c = v1.dot(new MutableVector3D(v2));
	if (c > 1.0)
		c = 1.0;
	else if (c < -1.0)
		c = -1.0;
  
	return Angle.fromRadians(IMathUtils.instance().acos(c));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D rotatedAroundAxis(const MutableVector3D& axis, const Angle& theta) const
  public final MutableVector3D rotatedAroundAxis(MutableVector3D axis, Angle theta)
  {
	final double u = axis.x();
	final double v = axis.y();
	final double w = axis.z();
  
  //  const double cosTheta = theta.cosinus();
  //  const double sinTheta = theta.sinus();
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	final double cosTheta = COS(theta._radians);
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
	final double sinTheta = SIN(theta._radians);
  
	final double ms = axis.squaredLength();
	final double m = IMathUtils.instance().sqrt(ms);
  
	return new MutableVector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
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
//ORIGINAL LINE: MutableVector3D transformedBy(const MutableMatrix44D &m, const double homogeneus) const
  public final MutableVector3D transformedBy(MutableMatrix44D m, double homogeneus)
  {
	return new MutableVector3D(_x * m.get0() + _y * m.get4() + _z * m.get8() + homogeneus * m.get12(), _x * m.get1() + _y * m.get5() + _z * m.get9() + homogeneus * m.get13(), _x * m.get2() + _y * m.get6() + _z * m.get10() + homogeneus * m.get14());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D asVector3D() const
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: double c = MutableVector3D::normalizedDot(a, b);
	double c = MutableVector3D.normalizedDot(new MutableVector3D(a), new MutableVector3D(b));
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D rotateAroundAxis(const MutableVector3D& axis, const Angle& theta) const
  public final MutableVector3D rotateAroundAxis(MutableVector3D axis, Angle theta)
  {
	final double u = axis._x;
	final double v = axis._y;
	final double w = axis._z;
  
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro COS was defined in alternate ways and cannot be replaced in-line:
	final double cosTheta = COS(theta._radians);
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
	final double sinTheta = SIN(theta._radians);
  
	final double ms = axis.squaredLength();
	final double m = IMathUtils.instance().sqrt(ms);
  
	return new MutableVector3D(((u * (u * _x + v * _y + w * _z)) + (((_x * (v * v + w * w)) - (u * (v * _y + w * _z))) * cosTheta) + (m * ((-w * _y) + (v * _z)) * sinTheta)) / ms, ((v * (u * _x + v * _y + w * _z)) + (((_y * (u * u + w * w)) - (v * (u * _x + w * _z))) * cosTheta) + (m * ((w * _x) - (u * _z)) * sinTheta)) / ms, ((w * (u * _x + v * _y + w * _z)) + (((_z * (u * u + v * v)) - (w * (u * _x + v * _y))) * cosTheta) + (m * (-(v * _x) + (u * _y)) * sinTheta)) / ms);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(const Vector3D& that) const
  public final double squaredDistanceTo(Vector3D that)
  {
	final double dx = _x - that._x;
	final double dy = _y - that._y;
	final double dz = _z - that._z;
	return (dx * dx) + (dy * dy) + (dz * dz);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(const MutableVector3D& that) const
  public final double squaredDistanceTo(MutableVector3D that)
  {
	final double dx = _x - that._x;
	final double dy = _y - that._y;
	final double dz = _z - that._z;
	return (dx * dx) + (dy * dy) + (dz * dz);
  }

}
