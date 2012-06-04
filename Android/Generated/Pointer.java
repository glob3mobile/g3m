package org.glob3.mobile.generated; 
//
//  TouchEvent.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class Pointer
{
  private final Vector2D _pos = new Vector2D();
  private final Vector2D _prevPos = new Vector2D();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Pointer operator =(Pointer v);

  public Pointer(Pointer other)
  {
	  _pos = new Vector2D(other._pos);
	  _prevPos = new Vector2D(other._prevPos);

  }

  public Pointer(Vector2D pos, Vector2D prev)
  {
	  _pos = new Vector2D(pos);
	  _prevPos = new Vector2D(prev);
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
}