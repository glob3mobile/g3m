//
//  GEO2DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#include "GEO2DMultiPolygonGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEO2DPolygonData.hpp"
#include "GEORasterSymbolizer.hpp"


GEO2DMultiPolygonGeometry::~GEO2DMultiPolygonGeometry() {
  if (_polygonsData != NULL) {
    const size_t polygonsDataSize = _polygonsData->size();
    for (size_t i = 0; i < polygonsDataSize; i++) {
      GEO2DPolygonData* polygonData = _polygonsData->at(i);
      if (polygonData != NULL) {
        polygonData->_release();
      }
    }
    delete _polygonsData;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

std::vector<GEOSymbol*>* GEO2DMultiPolygonGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO2DMultiPolygonGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

bool GEO2DMultiPolygonGeometry::contain(const Geodetic2D& point) const {
  if (_polygonsData == NULL) {
    return false;
  }
  const size_t polygonsDataSize = _polygonsData->size();
  for (size_t i = 0; i < polygonsDataSize; i++) {
    GEO2DPolygonData* polygonData = _polygonsData->at(i);
    if (polygonData->contains(point)) {
      return true;
    }
  }

  return false;
}
