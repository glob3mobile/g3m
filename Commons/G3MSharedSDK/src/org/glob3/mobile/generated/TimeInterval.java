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

}