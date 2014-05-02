//
//  GEOGeometry2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEOGeometry2D.hpp"

#include "GEORasterSymbol.hpp"

void GEOGeometry2D::rasterize(const GEORasterSymbolizer* symbolizer,
                              ICanvas* canvas,
                              const GEORasterProjection* projection,
                              int tileLevel) const {

  std::vector<GEORasterSymbol*>* symbols = createRasterSymbols(symbolizer);
  if (symbols != NULL) {
    const int symbolsSize = symbols->size();
    for (int i = 0; i < symbolsSize; i++) {
      GEORasterSymbol* symbol = symbols->at(i);
      if (symbol != NULL) {
        symbol->rasterize(canvas, projection, tileLevel);
        delete symbol;
      }
    }

    delete symbols;
  }

}
