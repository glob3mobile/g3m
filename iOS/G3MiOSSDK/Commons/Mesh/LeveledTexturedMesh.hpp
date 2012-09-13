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
#include "GLTextureId.hpp"

#include "TextureMapping.hpp"
#include "IFloatBuffer.hpp"

#include <vector>

class LazyTextureMappingInitializer {
public:
  virtual ~LazyTextureMappingInitializer() {
  }
  
  virtual void initialize() = 0;
  
  virtual const MutableVector2D getScale() const = 0;
  
  virtual const MutableVector2D getTranslation() const = 0;
  
  virtual IFloatBuffer* getTexCoords() const = 0;
};

class LazyTextureMapping : public TextureMapping {
private:
  mutable LazyTextureMappingInitializer* _initializer;
  
  GLTextureId  _glTextureId;
  
  mutable bool _initialized;

  mutable bool             _ownedTexCoords;
  mutable IFloatBuffer*    _texCoords;
  mutable MutableVector2D  _translation;
  mutable MutableVector2D  _scale;
  
  TexturesHandler* _texturesHandler;
  
  LazyTextureMapping& operator=(const LazyTextureMapping& that);
  
  LazyTextureMapping(const LazyTextureMapping& that);
  void releaseGLTextureId();

public:
  LazyTextureMapping(LazyTextureMappingInitializer* initializer,
                     TexturesHandler* texturesHandler,
                     bool ownedTexCoords) :
  _initializer(initializer),
  _glTextureId(GLTextureId::invalid()),
  _initialized(false),
  _texCoords(NULL),
  _translation(0,0),
  _scale(1,1),
  _texturesHandler(texturesHandler),
  _ownedTexCoords(ownedTexCoords)
  {
    
  }
  
  virtual ~LazyTextureMapping() {
    if (_initializer != NULL) {
      delete _initializer;
      _initializer = NULL;
    }

    if (_texCoords != NULL) {
      if (_ownedTexCoords) {
        delete _texCoords;
      }
      _texCoords = NULL;
    }
    
    releaseGLTextureId();
  }
  
  void bind(const RenderContext* rc) const;

  bool isValid() const {
    return _glTextureId.isValid();
  }
  
  void setGLTextureId(const GLTextureId glTextureId) {
    releaseGLTextureId();
    _glTextureId = glTextureId;
  }
  

  const GLTextureId getGLTextureId() const {
    return _glTextureId;
  }

};


class LeveledTexturedMesh : public Mesh {
private:
  const Mesh* _mesh;
  const bool  _ownedMesh;

  mutable std::vector<LazyTextureMapping*>* _mappings;
  
  
  const   int _levelsCount;
  
  mutable int  _currentLevel;
  mutable bool _currentLevelIsValid;
  
  LazyTextureMapping* getCurrentTextureMapping() const;

public:
  LeveledTexturedMesh(const Mesh* mesh,
                      bool ownedMesh,
                      std::vector<LazyTextureMapping*>* mappings) :
  _mesh(mesh),
  _ownedMesh(ownedMesh),
  _mappings(mappings),
  _levelsCount(mappings->size()),
  _currentLevel(mappings->size() + 1),
  _currentLevelIsValid(false)
  {
    if (_mappings->size() <= 0) {
      printf("LOGIC ERROR\n");
    }
  }
  
  virtual ~LeveledTexturedMesh();
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  void render(const RenderContext* rc) const;
  
  Extent* getExtent() const;

  bool setGLTextureIdForLevel(int level,
                              const GLTextureId glTextureId);
  
//  void setGLTextureIdForInversedLevel(int inversedLevel,
//                                      const GLTextureId glTextureId);
  
  const GLTextureId getTopLevelGLTextureId() const;

};

#endif
