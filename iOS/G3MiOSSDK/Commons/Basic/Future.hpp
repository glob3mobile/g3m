//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//

#ifndef __G3MiOSSDK__Future__
#define __G3MiOSSDK__Future__

#include <stddef.h>

template <class T>
class Future {
private:
  T* _value;

protected:
  Future() :
  _value(NULL)
  {
  }

  void set(T* value) {
    _value = value;
  }

public:
  virtual T* get() const = 0;

  virtual ~Future() {
    delete _value;
  }

};

#endif
