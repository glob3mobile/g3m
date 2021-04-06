//
//  FlatColorMesh.hpp
//  G3M
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#ifndef __G3M__FlatColorMesh__
#define __G3M__FlatColorMesh__

#include "Mesh.hpp"
#include "TextureMapping.hpp"
#include "Vector3D.hpp"

#include "GLState.hpp"


class FlatColorMesh : public Mesh {
private:
  Mesh*        _mesh;
  const bool   _ownedMesh;

  Color* const _flatColor;
  const bool   _ownedColor;

  GLState* _glState;

  void createGLState();


public:

  FlatColorMesh(Mesh* mesh,
                bool ownedMesh,
                Color* color,
                bool ownedColor) :
  _mesh(mesh),
  _ownedMesh(ownedMesh),
  _flatColor(color),
  _ownedColor(ownedColor),
  _glState(new GLState())
  {
    createGLState();
  }

  ~FlatColorMesh() {
    if (_ownedMesh) {
      delete _mesh;
    }
    if (_ownedColor) {
      delete _flatColor;
    }

    _glState->_release();

#ifdef JAVA_CODE
    super.dispose();
#endif

  }

  BoundingVolume* getBoundingVolume()  const {
    return (_mesh == NULL) ? NULL : _mesh->getBoundingVolume();
  }

  size_t getVerticesCount() const {
    return _mesh->getVerticesCount();
  }

  const Vector3D getVertex(const size_t index) const {
    return _mesh->getVertex(index);
  }

  virtual void getVertex(const size_t index,
                         MutableVector3D& result) const {
    _mesh->getVertex(index, result);
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _flatColor->_alpha != 1.0;
  }

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentState) const;

};

#endif
