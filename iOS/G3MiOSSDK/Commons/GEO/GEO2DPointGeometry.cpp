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

GEO2DPointGeometry* GEO2DPointGeometry::deepCopy() const {
  return new GEO2DPointGeometry(_position);
}

long long GEO2DPointGeometry::createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                                 const VectorStreamingRenderer::Node*      node) const {
  return vectorSet->createFeatureMark(node, this);
}
