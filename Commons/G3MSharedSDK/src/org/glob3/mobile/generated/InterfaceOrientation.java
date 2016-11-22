package org.glob3.mobile.generated;
//
//  IDeviceAttitude.cpp
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/4/15.
//
//

//
//  IDeviceAttitude.h
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/4/15.
//
//




public enum InterfaceOrientation
{
  PORTRAIT,
  PORTRAIT_UPSIDEDOWN,
  LANDSCAPE_RIGHT,
  LANDSCAPE_LEFT;

   public int getValue()
   {
      return this.ordinal();
   }

   public static InterfaceOrientation forValue(int value)
   {
      return values()[value];
   }
}
