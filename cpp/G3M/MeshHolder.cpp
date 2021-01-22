//
//  MeshHolder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "MeshHolder.hpp"

#include "Vector3D.hpp"


const Vector3D MeshHolder::getVertex(const size_t index) const {
  return _mesh->getVertex(index);
}

void MeshHolder::getVertex(const size_t index,
                           MutableVector3D& result) const {
  _mesh->getVertex(index, result);
}
