//
//  GEOGeometry2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEOGeometry2D__
#define __G3MiOSSDK__GEOGeometry2D__

class Geodetic2D;

#include "GEOGeometry.hpp"

class GEORasterSymbol;

class GEOGeometry2D : public GEOGeometry  {
protected:
protected:
  virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const = 0;

public:

  virtual ~GEOGeometry2D() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  void rasterize(const GEORasterSymbolizer* symbolizer,
                 ICanvas* canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

  virtual GEOGeometry2D* deepCopy() const = 0;
  
  virtual bool contain(const Geodetic2D& point) const;


};

#endif
