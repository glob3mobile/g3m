package org.glob3.mobile.generated; 
//
//  ShaderProgram.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  ShaderProgram.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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