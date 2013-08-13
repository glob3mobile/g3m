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

GEO2DMultiPolygonGeometry::~GEO2DMultiPolygonGeometry() {
  if (_polygonsData != NULL) {
    const int polygonsDataSize = _polygonsData->size();
    for (int i = 0; i < polygonsDataSize; i++) {
      GEO2DPolygonData* polygonData = _polygonsData->at(i);
      delete polygonData;
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
