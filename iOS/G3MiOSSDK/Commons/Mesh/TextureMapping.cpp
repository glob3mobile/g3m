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
//#include "GPUProgramState.hpp"

#include "GLState.hpp"

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }

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
      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                              _texCoords, 2, 0, false, 0,
                                              isTransparent(),
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                              true, _translation.asVector2D(), _scale.asVector2D()), false); //TRANSFORM
    }
    else {
      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                              _texCoords, 2, 0, false, 0,
                                              isTransparent(),
                                              GLBlendFactor::srcAlpha(),
                                              GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                              false, Vector2D::zero(), Vector2D::zero() ), false); //TRANSFORM
    }
  }
}
