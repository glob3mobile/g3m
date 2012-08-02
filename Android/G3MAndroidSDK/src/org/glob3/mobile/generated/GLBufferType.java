package org.glob3.mobile.generated; 
//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public enum GLBufferType
{
	ColorBuffer,
	DepthBuffer;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLBufferType forValue(int value)
	{
		return values()[value];
	}
}