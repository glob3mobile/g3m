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

GLState* SimpleTextureMapping::bind(const G3MRenderContext* rc, const GLState& parentState, GPUProgramState& progState) const {

  GLState* state= new GLState(parentState);
  if (_texCoords != NULL) {
    
    
    //Activating Attribute in Shader program
    progState.setAttributeEnabled("TextureCoord", true);
    progState.setUniformValue("EnableTexture", true);
    progState.setAttributeValue("TextureCoord",
                                _texCoords, 2,
                                2,
                                0,
                                false,
                                0);
    
    progState.setUniformValue("ScaleTexCoord", _scale.asVector2D());
    progState.setUniformValue("TranslationTexCoord", _translation.asVector2D());
  
    state->bindTexture(_glTextureId);
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
  
  return state;
}

SimpleTextureMapping::~SimpleTextureMapping() {
  if (_ownedTexCoords) {
    delete _texCoords;
  }
}

void SimpleTextureMapping::modifyGLState(GLState* glState) const{
  if (_texCoords != NULL) {
    glState->bindTexture(_glTextureId);
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}

void SimpleTextureMapping::modifyGPUProgramState(GPUProgramState* progState) const{
  if (_texCoords != NULL) {
    //Activating Attribute in Shader program
    progState->setAttributeEnabled("TextureCoord", true);
    progState->setUniformValue("EnableTexture", true);
    progState->setAttributeValue("TextureCoord",
                                _texCoords, 2,
                                2,
                                0,
                                false,
                                0);
    
    progState->setUniformValue("ScaleTexCoord", _scale.asVector2D());
    progState->setUniformValue("TranslationTexCoord", _translation.asVector2D());
  }
  else {
    ILogger::instance()->logError("SimpleTextureMapping::bind() with _texCoords == NULL");
  }
}
