//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGRotateNode.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "Vector3D.hpp"
#include "GPUProgramState.hpp"

//GLState* SGRotateNode::createState(const G3MRenderContext* rc,
//                     const GLState& parentState) {
//  return NULL;
//}
//
//GPUProgramState* SGRotateNode::createGPUProgramState(const G3MRenderContext* rc,
//                                                        const GPUProgramState* parentState){
//  
//  GPUProgramState* progState = new GPUProgramState(parentState);
//  progState->multiplyUniformValue("Modelview", MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(_angle),
//                                                                                      Vector3D(_x, _y, _z)));
//  return progState;
//  
//}

void SGRotateNode::modifyGLState(GLState& glState) const{
  
}

void SGRotateNode::modifyGPUProgramState(GPUProgramState& progState) const{
  progState.multiplyUniformValue("Modelview", &_rotationMatrix);
}