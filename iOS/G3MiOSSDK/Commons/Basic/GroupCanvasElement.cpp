//
//  GroupCanvasElement.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#include "GroupCanvasElement.hpp"

GroupCanvasElement::~GroupCanvasElement() {
  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CanvasElement* child = _children[i];
    delete child;
  }

  delete _extent;

  JAVA_POST_DISPOSE
}

void GroupCanvasElement::add(CanvasElement* child) {
  _children.push_back(child);
  clearCaches();
}

void GroupCanvasElement::clearCaches() {
  delete _extent;
  _extent = NULL;
}


const Vector2F GroupCanvasElement::getExtent(ICanvas* canvas) {
  if (_extent == NULL) {
    _extent = calculateExtent(canvas);
  }

  return *_extent;
}
