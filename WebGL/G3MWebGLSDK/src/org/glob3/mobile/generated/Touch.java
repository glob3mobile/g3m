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
  private final Vector2D _pos ;
  private final Vector2D _prevPos ;
  private final byte _tapCount;

  public Touch(Touch other)
  {
	  _pos = new Vector2D(other._pos);
	  _prevPos = new Vector2D(other._prevPos);
	  _tapCount = other._tapCount;
  }

  public Touch(Vector2D pos, Vector2D prev)
  {
	  this(pos, prev, (byte)0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Touch(const Vector2D& pos, const Vector2D& prev, const byte tapCount= (byte)0): _pos(pos), _prevPos(prev), _tapCount(tapCount)
  public Touch(Vector2D pos, Vector2D prev, byte tapCount)
  {
	  _pos = new Vector2D(pos);
	  _prevPos = new Vector2D(prev);
	  _tapCount = tapCount;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2D getPos() const
  public final Vector2D getPos()
  {
	  return _pos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2D getPrevPos() const
  public final Vector2D getPrevPos()
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