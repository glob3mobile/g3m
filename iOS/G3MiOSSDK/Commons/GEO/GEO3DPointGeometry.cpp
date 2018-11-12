//
//  GEO3DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

#include "GEO3DPointGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"


std::vector<GEOSymbol*>* GEO3DPointGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO3DPointGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

int GEO3DPointGeometry::symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                                  const VectorStreamingRenderer::Node*      node) const {
  return vectorSet->symbolizeGeometry(node, this);
}
