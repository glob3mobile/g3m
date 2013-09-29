package org.glob3.mobile.generated; 
//
//  RenderState.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//

//
//  RenderState.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/29/13.
//
//



public enum RenderState_Type
{
  READY,
  BUSY,
  ERROR;

   public int getValue()
   {
      return this.ordinal();
   }

   public static RenderState_Type forValue(int value)
   {
      return values()[value];
   }
}