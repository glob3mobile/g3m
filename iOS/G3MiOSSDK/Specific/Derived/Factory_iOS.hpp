//
//  Factory_iOS.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Factory_iOS_h
#define G3MiOSSDK_Factory_iOS_h

#include "IFactory.hpp"

#include "Timer_iOS.h"


class Factory_iOS: public IFactory {
public:
  virtual ITimer* createTimer() const {
    return new Timer_iOS();
  }
  
  virtual void deleteTimer(ITimer* timer) const {
    delete ((Timer_iOS *) timer);
  }
  
};

#endif
