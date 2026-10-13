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
  SCALE_AND_ALPHA,
  WIDTH, // scales the width only, around the anchor: a label at the side folds into its icon
  WIDTH_AND_ALPHA,
  HEIGHT, // the same for a label above or below its icon
  HEIGHT_AND_ALPHA;

   public int getValue()
   {
      return this.ordinal();
   }

   public static MarkTransitionMode forValue(int value)
   {
      return values()[value];
   }
}