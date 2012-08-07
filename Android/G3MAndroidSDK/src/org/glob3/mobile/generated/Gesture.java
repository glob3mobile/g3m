package org.glob3.mobile.generated; 
//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;


public enum Gesture
{
  None, // used only for animation, not for gesture
  Drag,
  Zoom,
  Rotate,
  DoubleDrag;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Gesture forValue(int value)
	{
		return values()[value];
	}
}