package org.glob3.mobile.generated;import java.util.*;

//
//  Vector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

//
//  Vector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;

public class Vector2F
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2F operator =(Vector2F v);


  public static Vector2F zero()
  {
	return new Vector2F(0, 0);
  }

  public static Vector2F nan()
  {
//C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
	return new Vector2F(NANF, NANF);
  }

  public final float _x;
  public final float _y;


  public Vector2F(float x, float y)
  {
	  _x = x;
	  _y = y;
  }

  public Vector2F(Vector2F v)
  {
	  _x = v._x;
	  _y = v._y;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(const Vector2I& that) const
  public final double squaredDistanceTo(Vector2I that)
  {
	final double dx = _x - that._x;
	final double dy = _y - that._y;
	return (dx * dx) + (dy * dy);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(const Vector2F& that) const
  public final double squaredDistanceTo(Vector2F that)
  {
	final double dx = _x - that._x;
	final double dy = _y - that._y;
	return (dx * dx) + (dy * dy);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const double squaredDistanceTo(float x, float y) const
  public final double squaredDistanceTo(float x, float y)
  {
	final double dx = _x - x;
	final double dy = _y - y;
	return (dx * dx) + (dy * dy);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F add(const Vector2F& v) const
  public final Vector2F add(Vector2F v)
  {
	return new Vector2F(_x + v._x, _y + v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return (_x != _x) || (_y != _y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F sub(const Vector2F& v) const
  public final Vector2F sub(Vector2F v)
  {
	return new Vector2F(_x - v._x, _y - v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F times(const float magnitude) const
  public final Vector2F times(float magnitude)
  {
	return new Vector2F(_x * magnitude, _y * magnitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F div(float v) const
  public final Vector2F div(float v)
  {
	return new Vector2F(_x / v, _y / v);
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
	return _x * _x + _y * _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F clampLength(float min, float max) const
  public final Vector2F clampLength(float min, float max)
  {
	float length = (float) this.length();
	if (length < min)
	{
	  return times(min / length);
	}
	if (length > max)
	{
	  return times(max / length);
	}
	return this;
  }

}
