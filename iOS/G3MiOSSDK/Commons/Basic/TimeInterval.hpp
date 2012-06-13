//
//  TimeInterval.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_TimeInterval_hpp
#define G3MiOSSDK_TimeInterval_hpp


class TimeInterval {
private:
  const long _milliseconds;
  
  TimeInterval(const long milliseconds) : _milliseconds(milliseconds) {
    
  }
  
  
public:
  TimeInterval(const TimeInterval& other) : _milliseconds(other._milliseconds) {
    
  }
  
  
  static TimeInterval fromMilliseconds(const long milliseconds) {
    return TimeInterval(milliseconds);
  }
  
  static TimeInterval fromSeconds(const double seconds) {
    return TimeInterval::fromMilliseconds(seconds * 1000.0);
  }
  
  
  long milliseconds() const {
    return _milliseconds;
  }
  
};


#endif
