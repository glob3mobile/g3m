package org.glob3.mobile.generated;//
//  ViewMode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/5/16.
//
//

//
//  ViewMode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/5/16.
//
//


public enum ViewMode
{
  MONO,
  STEREO;

   public int getValue()
   {
      return this.ordinal();
   }

   public static ViewMode forValue(int value)
   {
      return values()[value];
   }
}
