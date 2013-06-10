//
//  AbstractMeshShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#include "AbstractMeshShape.hpp"
#include "Mesh.hpp"
//#include "GL.hpp"

AbstractMeshShape::~AbstractMeshShape() {
  delete _mesh;
}

void AbstractMeshShape::cleanMesh() {
  delete _mesh;
  _mesh = NULL;
}


Mesh* AbstractMeshShape::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

bool AbstractMeshShape::isReadyToRender(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  return (mesh != NULL);
}

void AbstractMeshShape::rawRender(const G3MRenderContext* rc,
                                  const GLState& parentState,
                                  bool renderNotReadyShapes) {
  const Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, parentState);
  }
}

bool AbstractMeshShape::isTransparent(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  if (mesh == NULL) {
    return false;
  }
  return mesh->isTransparent(rc);
}
