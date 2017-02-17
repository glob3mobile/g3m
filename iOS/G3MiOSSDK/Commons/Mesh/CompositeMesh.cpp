//
//  CompositeMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//

#include "CompositeMesh.hpp"

#include "Vector3D.hpp"
#include "BoundingVolume.hpp"


CompositeMesh::~CompositeMesh() {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    delete child;
  }

  delete _boundingVolume;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

size_t CompositeMesh::getVertexCount() const {
  size_t result = 0;
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    result += child->getVertexCount();
  }
  return result;
}

bool CompositeMesh::isTransparent(const G3MRenderContext* rc) const {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    if (child->isTransparent(rc)) {
      return true;
    }
  }
  return false;
}

const Vector3D CompositeMesh::getVertex(size_t index) const {
  int acumIndex = 0;
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    const size_t childIndex = index - acumIndex;
    const size_t childSize = child->getVertexCount();
    if (childIndex < childSize) {
      return child->getVertex(childIndex);
    }
    acumIndex += childSize;
  }
  return Vector3D::NANV;
}

BoundingVolume* CompositeMesh::calculateBoundingVolume() const {
  const size_t childrenCount = _children.size();
  if (childrenCount == 0) {
    return NULL;
  }

  BoundingVolume* result = _children[0]->getBoundingVolume()->copy();
  for (size_t i = 1; i < childrenCount; i++) {
    Mesh* child = _children[i];
    BoundingVolume* newResult = result->mergedWith( child->getBoundingVolume() );
    delete result;
    result = newResult;
  }

  return result;
}

BoundingVolume* CompositeMesh::getBoundingVolume() const {
  if (_boundingVolume == NULL) {
    _boundingVolume = calculateBoundingVolume();
  }
  return _boundingVolume;
}

void CompositeMesh::addMesh(Mesh* mesh) {
  delete _boundingVolume;
  _boundingVolume = NULL;

  _children.push_back(mesh);
}

void CompositeMesh::rawRender(const G3MRenderContext* rc,
                              const GLState* parentGLState) const {
  const size_t childrenCount = _children.size();
  for (size_t i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    child->render(rc, parentGLState);
  }
}
