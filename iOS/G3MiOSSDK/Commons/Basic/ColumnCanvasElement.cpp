//
//  ColumnCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "ColumnCanvasElement.hpp"

#include "ICanvas.hpp"


Vector2F* ColumnCanvasElement::calculateExtent(ICanvas* canvas) {
  float width  = 0;
  float height = 0;

  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];

    const Vector2F childExtent = child->getExtent(canvas);

    if (childExtent._x > width) {
      width = childExtent._x;
    }

    height += childExtent._y;
  }

  return new Vector2F(width, height);
}


void ColumnCanvasElement::drawAt(float left,
                                 float top,
                                 ICanvas* canvas) {
  const Vector2F extent = getExtent(canvas);

  canvas->setFillColor(_color);
  canvas->fillRectangle(left, top,
                        extent._x,
                        extent._y);

  const float halfWidth = extent._x / 2;

  float cursorTop = top;

  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];

    const Vector2F childExtent = child->getExtent(canvas);

    const float cursorLeft = left + halfWidth - (childExtent._x / 2);

    child->drawAt(cursorLeft, cursorTop, canvas);

    cursorTop += childExtent._y;
  }
}
