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


void LazyTextureMapping::bind(const RenderContext* rc) const {
  if (!_initialized) {
    _initializer->calculate();
    
    _scale       = _initializer->getScale();
    _translation = _initializer->getTranslation();
    _texCoords   = _initializer->getTexCoords();
    
    delete _initializer;
    
    _initialized = true;
  }
  
  GL* gl = rc->getGL();
  
  gl->transformTexCoords(_scale, _translation);
  gl->bindTexture(_glTextureId);
  gl->setTextureCoordinates(2, 0, _texCoords);
}


void LazyTextureMapping::releaseGLTextureId() {
  if (_texturesHandler) {
    if (_glTextureId.isValid()) {
      _texturesHandler->releaseGLTextureId(_glTextureId);
      _glTextureId = GLTextureID::invalid();
    }
  }
}



LeveledTexturedMesh::~LeveledTexturedMesh() {
  if (_ownedMesh) {
    delete _mesh;
  }
  
  if (_mappings != NULL) {
    for (int i = 0; i < _mappings->size(); i++) {
      LazyTextureMapping* mapping = _mappings->at(i);
      if (mapping != NULL) {
        mapping->releaseGLTextureId();
        
        delete mapping;
      }
    }
    
    delete _mappings;
    _mappings = NULL;
  }
  
}

int LeveledTexturedMesh::getVertexCount() const {
  return _mesh->getVertexCount();
}

const Vector3D LeveledTexturedMesh::getVertex(int i) const {
  return _mesh->getVertex(i);
}

Extent* LeveledTexturedMesh::getExtent() const {
  return _mesh->getExtent();
}

LazyTextureMapping* LeveledTexturedMesh::getCurrentTextureMapping() const {
  if (_currentLevelDirty) {
    // backward iteration, last is best level
//    for (int i = _levelsCount-1; i >=0; i--) {
    for (int i = 0; i < _levelsCount; i++) {
      LazyTextureMapping* mapping = _mappings->at(i);
      if (mapping != NULL) {
        if (mapping->isValid()) {
          _currentLevel = i;
          _currentLevelDirty = false;
          break;
        }
      }
    }
    
    if (!_currentLevelDirty) {
//      for (int i = 0; i < _currentLevel; i++) {
      for (int i = _currentLevel+1; i < _levelsCount; i++) {
        LazyTextureMapping* mapping = _mappings->at(i);
        if (mapping != NULL) {
          mapping->releaseGLTextureId();

          _mappings->at(i) = NULL;
          delete mapping;
        }
      }
    }
  }
  
  return _currentLevelDirty ? NULL : _mappings->at(_currentLevel);
}

void LeveledTexturedMesh::render(const RenderContext* rc) const {
  LazyTextureMapping* mapping = getCurrentTextureMapping();
  
  if (mapping == NULL) {
    _mesh->render(rc);
  }
  else {
    GL *gl = rc->getGL();
    
    gl->enableTextures();
    gl->enableTexture2D();
    
    mapping->bind(rc);
    
    _mesh->render(rc);
    
    gl->disableTexture2D();
    gl->disableTextures();
  }
}

void LeveledTexturedMesh::setGLTextureIDForLevel(int level,
                                                 const GLTextureID glTextureID) {
  int __XXXX;
  
  if (level < _currentLevel || _currentLevelDirty) {
    if (glTextureID.isValid()) {
      if (_mappings->at(level)->setGLTextureID(glTextureID)) {
//        _currentLevel = -1; // force recalculation
        _currentLevelDirty=true;
      }
    }
  }
}
