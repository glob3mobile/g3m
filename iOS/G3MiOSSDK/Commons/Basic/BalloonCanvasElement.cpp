//
//  BalloonCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "BalloonCanvasElement.hpp"

#include "ICanvas.hpp"

const Vector2F BalloonCanvasElement::getExtent(ICanvas* canvas) {
  const Vector2F childExtent = _child->getExtent(canvas);

  const float twoMargin = _margin * 2;
  return Vector2F(childExtent._x + twoMargin,
                  (childExtent._y + _arrowLenght + twoMargin) * 2);
}

void BalloonCanvasElement::drawAt(float left,
                                  float top,
                                  ICanvas* canvas) {
  const Vector2F childExtent = _child->getExtent(canvas);
  const Vector2F extent = getExtent(canvas);

  canvas->setFillColor(_color);

//  canvas->setLineColor(Color::black());
//  canvas->setLineWidth(0.2f);

  const float halfArrowPointSize = _arrowPointSize / 2;
//  canvas->fillRoundedRectangle(left + (extent._x / 2) - halfArrowPointSize,
//                               top  + (extent._y / 2) - halfArrowPointSize,
//                               _arrowPointSize, _arrowPointSize,
//                               halfArrowPointSize);

  canvas->fillRoundedRectangle(left + (extent._x / 2) - halfArrowPointSize,
                               top  + (extent._y / 2) - halfArrowPointSize,
                               _arrowPointSize, _arrowPointSize,
                               halfArrowPointSize);

  canvas->fillRoundedRectangle(left, top,
                               extent._x, childExtent._y + (_margin * 2),
                               _radius);

  _child->drawAt(left + _margin,
                 top  + _margin,
                 canvas);
}
