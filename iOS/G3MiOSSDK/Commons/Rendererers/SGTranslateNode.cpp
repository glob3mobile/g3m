//
//  SGTranslateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTranslateNode.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "GPUProgramState.hpp"

//GLState* SGTranslateNode::createState(const G3MRenderContext* rc,
//                     const GLState& parentState) {
//  return NULL;
//}
//
//GPUProgramState* SGTranslateNode::createGPUProgramState(const G3MRenderContext* rc,
//                                               const GPUProgramState* parentState){
//  
//  GPUProgramState* progState = new GPUProgramState(parentState);
//  progState->multiplyUniformValue("Modelview", MutableMatrix44D::createTranslationMatrix(_x, _y, _z));
//  return progState;
//  
//}

void SGTranslateNode::modifyGLState(GLState& glState) const{
  
}

void SGTranslateNode::modifyGPUProgramState(GPUProgramState& progState) const{
  progState.multiplyUniformValue("Modelview", &_translationMatrix);
}


