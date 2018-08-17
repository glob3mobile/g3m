package org.glob3.mobile.generated;import java.util.*;

//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//

//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/15.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2F;

public class MutableVector2F
{
  public float _x;
  public float _y;

  public MutableVector2F()
  {
	  _x = 0F;
	  _y = 0F;
  }

  public MutableVector2F(float x, float y)
  {
	  _x = x;
	  _y = y;
  }

  public MutableVector2F(MutableVector2F that)
  {
	  _x = that._x;
	  _y = that._y;
  }

  public MutableVector2F(Vector2F that)
  {
	  _x = that._x;
	  _y = that._y;
  
  }

  public final void set(float x, float y)
  {
	_x = x;
	_y = y;
  }

  public final void add(float x, float y)
  {
	_x += x;
	_y += y;
  }

  public final void times(float k)
  {
	_x *= k;
	_y *= k;
  }

//C++ TO JAVA CONVERTER NOTE: This 'copyFrom' method was converted from the original C++ copy assignment operator:
//ORIGINAL LINE: MutableVector2F& operator =(const MutableVector2F& that)
  public final MutableVector2F copyFrom(MutableVector2F that)
  {
	_x = that._x;
	_y = that._y;
	return this;
  }

  public static MutableVector2F zero()
  {
	return new MutableVector2F(0, 0);
  }

  public static MutableVector2F nan()
  {
  //C++ TO JAVA CONVERTER TODO TASK: The #define macro NANF was defined in alternate ways and cannot be replaced in-line:
	return new MutableVector2F(NANF, NANF);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2F asVector2F() const
  public final Vector2F asVector2F()
  {
	return new Vector2F(_x, _y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return ((_x != _x) || (_y != _y));
  }

}
