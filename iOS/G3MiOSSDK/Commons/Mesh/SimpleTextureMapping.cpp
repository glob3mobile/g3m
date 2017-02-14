//
//  SimpleTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

#include "SimpleTextureMapping.hpp"


#include "G3MContext.hpp"
#include "GL.hpp"
#include "GPUProgramManager.hpp"
#include "GPUProgram.hpp"
#include "GLState.hpp"
#include "TextureIDReference.hpp"

const IGLTextureID* SimpleTextureMapping::getGLTextureID() const {
  return _glTextureID->getID();
}

void SimpleTextureMapping::releaseGLTextureID() {

  if (_glTextureID != NULL) {
#ifdef C_CODE
    delete _glTextureID;
#endif
#ifdef JAVA_CODE
    _glTextureID.dispose();
#endif
    _glTextureID = NULL;
  }
  else {
    ILogger::instance()->logError("Releasing invalid simple texture mapping");
  }
}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }

  releaseGLTextureID();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void SimpleTextureMapping::modifyGLState(GLState& state) const {
  if (_texCoords == NULL) {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
  else {
    TextureGLFeature* tglf = (TextureGLFeature*) state.getGLFeature(GLF_TEXTURE);
    if (tglf != NULL && tglf->getTextureID() == _glTextureID->getID()) {
      tglf->setScale(_scaleU, _scaleV);
      tglf->setTranslation(_translationU, _translationV);
      tglf->setRotationAngleInRadiansAndRotationCenter(_rotationInRadians, _rotationCenterU, _rotationCenterV);
    }
    else {
      state.clearGLFeatureGroup(COLOR_GROUP);
      if ((_scaleU != 1) ||
          (_scaleV != 1) ||
          (_translationU != 0) ||
          (_translationV != 0) ||
          (_rotationInRadians != 0)) {
        state.addGLFeature(new TextureGLFeature(_glTextureID->getID(),
                                                _texCoords,
                                                2,
                                                0,
                                                false,
                                                0,
                                                _transparent,
                                                _glTextureID->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
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
      else {
        state.addGLFeature(new TextureGLFeature(_glTextureID->getID(),
                                                _texCoords,
                                                2,
                                                0,
                                                false,
                                                0,
                                                _transparent,
                                                _glTextureID->isPremultiplied() ? GLBlendFactor::one() : GLBlendFactor::srcAlpha(),
                                                GLBlendFactor::oneMinusSrcAlpha()
                                                ),
                           false);
      }
    }
  }
}
