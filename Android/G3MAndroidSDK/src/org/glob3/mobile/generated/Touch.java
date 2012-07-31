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

  public Touch(Touch other)
  {
	  _pos = new Vector2D(other._pos);
	  _prevPos = new Vector2D(other._prevPos);

  }

  public Touch(Vector2D pos, Vector2D prev)
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
  public void dispose()
  {
  }
}