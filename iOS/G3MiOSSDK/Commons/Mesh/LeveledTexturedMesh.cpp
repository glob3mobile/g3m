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
#include "GPUProgramState.hpp"
#include "Camera.hpp"

void LazyTextureMapping::modifyGLState(GLState& state) const{
  
  GLGlobalState* glGlobalState = state.getGLGlobalState();
  GPUProgramState* progState = state.getGPUProgramState();
  
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
    glGlobalState->bindTexture(_glTextureId);
    
    progState->setUniformValue(GPUVariable::SCALE_TEXTURE_COORDS, _scale.asVector2D());
    progState->setUniformValue(GPUVariable::TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
    progState->setAttributeValue(GPUVariable::TEXTURE_COORDS,
                                 _texCoords, 2,
                                 2,
                                 0,
                                 false,
                                 0);
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

Extent* LeveledTexturedMesh::getExtent() const {
  return (_mesh == NULL) ? NULL : _mesh->getExtent();
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

//void LeveledTexturedMesh::setGLTextureIdForInversedLevel(int inversedLevel,
//                                                         const const GLTextureId*glTextureId) {
//  const int level = _mappings->size() - inversedLevel - 1;
//  setGLTextureIdForLevel(level, glTextureId);
//}

bool LeveledTexturedMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_mesh->isTransparent(rc)) {
    return true;
  }
  
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  
  if (mapping == NULL) {
    return false;
  }
  
  return mapping->isTransparent(rc);
}

void LeveledTexturedMesh::updateGLState(){
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  mapping->modifyGLState(_glState);
}

void LeveledTexturedMesh::render(const G3MRenderContext* rc, const GLState* parentGLState){
  
  updateGLState();
  
  _glState.setParent(parentGLState);
  ((Mesh*)_mesh)->render(rc, &_glState);
}
