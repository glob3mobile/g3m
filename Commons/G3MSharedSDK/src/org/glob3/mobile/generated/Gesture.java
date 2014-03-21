package org.glob3.mobile.generated; 
//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CameraRenderer.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//






//class RenderState;
//class CameraEventHandler;


//class ILogger;
//class Camera;
//class Vector3D;
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