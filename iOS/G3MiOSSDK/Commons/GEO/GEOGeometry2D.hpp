//
//  GEOGeometry2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEOGeometry2D__
#define __G3MiOSSDK__GEOGeometry2D__

class GEOGeometry2D {
public:
#ifdef C_CODE
  virtual ~GEOGeometry2D() { }
#endif
#ifdef JAVA_CODE
  public void dispose();
#endif
};

#endif
