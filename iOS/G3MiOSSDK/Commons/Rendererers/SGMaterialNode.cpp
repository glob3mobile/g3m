//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGMaterialNode.hpp"

#include "Context.hpp"
#include "GLGlobalState.hpp"
//#include "GPUProgramState.hpp"

//GLGlobalState* SGMaterialNode::createState(const G3MRenderContext* rc,
//                                     const GLGlobalState& parentState) {
//  return NULL;
//}
//
//GPUProgramState* SGMaterialNode::createGPUProgramState(const G3MRenderContext* rc,
//                                                       const GPUProgramState* parentState){
//
//
//  //  GPUProgramState* progState = new GPUProgramState(parentState);
//
//  //  if (_baseColor != NULL){
//  //    progState->setUniformValue(EnableFlatColor, true);
//  //    progState->setUniformValue(FLAT_COLOR,
//  //                               (double)_baseColor->getRed(),
//  //                               (double)_baseColor->getBlue(),
//  //                               (double) _baseColor->getGreen(),
//  //                               (double) _baseColor->getAlpha());
//  //    const float colorsIntensity = 1;
//  //    progState->setUniformValue(FlatColorIntensity, colorsIntensity);
//  //  }
//  //
//  //  return progState;
//}


//void SGMaterialNode::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
//
//}
//void SGMaterialNode::modifyGPUProgramState(GPUProgramState& progState) const{
//
//  if (_baseColor != NULL){
//    //    progState.setUniformValue(EnableFlatColor, true);
//    progState.setUniformValue(FLAT_COLOR, _baseColor->getRed(),
//                              _baseColor->getGreen(),
//                              _baseColor->getBlue(),
//                              _baseColor->getAlpha());
//    //    const float colorsIntensity = 1;
//    //    progState.setUniformValue(FlatColorIntensity, colorsIntensity);
//  }
//}
