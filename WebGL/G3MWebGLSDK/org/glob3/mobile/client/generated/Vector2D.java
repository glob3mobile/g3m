package org.glob3.mobile.client.generated; 
//
//  Vector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Vector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public class Vector2D
{
  private final double _x;
  private final double _y;


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Vector2D operator =(Vector2D v);


  public Vector2D(double x, double y)
  {
	  _x = x;
	  _y = y;

  }

  public Vector2D(Vector2D v)
  {
	  _x = v.x();
	  _y = v.y();

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D normalized() const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2D normalized();

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
	return _x * _x + _y * _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D add(const Vector2D& v) const
  public final Vector2D add(Vector2D v)
  {
	return new Vector2D(_x + v._x, _y + v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D sub(const Vector2D& v) const
  public final Vector2D sub(Vector2D v)
  {
	return new Vector2D(_x - v._x, _y - v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D times(const Vector2D& v) const
  public final Vector2D times(Vector2D v)
  {
	return new Vector2D(_x * v._x, _y * v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D times(const double magnitude) const
  public final Vector2D times(double magnitude)
  {
	return new Vector2D(_x * magnitude, _y * magnitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D div(const Vector2D& v) const
  public final Vector2D div(Vector2D v)
  {
	return new Vector2D(_x / v._x, _y / v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D div(const double v) const
  public final Vector2D div(double v)
  {
	return new Vector2D(_x / v, _y / v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angle() const
  public final Angle angle()
  {
	double a = Math.atan2(_y, _x);
	return Angle.fromRadians(a);
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

}