//
//  GEO2DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEO2DPointGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEOSymbolizationContext.hpp"

std::vector<GEOSymbol*>* GEO2DPointGeometry::createSymbols(const G3MRenderContext* rc,
                                                           const GEOSymbolizationContext& sc) const {
  return sc.getSymbolizer()->createSymbols(this);
}
