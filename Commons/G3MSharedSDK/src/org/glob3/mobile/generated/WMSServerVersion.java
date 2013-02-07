package org.glob3.mobile.generated; 
//
//  WMSLayer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  WMSLayer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
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