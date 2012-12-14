//
//  MeshShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/5/12.
//
//

#include "MeshShape.hpp"
#include "Mesh.hpp"
//#include "GL.hpp"

MeshShape::~MeshShape() {
  delete _mesh;
}

void MeshShape::cleanMesh() {
  delete _mesh;
  _mesh = NULL;
}


Mesh* MeshShape::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

bool MeshShape::isReadyToRender(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  return (mesh != NULL);
}

void MeshShape::rawRender(const G3MRenderContext* rc,
                          const GLState& parentState) {
  const Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    mesh->render(rc, parentState);
  }
}

bool MeshShape::isTransparent(const G3MRenderContext* rc) {
  const Mesh* mesh = getMesh(rc);
  if (mesh == NULL) {
    return false;
  }
  return mesh->isTransparent(rc);
}
