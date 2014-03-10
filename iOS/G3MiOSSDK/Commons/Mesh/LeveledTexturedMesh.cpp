//
//  LeveledTexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/08/12.
//
//

#include "LeveledTexturedMesh.hpp"

#include "Vector3D.hpp"
#include "GL.hpp"
#include "TextureIDReference.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
#include "Camera.hpp"
#include "GLState.hpp"

void LazyTextureMapping::modifyGLState(GLState& state) const{
  if (!_initialized) {
    _initializer->initialize();

    const Vector2F scale = _initializer->getScale();
    _scaleU = scale._x;
    _scaleV = scale._y;

    const Vector2F translation = _initializer->getTranslation();
    _translationU = translation._x;
    _translationV = translation._y;

    _texCoords   = _initializer->createTextCoords();

    delete _initializer;
    _initializer = NULL;

    _initialized = true;
  }

  if (_texCoords != NULL) {
    state.clearGLFeatureGroup(COLOR_GROUP);

    if (_scaleU != 1 ||
        _scaleV != 1 ||
        _translationU != 0 ||
        _translationV != 0) {
      state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                              _texCoords, 2, 0, false, 0,
                                              _transparent,
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                              _translationU,
                                              _translationV,
                                              _scaleU,
                                              _scaleV,
                                              0, 0, 0),
                         false);
    }
    else {
      state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                              _texCoords, 2, 0, false, 0,
                                              _transparent,
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha()),
                         false);
    }

  }
  else {
    ILogger::instance()->logError("LazyTextureMapping::bind() with _texCoords == NULL");
  }

}

void LazyTextureMapping::releaseGLTextureId() {
  if (_glTextureId != NULL) {
#ifdef C_CODE
    delete _glTextureId;
#endif
#ifdef JAVA_CODE
    _glTextureId.dispose();
#endif
    _glTextureId = NULL;
  }
}

LeveledTexturedMesh::~LeveledTexturedMesh() {
#ifdef JAVA_CODE
  synchronized (this) {
#endif

    if (_ownedMesh) {
      delete _mesh;
    }

    if (_mappings != NULL) {
      for (int i = 0; i < _mappings->size(); i++) {
        LazyTextureMapping* mapping = _mappings->at(i);
        delete mapping;
      }

      delete _mappings;
      _mappings = NULL;
    }

    _glState->_release();

#ifdef JAVA_CODE
  }
#endif

#ifdef JAVA_CODE
  super.dispose();
#endif
}

int LeveledTexturedMesh::getVertexCount() const {
  return _mesh->getVertexCount();
}

const Vector3D LeveledTexturedMesh::getVertex(int i) const {
  return _mesh->getVertex(i);
}

BoundingVolume* LeveledTexturedMesh::getBoundingVolume() const {
  return (_mesh == NULL) ? NULL : _mesh->getBoundingVolume();
}

LazyTextureMapping* LeveledTexturedMesh::getCurrentTextureMapping() const {
  if (_currentLevel < 0) {
    int newCurrentLevel = -1;

    const int levelsCount = _mappings->size();

    for (int i = 0; i < levelsCount; i++) {
      const LazyTextureMapping* mapping = _mappings->at(i);
      if (mapping != NULL) {
        if (mapping->isValid()) {
          newCurrentLevel = i;
          break;
        }
      }
    }

    if (newCurrentLevel >= 0) {
      // ILogger::instance()->logInfo("LeveledTexturedMesh: changed from level %d to %d",
      //                              _currentLevel,
      //                              newCurrentLevel);
      _currentLevel = newCurrentLevel;

      _mappings->at(_currentLevel)->modifyGLState(*_glState);

      if (_currentLevel < levelsCount-1) {
        for (int i = levelsCount-1; i > _currentLevel; i--) {
          const LazyTextureMapping* mapping = _mappings->at(i);
          delete mapping;
#ifdef JAVA_CODE
          _mappings.remove(i);
#endif
        }
#ifdef C_CODE
        _mappings->erase(_mappings->begin() + _currentLevel + 1,
                         _mappings->end());
#endif
#ifdef JAVA_CODE
        _mappings.trimToSize();
#endif
      }
    }
  }

  return (_currentLevel >= 0) ? _mappings->at(_currentLevel) : NULL;
}

const TextureIDReference* LeveledTexturedMesh::getTopLevelTextureId() const {
  const LazyTextureMapping* mapping = getCurrentTextureMapping();
  if (mapping != NULL) {
    if (_currentLevel == 0) {
      return mapping->getGLTextureId();
    }
  }

  return NULL;
}

bool LeveledTexturedMesh::setGLTextureIdForLevel(int level,
                                                 const TextureIDReference* glTextureId) {

  if (_mappings->size() > 0) {
    if (glTextureId != NULL) {
      if ((_currentLevel < 0) || (level < _currentLevel)) {
        _mappings->at(level)->setGLTextureId(glTextureId);
        _currentLevel = -1;
        return true;
      }
    }
  }

  return false;
}

bool LeveledTexturedMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_mesh->isTransparent(rc)) {
    return true;
  }

  LazyTextureMapping* mapping = getCurrentTextureMapping();

  return (mapping == NULL) ? false : mapping->_transparent;
}

void LeveledTexturedMesh::rawRender(const G3MRenderContext* rc,
                                    const GLState* parentGLState) const{
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  if (mapping == NULL) {
    ILogger::instance()->logError("LeveledTexturedMesh: No Texture Mapping");
    _mesh->render(rc, parentGLState);
  }
  else {
    _glState->setParent(parentGLState);
    _mesh->render(rc, _glState);
  }
}
