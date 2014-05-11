//
//  GEOLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEOLineRasterSymbol__
#define __G3MiOSSDK__GEOLineRasterSymbol__

#include "GEORasterSymbol.hpp"

#include "Color.hpp"
class Geodetic2D;
#include "GEO2DLineRasterStyle.hpp"
#include "GEO2DCoordinatesData.hpp"

class GEOLineRasterSymbol : public GEORasterSymbol {
private:
  const GEO2DCoordinatesData* _coordinates;
#ifdef C_CODE
  const GEO2DLineRasterStyle      _style;
#endif
#ifdef JAVA_CODE
  private final GEO2DLineRasterStyle      _style;
#endif

protected:
  void rawRasterize(ICanvas*                   canvas,
                    const GEORasterProjection* projection) const;

public:
  GEOLineRasterSymbol(const GEO2DCoordinatesData* coordinates,
                      const GEO2DLineRasterStyle& style,
                      const int minTileLevel = -1,
                      const int maxTileLevel = -1);

  ~GEOLineRasterSymbol();

  const Sector* getSector() const;

};

#endif
