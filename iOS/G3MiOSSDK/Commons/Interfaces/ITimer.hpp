//
//  ITimer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_ITimer
#define G3MiOSSDK_ITimer

#include "TimeInterval.hpp"

class ITimer {
public:
  virtual TimeInterval now() const = 0;
  
  virtual void start() = 0;
  
  virtual TimeInterval elapsedTime() const = 0;

  virtual long long elapsedTimeInMilliseconds() const = 0;

  virtual ~ITimer() {
  }
  
};

#endif
