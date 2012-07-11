package org.glob3.mobile.generated; 
//
//  IGL.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public enum CullFace
{
  FRONT,
  BACK,
  FRONT_AND_BACK;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CullFace forValue(int value)
	{
		return values()[value];
	}
}