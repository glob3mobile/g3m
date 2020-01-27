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

const IGLTextureID* MultiTextureMapping::getGLTextureID() const {
  return _glTextureID1->getID();
}

void MultiTextureMapping::releaseGLTextureID() {

  if (_glTextureID1 != NULL) {
#ifdef C_CODE
    delete _glTextureID1;
#endif
#ifdef JAVA_CODE
    _glTextureID1.dispose();
#endif
    _glTextureID1 = NULL;
  }
  else {
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }

  if (_glTextureID2 != NULL) {
#ifdef C_CODE
    delete _glTextureID2;
#endif
#ifdef JAVA_CODE
    _glTextureID2.dispose();
#endif
    _glTextureID2 = NULL;
  }
  else {
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }
}

MultiTextureMapping::~MultiTextureMapping() {
  if (_ownedTexCoords1) {
    delete _texCoords1;
  }

  if (_ownedTexCoords2) {
    delete _texCoords2;
  }

  releaseGLTextureID();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MultiTextureMapping::modifyGLState(GLState& state) const {
  GLFeatureSet* tglfs = state.getGLFeatures(GLF_TEXTURE);

  for (int i = 0; i < tglfs->size(); i++) {
    TextureGLFeature* tglf =  (TextureGLFeature*) tglfs->get(0);
    if ((tglf->getTarget() == 0) &&
        (tglf->getTextureID() == _glTextureID1->getID())) {
      tglf->setScale(_scaleU, _scaleV);
      tglf->setTranslation(_translationU, _translationV);
      tglf->setRotation(_rotationInRadians,
                        _rotationCenterU, _rotationCenterV);
      delete tglfs;
      return; //The TextureGLFeature for target 0 already exists and we do not have to recreate the state
    }
  }
  delete tglfs;

  //CREATING TWO TEXTURES GLFEATURE

  state.clearGLFeatureGroup(COLOR_GROUP);

  // TARGET 0
  if (_texCoords1 == NULL) {
    ILogger::instance()->logError("MultiTextureMapping::bind() with _texCoords == NULL");
  }
  else {
    state.addGLFeature(new TextureGLFeature(_glTextureID1->getID(),
                                            _texCoords1,
                                            2,
                                            0,
                                            false,
                                            0,
                                            _transparent1,
                                            _glTextureID1->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
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
    state.addGLFeature(new TextureGLFeature(_glTextureID2->getID(),
                                            _texCoords2,
                                            2,
                                            0,
                                            false,
                                            0,
                                            _transparent2,
                                            _glTextureID1->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
                                            GLBlendFactor::oneMinusSrcAlpha(),
                                            1), //TARGET
                       false);
  }

}
