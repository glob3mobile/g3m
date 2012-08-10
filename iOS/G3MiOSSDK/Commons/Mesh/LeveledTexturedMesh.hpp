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

#include "TextureMapping.hpp"

#include <vector>

class LazyTextureMappingInitializer {
public:
  virtual ~LazyTextureMappingInitializer() {
  }
  
  virtual void calculate() = 0;
  
  virtual MutableVector2D getScale() const = 0;
  
  virtual MutableVector2D getTranslation() const = 0;
  
  virtual float const*    getTexCoords() const = 0;
  
};

class LazyTextureMapping : public TextureMapping {
private:
  LazyTextureMappingInitializer* _initializer;
  
  GLTextureID  _glTextureId;
  
  mutable bool _initialized;
  mutable float const*    _texCoords;
  mutable MutableVector2D _translation;
  mutable MutableVector2D _scale;
  
  TexturesHandler* _texturesHandler;
  
public:
  LazyTextureMapping(LazyTextureMappingInitializer* initializer,
                     TexturesHandler* texturesHandler) :
  _initializer(initializer),
  _glTextureId(GLTextureID::invalid()),
  _initialized(false),
  _texCoords(NULL),
  _translation(0,0),
  _scale(1,1),
  _texturesHandler(texturesHandler)
  {
    
  }
  
  virtual ~LazyTextureMapping() {
  }
  
  void bind(const RenderContext* rc) const;

  bool isValid() const {
    return _glTextureId.isValid();
  }
  
  void setGLTextureID(const GLTextureID glTextureId) {
    _glTextureId = glTextureId;
  }
  
  void releaseGLTextureId();

//  const GLTextureID getGLTextureID() const {
//    return _glTextureId;
//  }

};


class LeveledTexturedMesh : public Mesh {
private:
  const Mesh* _mesh;
  const bool  _ownedMesh;

  mutable std::vector<LazyTextureMapping*> _mappings;
  
  
  const   int _levelsCount;
  mutable int _currentLevel;
  
  LazyTextureMapping* getCurrentTextureMapping() const;

public:
  LeveledTexturedMesh(const Mesh* mesh,
                      bool ownedMesh,
                      std::vector<LazyTextureMapping*> mappings) :
  _mesh(mesh),
  _ownedMesh(ownedMesh),
  _mappings(mappings),
  _levelsCount(mappings.size()),
  _currentLevel(-1)
  {

  }
  
  virtual ~LeveledTexturedMesh();
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  void render(const RenderContext* rc) const;
  
  Extent* getExtent() const;

  void setGLTextureIDForLevel(int level,
                              const GLTextureID glTextureID);
  
};

#endif
