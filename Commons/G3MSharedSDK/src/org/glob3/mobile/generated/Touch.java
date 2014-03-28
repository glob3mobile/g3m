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



///#include "Vector2I.hpp"


public class Touch
{
///#ifdef C_CODE
  private final Vector2F _pos = new Vector2F();
  private final Vector2F _prevPos = new Vector2F();
///#endif
///#ifdef JAVA_CODE
//  private final Vector2I _pos;
//  private final Vector2I _prevPos;
///#endif

  private final byte _tapCount;

  public Touch(Touch other)
  {
     _pos = new Vector2F(other._pos);
     _prevPos = new Vector2F(other._prevPos);
     _tapCount = other._tapCount;
  }

  public Touch(Vector2F pos, Vector2F prev)
  {
     this(pos, prev, (byte)0);
  }
  public Touch(Vector2F pos, Vector2F prev, byte tapCount)
  {
     _pos = new Vector2F(pos);
     _prevPos = new Vector2F(prev);
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