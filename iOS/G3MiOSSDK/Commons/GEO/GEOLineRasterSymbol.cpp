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

GEOLineRasterSymbol::GEOLineRasterSymbol(const GEO2DCoordinatesData* coordinates,
                                         const GEO2DLineRasterStyle& style,
                                         const int minTileLevel,
                                         const int maxTileLevel):
GEORasterSymbol(//calculateSectorFromCoordinates(coordinates),
                minTileLevel,
                maxTileLevel),
_coordinates( coordinates ),
_style(style)
{
  if (_coordinates != NULL) {
    _coordinates->_retain();
  }
}

const Sector* GEOLineRasterSymbol::getSector() const {
  return (_coordinates == NULL) ? NULL : _coordinates->getSector();
}

GEOLineRasterSymbol::~GEOLineRasterSymbol() {
  if (_coordinates != NULL) {
//    const int size = _coordinates->size();
//
//    for (int i = 0; i < size; i++) {
//      const Geodetic2D* coordinate = _coordinates->at(i);
//      delete coordinate;
//    }
//
//    delete _coordinates;
    _coordinates->_release();
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
