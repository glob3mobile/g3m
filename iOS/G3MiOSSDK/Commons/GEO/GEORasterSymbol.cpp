//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEORasterSymbol.hpp"

#include "GEOSymbolizationContext.hpp"
#include "GEOTileRasterizer.hpp"

void GEORasterSymbol::symbolize(const G3MRenderContext* rc,
                                const GEOSymbolizationContext& sc) const {
  if (_sector != NULL) {
    sc.getGEOTileRasterizer()->addSymbol( createSymbol() );
  }
}
