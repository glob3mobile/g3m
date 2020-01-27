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

  const size_t childrenSize = _children.size();
  for (size_t i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];

    const Vector2F childExtent = child->getExtent(canvas);

    if (childExtent._x > width) {
      width = childExtent._x;
    }

    height += childExtent._y;
  }

  return new Vector2F(width, height);
}

void ColumnCanvasElement::rawDrawAt(float left,
                                    float top,
                                    const Vector2F& extent,
                                    ICanvas* canvas) {
  const float halfWidth = extent._x / 2;

  float cursorTop = top;

  const size_t childrenSize = _children.size();
  for (size_t i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];

    const Vector2F childExtent = child->getExtent(canvas);

    float cursorLeft;
    switch (_elementAlign) {
      case Left:
        cursorLeft = left;
        break;
      case Right:
        cursorLeft = left + extent._x - childExtent._x;
        break;
      case Center:
      default:
        cursorLeft = left + halfWidth - (childExtent._x / 2);
        break;
    }

    child->drawAt(cursorLeft, cursorTop, canvas);
    
    cursorTop += childExtent._y;
  }
}
