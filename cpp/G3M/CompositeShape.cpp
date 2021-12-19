//
//  CompositeShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/6/12.
//
//

#include "CompositeShape.hpp"

CompositeShape::~CompositeShape() {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
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
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    if (!child->isReadyToRender(rc)) {
      return false;
    }
  }

  return true;
}

void CompositeShape::rawRender(const G3MRenderContext* rc,
                               GLState* parentState,
                               bool renderNotReadyShapes) {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    child->render(rc, parentState, renderNotReadyShapes);
  }
}

bool CompositeShape::isTransparent(const G3MRenderContext* rc) {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    if (child->isTransparent(rc)) {
      return true;
    }
  }
  return false;
}

std::vector<double> CompositeShape::intersectionsDistances(const Planet* planet,
                                                           const Vector3D& origin,
                                                           const Vector3D& direction) const {
  std::vector<double> intersections;

  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Shape* child = _children[i];
    std::vector<double> childResults = child->intersectionsDistances(planet, origin, direction);

    for (size_t j = 0; j < childResults.size(); j++) {
      intersections.push_back( childResults[j] );
    }
  }

  // sort vector
#ifdef C_CODE
  std::sort(intersections.begin(),
            intersections.end());
#endif
#ifdef JAVA_CODE
  java.util.Collections.sort(intersections);
#endif

  return intersections;
}

void CompositeShape::removeAllShapes(bool deleteShapes) {
  if (deleteShapes) {
    const size_t childrenCount = _children.size();
    for (size_t i = 0; i < childrenCount; i++) {
      Shape* child = _children[i];
      delete child;
    }
  }
  _children.clear();
}
