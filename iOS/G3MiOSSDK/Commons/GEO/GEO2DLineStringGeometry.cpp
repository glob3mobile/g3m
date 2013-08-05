//
//  GEO2DLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEO2DLineStringGeometry.hpp"

#include "Geodetic2D.hpp"
#include "GEOSymbolizer.hpp"
#include "GEOSymbolizationContext.hpp"

GEO2DLineStringGeometry::~GEO2DLineStringGeometry() {
  const int coordinatesCount = _coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = _coordinates->at(i);
    delete coordinate;
  }
  delete _coordinates;
}

std::vector<GEOSymbol*>* GEO2DLineStringGeometry::createSymbols(const G3MRenderContext* rc,
                                                                const GEOSymbolizationContext& sc) const {
  return sc.getSymbolizer()->createSymbols(this);
}
