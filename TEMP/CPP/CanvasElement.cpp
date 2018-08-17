//
//  CanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "CanvasElement.hpp"

#include "ICanvas.hpp"

void CanvasElement::drawCentered(ICanvas* canvas) {
  const Vector2F extent = getExtent(canvas);
  drawAt((canvas->getWidth()  - extent._x) / 2,
         (canvas->getHeight() - extent._y) / 2,
         canvas);
}
