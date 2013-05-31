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

  void render(const G3MRenderContext* rc) const {
    _mesh->render(rc);
  }

  Extent* getExtent() const {
    return _mesh->getExtent();
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _mesh->isTransparent(rc);
  }
  
  void notifyGLClientChildrenParentHasChanged(){
    _mesh->actualizeGLGlobalState(this);
  }
  void modifyGLGlobalState(GLGlobalState& GLGlobalState) const{}
  void modifyGPUProgramState(GPUProgramState& progState) const{}
  
  //Scene Graph Node
  void rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){
    //TODO: Implement
    //It's necessary a holder with the Scene Graph approach
  }
  bool isInsideCameraFrustum(const G3MRenderContext* rc){
    //TODO: Implement
    return true;
  }
  void modifiyGLState(GLState* state){
    //TODO: Implement
  }
  
};

#endif
