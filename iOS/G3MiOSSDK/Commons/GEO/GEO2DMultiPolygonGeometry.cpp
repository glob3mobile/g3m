//
//  GEO2DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#include "GEO2DMultiPolygonGeometry.hpp"

#include "GEOSymbolizationContext.hpp"
#include "GEOSymbolizer.hpp"


GEO2DMultiPolygonGeometry::~GEO2DMultiPolygonGeometry() {
  delete _polygonsData;
}


std::vector<GEOSymbol*>* GEO2DMultiPolygonGeometry::createSymbols(const G3MRenderContext* rc,
                                                                  const GEOSymbolizationContext& sc) const {
  return sc.getSymbolizer()->createSymbols(this);
}
