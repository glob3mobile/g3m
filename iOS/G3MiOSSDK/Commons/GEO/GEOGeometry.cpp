//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEOGeometry.hpp"
#include "GEOSymbol.hpp"
#include "GEOFeature.hpp"


void GEOGeometry::setFeature(GEOFeature* feature) {
  if (_feature != feature) {
    delete _feature;
    _feature = feature;
  }
}

void GEOGeometry::symbolize(const G3MRenderContext* rc,
                            const GEOSymbolizer*    symbolizer,
                            MeshRenderer*           meshRenderer,
                            ShapesRenderer*         shapesRenderer,
                            MarksRenderer*          marksRenderer,
                            GEOTileRasterizer*      geoTileRasterizer) const {
  std::vector<GEOSymbol*>* symbols = createSymbols(symbolizer);
  if (symbols != NULL) {

    const int symbolsSize = symbols->size();
    for (int i = 0; i < symbolsSize; i++) {
      const GEOSymbol* symbol = symbols->at(i);
      if (symbol != NULL) {
        const bool deleteSymbol = symbol->symbolize(rc,
                                                    symbolizer,
                                                    meshRenderer,
                                                    shapesRenderer,
                                                    marksRenderer,
                                                    geoTileRasterizer);
        if (deleteSymbol) {
          delete symbol;
        }
      }
    }

    delete symbols;
  }
}
