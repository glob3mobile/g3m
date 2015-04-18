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
#include "GEORasterSymbolizer.hpp"

GEO2DLineStringGeometry::~GEO2DLineStringGeometry() {
  if (_coordinatesData != NULL) {
    _coordinatesData->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO2DLineStringGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO2DLineStringGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

GEO2DLineStringGeometry* GEO2DLineStringGeometry::deepCopy() const {
  return new GEO2DLineStringGeometry(_coordinatesData);
}
