//
//  GEOGeometry2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEOGeometry2D__
#define __G3MiOSSDK__GEOGeometry2D__

#include "GEOGeometry.hpp"

class GEOGeometry2D : public GEOGeometry  {
public:

  virtual ~GEOGeometry2D() {
    JAVA_POST_DISPOSE
  }

};

#endif
