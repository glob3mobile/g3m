//
//  GEO3DLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Nico on 22/10/2018.
//

#include "GEO3DLineStringGeometry.hpp"

#include "Geodetic3D.hpp"
#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"

GEO3DLineStringGeometry::~GEO3DLineStringGeometry() {
  if (_coordinatesData != NULL) {
    _coordinatesData->_release();
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO3DLineStringGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO3DLineStringGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

GEO3DLineStringGeometry* GEO3DLineStringGeometry::deepCopy() const {
  return new GEO3DLineStringGeometry(_coordinatesData);
}
