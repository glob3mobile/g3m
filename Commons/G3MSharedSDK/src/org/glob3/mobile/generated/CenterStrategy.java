package org.glob3.mobile.generated;
//
//  CenterStrategy.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/27/18.
//

//
//  CenterStrategy.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/27/18.
//



public enum CenterStrategy
{
  NO_CENTER,
  FIRST_VERTEX,
  GIVEN_CENTER;

   public int getValue()
   {
      return this.ordinal();
   }

   public static CenterStrategy forValue(int value)
   {
      return values()[value];
   }
}
