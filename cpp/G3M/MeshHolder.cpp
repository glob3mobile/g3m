//
//  MeshHolder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#include "MeshHolder.hpp"

#include "Vector3D.hpp"
#include "Color.hpp"


const Vector3D MeshHolder::getVertex(const size_t index) const {
  return _mesh->getVertex(index);
}

void MeshHolder::getVertex(const size_t index,
                           MutableVector3D& result) const {
  _mesh->getVertex(index, result);
}

Color MeshHolder::getColor(const size_t index) const {
  return _mesh->getColor(index);
}

void MeshHolder::getColor(const size_t index,
                          MutableColor& result) const {
  _mesh->getColor(index, result);
}
