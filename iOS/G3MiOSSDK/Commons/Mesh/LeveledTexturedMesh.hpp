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

#include "TextureMapping.hpp"
#include "IFloatBuffer.hpp"
#include "ILogger.hpp"

#include <vector>

#include "IGLTextureId.hpp"

class LazyTextureMappingInitializer {
public:
  virtual ~LazyTextureMappingInitializer() {
  }
  
  virtual void initialize() = 0;
  
  virtual const MutableVector2D getScale() const = 0;
  
  virtual const MutableVector2D getTranslation() const = 0;
  
  virtual IFloatBuffer* createTextCoords() const = 0;
};

class LazyTextureMapping : public TextureMapping {
private:
  mutable LazyTextureMappingInitializer* _initializer;
  
#ifdef C_CODE
  const IGLTextureId* _glTextureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _glTextureId;
#endif
  
  mutable bool _initialized;

  mutable bool             _ownedTexCoords;
  mutable IFloatBuffer*    _texCoords;
  mutable MutableVector2D  _translation;
  mutable MutableVector2D  _scale;
    
  TexturesHandler* _texturesHandler;
  
  LazyTextureMapping& operator=(const LazyTextureMapping& that);
  
  LazyTextureMapping(const LazyTextureMapping& that);
  void releaseGLTextureId();

  const bool _transparent;

public:
  LazyTextureMapping(LazyTextureMappingInitializer* initializer,
                     TexturesHandler* texturesHandler,
                     bool ownedTexCoords,
                     bool transparent) :
  _initializer(initializer),
  _glTextureId(NULL),
  _initialized(false),
  _texCoords(NULL),
  _translation(0,0),
  _scale(1,1),
  _texturesHandler(texturesHandler),
  _ownedTexCoords(ownedTexCoords),
  _transparent(transparent)
  {
  }
  
  virtual ~LazyTextureMapping() {
    delete _initializer;
    _initializer = NULL;
    
    if (_ownedTexCoords) {
        delete _texCoords;
    }
    _texCoords = NULL;
    
    releaseGLTextureId();
  }
  
  void bind(const G3MRenderContext* rc) const;

  bool isValid() const {
    return _glTextureId != NULL;
  }
  
  void setGLTextureId(const IGLTextureId* glTextureId) {
    releaseGLTextureId();
    _glTextureId = glTextureId;
  }
  

  const IGLTextureId* getGLTextureId() const {
    return _glTextureId;
  }

  bool isTransparent(const G3MRenderContext* rc) const {
    return _transparent;
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
      ILogger::instance()->logError("LOGIC ERROR\n");
    }
  }
  
  virtual ~LeveledTexturedMesh();
  
  int getVertexCount() const;
  
  const Vector3D getVertex(int i) const;
  
  void render(const G3MRenderContext* rc,
              const GLState& parentState) const;
  
  BoundingVolume* getBoundingVolume() const;

  bool setGLTextureIdForLevel(int level,
                              const IGLTextureId* glTextureId);
  
//  void setGLTextureIdForInversedLevel(int inversedLevel,
//                                      const const GLTextureId*glTextureId);
  
  const IGLTextureId* getTopLevelGLTextureId() const;
  
  bool isTransparent(const G3MRenderContext* rc) const;
  
};

#endif
