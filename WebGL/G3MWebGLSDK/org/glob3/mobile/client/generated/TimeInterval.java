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

  // the next function is for compatibility with Java
  private TimeInterval(double milliseconds)
  {
	  _milliseconds = (int)milliseconds;
	int __ASK_JM_why;
  }

  public TimeInterval(TimeInterval other)
  {
	  _milliseconds = other._milliseconds;

  }

  public TimeInterval()
  {
	  _milliseconds = 0;
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