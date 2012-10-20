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
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Touch(const Vector2I& pos, const Vector2I& prev, const byte tapCount= (byte)0): _pos(pos), _prevPos(prev), _tapCount(tapCount)
  public Touch(Vector2I pos, Vector2I prev, byte tapCount)
  {
	  _pos = new Vector2I(pos);
	  _prevPos = new Vector2I(prev);
	  _tapCount = tapCount;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2I getPos() const
  public final Vector2I getPos()
  {
	  return _pos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2I getPrevPos() const
  public final Vector2I getPrevPos()
  {
	  return _prevPos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const byte getTapCount() const
  public final byte getTapCount()
  {
	  return _tapCount;
  }
  public void dispose()
  {
  }
}