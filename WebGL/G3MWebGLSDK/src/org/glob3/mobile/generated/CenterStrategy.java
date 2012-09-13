package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public enum CenterStrategy
{
  NoCenter,
  FirstVertex,
  GivenCenter;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CenterStrategy forValue(int value)
	{
		return values()[value];
	}
}