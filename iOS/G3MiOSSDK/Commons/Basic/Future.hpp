//
//  Future.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/31/13.
//
//

#ifndef __G3MiOSSDK__Future__
#define __G3MiOSSDK__Future__

template <class T>
class Future {
private:
  T _value;

public:
  virtual T get() const = 0;


};

#endif
