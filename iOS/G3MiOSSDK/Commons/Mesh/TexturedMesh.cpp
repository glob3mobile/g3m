//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TexturedMesh.hpp"

#include "GL.hpp"

#include "GPUProgramState.hpp"

void TexturedMesh::render(const G3MRenderContext* rc,
                          const GLState& parentState,const GPUProgramState* parentProgramState) const {
  
  GPUProgramState progState(parentProgramState);
  
  GLState* state = _textureMapping->bind(rc, parentState, progState);

//  if (_transparent) {
//    state->enableBlend();
//    state->setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
//  }

  _mesh->render(rc, *state, &progState);
  
  delete state;
}

void TexturedMesh::modifyGLState(GLState& glState) const{
  _textureMapping->modifyGLState(glState);
  if (_transparent) {
    glState.enableBlend();
    glState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  }
}

void TexturedMesh::modifyGPUProgramState(GPUProgramState& progState) const{
  _textureMapping->modifyGPUProgramState(progState);
}

void TexturedMesh::notifyGLClientChildrenParentHasChanged(){
  Mesh* mesh = (Mesh*)_mesh;
  mesh->actualizeGLState(this);
}
