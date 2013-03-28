//
//  GEORenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEORenderer.hpp"

#include "GEOObject.hpp"
#include "GEOSymbolizer.hpp"
#include "GEOSymbolizationContext.hpp"


GEORenderer::~GEORenderer() {
  delete _symbolizer;

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEOObject* geoObject = _children[i];
    delete geoObject;
  }
}

void GEORenderer::addGEOObject(GEOObject* geoObject) {
  _children.push_back(geoObject);
}

void GEORenderer::render(const G3MRenderContext* rc,
                         const GLState& parentState) {
  const int childrenCount = _children.size();
  if (childrenCount > 0) {
    const GEOSymbolizationContext sc(_symbolizer,
                                     _meshRenderer,
                                     _shapesRenderer,
                                     _marksRenderer);

    for (int i = 0; i < childrenCount; i++) {
      const GEOObject* geoObject = _children[i];
      if (geoObject != NULL) {
        geoObject->symbolize(rc, sc);

        delete geoObject;
      }
    }
    _children.clear();
  }
}
