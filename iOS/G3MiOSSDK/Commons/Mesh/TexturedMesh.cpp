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

void TexturedMesh::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
  _textureMapping->modifyGLGlobalState(GLGlobalState);
  if (_transparent) {
    GLGlobalState.enableBlend();
    GLGlobalState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  }
}

void TexturedMesh::modifyGPUProgramState(GPUProgramState& progState) const{
  _textureMapping->modifyGPUProgramState(progState);
}

void TexturedMesh::notifyGLClientChildrenParentHasChanged(){
  Mesh* mesh = (Mesh*)_mesh;
  mesh->actualizeGLGlobalState(this);
}
