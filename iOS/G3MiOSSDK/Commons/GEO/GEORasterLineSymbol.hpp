//
//  GEORasterLineSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEORasterLineSymbol__
#define __G3MiOSSDK__GEORasterLineSymbol__

#include "GEORasterSymbol.hpp"

#include "Color.hpp"
class Geodetic2D;
#include "GEOLine2DRasterStyle.hpp"

class GEORasterLineSymbol : public GEORasterSymbol {
private:
#ifdef C_CODE
  mutable const std::vector<Geodetic2D*>* _coordinates;
  const GEOLine2DRasterStyle              _style;
#endif
#ifdef JAVA_CODE
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEOLine2DRasterStyle      _style;
#endif


public:
  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const GEOLine2DRasterStyle& style);
  
  ~GEORasterLineSymbol();

  void rasterize(ICanvas*                   canvas,
                 const GEORasterProjection* projection) const;


};

#endif
