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

GEORenderer::~GEORenderer() {
  delete _symbolizer;
}

void GEORenderer::initialize(const G3MContext* context) {
  _context = context;
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEOObject* geoObject = _children[i];
    geoObject->initialize(_context);
  }
}

void GEORenderer::addGEOObject(GEOObject* geoObject) {
  _children.push_back(geoObject);
  if (_context != NULL) {
    geoObject->initialize(_context);
  }
}

bool GEORenderer::isReadyToRender(const G3MRenderContext* rc) {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEOObject* geoObject = _children[i];
    if (!geoObject->isReadyToRender(rc)) {
      return false;
    }
  }

  return true;
}

void GEORenderer::render(const G3MRenderContext* rc,
                         const GLState& parentState) {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEOObject* geoObject = _children[i];
    geoObject->render(rc, parentState, &_programState, _symbolizer);
  }
}
