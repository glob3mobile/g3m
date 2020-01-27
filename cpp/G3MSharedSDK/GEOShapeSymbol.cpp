//
//  GEOShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEOShapeSymbol.hpp"

#include "ShapesRenderer.hpp"
#include "Shape.hpp"


GEOShapeSymbol::~GEOShapeSymbol() {
  delete _shape;

#ifdef JAVA_CODE
  super.dispose();
#endif
}


bool GEOShapeSymbol::symbolize(const G3MRenderContext* rc,
                               const GEOSymbolizer*    symbolizer,
                               MeshRenderer*           meshRenderer,
                               ShapesRenderer*         shapesRenderer,
                               MarksRenderer*          marksRenderer,
                               GEOVectorLayer*         geoVectorLayer) const {
  if (_shape != NULL) {
    if (shapesRenderer == NULL) {
      ILogger::instance()->logError("Can't symbolize with Shape, ShapesRenderer was not set");
      delete _shape;
    }
    else {
      shapesRenderer->addShape(_shape);
    }
    _shape = NULL;
  }
  return true;
}
