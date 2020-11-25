package org.glob3.mobile.generated;
//
//  ILogger.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 25/07/12.
//

//
//  ILogger.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 31/05/12.
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