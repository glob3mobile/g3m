//
//  ShapesRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "ShapesRenderer.hpp"


void ShapesRenderer::render(const RenderContext* rc) {
  const int shapesCount = _shapes.size();
  for (int i = 0; i < shapesCount; i++) {
    Shape* shape = _shapes[i];
    shape->render(rc);
  }
}
