package org.glob3.mobile.generated; 
//
//  LabelPosition.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/14/13.
//
//


public enum LabelPosition
{
  Bottom,
  Right;

   public int getValue()
   {
      return this.ordinal();
   }

   public static LabelPosition forValue(int value)
   {
      return values()[value];
   }
}