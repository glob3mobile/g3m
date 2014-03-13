//
//  ICanvas.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/9/13.
//
//

#include "ICanvas.hpp"

#include "GFont.hpp"
#include "RectangleF.hpp"
#include "IImage.hpp"

ICanvas::~ICanvas() {
  delete _currentFont;
}

void ICanvas::initialize(int width, int height) {
  if ((width <= 0) || (height <= 0)) {
    ILogger::instance()->logError("Invalid extent");
    return;
  }

  if (isInitialized()) {
    ILogger::instance()->logError("Canvas already initialized");
    return;
  }

  _canvasWidth  = width;
  _canvasHeight = height;
  _initialize(width, height);
}

void ICanvas::checkInitialized() const {
  if (!isInitialized()) {
    ILogger::instance()->logError("Canvas is not initialized");
  }
}

void ICanvas::checkCurrentFont() const {
  if (_currentFont == NULL) {
    ILogger::instance()->logError("Current font no set");
  }
}


void ICanvas::setFillColor(const Color& color) {
  checkInitialized();
  _setFillColor(color);
}

void ICanvas::setLineColor(const Color& color) {
  checkInitialized();
  _setLineColor(color);
}

void ICanvas::setLineWidth(float width) {
  checkInitialized();
  _setLineWidth(width);
}

void ICanvas::setLineCap(StrokeCap cap) {
  checkInitialized();
  _setLineCap(cap);
}

void ICanvas::setLineJoin(StrokeJoin join) {
  checkInitialized();
  _setLineJoin(join);
}

void ICanvas::setLineMiterLimit(float limit) {
  checkInitialized();
  _setLineMiterLimit(limit);
}

void ICanvas::setLineDash(float lengths[],
                          int count,
                          int phase) {
  checkInitialized();
  _setLineDash(lengths,
               count,
               phase);
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

void ICanvas::fillRectangle(float left, float top,
                            float width, float height) {
  checkInitialized();
  _fillRectangle(left, top,
                 width, height);
}

void ICanvas::strokeRectangle(float left, float top,
                              float width, float height) {
  checkInitialized();
  _strokeRectangle(left, top,
                   width, height);
}

void ICanvas::fillRoundedRectangle(float left, float top,
                                   float width, float height,
                                   float radius) {
  checkInitialized();
  _fillRoundedRectangle(left, top,
                        width, height,
                        radius);
}

void ICanvas::strokeRoundedRectangle(float left, float top,
                                     float width, float height,
                                     float radius) {
  checkInitialized();
  _strokeRoundedRectangle(left, top,
                          width, height,
                          radius);
}

void ICanvas::fillAndStrokeRectangle(float left, float top,
                                     float width, float height) {
  checkInitialized();
  _fillAndStrokeRectangle(left, top,
                          width, height);
}

void ICanvas::fillAndStrokeRoundedRectangle(float left, float top,
                                            float width, float height,
                                            float radius) {
  checkInitialized();
  _fillAndStrokeRoundedRectangle(left, top,
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
                       float left, float top) {
  checkInitialized();
  checkCurrentFont();
  _fillText(text, left, top);
}

void ICanvas::drawImage(const IImage* image,
                        float destLeft, float destTop) {
  checkInitialized();
  _drawImage(image, destLeft, destTop);
}

void ICanvas::drawImage(const IImage* image,
                        float destLeft, float destTop, float destWidth, float destHeight) {
  checkInitialized();
  _drawImage(image, destLeft, destTop, destWidth, destHeight);
}

void ICanvas::drawImage(const IImage* image,
                        float srcLeft, float srcTop, float srcWidth, float srcHeight,
                        float destLeft, float destTop, float destWidth, float destHeight) {
  checkInitialized();

  if (!RectangleF::fullContains(0, 0, image->getWidth(), image->getHeight(),
                                srcLeft, srcTop, srcWidth, srcHeight)) {
    ILogger::instance()->logError("Invalid source rectangle in drawImage");
  }

  _drawImage(image,
             srcLeft, srcTop, srcWidth, srcHeight,
             destLeft, destTop, destWidth, destHeight);
}

void ICanvas::drawImage(const IImage* image,
                        float srcLeft, float srcTop, float srcWidth, float srcHeight,
                        float destLeft, float destTop, float destWidth, float destHeight,
                        float transparency) {
  checkInitialized();

  if (!RectangleF::fullContains(0, 0, image->getWidth(), image->getHeight(),
                                srcLeft, srcTop, srcWidth, srcHeight)) {
    ILogger::instance()->logError("Invalid source rectangle in drawImage");
  }

  if (transparency <= 0.0) {
    return;
  }

  if (transparency >= 1.0) {
    _drawImage(image,
               srcLeft, srcTop, srcWidth, srcHeight,
               destLeft, destTop, destWidth, destHeight);
  } else{
    _drawImage(image,
               srcLeft, srcTop, srcWidth, srcHeight,
               destLeft, destTop, destWidth, destHeight,
               transparency);
  }

}

void ICanvas::beginPath() {
  checkInitialized();
  _beginPath();
}

void ICanvas::closePath() {
  checkInitialized();
  _closePath();
}

void ICanvas::stroke() {
  checkInitialized();
  _stroke();
}

void ICanvas::fill() {
  checkInitialized();
  _fill();
}

void ICanvas::fillAndStroke() {
  checkInitialized();
  _fillAndStroke();
}

void ICanvas::moveTo(float x, float y) {
  checkInitialized();
  _moveTo(x, y);
}

void ICanvas::lineTo(float x, float y) {
  checkInitialized();
  _lineTo(x, y);
}
