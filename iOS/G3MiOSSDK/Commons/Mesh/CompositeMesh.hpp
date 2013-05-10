//
//  CompositeMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/17/12.
//
//

#ifndef __G3MiOSSDK__CompositeMesh__
#define __G3MiOSSDK__CompositeMesh__

#include "Mesh.hpp"

class CompositeMesh : public Mesh, public GLClientNotDrawable {
private:
  std::vector<Mesh*> _children;

  Extent* calculateExtent() const;

  mutable Extent* _extent;
  

public:
  virtual ~CompositeMesh();
  
  int getVertexCount() const;

  const Vector3D getVertex(int i) const;

  void render(const G3MRenderContext* rc,
              const GLState& parentState, const GPUProgramState* parentProgramState) const;

  Extent* getExtent() const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void addMesh(Mesh* mesh);

  //GLClientNotDrawable
  int WORKING_JM;
  void notifyGLClientChildrenParentHasChanged(){
    const int childrenCount = _children.size();
    for (int i = 1; i < childrenCount; i++) {
      Mesh* child = _children[i];
      child->actualizeGLState(this);
    }
  }
  void modifyGLState(GLState* glState) const{}
  void modifyGPUProgramState(GPUProgramState* progState) const{}

};

#endif
