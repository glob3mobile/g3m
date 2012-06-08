package org.glob3.mobile.generated; 
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D normalized() const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableVector3D normalized();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print() const
  public final void print()
  {
	  System.out.printf("%.2f  %.2f %.2f\n", _x, _y, _z);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D sub(const MutableVector3D& v) const
  public final MutableVector3D sub(MutableVector3D v)
  {
	return new MutableVector3D(_x - v._x, _y - v._y, _z - v._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D times(const MutableVector3D& v) const
  public final MutableVector3D times(MutableVector3D v)
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
//ORIGINAL LINE: double angleBetween(const MutableVector3D& other) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  double angleBetween(MutableVector3D other);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector3D rotatedAroundAxis(const MutableVector3D& other, const double theta) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableVector3D rotatedAroundAxis(MutableVector3D other, double theta);

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
//ORIGINAL LINE: MutableVector3D applyTransform(const MutableMatrix44D &m) const
  public final MutableVector3D applyTransform(MutableMatrix44D m)
  {
	double[] M = m.getMatrix();

	MutableVector3D v = new MutableVector3D(_x * M[0] + _y * M[4] + _z * M[8] + M[12], _x * M[1] + _y * M[5] + _z * M[9] + M[13], _x * M[2] + _y * M[6] + _z * M[10] + M[14]);

	return v;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D asVector3D() const
  public final Vector3D asVector3D()
  {
	return new Vector3D(_x, _y, _z);
  }

}