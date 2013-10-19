package org.glob3.mobile.generated; 
//
//  IDeviceInfo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//

//
//  IDeviceInfo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/8/13.
//
//


public enum DeviceInfo_Platform
{
  DEVICE_iOS,
  DEVICE_Android,
  DEVICE_GWT;

   public int getValue()
   {
      return this.ordinal();
   }

   public static DeviceInfo_Platform forValue(int value)
   {
      return values()[value];
   }
}