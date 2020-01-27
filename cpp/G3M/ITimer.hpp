//
//  ITimer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/06/12.
//

#ifndef G3M_ITimer
#define G3M_ITimer

#include "TimeInterval.hpp"

class ITimer {
public:
  virtual TimeInterval now() const = 0;

  virtual TimeInterval fromDaysFromNow(const double days) const = 0;

  virtual long long nowInMilliseconds() const = 0;

  virtual void start() = 0;
  
  virtual TimeInterval elapsedTime() const = 0;

  virtual long long elapsedTimeInMilliseconds() const = 0;

  virtual ~ITimer() {
  }
  
};

#endif
