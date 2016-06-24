package org.glob3.mobile.generated; 
//
//  MutableVector2F.cpp
//  G3MiOSSDK
//
//
//  Created by Diego Gomez Deck on 10/23/14.
//  Created by Diego Gomez Deck on 2/25/15.
//
//
//
//  MutableVector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/14.
//  Created by Diego Gomez Deck on 2/25/15.
//

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if ! __G3MiOSSDK__MutableVector2F__
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
<<<<<<< HEAD ======= >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0 <<<<<<< HEAD ======= >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
//#define __G3MiOSSDK__MutableVector2F__

//class Vector2F;

public class MutableVector2F
{
//C++ TO JAVA CONVERTER TODO TASK: Java does not allow bit fields:
private <<<<<<< HEAD private: float _x;
  private float _y;

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
======= public: float _x;
  public float _y;

public >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0 MutableVector2F()
  {
   _x = 0;
   _y = 0F;
  }

public <<<<<<< HEAD MutableVector2F(float x, float y)
  {
   ======= MutableVector2F = new <type missing>(float x, float y);
   _y = y;
  }

public final <<<<<<< HEAD float x()
{
    return _x;
  }

  public final float y()
  {
    return _y;
  }

  public final Vector2F asVector2F()
  {
    return new Vector2F(_x, _y);
  }
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
======= MutableVector2F(const MutableVector2F& that) : _x(that._x), _y(that._y)
  {
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableVector2F(Vector2F that);

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
public final >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0 MutableVector2F copyFrom(MutableVector2F that)
{
    _x = that._x;
    _y = that._y;
    return this;
  }

//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
<<<<<<< HEAD ======= static MutableVector2F zero()
{
    return new MutableVector2F(0, 0);
  }

  public static MutableVector2F nan()
  {
    return new MutableVector2F(java.lang.Float.NaN, java.lang.Float.NaN);
  }

  public final Vector2F asVector2F()
  {
    return new Vector2F(_x, _y);
  }

  public final boolean isNan()
  {
    return ((_x != _x) || (_y != _y));
  }

//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
}
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
<<<<<<< HEAD
//#else
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
<<<<<<< HEAD ======= >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0 <<<<<<< HEAD ======= >>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0 <<<<<<< HEAD
//#endif
//C++ TO JAVA CONVERTER TODO TASK: The following method format was not recognized, possibly due to an unrecognized macro:
======= MutableVector2F.MutableVector2F(const Vector2F& that) : _x(that._x), _y(that._y)
{

}
//C++ TO JAVA CONVERTER TODO TASK: The following statement was not recognized, possibly due to an unrecognized macro:
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
