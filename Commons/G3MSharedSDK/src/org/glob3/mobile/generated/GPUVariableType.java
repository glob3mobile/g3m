package org.glob3.mobile.generated; 
//
//  GPUVariable.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//

//
//  GPUVariable.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/06/13.
//
//



public enum GPUVariableType
{
  ATTRIBUTE(1),
  UNIFORM(2);

   private int intValue;
   private static java.util.HashMap<Integer, GPUVariableType> mappings;
   private static java.util.HashMap<Integer, GPUVariableType> getMappings()
   {
      if (mappings == null)
      {
         synchronized (GPUVariableType.class)
         {
            if (mappings == null)
            {
               mappings = new java.util.HashMap<Integer, GPUVariableType>();
            }
         }
      }
      return mappings;
   }

   private GPUVariableType(int value)
   {
      intValue = value;
      GPUVariableType.getMappings().put(value, this);
   }

   public int getValue()
   {
      return intValue;
   }

   public static GPUVariableType forValue(int value)
   {
      return getMappings().get(value);
   }
}