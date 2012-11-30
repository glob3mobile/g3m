//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOFeatureCollection.hpp"

#include "GEOFeature.hpp"

void GEOFeatureCollection::addFeature(GEOFeature* feature) {
  _features.push_back(feature);
}

GEOFeatureCollection::~GEOFeatureCollection() {
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    delete feature;
  }
}
