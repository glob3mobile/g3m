//
//  BoundingArea.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__BoundingArea__
#define __G3MiOSSDK__BoundingArea__

#include "Disposable.hpp"

class BoundingArea : public Disposable {
public:
  virtual ~BoundingArea() {
    JAVA_POST_DISPOSE
  }
  
};

#endif
