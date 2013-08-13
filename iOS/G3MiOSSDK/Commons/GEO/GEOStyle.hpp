//
//  GEOStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#ifndef __G3MiOSSDK__GEOStyle__
#define __G3MiOSSDK__GEOStyle__

#include "Disposable.hpp"

class GEOStyle : public Disposable {
public:
  virtual ~GEOStyle() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
};

#endif
