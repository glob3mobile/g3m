//
//  StenciledMultiTextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 24/12/13.
//
//

#include "StenciledMultiTextureMapping.hpp"

#include "ILogger.hpp"
#include "IFloatBuffer.hpp"
#include "GLState.hpp"


void StenciledMultiTextureMapping::releaseGLTextureId() {

  if (_glTextureId != NULL) {
#ifdef C_CODE
    delete _glTextureId;
#endif
#ifdef JAVA_CODE
    _glTextureId.dispose();
#endif
    _glTextureId = NULL;
  } else{
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
  } else{
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }

  if (_stencilID != NULL) {
#ifdef C_CODE
    delete _stencilID;
#endif
#ifdef JAVA_CODE
    _stencilID.dispose();
#endif
    _stencilID = NULL;
  } else{
    ILogger::instance()->logError("Releasing invalid Multi texture mapping");
  }
}

StenciledMultiTextureMapping::~StenciledMultiTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }

  if (_ownedTexCoords2){
    delete _texCoords2;
  }

  if (_ownedTexCoordsStencil){
    delete _texCoordsStencil;
  }

  releaseGLTextureId();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void StenciledMultiTextureMapping::modifyGLState(GLState& state) const{
  //TARGET 0
  if (_texCoords == NULL) {
    ILogger::instance()->logError("StenciledMultiTextureMapping::bind() with _texCoords == NULL");
  }
  else {
    state.clearGLFeatureGroup(COLOR_GROUP);

    if ((_scaleU != 1) ||
        (_scaleV != 1) ||
        (_translationU != 0) ||
        (_translationV != 0) ||
        (_rotationInRadians != 0)) {
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
    else {
      state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                              _texCoords,
                                              2,
                                              0,
                                              false,
                                              0,
                                              _transparent,
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha()),
                         false);
    }
  }

  //TAGET 1
  if (_texCoords2 == NULL) {
    ILogger::instance()->logError("StenciledMultiTextureMapping::bind() with _texCoords2 == NULL");
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

  //STENCIL
  if (_texCoordsStencil == NULL) {
    ILogger::instance()->logError("StenciledMultiTextureMapping::bind() with _texCoordsStencil == NULL");
  }
  else {

    state.addGLFeature(new TextureGLFeature(_stencilID->getID(),
                                            _texCoordsStencil,
                                            2,
                                            0,
                                            false,
                                            0,
                                            false,
                                            GLBlendFactor::srcAlpha(),
                                            GLBlendFactor::oneMinusSrcAlpha(),
                                            3), //TARGET
                       false);
  }
}
