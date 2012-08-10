//
//  LeveledTexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/08/12.
//
//

#include "LeveledTexturedMesh.hpp"

#include "Vector3D.hpp"

LeveledTexturedMesh::~LeveledTexturedMesh() {
  if (_ownedMesh) {
    delete _mesh;
  }
}

int LeveledTexturedMesh::getVertexCount() const {
  return _mesh->getVertexCount();
}

const Vector3D LeveledTexturedMesh::getVertex(int i) const {
  return _mesh->getVertex(i);
}

Extent* LeveledTexturedMesh::getExtent() const {
  return _mesh->getExtent();
}

void LeveledTexturedMesh::render(const RenderContext* rc) const {
  int __XXXX;
}

void LeveledTexturedMesh::setGLTextureIDForLevel(int level,
                                                 const GLTextureID& glTextureID) {
  int __XXXX;
}
