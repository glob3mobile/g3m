package org.glob3.mobile.generated; 
//
//  ILogger.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public enum LogLevel
{
  InfoLevel,
  WarningLevel,
  ErrorLevel;

	public int getValue()
	{
		return this.ordinal();
	}

	public static LogLevel forValue(int value)
	{
		return values()[value];
	}
}