package org.glob3.mobile.generated; 
//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

//
//  GPUProgram.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//



//class GPUAttribute;
//class GPUUniform;

//class GPUUniformBool;
//class GPUUniformVec2Float;
//class GPUUniformVec4Float;
//class GPUUniformFloat;
//class GPUUniformMatrix4Float;
//class GPUAttributeVec1Float;
//class GPUAttributeVec2Float;
//class GPUAttributeVec3Float;
//class GPUAttributeVec4Float;
//class GPUUniformValue;
//class GPUAttributeValue;
//class IFloatBuffer;
//class GL;
//class GPUProgramManager;

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