//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOFeatureCollection.hpp"

#include "GEOFeature.hpp"


GEOFeatureCollection::~GEOFeatureCollection() {
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    delete feature;
  }
  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
}

void GEOFeatureCollection::symbolize(const G3MRenderContext* rc,
                                     const GEOSymbolizer*    symbolizer,
                                     MeshRenderer*           meshRenderer,
                                     ShapesRenderer*         shapesRenderer,
                                     MarksRenderer*          marksRenderer,
                                     GEOVectorLayer*         geoVectorLayer) const {
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    feature->symbolize(rc,
                       symbolizer,
                       meshRenderer,
                       shapesRenderer,
                       marksRenderer,
                       geoVectorLayer);
  }
}

void GEOFeatureCollection::rasterize(const GEORasterSymbolizer* symbolizer,
                                     ICanvas* canvas,
                                     const GEORasterProjection* projection,
                                     int tileLevel) const {
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    if (feature != NULL) {
      feature->rasterize(symbolizer,
                         canvas,
                         projection,
                         tileLevel);
    }
  }
}

int GEOFeatureCollection::symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                                    const VectorStreamingRenderer::Node*      node) const {
  int result = 0;
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    if (feature != NULL) {
      result += feature->symbolize(vectorSet, node);
    }
  }
  return result;
}
