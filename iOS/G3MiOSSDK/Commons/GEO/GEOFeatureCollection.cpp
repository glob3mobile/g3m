//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOFeatureCollection.hpp"

#include "GEOFeature.hpp"

//void GEOFeatureCollection::addFeature(GEOFeature* feature) {
//  _features.push_back(feature);
//}

//void GEOFeatureCollection::render(const G3MRenderContext* rc,
//                                  const GLGlobalState& parentState, const GPUProgramState* parentProgramState,
//                                  const GEOSymbolizer* symbolizer) {
//  const int featuresCount = _features.size();
//  for (int i = 0; i < featuresCount; i++) {
//    GEOFeature* feature = _features[i];
//    feature->render(rc, parentState, parentProgramState, symbolizer);
//  }
//}

GEOFeatureCollection::~GEOFeatureCollection() {
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    delete feature;
  }
}

void GEOFeatureCollection::symbolize(const G3MRenderContext* rc,
                                     const GEOSymbolizationContext& sc) const {
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    feature->symbolize(rc, sc);
  }
}
