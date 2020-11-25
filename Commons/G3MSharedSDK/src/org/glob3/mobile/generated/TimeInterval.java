package org.glob3.mobile.generated;
//
//  TimeInterval.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 13/06/12.
//

//
//  TimeInterval.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 13/06/12.
//



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
    return fromMilliseconds((long)(seconds * 1000.0));
  }

  public static TimeInterval fromMinutes(double minutes)
  {
    return fromSeconds(minutes * 60.0);
  }

  public static TimeInterval fromHours(double hours)
  {
    return fromMinutes(hours * 60.0);
  }

  public static TimeInterval fromDays(double days)
  {
    return fromHours(days * 24.0);
  }

  public static TimeInterval forever()
  {
    return new TimeInterval(IMathUtils.instance().maxInt64());
  }

  public static TimeInterval zero()
  {
    return new TimeInterval(0);
  }

  public final long milliseconds()
  {
    return _milliseconds;
  }

  public final double seconds()
  {
    return (double) _milliseconds / 1000.0;
  }

  public final boolean lowerThan(TimeInterval that)
  {
    return _milliseconds < that._milliseconds;
  }

  public final boolean isZero()
  {
    return _milliseconds == 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + (int) (_milliseconds ^ (_milliseconds >>> 32));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TimeInterval other = (TimeInterval) obj;
    if (_milliseconds != other._milliseconds) {
      return false;
    }
    return true;
  }

}