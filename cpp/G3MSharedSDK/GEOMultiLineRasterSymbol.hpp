//
//  GEOMultiLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEOMultiLineRasterSymbol__
#define __G3MiOSSDK__GEOMultiLineRasterSymbol__

#include "GEORasterSymbol.hpp"
#include "Color.hpp"
#include "GEO2DLineRasterStyle.hpp"
class GEO2DCoordinatesArrayData;

class GEOMultiLineRasterSymbol : public GEORasterSymbol {
private:
  const GEO2DCoordinatesArrayData* _coordinatesArrayData;
  
#ifdef C_CODE
  const GEO2DLineRasterStyle _style;
#endif
#ifdef JAVA_CODE
  private final GEO2DLineRasterStyle _style;
#endif

protected:
  void rawRasterize(ICanvas*                   canvas,
                    const GEORasterProjection* projection) const;

public:
  GEOMultiLineRasterSymbol(const GEO2DCoordinatesArrayData* coordinatesArrayData,
                           const GEO2DLineRasterStyle& style,
                           const int minTileLevel = -1,
                           const int maxTileLevel = -1);

  ~GEOMultiLineRasterSymbol();

  const Sector* getSector() const;

};

#endif
