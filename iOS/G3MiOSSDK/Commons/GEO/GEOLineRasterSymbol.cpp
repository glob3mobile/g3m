//
//  GEOLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOLineRasterSymbol.hpp"

#include "Tile.hpp"
#include "ICanvas.hpp"

GEOLineRasterSymbol::GEOLineRasterSymbol(const std::vector<Geodetic2D*>* coordinates,
                                         const GEO2DLineRasterStyle& style,
                                         const int minTileLevel,
                                         const int maxTileLevel):
GEORasterSymbol(calculateSectorFromCoordinates(coordinates),
                minTileLevel,
                maxTileLevel),
_coordinates( copyCoordinates(coordinates) ),
_style(style)
{
}


GEOLineRasterSymbol::~GEOLineRasterSymbol() {
  if (_coordinates != NULL) {
    const int size = _coordinates->size();

    for (int i = 0; i < size; i++) {
      const Geodetic2D* coordinate = _coordinates->at(i);
      delete coordinate;
    }

    delete _coordinates;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void GEOLineRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                       const GEORasterProjection* projection) const {
  if (_style.apply(canvas)) {
    rasterLine(_coordinates,
               canvas,
               projection);
  }
}
