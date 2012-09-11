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
  private final long _milliseconds;

  private TimeInterval(long milliseconds)
  {
	  _milliseconds = milliseconds;

  }

  public TimeInterval(TimeInterval other)
  {
	  _milliseconds = other._milliseconds;

  }

  public TimeInterval()
  {
	  _milliseconds = 0;
  }

  public static TimeInterval fromMilliseconds(long milliseconds)
  {
	return new TimeInterval(milliseconds);
  }

  public static TimeInterval fromSeconds(double seconds)
  {
	return TimeInterval.fromMilliseconds((long)(seconds *1000.0));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long milliseconds() const
  public final long milliseconds()
  {
	return _milliseconds;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double seconds() const
  public final double seconds()
  {
	return (double) _milliseconds / 1000.0;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThan(const TimeInterval& that) const
  public final boolean lowerThan(TimeInterval that)
  {
	return _milliseconds < that._milliseconds;
  }

}