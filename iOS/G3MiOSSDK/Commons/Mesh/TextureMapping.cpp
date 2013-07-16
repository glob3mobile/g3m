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
}

void SimpleTextureMapping::modifyGLState(GLState& state) const{
  
//  GLGlobalState* glGlobalState = state.getGLGlobalState();
//  GPUProgramState* progState = state.getGPUProgramState();

  if (_texCoords != NULL) {
//    glGlobalState->bindTexture(_glTextureId);
//    
//    progState->setAttributeValue(TEXTURE_COORDS,
//                                _texCoords, 2,
//                                2,
//                                0,
//                                false,
//                                0);
//    
//    if (!_scale.isEqualsTo(1.0, 1.0) || !_translation.isEqualsTo(0.0, 0.0)){
//      progState->setUniformValue(SCALE_TEXTURE_COORDS, _scale.asVector2D());
//      progState->setUniformValue(TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
//    } else{
//      //ILogger::instance()->logInfo("No transformed TC");
//      progState->removeGPUUniformValue(SCALE_TEXTURE_COORDS);
//      progState->removeGPUUniformValue(TRANSLATION_TEXTURE_COORDS);
//    }

    state.clearGLFeatureGroup(COLOR_GROUP);

    if (!_scale.isEqualsTo(1.0, 1.0) || !_translation.isEqualsTo(0.0, 0.0)){

      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                                        _texCoords, 2, 0, false, 0,
                                                        isTransparent(),
                                                        GLBlendFactor::srcAlpha(),
                                                        GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                                        true, _translation.asVector2D(), _scale.asVector2D()), false); //TRANSFORM
    } else{
      state.addGLFeature(new TextureGLFeature(_glTextureId,
                                                        _texCoords, 2, 0, false, 0,
                                                        isTransparent(),
                                                        GLBlendFactor::srcAlpha(),
                                                        GLBlendFactor::oneMinusSrcAlpha(),    //BLEND
                                                        false, Vector2D::zero(), Vector2D::zero() ), false); //TRANSFORM
    }
    
//    progState->setUniformValue(SCALE_TEXTURE_COORDS, _scale.asVector2D());
//    progState->setUniformValue(TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
    
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}
