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




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector3D;

public class Vector3D
{
  private final double _x;
  private final double _y;
  private final double _z;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector3D operator =(Vector3D that);


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
	return new Vector3D(IMathUtils.instance().NanD(), IMathUtils.instance().NanD(), IMathUtils.instance().NanD());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return (IMathUtils.instance().isNan(_x) || IMathUtils.instance().isNan(_y) || IMathUtils.instance().isNan(_z));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return (_x == 0) && (_y == 0) && (_z == 0);
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
	return IMathUtils.instance().sqrt(squaredLength());
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
	final Vector3D v1 = normalized();
	final Vector3D v2 = other.normalized();
  
	double c = v1.dot(v2);
	if (c > 1.0)
		c = 1.0;
	else if (c < -1.0)
		c = -1.0;
  
	return Angle.fromRadians(IMathUtils.instance().acos(c));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D rotateAroundAxis(const Vector3D& axis, const Angle& theta) const
  public final Vector3D rotateAroundAxis(Vector3D axis, Angle theta)
  {
	final double u = axis.x();
	final double v = axis.y();
	final double w = axis.z();
  
	final double cosTheta = theta.cosinus();
	final double sinTheta = theta.sinus();
  
	final double ms = axis.squaredLength();
	final double m = IMathUtils.instance().sqrt(ms);
  
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
//ORIGINAL LINE: Vector3D transformedBy(const MutableMatrix44D &m, const double homogeneus) const
  public final Vector3D transformedBy(MutableMatrix44D m, double homogeneus)
  {
	return new Vector3D(_x * m.get(0) + _y * m.get(4) + _z * m.get(8) + homogeneus * m.get(12), _x * m.get(1) + _y * m.get(5) + _z * m.get(9) + homogeneus * m.get(13), _x * m.get(2) + _y * m.get(6) + _z * m.get(10) + homogeneus * m.get(14));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D asMutableVector3D() const
  public final MutableVector3D asMutableVector3D()
  {
	return new MutableVector3D(_x, _y, _z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double maxAxis() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D projectionInPlane(const Vector3D& normal) const
  public final Vector3D projectionInPlane(Vector3D normal)
  {
	Vector3D axis = normal.cross(this);
	MutableMatrix44D m = MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(90), axis);
	Vector3D projected = normal.transformedBy(m, 0).normalized();
	return projected.times(this.length());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(V2D ").add(_x).add(", ").add(_y).add(", ").add(_z).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}