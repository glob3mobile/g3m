//
//  MeshHolder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

#ifndef __G3MiOSSDK__MeshHolder__
#define __G3MiOSSDK__MeshHolder__

#include "Mesh.hpp"
#include "Vector3D.hpp"

//#include "GPUProgramState.hpp"

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

  int getVertexCount() const {
    return _mesh->getVertexCount();
  }

  const Vector3D getVertex(int i) const {
    return _mesh->getVertex(i);
  }

  BoundingVolume* getBoundingVolume() const {
    return _mesh->getBoundingVolume();
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _mesh->isTransparent(rc);
  }

  void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const{
    _mesh->render(rc, parentGLState);
  }

  void showNormals(bool v) const{
    _mesh->showNormals(v);
  }

};

#endif
