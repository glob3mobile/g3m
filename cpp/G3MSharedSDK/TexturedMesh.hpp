//
//  TexturedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//

#ifndef G3MiOSSDK_TexturedMesh
#define G3MiOSSDK_TexturedMesh

#include "Mesh.hpp"

class TextureMapping;


class TexturedMesh : public Mesh {
private:
  Mesh*                 _mesh;
  const TextureMapping* _textureMapping;
  const bool            _ownedMesh;
  const bool            _ownedTexMapping;
  const bool            _transparent;
  
  GLState* _glState;
  
  void createGLState();

  
public:
  
  TexturedMesh(Mesh* mesh,
               bool ownedMesh,
               TextureMapping* const textureMapping,
               bool ownedTexMapping,
               bool transparent);
  
  ~TexturedMesh();

  BoundingVolume* getBoundingVolume()  const {
    return (_mesh == NULL) ? NULL : _mesh->getBoundingVolume();
  }
  
  size_t getVertexCount() const {
    return _mesh->getVertexCount();
  }
  
  const Vector3D getVertex(const size_t index) const;
  
  const TextureMapping* const getTextureMapping() const {
    return _textureMapping;
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _transparent;
  }
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentState) const;

};

#endif
