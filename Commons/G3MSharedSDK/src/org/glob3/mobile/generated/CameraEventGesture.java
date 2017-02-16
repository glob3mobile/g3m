package org.glob3.mobile.generated;
//
//  CameraEventGesture.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/14/17.
//
//

//
//  CameraEventGesture.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/14/17.
//
//


public enum CameraEventGesture
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

   public static CameraEventGesture forValue(int value)
   {
      return values()[value];
   }
}
