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
#include "IGLTextureId.hpp"
#include "GLState.hpp"
#include <vector>
#include "Vector2F.hpp"

class TextureIDReference;

class LazyTextureMappingInitializer {
public:
  virtual ~LazyTextureMappingInitializer() {
  }

  virtual void initialize() = 0;

  virtual const Vector2F getScale() const = 0;

  virtual const Vector2F getTranslation() const = 0;

  virtual IFloatBuffer* createTextCoords() const = 0;
};


class LazyTextureMapping : public TextureMapping {
private:
  mutable LazyTextureMappingInitializer* _initializer;

#ifdef C_CODE
  const TextureIDReference* _glTextureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _glTextureId;
#endif

  mutable bool _initialized;

  mutable bool          _ownedTexCoords;
  mutable IFloatBuffer* _texCoords;

  mutable float _translationU;
  mutable float _translationV;

  mutable float _scaleU;
  mutable float _scaleV;

  LazyTextureMapping& operator=(const LazyTextureMapping& that);

  LazyTextureMapping(const LazyTextureMapping& that);
  void releaseGLTextureId();



public:
  const bool _transparent;

  LazyTextureMapping(LazyTextureMappingInitializer* initializer,
                     bool ownedTexCoords,
                     bool transparent) :
  _initializer(initializer),
  _glTextureId(NULL),
  _initialized(false),
  _texCoords(NULL),
  _translationU(0),
  _translationV(0),
  _scaleU(1),
  _scaleV(1),
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

#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  bool isValid() const {
    return _glTextureId != NULL;
  }

  void setGLTextureId(const TextureIDReference* glTextureId) {
    releaseGLTextureId();
    _glTextureId = glTextureId;
  }

  const TextureIDReference* getGLTextureId() const {
    return _glTextureId;
  }

  void modifyGLState(GLState& state) const;

};


class LeveledTexturedMesh : public Mesh {
private:
  const Mesh * _mesh;
  const bool  _ownedMesh;

  mutable std::vector<LazyTextureMapping*>* _mappings;
  mutable int  _currentLevel;

  LazyTextureMapping* getCurrentTextureMapping() const;

  mutable GLState* _glState;

public:
  LeveledTexturedMesh(const Mesh* mesh,
                      bool ownedMesh,
                      std::vector<LazyTextureMapping*>* mappings) :
  _mesh(mesh),
  _ownedMesh(ownedMesh),
  _mappings(mappings),
  _currentLevel(-1),
  _glState(new GLState())
  {
    if (_mappings->size() <= 0) {
      ILogger::instance()->logError("LeveledTexturedMesh: empty mappings");
    }
  }

  virtual ~LeveledTexturedMesh();

  int getVertexCount() const;

  const Vector3D getVertex(int i) const;

  BoundingVolume* getBoundingVolume() const;

  bool setGLTextureIdForLevel(int level,
                              const TextureIDReference* glTextureId);

  const TextureIDReference* getTopLevelTextureId() const;

  bool isTransparent(const G3MRenderContext* rc) const;

  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentGLState) const;

  void showNormals(bool v) const{
    _mesh->showNormals(v);
  }

};

#endif
