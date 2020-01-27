//
//  RCObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 17/08/12.
//
//

#ifndef __G3MiOSSDK__RCObject__
#define __G3MiOSSDK__RCObject__


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

  RCObject(const RCObject& that);
  RCObject& operator=(const RCObject& that);

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

  bool _release() const {
    if (--_referenceCounter == 0) {
      _suicide();
      return true;
    }
    return false;
  }

  long _getReferenceCounter() const {
    return _referenceCounter;
  }

};

#endif
