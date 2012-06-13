//
//  ITimer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_ITimer_hpp
#define G3MiOSSDK_ITimer_hpp

class ITimer {
public:
  virtual double now() const = 0;
  
  virtual void start() = 0;
  
  virtual double elapsedTime() const = 0;
  
  virtual ~ITimer() { }
  
};

#endif
