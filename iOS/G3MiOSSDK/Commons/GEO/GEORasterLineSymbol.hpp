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
#include "GEO2DLineRasterStyle.hpp"

class GEORasterLineSymbol : public GEORasterSymbol {
private:
#ifdef C_CODE
  const std::vector<Geodetic2D*>* _coordinates;
  const GEO2DLineRasterStyle      _style;
#endif
#ifdef JAVA_CODE
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _style;
#endif


public:
  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const GEO2DLineRasterStyle& style,
                      const int minTileLevel = -1,
                      const int maxTileLevel = -1);

  ~GEORasterLineSymbol();

  void rawRasterize(ICanvas*                   canvas,
                    const GEORasterProjection* projection) const;


};

#endif
