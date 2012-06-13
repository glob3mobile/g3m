//
//  Timer_iOS.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Timer_iOS_h
#define G3MiOSSDK_Timer_iOS_h

#include "ITimer.hpp"

class Timer_iOS : public ITimer {
private:
  double _started;
  
  
public:
  
  Timer_iOS() {
    start();
  }
  
  double now() const {
    return CACurrentMediaTime();
  }

  virtual void start() {
    _started = now();
  }
  
  virtual double elapsedTime() const {
    return (now() - _started);
  }
  
};

#endif
