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

void TexturedMesh::render(const G3MRenderContext* rc) const {
  _mesh->render(rc);
}

void TexturedMesh::createGLState(){
  GLGlobalState& globalState = *_glState.getGLGlobalState();
  _textureMapping->modifyGLGlobalState(globalState);
  if (_transparent) {
    globalState.enableBlend();
    globalState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  }
  
  GPUProgramState& progState = *_glState.getGPUProgramState();
  _textureMapping->modifyGPUProgramState(progState);
}

void TexturedMesh::render(const G3MRenderContext* rc, GLState* parentState){
  _glState.setParent(parentState);
  _mesh->render(rc, &_glState);
}
