package org.glob3.mobile.generated;
//
//  ShaderType.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

//
//  ShaderType.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//


public enum ShaderType
{
  VERTEX_SHADER,
  FRAGMENT_SHADER;

   public int getValue()
   {
      return this.ordinal();
   }

   public static ShaderType forValue(int value)
   {
      return values()[value];
   }
}
