//
//  CompositeMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//

#include "CompositeMesh.hpp"

#include "Vector3D.hpp"

CompositeMesh::~CompositeMesh() {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    delete child;
  }

  delete _extent;
}

int CompositeMesh::getVertexCount() const {
  int result = 0;
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    result += child->getVertexCount();
  }
  return result;
}

bool CompositeMesh::isTransparent(const G3MRenderContext* rc) const {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    if (child->isTransparent(rc)) {
      return true;
    }
  }
  return false;
}

void CompositeMesh::render(const G3MRenderContext* rc) const {
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    child->render(rc);
  }
}

const Vector3D CompositeMesh::getVertex(int index) const {
  int acumIndex = 0;
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    const int childIndex = index - acumIndex;
    const int childSize = child->getVertexCount();
    if (childIndex < childSize) {
      return child->getVertex(childIndex);
    }
    acumIndex += childSize;
  }
  return Vector3D::nan();
}

Extent* CompositeMesh::calculateExtent() const {
  const int childrenCount = _children.size();
  if (childrenCount == 0) {
    return NULL;
  }

  Extent* result = _children[0]->getExtent();
  for (int i = 1; i < childrenCount; i++) {
    Mesh* child = _children[i];
    result = result->mergedWith( child->getExtent() );
  }

  return result;
}

Extent* CompositeMesh::getExtent() const {
  if (_extent == NULL) {
    _extent = calculateExtent();
  }
  return _extent;
}

void CompositeMesh::addMesh(Mesh* mesh) {
  delete _extent;
  _extent = NULL;

  _children.push_back(mesh);
  
  SceneGraphNode::addChild(mesh);
}

void CompositeMesh::notifyGLClientChildrenParentHasChanged(){
  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    Mesh* child = _children[i];
    child->actualizeGLGlobalState(this);
  }
}

void CompositeMesh::rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){}
bool CompositeMesh::isInsideCameraFrustum(const G3MRenderContext* rc){
  return true;
}
void CompositeMesh::modifiyGLState(GLState* state){}
