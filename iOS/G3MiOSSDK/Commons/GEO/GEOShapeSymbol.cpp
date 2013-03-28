//
//  GEOShapeSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEOShapeSymbol.hpp"

#include "GEOSymbolizationContext.hpp"
#include "ShapesRenderer.hpp"

void GEOShapeSymbol::symbolize(const G3MRenderContext* rc,
                               const GEOSymbolizationContext& sc) const {
  ShapesRenderer* shapeRenderer = sc.getShapesRenderer();
  if (shapeRenderer == NULL) {
    ILogger::instance()->logError("Can't simbolize with Shape, ShapesRenderer was not set");
  }
  else {
    Shape* shape = createShape(rc);
    if (shape != NULL) {
      shapeRenderer->addShape(shape);
    }
  }
}
