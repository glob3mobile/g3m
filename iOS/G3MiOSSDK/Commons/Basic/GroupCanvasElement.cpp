//
//  GroupCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "GroupCanvasElement.hpp"
#include "ICanvas.hpp"


GroupCanvasElement::~GroupCanvasElement() {
  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];
    delete child;
  }

  delete _extent;
  delete _rawExtent;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GroupCanvasElement::add(CanvasElement* child) {
  _children.push_back(child);
  clearCaches();
}

void GroupCanvasElement::clearCaches() {
  delete _extent;
  _extent = NULL;

  delete _rawExtent;
  _rawExtent = NULL;
}

const Vector2F GroupCanvasElement::getExtent(ICanvas* canvas) {
  if (_extent == NULL) {
    _rawExtent = calculateExtent(canvas);

    const float extra = (_margin + _padding) * 2;
    _extent = new Vector2F(_rawExtent->_x + extra,
                           _rawExtent->_y + extra);
  }

  return *_extent;
}

void GroupCanvasElement::drawAt(float left,
                                float top,
                                ICanvas* canvas) {
  const Vector2F extent = getExtent(canvas);

//  canvas->setLineColor(Color::yellow());
//  canvas->strokeRectangle(left,
//                          top,
//                          extent._x,
//                          extent._y);

  canvas->setFillColor(_color);
  canvas->fillRoundedRectangle(left + _margin,
                               top  + _margin,
                               extent._x - _margin*2,
                               extent._y - _margin*2,
                               _cornerRadius);

  const float extra = _margin + _padding;
  rawDrawAt(left + extra,
            top + extra,
            *_rawExtent,
            canvas);
}
