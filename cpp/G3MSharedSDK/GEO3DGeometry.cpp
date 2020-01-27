//
//  GEO3DGeometry.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

#include "GEO3DGeometry.hpp"

#include "GEORasterSymbol.hpp"

void GEO3DGeometry::rasterize(const GEORasterSymbolizer* symbolizer,
                              ICanvas* canvas,
                              const GEORasterProjection* projection,
                              int tileLevel) const {

  std::vector<GEORasterSymbol*>* symbols = createRasterSymbols(symbolizer);
  if (symbols != NULL) {
    const size_t symbolsSize = symbols->size();
    for (size_t i = 0; i < symbolsSize; i++) {
      GEORasterSymbol* symbol = symbols->at(i);
      if (symbol != NULL) {
        symbol->rasterize(canvas, projection, tileLevel);
        delete symbol;
      }
    }

    delete symbols;
  }
}


bool GEO3DGeometry::contain(const Geodetic3D& point) const {
  return false;
}

