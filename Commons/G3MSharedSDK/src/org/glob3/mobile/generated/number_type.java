package org.glob3.mobile.generated; 
//
//  JSONNumber.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  JSONNumber.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public enum number_type
{
  int_type,
  float_type,
  double_type;

	public int getValue()
	{
		return this.ordinal();
	}

	public static number_type forValue(int value)
	{
		return values()[value];
	}
}