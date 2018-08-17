//
//  GEOFeature.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

#include "GEOFeature.hpp"

#include "GEOGeometry.hpp"
#include "JSONObject.hpp"

GEOFeature::GEOFeature(const JSONBaseObject* id,
                       const GEOGeometry* geometry,
                       const JSONObject* properties) :
_id(id),
_geometry(geometry),
_properties(properties)
{
  if (_geometry != NULL) {
    _geometry->setFeature(this);
  }
}

GEOFeature::~GEOFeature() {
  delete _id;
  delete _geometry;
  delete _properties;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GEOFeature::symbolize(const G3MRenderContext* rc,
                           const GEOSymbolizer*    symbolizer,
                           MeshRenderer*           meshRenderer,
                           ShapesRenderer*         shapesRenderer,
                           MarksRenderer*          marksRenderer,
                           GEOVectorLayer*         geoVectorLayer
                           ) const {
  if (_geometry != NULL) {
    _geometry->symbolize(rc,
                         symbolizer,
                         meshRenderer,
                         shapesRenderer,
                         marksRenderer,
                         geoVectorLayer);
  }
}

void GEOFeature::rasterize(const GEORasterSymbolizer* symbolizer,
                           ICanvas* canvas,
                           const GEORasterProjection* projection,
                           int tileLevel) const {
  if (_geometry != NULL) {
    _geometry->rasterize(symbolizer,
                         canvas,
                         projection,
                         tileLevel);
  }
}

long long GEOFeature::getCoordinatesCount() const {
  return (_geometry == NULL) ? 0 : _geometry->getCoordinatesCount();
}

GEOFeature* GEOFeature::deepCopy() const {
  return new GEOFeature((_id         == NULL) ? NULL : _id->deepCopy(),
                        (_geometry   == NULL) ? NULL : _geometry->deepCopy(),
                        (_properties == NULL) ? NULL : _properties->deepCopy());
}

long long GEOFeature::createFeatureMarks(const VectorStreamingRenderer::VectorSet* vectorSet,
                                         const VectorStreamingRenderer::Node*      node) const {
  if (_geometry != NULL) {
    return _geometry->createFeatureMarks(vectorSet, node);
  }
  return 0;
}
