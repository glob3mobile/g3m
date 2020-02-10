//
//  GEO2DPointGeometry.cpp
//  G3M
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

int GEO2DPointGeometry::symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                                  const VectorStreamingRenderer::Node*      node) const {
  return vectorSet->symbolizeGeometry(node, this);
}
