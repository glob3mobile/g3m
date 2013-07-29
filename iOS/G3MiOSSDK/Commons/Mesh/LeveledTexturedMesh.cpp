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
#include "TexturesHandler.hpp"

#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
//#include "GPUProgramState.hpp"
#include "Camera.hpp"

void LazyTextureMapping::modifyGLState(GLState& state) const{
  if (!_initialized) {
    _initializer->initialize();

    _scale       = _initializer->getScale();
    _translation = _initializer->getTranslation();
    _texCoords   = _initializer->createTextCoords();

    delete _initializer;
    _initializer = NULL;

    _initialized = true;
  }

  if (_texCoords != NULL) {
    state.clearGLFeatureGroup(COLOR_GROUP);

    if (!_scale.isEqualsTo(1.0, 1.0) || !_translation.isEqualsTo(0.0, 0.0)){

      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                                        _texCoords, 2, 0, false, 0,
                                                        isTransparent(),
                                                        GLBlendFactor::srcAlpha(),
                                                        GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                                        true, _translation.asVector2D(), _scale.asVector2D()),
                         false); //TRANSFORM
    } else{
      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                                        _texCoords, 2, 0, false, 0,
                                                        isTransparent(),
                                                        GLBlendFactor::srcAlpha(),
                                                        GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                                        false, Vector2D::zero(), Vector2D::zero() ),
                         false); //TRANSFORM
    }

  }
  else {
    ILogger::instance()->logError("LazyTextureMapping::bind() with _texCoords == NULL");
  }

}

void LazyTextureMapping::releaseGLTextureId() {
  if (_texturesHandler) {
    if (_glTextureId != NULL) {
      _texturesHandler->releaseGLTextureId(_glTextureId);
      _glTextureId = NULL;
    }
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

#ifdef JAVA_CODE
  }
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
  if (_mappings == NULL) {
    return NULL;
  }

  if (!_currentLevelIsValid) {

    int newCurrentLevel = 0;

    for (int i = 0; i < _levelsCount; i++) {
      LazyTextureMapping* mapping = _mappings->at(i);
      if (mapping != NULL) {
        if (mapping->isValid()) {
          //ILogger::instance()->logInfo("LeveledTexturedMesh changed from level %d to %d", _currentLevel, i);
          newCurrentLevel = i;
          _currentLevelIsValid = true;
          break;
        }
      }
    }

    if (newCurrentLevel != _currentLevel){
      _currentLevel = newCurrentLevel;
      //MESH SHOULD BE NOTIFIED TO CHANGE STATE FROM TILE

      if (_currentLevelIsValid) {
        for (int i = _currentLevel+1; i < _levelsCount; i++) {
          LazyTextureMapping* mapping = _mappings->at(i);
          if (mapping != NULL) {
            _mappings->at(i) = NULL;
            delete mapping;
          }
        }
      }
    }


  }

  return _currentLevelIsValid ? _mappings->at(_currentLevel) : NULL;
}

const IGLTextureId* LeveledTexturedMesh::getTopLevelGLTextureId() const {
  const LazyTextureMapping* mapping = getCurrentTextureMapping();
  if (mapping != NULL) {
    if (_currentLevel == 0) {
      return mapping->getGLTextureId();
    }
  }

  return NULL;
}


void LeveledTexturedMesh::render(const G3MRenderContext* rc) const {
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  if (mapping == NULL) {
    _mesh->render(rc);
  }
  else {
    _mesh->render(rc);
  }
}

bool LeveledTexturedMesh::setGLTextureIdForLevel(int level,
                                                 const IGLTextureId* glTextureId) {
  if (_mappings->size() <= 0) {
    return false;
  }
  if (glTextureId != NULL) {
    if (!_currentLevelIsValid || (level < _currentLevel)) {
      _mappings->at(level)->setGLTextureId(glTextureId);
      _currentLevelIsValid = false;
      return true;
    }
  }

  return false;
}

bool LeveledTexturedMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_mesh->isTransparent(rc)) {
    return true;
  }

  LazyTextureMapping* mapping = getCurrentTextureMapping();

  if (mapping == NULL) {
    return false;
  }

  return mapping->isTransparent();
}

void LeveledTexturedMesh::updateGLState(){
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  if (mapping != NULL && mapping != _mappingOnGLState){
    _mappingOnGLState = mapping;
    mapping->modifyGLState(_glState);
  }
}

void LeveledTexturedMesh::render(const G3MRenderContext* rc, const GLState* parentGLState){

  updateGLState();

  _glState.setParent(parentGLState);
  ((Mesh*)_mesh)->render(rc, &_glState);
}
