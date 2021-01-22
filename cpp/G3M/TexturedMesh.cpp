//
//  TexturedMesh.cpp
//  G3M
//
//  Created by José Miguel S N on 12/07/12.
//

#include "TexturedMesh.hpp"

#include "TextureMapping.hpp"
#include "GLState.hpp"
#include "Vector3D.hpp"


void TexturedMesh::createGLState() {
}

void TexturedMesh::rawRender(const G3MRenderContext* rc,
                             const GLState* parentState) const {
#warning To Diego: As a textureMapping could be used by more than 1 TexturedMesh (I think) it's necessary to check glState consistency at every frame. Otherwise you should store somehow a list of every user of the mapping in order to change their states when any parameter of the mapping is updated. Method modifyGLState() is now much lighter though.
  _textureMapping->modifyGLState(*_glState);

  _glState->setParent(parentState);
  _mesh->render(rc, _glState);
}


TexturedMesh::TexturedMesh(Mesh* mesh,
                           bool ownedMesh,
                           TextureMapping* const textureMapping,
                           bool ownedTexMapping,
                           bool transparent) :
_mesh(mesh),
_ownedMesh(ownedMesh),
_textureMapping(textureMapping),
_ownedTexMapping(ownedTexMapping),
_transparent(transparent),
_glState(new GLState())
{
  createGLState();
}

TexturedMesh::~TexturedMesh() {
  if (_ownedMesh) {
    delete _mesh;
  }
  if (_ownedTexMapping) {
    delete _textureMapping;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Vector3D TexturedMesh::getVertex(const size_t index) const {
  return _mesh->getVertex(index);
}

void TexturedMesh::getVertex(const size_t index,
                             MutableVector3D& result) const {
  _mesh->getVertex(index, result);
}
