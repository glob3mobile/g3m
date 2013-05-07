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
  
  //state->enableTextures();
  
  //  GPUProgram* prog = rc->getGPUProgramManager()->getProgram("DefaultProgram");
  
  //  int _WORKING_JM;
  //UniformBool* enableTexture = prog->getUniformBool("EnableTexture");
  //enableTexture->set(true);
  
  //  state->enableTexture2D();
  
  
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
    
//    state->scaleTextureCoordinates(_scale);
//    state->translateTextureCoordinates(_translation);
    state->bindTexture(_glTextureId);
    
//    state->setTextureCoordinates(_texCoords, 2, 0);
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
