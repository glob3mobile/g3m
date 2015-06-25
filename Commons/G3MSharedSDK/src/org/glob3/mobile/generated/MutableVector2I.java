package org.glob3.mobile.generated; 
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

  public final int x()
  {
    return _x;
  }

  public final int y()
  {
    return _y;
  }

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