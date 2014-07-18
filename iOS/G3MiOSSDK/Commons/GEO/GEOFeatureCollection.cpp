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
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
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
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
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
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    if (feature != NULL) {
      feature->rasterize(symbolizer,
                         canvas,
                         projection,
                         tileLevel);
    }
  }
}


long long GEOFeatureCollection::getCoordinatesCount() const {
  long long result = 0;
  const int featuresCount = _features.size();
  for (int i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    result += feature->getCoordinatesCount();
  }
  return result;
}

const std::vector<GEOFeature*> GEOFeatureCollection::copy(const std::vector<GEOFeature*>& features) {
  std::vector<GEOFeature*> result;
  const int size = features.size();
  for (int i = 0; i < size; i++) {
    GEOFeature* feature = features[i];
    result.push_back( (feature == NULL) ? NULL : feature->deepCopy() );
  }
  return result;
}

GEOFeatureCollection* GEOFeatureCollection::deepCopy() const {
  return new GEOFeatureCollection( copy(_features) );
}
