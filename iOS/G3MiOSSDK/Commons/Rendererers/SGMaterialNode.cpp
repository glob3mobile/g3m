//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGMaterialNode.hpp"

#include "Context.hpp"
#include "GLState.hpp"
#include "GPUProgramState.hpp"

GLState* SGMaterialNode::createState(const G3MRenderContext* rc,
                                     const GLState& parentState) {
//  if (_baseColor == NULL) {
//    return NULL;
//  }
//  
//  GLState* state = new GLState(parentState);
//  const float colorsIntensity = 1;
//  //state->enableFlatColor(*_specularColor, colorsIntensity);
//  state->enableFlatColor(*_baseColor, colorsIntensity);
//  
//  return state;
  return NULL;
}

GPUProgramState* SGMaterialNode::createGPUProgramState(const G3MRenderContext* rc,
                                                       const GPUProgramState* parentState){
  
  
  GPUProgramState* progState = new GPUProgramState(parentState);
  
  if (_baseColor != NULL){
    progState->setUniformValue("EnableFlatColor", true);
    progState->setUniformValue("FlatColor",
                               (double)_baseColor->getRed(),
                               (double)_baseColor->getBlue(),
                               (double) _baseColor->getGreen(),
                               (double) _baseColor->getAlpha());
    const float colorsIntensity = 1;
    progState->setUniformValue("FlatColorIntensity", colorsIntensity);
  }
  
  return progState;
}
