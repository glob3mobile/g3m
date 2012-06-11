package org.glob3.mobile.generated; 
//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public enum Gesture
{
  None, // used only for animation, not for gesture
  Drag,
  Zoom,
  Rotate;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Gesture forValue(int value)
	{
		return values()[value];
	}
}