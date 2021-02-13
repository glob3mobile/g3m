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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Touch(Touch other);

  public Touch(Vector2F pos, Vector2F prev)
  {
     this(pos, prev, (byte)1);
  }
  public Touch(Vector2F pos, Vector2F prev, byte tapCount)
  {
     _pos = pos;
     _prevPos = prev;
     _tapCount = tapCount;

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

  public void dispose()
  {
  }
}