//
//  GEOFeatureCollection.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOFeatureCollection.hpp"

#include "GEOFeature.hpp"
#include "Sector.hpp"
#include "Angle.hpp"


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


long long GEOFeatureCollection::getCoordinatesCount() const {
  long long result = 0;
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    result += feature->getCoordinatesCount();
  }
  return result;
}

const std::vector<GEOFeature*> GEOFeatureCollection::copy(const std::vector<GEOFeature*>& features) {
  std::vector<GEOFeature*> result;
  const size_t size = features.size();
  for (size_t i = 0; i < size; i++) {
    GEOFeature* feature = features[i];
    result.push_back( (feature == NULL) ? NULL : feature->deepCopy() );
  }
  return result;
}

GEOFeatureCollection* GEOFeatureCollection::deepCopy() const {
  return new GEOFeatureCollection( copy(_features) );
}

long long GEOFeatureCollection::createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                                   const VectorStreamingRenderer::Node*      node) const {
  long long result = 0;
  const size_t featuresCount = _features.size();
  for (size_t i = 0; i < featuresCount; i++) {
    GEOFeature* feature = _features[i];
    if (feature != NULL) {
      result += feature->createFeatureMarks(vectorSet, node);
    }
  }
  return result;
}

const Sector* GEOFeatureCollection::calculateSector() const {
  const size_t featuresCount = _features.size();
  if (featuresCount == 0) {
    return NULL;
  }

  const Sector* sector0 = _features[0]->getSector();

  double minLatRad = sector0->_lower._latitude._radians;
  double maxLatRad = sector0->_upper._latitude._radians;

  double minLonRad = sector0->_lower._longitude._radians;
  double maxLonRad = sector0->_upper._longitude._radians;;

  for (size_t i = 1; i < featuresCount; i++) {
    const Sector* sector = _features[i]->getSector();

    const double lowerLatRad = sector->_lower._latitude._radians;
    if (lowerLatRad < minLatRad) {
      minLatRad = lowerLatRad;
    }
    const double upperLatRad = sector->_upper._latitude._radians;
    if (upperLatRad > maxLatRad) {
      maxLatRad = upperLatRad;
    }

    const double lowerLonRad = sector->_lower._longitude._radians;
    if (lowerLonRad < minLonRad) {
      minLonRad = lowerLonRad;
    }
    const double upperLonRad = sector->_upper._longitude._radians;
    if (upperLonRad > maxLonRad) {
      maxLonRad = upperLonRad;
    }
  }

  const double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
  const double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;

  const double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
  const double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;

  return Sector::newFromRadians(lowerLatRadians, lowerLonRadians,
                                upperLatRadians, upperLonRadians);
}
