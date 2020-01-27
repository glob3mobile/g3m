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
                           GEOVectorLayer*         geoVectorLayer) const {
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

int GEOFeature::symbolize(const VectorStreamingRenderer::VectorSet* vectorSet,
                          const VectorStreamingRenderer::Node*      node) const {
  return (_geometry == NULL) ? 0 : _geometry->symbolize(vectorSet, node);
}
