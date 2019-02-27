//
//  Timer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//

#ifndef G3MiOSSDK_Timer_iOS
#define G3MiOSSDK_Timer_iOS

#include "ITimer.hpp"

class Timer_iOS : public ITimer {
private:
  double _startTimeInSeconds;

  static double nowInSeconds() {
    return CACurrentMediaTime();
  }
  
public:
  
  Timer_iOS() {
    start();
  }
  
  TimeInterval now() const {
    return TimeInterval::fromSeconds(nowInSeconds());
  }

  TimeInterval fromDaysFromNow(const double days) const {
    const double daysInSeconds = (days
                                  * 24 /* hours */
                                  * 60 /* minutes */
                                  * 60 /* seconds */);

    const double seconds = nowInSeconds() + daysInSeconds;

    return TimeInterval::fromSeconds(seconds);
  }

  long long nowInMilliseconds() const {
    return (long long) (nowInSeconds() * 1000.0);
  }

  void start() {
    _startTimeInSeconds = nowInSeconds();
  }
  
  TimeInterval elapsedTime() const {
    return TimeInterval::fromSeconds(nowInSeconds() - _startTimeInSeconds);
  }

  long long elapsedTimeInMilliseconds() const {
    return (long long) ((nowInSeconds() - _startTimeInSeconds) * 1000.0);
  }

};

#endif
