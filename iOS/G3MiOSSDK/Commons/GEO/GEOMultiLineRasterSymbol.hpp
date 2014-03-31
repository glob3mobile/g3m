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

class GEOMultiLineRasterSymbol : public GEORasterSymbol {
private:
#ifdef C_CODE
  mutable const std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;
  const GEO2DLineRasterStyle                            _style;
#else
  std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;
#endif
#ifdef JAVA_CODE
  //private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;
  private final GEO2DLineRasterStyle                           _style;
#endif

protected:
  void rawRasterize(ICanvas*                   canvas,
                    const GEORasterProjection* projection) const;

public:
  GEOMultiLineRasterSymbol(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                           const GEO2DLineRasterStyle& style,
                           const int minTileLevel = -1,
                           const int maxTileLevel = -1);

  ~GEOMultiLineRasterSymbol();

};

#endif
