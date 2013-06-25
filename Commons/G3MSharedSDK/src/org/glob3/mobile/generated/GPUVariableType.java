package org.glob3.mobile.generated; 
//
//  GPUVariable.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

//
//  GPUVariable.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//



public enum GPUVariableType
{
  ATTRIBUTE,
  UNIFORM;

   public int getValue()
   {
      return this.ordinal();
   }

   public static GPUVariableType forValue(int value)
   {
      return values()[value];
   }
}