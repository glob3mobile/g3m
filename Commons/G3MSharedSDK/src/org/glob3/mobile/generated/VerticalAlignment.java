package org.glob3.mobile.generated;
//
//  ICanvasUtils.cpp
//  G3M
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//

//
//  ICanvasUtils.hpp
//  G3M
//
//  Created by Mari Luz Mateo on 07/05/14.
//
//




//class ICanvas;
//class GFont;
//class Vector2F;


public enum VerticalAlignment
{
  Top,
  Middle,
  Bottom;

   public int getValue()
   {
      return this.ordinal();
   }

   public static VerticalAlignment forValue(int value)
   {
      return values()[value];
   }
}
