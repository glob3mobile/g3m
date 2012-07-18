//
//  TexturedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TexturedMesh_hpp
#define G3MiOSSDK_TexturedMesh_hpp

#include "Mesh.hpp"

#include "TextureMapping.hpp"

class TexturedMesh: public Mesh
{
private:
  const Mesh*           _mesh;
  const TextureMapping* _textureMapping;
  const bool            _ownedMesh;
  const bool            _ownedTexMapping;
  
public:
  
  TexturedMesh(const Mesh* mesh,
               bool ownedMesh,
               const TextureMapping* textureMapping,
               bool ownedTexMapping) :
  _mesh(mesh),
  _ownedMesh(ownedMesh),
  _textureMapping(textureMapping),
  _ownedTexMapping(ownedTexMapping)
  {
    
  }
  
  ~TexturedMesh(){
    if (_ownedMesh) {
      delete _mesh;
    }
    if (_ownedTexMapping){
      delete _textureMapping;
    }
  }
  
  void render(const RenderContext* rc) const;

  Extent *getExtent()  const { return _mesh->getExtent(); }
  
  int getVertexCount() const { return _mesh->getVertexCount(); }
  const float* getVertex() const { return _mesh->getVertex();}

};

#endif
