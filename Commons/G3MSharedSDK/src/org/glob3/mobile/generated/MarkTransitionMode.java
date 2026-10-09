package org.glob3.mobile.generated;
//
//  MarkTransitionMode.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

//
//  MarkTransitionMode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//


// how an outfit comes in and goes away when a decluttering MarksRenderer changes it
public enum MarkTransitionMode
{
  SCALE,
  ALPHA,
  SCALE_AND_ALPHA;

   public int getValue()
   {
      return this.ordinal();
   }

   public static MarkTransitionMode forValue(int value)
   {
      return values()[value];
   }
}