//
//  MultiTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

#include "MultiTextureMapping.hpp"

#include "ILogger.hpp"
#include "IFloatBuffer.hpp"
#include "GLState.hpp"
#include "TextureIDReference.hpp"

const IGLTextureId* MultiTextureMapping::getGLTextureId() const {
  return _glTextureId->getID();
}

void MultiTextureMapping::releaseGLTextureId() {

  if (_glTextureId != NULL) {
#ifdef C_CODE
    delete _glTextureId;
#endif
#ifdef JAVA_CODE
    _glTextureId.dispose();
#endif
    _glTextureId = NULL;
  }
  else {
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }

  if (_glTextureId2 != NULL) {
#ifdef C_CODE
    delete _glTextureId2;
#endif
#ifdef JAVA_CODE
    _glTextureId2.dispose();
#endif
    _glTextureId2 = NULL;
  }
  else {
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }
}

MultiTextureMapping::~MultiTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }

  if (_ownedTexCoords2) {
    delete _texCoords2;
  }

  releaseGLTextureId();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MultiTextureMapping::modifyGLState(GLState& state) const{


  GLFeatureSet tglfs = state.getGLFeatures(GLF_TEXTURE);

  for (int i = 0; i < tglfs.size(); i++) {
    TextureGLFeature* tglf =  (TextureGLFeature*) tglfs.get(0);
    if (tglf->getTarget() == 0 && tglf->getTextureID() == _glTextureId->getID()) {
      tglf->setScale(_scaleU, _scaleV);
      tglf->setTranslation(_translationU, _translationV);
      tglf->setRotationAngleInRadiansAndRotationCenter(_rotationInRadians, _rotationCenterU, _rotationCenterV);
      return; //The TextureGLFeature for target 0 already exists and we do not have to recreate the state
    }
  }

  //CREATING TWO TEXTURES GLFEATURE

  state.clearGLFeatureGroup(COLOR_GROUP);

  // TARGET 0
  if (_texCoords == NULL) {
    ILogger::instance()->logError("MultiTextureMapping::bind() with _texCoords == NULL");
  }
  else {
    state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                            _texCoords,
                                            2,
                                            0,
                                            false,
                                            0,
                                            _transparent,
                                            GLBlendFactor::srcAlpha(),
                                            GLBlendFactor::oneMinusSrcAlpha(),
                                            _translationU,
                                            _translationV,
                                            _scaleU,
                                            _scaleV,
                                            _rotationInRadians,
                                            _rotationCenterU,
                                            _rotationCenterV),
                       false);

  }

  // TARGET 1
  if (_texCoords2 == NULL) {
    ILogger::instance()->logError("MultiTextureMapping::bind() with _texCoords2 == NULL");
  }
  else {
    state.addGLFeature(new TextureGLFeature(_glTextureId2->getID(),
                                            _texCoords2,
                                            2,
                                            0,
                                            false,
                                            0,
                                            _transparent2,
                                            GLBlendFactor::srcAlpha(),
                                            GLBlendFactor::oneMinusSrcAlpha(),
                                            1), //TARGET
                       false);
  }

}
