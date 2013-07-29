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
#include "ILogger.hpp"
#include "Context.hpp"
#include "Camera.hpp"

class GEORenderer_ObjectSymbolizerPair {
public:
  const GEOObject*     _geoObject;
  const GEOSymbolizer* _symbolizer;

  GEORenderer_ObjectSymbolizerPair(GEOObject* geoObject,
                                   GEOSymbolizer* symbolizer) :
  _geoObject(geoObject),
  _symbolizer(symbolizer)
  {

  }

  ~GEORenderer_ObjectSymbolizerPair() {
    delete _geoObject;
    delete _symbolizer;
  }
};


GEORenderer::~GEORenderer() {
  delete _defaultSymbolizer;

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEORenderer_ObjectSymbolizerPair* pair = _children[i];
    delete pair;
  }
}

void GEORenderer::addGEOObject(GEOObject* geoObject,
                               GEOSymbolizer* symbolizer) {
  if ( (symbolizer == NULL) && (_defaultSymbolizer == NULL) ) {
    ILogger::instance()->logError("Can't add a geoObject without a symbolizer if the defaultSymbolizer was not given in the GEORenderer constructor");
    delete geoObject;
  }
  else {
    _children.push_back( new GEORenderer_ObjectSymbolizerPair(geoObject, symbolizer) );
  }
}

void GEORenderer::render(const G3MRenderContext* rc) {
  const int childrenCount = _children.size();
  if (childrenCount > 0) {
    for (int i = 0; i < childrenCount; i++) {
      const GEORenderer_ObjectSymbolizerPair* pair = _children[i];

      if (pair->_geoObject != NULL) {
        const GEOSymbolizer* symbolizer = (pair->_symbolizer == NULL) ? _defaultSymbolizer : pair->_symbolizer;

        const GEOSymbolizationContext sc(symbolizer,
                                         _meshRenderer,
                                         _shapesRenderer,
                                         _marksRenderer,
                                         _geoTileRasterizer);
        pair->_geoObject->symbolize(rc, sc);
      }

      delete pair;
    }
    _children.clear();
  }
}


