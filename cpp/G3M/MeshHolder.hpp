//
//  MeshHolder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3M__MeshHolder__
#define __G3M__MeshHolder__

#include "Mesh.hpp"


class MeshHolder : public Mesh {
private:
  Mesh* _mesh;

public:
  MeshHolder(Mesh* mesh) :
  _mesh(mesh)
  {

  }

  void setMesh(Mesh* mesh) {
    if (_mesh != mesh) {
      delete _mesh;
      _mesh = mesh;
    }
  }

  ~MeshHolder() {
    delete _mesh;
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  size_t getVerticesCount() const {
    return _mesh->getVerticesCount();
  }

  const Vector3D getVertex(const size_t index) const;
  void getVertex(const size_t index,
                 MutableVector3D& result) const;

  BoundingVolume* getBoundingVolume() const {
    return _mesh->getBoundingVolume();
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _mesh->isTransparent(rc);
  }

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const {
    _mesh->render(rc, parentGLState);
  }

  Mesh* getMesh() const {
    return _mesh;
  }
  
};

#endif
