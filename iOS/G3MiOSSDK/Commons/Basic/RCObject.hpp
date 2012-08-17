//
//  RCObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#ifndef __G3MiOSSDK__RCObject__
#define __G3MiOSSDK__RCObject__

//#include <string>

class RCObject {
private:
  mutable long _referenceCounter;

  void _suicide() const {
    delete this;
  }
  
protected:
  RCObject():
  _referenceCounter(1) // the object starts retained
  {
    
  }
  
public:
  virtual ~RCObject() {
    
  }
  
  void _retain() const {
    _referenceCounter++;
  }
  
  void _release() const {
    if (--_referenceCounter == 0) {
      _suicide();
    }
  }
  
//  virtual const std::string description() const = 0;
  
};

#endif
