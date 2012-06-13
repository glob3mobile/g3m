package org.glob3.mobile.generated; 
//
//  TimeInterval.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class TimeInterval
{
  private final int _milliseconds;

  private TimeInterval(int milliseconds)
  {
	  _milliseconds = milliseconds;

  }


  public TimeInterval(TimeInterval other)
  {
	  _milliseconds = other._milliseconds;

  }


  public static TimeInterval fromMilliseconds(int milliseconds)
  {
	return new TimeInterval(milliseconds);
  }

  public static TimeInterval fromSeconds(double seconds)
  {
	return TimeInterval.fromMilliseconds((int)(seconds *1000.0));
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int milliseconds() const
  public final int milliseconds()
  {
	return _milliseconds;
  }

}