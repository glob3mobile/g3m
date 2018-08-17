package org.glob3.mobile.generated;//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//

//
//  CameraRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//






//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CameraEventHandler;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TouchEvent;


public enum Gesture
{
  None,
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
