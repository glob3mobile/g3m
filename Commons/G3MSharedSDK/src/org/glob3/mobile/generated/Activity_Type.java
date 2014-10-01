package org.glob3.mobile.generated; 
//
//  ILocationManager.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//

//
//  ILocationManager.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 29/09/14.
//
//






public enum Activity_Type
{
  OTHER,
  AUTOMOTIVE_NAVIGATION,
  FITNESS,
  OTHER_NAVIGATION;

   public int getValue()
   {
      return this.ordinal();
   }

   public static Activity_Type forValue(int value)
   {
      return values()[value];
   }
}