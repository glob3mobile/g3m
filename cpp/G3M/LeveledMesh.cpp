//
//  LeveledMesh.cpp
//  G3M
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

#include "LeveledMesh.hpp"

#include "Vector3D.hpp"

const Vector3D LeveledMesh::getVertex(const size_t index) const {
  return _mesh->getVertex(index);
}

void LeveledMesh::getVertex(const size_t index,
                             MutableVector3D& result) const {
  _mesh->getVertex(index, result);
}
