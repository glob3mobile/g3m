//
//  GEO3DGeometry.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

#ifndef GEO3DGeometry_hpp
#define GEO3DGeometry_hpp

class Geodetic3D;

#include "GEOGeometry.hpp"

class GEORasterSymbol;

class GEO3DGeometry : public GEOGeometry  {
protected:
  virtual std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const = 0;

public:

  virtual ~GEO3DGeometry() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void rasterize(const GEORasterSymbolizer* symbolizer,
                 ICanvas* canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

  virtual bool contain(const Geodetic3D& point) const;

};

#endif
