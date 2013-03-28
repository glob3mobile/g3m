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

GEOGeometry::~GEOGeometry() {

}

void GEOGeometry::setFeature(GEOFeature* feature) {
  if (_feature != feature) {
    delete _feature;
    _feature = feature;
  }
}

void GEOGeometry::symbolize(const G3MRenderContext* rc,
                            const GEOSymbolizationContext& sc) const {
  std::vector<GEOSymbol*>* symbols = createSymbols(rc, sc);
  if (symbols == NULL) {
    return;
  }

  const int symbolsSize = symbols->size();
  for (int i = 0; i < symbolsSize; i++) {
    const GEOSymbol* symbol = symbols->at(i);

    symbol->symbolize(rc, sc);

    delete symbol;
  }

  delete symbols;
}
