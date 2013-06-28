//
//  FlatColorMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#include "FlatColorMesh.hpp"

void FlatColorMesh::render(const G3MRenderContext* rc) const {
  _mesh->render(rc);
}

void FlatColorMesh::createGLState(){
  GLGlobalState& globalState = *_glState.getGLGlobalState();
  
  if (isTransparent(NULL)) {
    globalState.enableBlend();
    globalState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  }
  
  GPUProgramState& progState = *_glState.getGPUProgramState();
  
  progState.setUniformValue(GPUVariable::FLAT_COLOR,
                            (double) _flatColor->getRed(),
                            (double) _flatColor->getGreen(),
                            (double) _flatColor->getBlue(),
                            (double) _flatColor->getAlpha());
  
  
}

void FlatColorMesh::render(const G3MRenderContext* rc, const GLState* parentState){
  _glState.setParent(parentState);
  _mesh->render(rc, &_glState);
}
