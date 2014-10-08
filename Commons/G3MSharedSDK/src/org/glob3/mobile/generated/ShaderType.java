package org.glob3.mobile.generated; 
//
//  IGPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

//
//  IGPUProgram.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//



//class GPUAttribute;
//class GPUUniform;
//class GL;

//class GPUUniformBool;
//class GPUUniformVec2Float;
//class GPUUniformVec4Float;
//class GPUUniformFloat;
//class GPUUniformMatrix4Float;
//class GPUAttributeVec1Float;
//class GPUAttributeVec2Float;
//class GPUAttributeVec3Float;
//class GPUAttributeVec4Float;
//class GPUVariable;
//class GPUUniformValue;
//class GPUAttributeValue;


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