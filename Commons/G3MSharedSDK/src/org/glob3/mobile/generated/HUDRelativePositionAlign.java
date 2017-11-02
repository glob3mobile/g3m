package org.glob3.mobile.generated;
//
//  HUDRelativePosition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//

//
//  HUDRelativePosition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/20/13.
//
//



public enum HUDRelativePositionAlign
{
  LEFT,
  RIGHT,
  CENTER,
  ABOVE,
  BELOW,
  MIDDLE;

   public int getValue()
   {
      return this.ordinal();
   }

   public static HUDRelativePositionAlign forValue(int value)
   {
      return values()[value];
   }
}
