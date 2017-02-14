//
//  Mesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#include "Mesh.hpp"


Mesh::Mesh() :
_enable(true),
_userData(NULL)
{
}

void Mesh::setEnable(bool enable) {
  _enable = enable;
}

bool Mesh::isEnable() const {
  return _enable;
}

Mesh::~Mesh() {
  delete _userData;
}

Mesh::MeshUserData* Mesh::getUserData() const {
  return _userData;
}

void Mesh::setUserData(MeshUserData* userData) {
  if (_userData != userData) {
    delete _userData;
    _userData = userData;
  }
}

void Mesh::render(const G3MRenderContext* rc,
                  const GLState* parentGLState) const {
  if (_enable) {
    rawRender(rc, parentGLState);
  }
}
