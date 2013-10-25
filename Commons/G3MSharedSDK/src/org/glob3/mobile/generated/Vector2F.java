package org.glob3.mobile.generated; 
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


//class Vector2I;

public class Vector2F
{


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2F operator =(Vector2F v);

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

  public final float x()
  {
    return _x;
  }

  public final float y()
  {
    return _y;
  }

  public static Vector2F nan()
  {
    return new Vector2F(java.lang.Float.NaN, java.lang.Float.NaN);
  }

  public final double squaredDistanceTo(Vector2I that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    return (dx * dx) + (dy * dy);
  }

  public final double squaredDistanceTo(Vector2F that)
  {
    final double dx = _x - that._x;
    final double dy = _y - that._y;
    return (dx * dx) + (dy * dy);
  }

}