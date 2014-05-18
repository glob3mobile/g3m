//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEO2DPointGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"

std::vector<GEOSymbol*>* GEO2DPointGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO2DPointGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

const GEO2DPointGeometry* GEO2DPointGeometry::deepCopy() const {
  return new GEO2DPointGeometry(_position);
}
