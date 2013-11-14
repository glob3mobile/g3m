//
//  CompositeShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

#include "CompositeShape.hpp"

CompositeShape::~CompositeShape() {
  int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    delete child;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void CompositeShape::addShape(Shape* shape) {
  _children.push_back(shape);
}

bool CompositeShape::isReadyToRender(const G3MRenderContext* rc) {
  int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    if (child->isReadyToRender(rc)) {
      return true;
    }
  }

  return false;
}

void CompositeShape::rawRender(const G3MRenderContext* rc,
                              GLState* parentState,
                               bool renderNotReadyShapes) {
  int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    child->render(rc, parentState, renderNotReadyShapes);
  }
}
