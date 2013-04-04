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

GLState* SimpleTextureMapping::bind(const G3MRenderContext* rc, const GLState& parentState) const {
  GLState* state= new GLState(parentState);
  state->enableTextures();
  
  GPUProgram* prog = rc->getGPUProgramManager()->getProgram("DefaultProgram");
  UniformBool* enableTexture = prog->getUniformBool("EnableTexture");
  enableTexture->set(true);
  
  //state->enableTexture2D();
  
  if (_texCoords != NULL) {
    state->scaleTextureCoordinates(_scale);
    state->translateTextureCoordinates(_translation);
    state->bindTexture(_glTextureId);
    
    state->setTextureCoordinates(_texCoords, 2, 0);  }
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
