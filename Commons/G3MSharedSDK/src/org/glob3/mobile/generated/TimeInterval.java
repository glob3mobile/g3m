package org.glob3.mobile.generated;import java.util.*;

//
//  TimeInterval.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//

//
//  TimeInterval.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//


///#include "IMathUtils.hpp"


public class TimeInterval
{
  private TimeInterval(long milliseconds)
  {
	  _milliseconds = milliseconds;
  }

  public final long _milliseconds;

  public TimeInterval(TimeInterval other)
  {
	  _milliseconds = other._milliseconds;
  }

  public static TimeInterval fromMilliseconds(long milliseconds)
  {
	return new TimeInterval(milliseconds);
  }

  public static TimeInterval fromSeconds(double seconds)
  {
	return TimeInterval.fromMilliseconds((long)(seconds * 1000.0));
  }

  public static TimeInterval fromMinutes(double minutes)
  {
	return TimeInterval.fromSeconds(minutes * 60.0);
  }

  public static TimeInterval fromHours(double hours)
  {
	return TimeInterval.fromMinutes(hours * 60.0);
  }

  public static TimeInterval fromDays(double days)
  {
	return TimeInterval.fromHours(days * 24.0);
  }

  public static TimeInterval forever()
  {
	return new TimeInterval(IMathUtils.instance().maxInt64());
  }

  public static TimeInterval zero()
  {
	return new TimeInterval(0);
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isZero() const
  public final boolean isZero()
  {
	return _milliseconds == 0;
  }


//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public int hashCode()
  {
	final int prime = 31;
	int result = 1;
	result = (prime * result) + (int)(_milliseconds ^ (_milliseconds >>> 32));
	return result;
  }


  public Override public boolean equals(final Object obj)
  {
	if (this == obj)
	{
	  return true;
	}
	if (obj == null)
	{
	  return false;
	}
	if (getClass() != obj.getClass())
	{
	  return false;
	}
	final TimeInterval other = (TimeInterval) obj;
	if (_milliseconds != other._milliseconds)
	{
	  return false;
	}
	return true;
  }
//#endif

}
