package org.glob3.mobile.generated; 
//
//  ICanvas.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

//
//  ICanvas.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//


//class Color;
//class IImageListener;
//class GFont;
//class IImage;


public enum StrokeCap
{
  CAP_BUTT,
  CAP_ROUND,
  CAP_SQUARE;

   public int getValue()
   {
      return this.ordinal();
   }

   public static StrokeCap forValue(int value)
   {
      return values()[value];
   }
}