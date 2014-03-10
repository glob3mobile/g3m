//
//  TexturedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TexturedMesh
#define G3MiOSSDK_TexturedMesh

#include "Mesh.hpp"
#include "TextureMapping.hpp"
#include "Vector3D.hpp"

#include "GLState.hpp"


class TexturedMesh: public Mesh
{
private:
  Mesh*                 _mesh;
  const TextureMapping* _textureMapping;
  const bool            _ownedMesh;
  const bool            _ownedTexMapping;
  const bool            _transparent;
  
  GLState* _glState;
  
  void createGLState();

  
public:
  
  TexturedMesh(Mesh* mesh,
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
  
  ~TexturedMesh() {
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

  BoundingVolume* getBoundingVolume()  const {
    return (_mesh == NULL) ? NULL : _mesh->getBoundingVolume();
  }
  
  int getVertexCount() const {
    return _mesh->getVertexCount();
  }
  
  const Vector3D getVertex(int i) const {
    return _mesh->getVertex(i);
  }
  
  const TextureMapping* const getTextureMapping() const {
    return _textureMapping;
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _transparent;
  }
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentState) const;

  void showNormals(bool v) const{
    _mesh->showNormals(v);
  }
};

#endif
