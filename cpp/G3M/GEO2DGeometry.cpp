//
//  GEO2DGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEO2DGeometry.hpp"

#include "GEORasterSymbol.hpp"

void GEO2DGeometry::rasterize(const GEORasterSymbolizer* symbolizer,
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


bool GEO2DGeometry::contain(const Geodetic2D& point) const {
  return false;
}

