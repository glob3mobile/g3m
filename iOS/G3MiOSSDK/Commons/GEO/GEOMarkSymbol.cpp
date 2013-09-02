//
//  GEOMarkSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/28/13.
//
//

#include "GEOMarkSymbol.hpp"

#include "ILogger.hpp"
#include "Mark.hpp"
#include "MarksRenderer.hpp"

GEOMarkSymbol::~GEOMarkSymbol() {
  delete _mark;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

bool GEOMarkSymbol::symbolize(const G3MRenderContext* rc,
                              const GEOSymbolizer*    symbolizer,
                              MeshRenderer*           meshRenderer,
                              ShapesRenderer*         shapesRenderer,
                              MarksRenderer*          marksRenderer,
                              GEOTileRasterizer*      geoTileRasterizer) const {
  if (_mark != NULL) {
    if (marksRenderer == NULL) {
      ILogger::instance()->logError("Can't simbolize with Mark, MarksRenderer was not set");
      delete _mark;
    }
    else {
      marksRenderer->addMark(_mark);
    }
    _mark = NULL;
  }
  return true;
}
