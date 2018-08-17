package org.glob3.mobile.generated;//
//  Vector2S.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/16.
//
//

//
//  Vector2S.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/2/16.
//
//


//class Vector2I;

public class Vector2S
{
  public short _x;
  public short _y;

  public Vector2S(short x, short y)
  {
     _x = x;
     _y = y;
  }

  public final Vector2I asVector2I()
  {
    return new Vector2I(_x,_y);
  }

  public final boolean isEquals(Vector2S that)
  {
    return ((_x == that._x) && (_y == that._y));
  }


}
