//
//  LeveledTexturedMesh.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/08/12.
//
//

#ifndef __G3MiOSSDK__LeveledTexturedMesh__
#define __G3MiOSSDK__LeveledTexturedMesh__

#include "Mesh.hpp"

#include "GLTextureID.hpp"

class LeveledTexturedMesh : public Mesh {
private:
  const Mesh* _mesh;
  const bool  _ownedMesh;

public:
  LeveledTexturedMesh(const Mesh* mesh,
                      bool ownedMesh) :
  _mesh(mesh),
  _ownedMesh(ownedMesh)
  {
    
  }
  
  virtual ~LeveledTexturedMesh();
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  void render(const RenderContext* rc) const;
  
  Extent* getExtent() const;

  void setGLTextureIDForLevel(int level,
                              const GLTextureID& glTextureID);
  
};

#endif
