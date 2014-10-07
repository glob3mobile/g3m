package org.glob3.mobile.generated; 
//
//  ILogger.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 25/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  ILogger.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public enum LogLevel
{
  SilenceLevel,
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