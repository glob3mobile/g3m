//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEO2DPointGeometry.hpp"

#include "GEOSymbolizer.hpp"

std::vector<GEOSymbol*>* GEO2DPointGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}
