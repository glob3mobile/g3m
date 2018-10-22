//
//  GEO2DGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEO2DGeometry__
#define __G3MiOSSDK__GEO2DGeometry__

class Geodetic2D;

#include "GEOGeometry.hpp"

class GEORasterSymbol;

class GEO2DGeometry : public GEOGeometry  {
protected:
  virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const = 0;

public:

  virtual ~GEO2DGeometry() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  void rasterize(const GEORasterSymbolizer* symbolizer,
                 ICanvas* canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

  virtual GEO2DGeometry* deepCopy() const = 0;
  
  virtual bool contain(const Geodetic2D& point) const;

};

#endif
