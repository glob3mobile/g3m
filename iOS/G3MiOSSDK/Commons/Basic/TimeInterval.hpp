//
//  TimeInterval.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TimeInterval
#define G3MiOSSDK_TimeInterval

#include "IMathUtils.hpp"


class TimeInterval {
private:

  explicit TimeInterval(const long long milliseconds) :
  _milliseconds(milliseconds)
  {

  }

public:
  const long long _milliseconds;

  TimeInterval(const TimeInterval& other) :
  _milliseconds(other._milliseconds)
  {

  }

  static TimeInterval fromMilliseconds(const long long milliseconds) {
    return TimeInterval(milliseconds);
  }

  static TimeInterval fromSeconds(const double seconds) {
    return TimeInterval::fromMilliseconds((long long) (seconds * 1000.0));
  }

  static TimeInterval fromMinutes(const double minutes) {
    return TimeInterval::fromSeconds(minutes * 60.0);
  }

  static TimeInterval fromHours(const double hours) {
    return TimeInterval::fromMinutes(hours * 60.0);
  }

  static TimeInterval fromDays(const double days) {
    return TimeInterval::fromHours(days * 24.0);
  }

  static TimeInterval forever() {
    return TimeInterval(IMathUtils::instance()->maxInt64());
  }

  static TimeInterval zero() {
    return TimeInterval(0);
  }

  long long milliseconds() const {
    return _milliseconds;
  }

  double seconds() const {
    return (double) _milliseconds / 1000.0;
  }

  bool lowerThan(const TimeInterval& that) const {
    return _milliseconds < that._milliseconds;
  }

  bool isZero() const {
    return _milliseconds == 0;
  }

  
#ifdef JAVA_CODE
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
#endif

};


#endif
