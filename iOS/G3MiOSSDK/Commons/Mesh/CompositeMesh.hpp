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

class CompositeMesh : public Mesh {
private:
  std::vector<Mesh*> _children;

  BoundingVolume* calculateBoundingVolume() const;

  mutable BoundingVolume* _boundingVolume;
  

public:
  virtual ~CompositeMesh();
  
  int getVertexCount() const;

  const Vector3D getVertex(int i) const;

  BoundingVolume* getBoundingVolume() const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void addMesh(Mesh* mesh);

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

  void showNormals(bool v) const;

};

#endif
