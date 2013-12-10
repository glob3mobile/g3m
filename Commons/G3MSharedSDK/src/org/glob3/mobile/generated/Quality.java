package org.glob3.mobile.generated; 
//
//  TilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//




public enum Quality
{
  QUALITY_LOW,
  QUALITY_MEDIUM,
  QUALITY_HIGH;

   public int getValue()
   {
      return this.ordinal();
   }

   public static Quality forValue(int value)
   {
      return values()[value];
   }
}