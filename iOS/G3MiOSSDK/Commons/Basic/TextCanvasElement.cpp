//
//  TextCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "TextCanvasElement.hpp"



#include "ICanvas.hpp"


const Vector2F TextCanvasElement::getExtent(ICanvas* canvas) {
  canvas->setFont(*_font);
  return canvas->textExtent(_text);
}

void TextCanvasElement::drawAt(float left,
                               float top,
                               ICanvas* canvas) {
  canvas->setFont(*_font);
  canvas->setFillColor(_color);
  canvas->fillText(_text, left, top);
}
