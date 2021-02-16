package org.glob3.mobile.generated;
//
//  TouchEvent.cpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//

//
//  TouchEvent.hpp
//  G3M
//
//  Created by José Miguel S N on 31/05/12.
//


public class Touch
{
  private final Vector2F _pos;
  private final Vector2F _prevPos;
  private final byte _tapCount;
  private final double _mouseWheelDelta;

  public Touch(Touch other)
  {
     _pos = other._pos;
     _prevPos = other._prevPos;
     _tapCount = other._tapCount;
     _mouseWheelDelta = other._mouseWheelDelta;
  }

  public Touch(Vector2F pos, Vector2F prev)
  {
     this(pos, prev, (byte)1);
  }
  public Touch(Vector2F pos, Vector2F prev, byte tapCount)
  {
     _pos = pos;
     _prevPos = prev;
     _tapCount = tapCount;
     _mouseWheelDelta = 0;
  }
  
  public Touch(Vector2F pos, Vector2F prev, byte tapCount, double mouseWheelDelta)
  {
     _pos = pos;
     _prevPos = prev;
     _tapCount = tapCount;
     _mouseWheelDelta = mouseWheelDelta;
  }

  public final Touch clone()
  {
    return new Touch(_pos, _prevPos, _tapCount);
  }

  public final Vector2F getPos()
  {
     return _pos;
  }
  public final Vector2F getPrevPos()
  {
     return _prevPos;
  }
  public final byte getTapCount()
  {
     return _tapCount;
  }
  
  public final double getMouseWheelDelta() { return _mouseWheelDelta;}

  public void dispose()
  {
  }
}