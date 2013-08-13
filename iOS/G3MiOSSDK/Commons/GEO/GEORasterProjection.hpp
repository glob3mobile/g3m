//
//  GEORasterProjection.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/11/13.
//
//

#ifndef __G3MiOSSDK__GEORasterProjection__
#define __G3MiOSSDK__GEORasterProjection__

#include "Vector2F.hpp"
class Geodetic2D;
#include "Sector.hpp"

class GEORasterProjection : public Disposable {
private:
  const Sector _sector;
  const bool   _mercator;
  const int    _imageWidth;
  const int    _imageHeight;

  double _mercatorUpperGlobalV;
  double _mercatorDeltaGlobalV;

public:

  GEORasterProjection(const Sector& sector,
                      bool mercator,
                      int imageWidth,
                      int imageHeight);
  
  ~GEORasterProjection() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  Vector2F project(const Geodetic2D* position) const;

};

#endif
