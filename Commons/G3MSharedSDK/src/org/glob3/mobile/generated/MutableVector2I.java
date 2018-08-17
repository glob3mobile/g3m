package org.glob3.mobile.generated;import java.util.*;

//
//  MutableVector2I.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/12.
//
//

//
//  MutableVector2I.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;


public class MutableVector2I
{
  private int _x;
  private int _y;

  public MutableVector2I()
  {
	  _x = 0;
	  _y = 0;

  }

  public MutableVector2I(int x, int y)
  {
	  _x = x;
	  _y = y;

  }

  public MutableVector2I(MutableVector2I that)
  {
	  _x = that._x;
	  _y = that._y;

  }

//C++ TO JAVA CONVERTER NOTE: This 'copyFrom' method was converted from the original C++ copy assignment operator:
//ORIGINAL LINE: MutableVector2I& operator =(const MutableVector2I& that)
  public final MutableVector2I copyFrom(MutableVector2I that)
  {
	_x = that._x;
	_y = that._y;
	return this;
  }

  public static MutableVector2I zero()
  {
	return new MutableVector2I(0, 0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int x() const
  public final int x()
  {
	return _x;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int y() const
  public final int y()
  {
	return _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2I asVector2I() const
  public final Vector2I asVector2I()
  {
	return new Vector2I(_x, _y);
  }

  public final void set(int x, int y)
  {
	_x = x;
	_y = y;
  }

}
