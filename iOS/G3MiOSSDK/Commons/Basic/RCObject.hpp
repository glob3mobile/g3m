//
//  RCObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#ifndef __G3MiOSSDK__RCObject__
#define __G3MiOSSDK__RCObject__

#include "ILogger.hpp"

class RCObject {
private:
  mutable long _referenceCounter;

  void _suicide() const {
#ifdef C_CODE
    delete this;
#endif
#ifdef JAVA_CODE
    this.dispose();
#endif
  }

protected:
  RCObject():
  _referenceCounter(1) // the object starts retained
  {

  }

  virtual ~RCObject();

public:

  void _retain() const {
    _referenceCounter++;
  }

  void _release() const {
    if (--_referenceCounter == 0) {
      _suicide();
    }
  }
  
};

#endif
