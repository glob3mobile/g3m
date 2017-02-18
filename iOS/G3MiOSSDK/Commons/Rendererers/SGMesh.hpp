//
//  SGMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

#ifndef SGMesh_hpp
#define SGMesh_hpp

#include "TransformableMesh.hpp"

class SGNode;

class SGMesh : public TransformableMesh {
private:
  SGNode*           _node;
  const std::string _uriPrefix;
  bool              _isTransparent;

  mutable bool _nodeInitialized;

protected:

  void userTransformMatrixChanged();

  void initializeGLState(GLState* glState) const;

public:

  SGMesh(SGNode*            node,
         const std::string& uriPrefix,
         bool               isTransparent);

  ~SGMesh();

  size_t getVertexCount() const;

  const Vector3D getVertex(size_t i) const;

  BoundingVolume* getBoundingVolume() const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;
  
};

#endif
