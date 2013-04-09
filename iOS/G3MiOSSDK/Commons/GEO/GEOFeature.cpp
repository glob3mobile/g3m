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
                       GEOGeometry* geometry,
                       const JSONObject* properties) :
_id(id),
_geometry(geometry),
_properties(properties)
{
  _geometry->setFeature(this);
}


GEOFeature::~GEOFeature() {
  delete _id;
  delete _geometry;
  delete _properties;
}

void GEOFeature::render(const G3MRenderContext* rc,
                        const GLState& parentState, const GPUProgramState* parentProgramState,
                        const GEOSymbolizer* symbolizer) {
  _geometry->render(rc, parentState, parentProgramState, symbolizer);
}
