package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public enum CenterStrategy
{
  NoCenter,
  AveragedVertex,
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