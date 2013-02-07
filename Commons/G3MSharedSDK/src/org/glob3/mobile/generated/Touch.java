package org.glob3.mobile.generated; 
//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TouchEvent.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class Touch
{
  private Vector2I _pos = new Vector2I();
  private Vector2I _prevPos = new Vector2I();
  private final byte _tapCount;

  public Touch(Touch other)
  {
     _pos = new Vector2I(other._pos);
     _prevPos = new Vector2I(other._prevPos);
     _tapCount = other._tapCount;
  }

  public Touch(Vector2I pos, Vector2I prev)
  {
     this(pos, prev, (byte)0);
  }
  public Touch(Vector2I pos, Vector2I prev, byte tapCount)
  {
     _pos = new Vector2I(pos);
     _prevPos = new Vector2I(prev);
     _tapCount = tapCount;
  }

  public final Vector2I getPos()
  {
     return _pos;
  }
  public final Vector2I getPrevPos()
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