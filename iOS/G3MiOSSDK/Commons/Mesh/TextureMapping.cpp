//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "TextureMapping.hpp"

#include "Context.hpp"
#include "GL.hpp"

#include "GPUProgramManager.hpp"
#include "GPUProgram.hpp"
#include "TexturesHandler.hpp"
#include "GLState.hpp"

void SimpleTextureMapping::releaseGLTextureId() {

  if (_glTextureId != NULL){
#ifdef C_CODE
    delete _glTextureId;
#endif
#ifdef JAVA_CODE
    _glTextureId.dispose();
#endif
    _glTextureId = NULL;
  } else{
    ILogger::instance()->logError("Releasing invalid simple texture mapping");
  }
}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }

  releaseGLTextureId();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void SimpleTextureMapping::modifyGLState(GLState& state) const{
  if (_texCoords == NULL) {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
  else {
    state.clearGLFeatureGroup(COLOR_GROUP);

    if (!_scale.isEquals(1.0, 1.0) || !_translation.isEquals(0.0, 0.0)) {
      state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                              _texCoords, 2, 0, false, 0,
                                              _transparent,
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                              true, _translation.asVector2D(), _scale.asVector2D()), false); //TRANSFORM
    }
    else {
      state.addGLFeature(new TextureGLFeature(_glTextureId->getID(),
                                              _texCoords, 2, 0, false, 0,
                                              _transparent,
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                              false, Vector2D::zero(), Vector2D::zero() ), false); //TRANSFORM
    }
  }
}
