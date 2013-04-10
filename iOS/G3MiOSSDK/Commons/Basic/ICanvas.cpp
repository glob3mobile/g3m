//
//  ICanvas.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#include "ICanvas.hpp"

#include "G3MError.hpp"
#include "GFont.hpp"

ICanvas::~ICanvas() {
  delete _currentFont;
}

void ICanvas::initialize(int width, int height) {
  if ((width <= 0) || (height <= 0)) {
    throw G3MError("Invalid extent");
  }

  if (isInitialized()) {
    throw G3MError("Canvas already initialized");
  }

  _width  = width;
  _height = height;
  _initialize(width, height);
}

void ICanvas::checkInitialized() const {
  if (!isInitialized()) {
    throw G3MError("Canvas is not initialized");
  }
}

void ICanvas::checkCurrentFont() const {
  if (_currentFont == NULL) {
    throw G3MError("Current font no set");
  }
}


void ICanvas::setFillColor(const Color& color) {
  checkInitialized();
  _setFillColor(color);
}

void ICanvas::setStrokeColor(const Color& color) {
  checkInitialized();
  _setStrokeColor(color);
}

void ICanvas::setStrokeWidth(float width) {
  checkInitialized();
  _setStrokeWidth(width);
}

void ICanvas::setShadow(const Color& color,
                        float blur,
                        float offsetX,
                        float offsetY) {
  checkInitialized();
  _setShadow(color, blur, offsetX, offsetY);
}

void ICanvas::removeShadow() {
  checkInitialized();
  _removeShadow();
}

void ICanvas::fillRectangle(float x, float y,
                            float width, float height) {
  checkInitialized();
  _fillRectangle(x, y,
                 width, height);
}

void ICanvas::strokeRectangle(float x, float y,
                              float width, float height) {
  checkInitialized();
  _strokeRectangle(x, y,
                   width, height);
}

void ICanvas::fillRoundedRectangle(float x, float y,
                                   float width, float height,
                                   float radius) {
  checkInitialized();
  _fillRoundedRectangle(x, y,
                        width, height,
                        radius);
}

void ICanvas::strokeRoundedRectangle(float x, float y,
                                     float width, float height,
                                     float radius) {
  checkInitialized();
  _strokeRoundedRectangle(x, y,
                          width, height,
                          radius);
}

void ICanvas::fillAndStrokeRectangle(float x, float y,
                                     float width, float height) {
  checkInitialized();
  _fillAndStrokeRectangle(x, y,
                          width, height);
}

void ICanvas::fillAndStrokeRoundedRectangle(float x, float y,
                                            float width, float height,
                                            float radius) {
  checkInitialized();
  _fillAndStrokeRoundedRectangle(x, y,
                                 width, height,
                                 radius);
}

void ICanvas::createImage(IImageListener* listener,
                          bool autodelete) {
  checkInitialized();
  _createImage(listener, autodelete);
}

void ICanvas::setFont(const GFont& font) {
  delete _currentFont;
  _currentFont = new GFont( font );
  _setFont(font);
}

const Vector2F ICanvas::textExtent(const std::string& text) {
  checkCurrentFont();
  return _textExtent(text);
}

void ICanvas::fillText(const std::string& text,
                       float x, float y) {
  checkInitialized();
  checkCurrentFont();
  _fillText(text, x, y);
}
