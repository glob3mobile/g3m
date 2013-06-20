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
#include "GPUProgramState.hpp"

//GLGlobalState* SimpleTextureMapping::bind(const G3MRenderContext* rc, const GLGlobalState& parentState, GPUProgramState& progState) const {
//
//  //GLGlobalState* state= new GLGlobalState(parentState);
//  GLGlobalState* state = NULL;
//  
//  if (_texCoords != NULL) {
//    
//    
//    //Activating Attribute in Shader program
//    progState.setAttributeEnabled(GPUVariable::TEXTURE_COORDS, true);
//    progState.setUniformValue(GPUVariable::EnableTexture, true);
//    progState.setAttributeValue(GPUVariable::TEXTURE_COORDS,
//                                _texCoords, 2,
//                                2,
//                                0,
//                                false,
//                                0);
//    
//    progState.setUniformValue(GPUVariable::SCALE_TEXTURE_COORDS, _scale.asVector2D());
//    progState.setUniformValue(GPUVariable::TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
//  
//    state->bindTexture(_glTextureId);
//  }
//  else {
//    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
//  }
//  
//  return state;
//}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }
}

void SimpleTextureMapping::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
  if (_texCoords != NULL) {
    GLGlobalState.bindTexture(_glTextureId);
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}

void SimpleTextureMapping::modifyGPUProgramState(GPUProgramState& progState) const{
  if (_texCoords != NULL) {
    //Activating Attribute in Shader program
    progState.setAttributeEnabled(GPUVariable::TEXTURE_COORDS, true);
    progState.setUniformValue(GPUVariable::EnableTexture, true);
    progState.setAttributeValue(GPUVariable::TEXTURE_COORDS,
                                _texCoords, 2,
                                2,
                                0,
                                false,
                                0);
    
    progState.setUniformValue(GPUVariable::SCALE_TEXTURE_COORDS, _scale.asVector2D());
    progState.setUniformValue(GPUVariable::TRANSLATION_TEXTURE_COORDS, _translation.asVector2D());
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}
