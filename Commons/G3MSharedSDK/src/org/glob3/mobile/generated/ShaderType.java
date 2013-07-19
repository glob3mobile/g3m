package org.glob3.mobile.generated; 
//
//  GPUProgram.cpp
//  G3MiOSSDK
//
<<<<<<< HEAD
//  Created by Jose Miguel SN on 27/03/13.
//
=======
//  Created by Agustin Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
>>>>>>> webgl-port
//

//
//  GPUProgram.h
//  G3MiOSSDK
//
<<<<<<< HEAD
//  Created by Jose Miguel SN on 27/03/13.
//
=======
//  Created by Agustin Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
>>>>>>> webgl-port
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