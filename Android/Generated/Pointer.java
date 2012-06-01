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





public class Pointer
{
	public Pointer(Vector2D pos, Vector2D prev)
	{
		_pos = pos;
		_prevPos = prev;
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getPos() const
	public final Vector2D getPos()
	{
		return _pos;
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D getPrevPos() const
	public final Vector2D getPrevPos()
	{
		return _prevPos;
	}

	private Vector2D _pos = new Vector2D();
	private Vector2D _prevPos = new Vector2D();
}