package org.glob3.mobile.generated; 
//
//  TileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

//
//  TileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//


public enum TileImageContribution
{
  NONE,
  FULL_COVERAGE_OPAQUE,
  FULL_COVERAGE_TRANSPARENT,
  PARTIAL_COVERAGE_OPAQUE,
  PARTIAL_COVERAGE_TRANSPARENT;

   public int getValue()
   {
      return this.ordinal();
   }

   public static TileImageContribution forValue(int value)
   {
      return values()[value];
   }
}