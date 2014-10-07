package org.glob3.mobile.generated; 
//
//  CartoCSSTokens.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/17/13.
//
//

//
//  CartoCSSTokens.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/17/13.
//
//




public enum CartoCSSTokenType
{
  ERROR,
  STRING,
  OPEN_BRACE, // {
  CLOSE_BRACE, // }
  EXPRESION, // [ source ]
  COLON, // :
  SEMICOLON, // ;
  SKIP;

   public int getValue()
   {
      return this.ordinal();
   }

   public static CartoCSSTokenType forValue(int value)
   {
      return values()[value];
   }
}