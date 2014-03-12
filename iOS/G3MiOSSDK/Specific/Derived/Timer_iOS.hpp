//
//  Timer_iOS.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Timer_iOS
#define G3MiOSSDK_Timer_iOS

#include "ITimer.hpp"

class Timer_iOS : public ITimer {
private:
  double _startedInSeconds;
  
  double nowInSeconds() const {
    return CACurrentMediaTime();
  }
  
public:
  
  Timer_iOS() {
    start();
  }
  
  TimeInterval now() const {
    return TimeInterval::fromSeconds(nowInSeconds());
  }
  
  void start() {
    _startedInSeconds = nowInSeconds();
  }
  
  TimeInterval elapsedTime() const {
    return TimeInterval::fromSeconds(nowInSeconds() - _startedInSeconds);
  }

  long long elapsedTimeInMilliseconds() const {
    return (long long) ((nowInSeconds() - _startedInSeconds) * 1000.0);
  }

};

#endif
