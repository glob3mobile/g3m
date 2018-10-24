//
//  GEO3DPointGeometry.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/21/18.
//

#include "GEO3DPointGeometry.hpp"

#include "GEOSymbolizer.hpp"
#include "GEORasterSymbolizer.hpp"
#include "Sector.hpp"


std::vector<GEOSymbol*>* GEO3DPointGeometry::createSymbols(const GEOSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

std::vector<GEORasterSymbol*>* GEO3DPointGeometry::createRasterSymbols(const GEORasterSymbolizer* symbolizer) const {
  return symbolizer->createSymbols(this);
}

GEO3DPointGeometry* GEO3DPointGeometry::deepCopy() const {
  return new GEO3DPointGeometry(_position);
}

long long GEO3DPointGeometry::createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                                 const VectorStreamingRenderer::Node*      node) const {
  return vectorSet->createFeatureMark(node, this);
}

const Sector* GEO3DPointGeometry::calculateSector() const {
  const double lowerLatRadians = _position._latitude._radians - 0.0001;
  const double upperLatRadians = _position._latitude._radians + 0.0001;

  const double lowerLonRadians = _position._longitude._radians - 0.0001;
  const double upperLonRadians = _position._longitude._radians + 0.0001;

  return Sector::newFromRadians(lowerLatRadians, lowerLonRadians,
                                upperLatRadians, upperLonRadians);
}
