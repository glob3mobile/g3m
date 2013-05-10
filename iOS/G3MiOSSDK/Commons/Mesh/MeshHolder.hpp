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

#include "GPUProgramState.hpp"

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
  }

  int getVertexCount() const {
    return _mesh->getVertexCount();
  }

  const Vector3D getVertex(int i) const {
    return _mesh->getVertex(i);
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState, const GPUProgramState* parentProgramState) const {
    _mesh->render(rc, parentState, parentProgramState);
  }

  Extent* getExtent() const {
    return _mesh->getExtent();
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _mesh->isTransparent(rc);
  }
  
  void notifyGLClientChildrenParentHasChanged(){
    _mesh->actualizeGLState(this);
  }
  void modifyGLState(GLState& glState) const{}
  void modifyGPUProgramState(GPUProgramState& progState) const{}
  
};

#endif
