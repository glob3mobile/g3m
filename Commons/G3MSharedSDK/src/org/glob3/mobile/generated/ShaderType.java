package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttribute;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniform;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformBool;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformVec2Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformVec4Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformFloat;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformMatrix4Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttributeVec1Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttributeVec2Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttributeVec3Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttributeVec4Float;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUUniformValue;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUAttributeValue;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
