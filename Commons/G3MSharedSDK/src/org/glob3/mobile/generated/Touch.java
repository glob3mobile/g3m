package org.glob3.mobile.generated; 
//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TouchEvent.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class Touch
{
  private final Vector2F _pos;
  private final Vector2F _prevPos;
  private final byte _tapCount;

  public Touch(Touch other)
  {
     _pos = other._pos;
     _prevPos = other._prevPos;
     _tapCount = other._tapCount;
  }

  public Touch(Vector2F pos, Vector2F prev)
  {
     this(pos, prev, (byte)0);
  }
  public Touch(Vector2F pos, Vector2F prev, byte tapCount)
  {
     _pos = pos;
     _prevPos = prev;
     _tapCount = tapCount;
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