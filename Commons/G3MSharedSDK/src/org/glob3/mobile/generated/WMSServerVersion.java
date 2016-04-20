package org.glob3.mobile.generated; 
//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//

//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//



public enum WMSServerVersion
{
  WMS_1_1_0,
  WMS_1_3_0;

   public int getValue()
   {
      return this.ordinal();
   }

   public static WMSServerVersion forValue(int value)
   {
      return values()[value];
   }
}