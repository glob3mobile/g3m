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

#include <vector>


class CompositeMesh : public Mesh {
private:
  std::vector<Mesh*> _children;

  BoundingVolume* calculateBoundingVolume() const;

  mutable BoundingVolume* _boundingVolume;
  

public:
  virtual ~CompositeMesh();
  
  size_t getVertexCount() const;

  const Vector3D getVertex(const size_t index) const;

  BoundingVolume* getBoundingVolume() const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void addMesh(Mesh* mesh);

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

};

#endif
