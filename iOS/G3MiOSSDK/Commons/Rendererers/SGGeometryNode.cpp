//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGGeometryNode.hpp"

#include "Context.hpp"
#include "GL.hpp"

#include "IFloatBuffer.hpp"
#include "IShortBuffer.hpp"

#include "GPUProgramState.hpp"

SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc,
                               const GLGlobalState& parentState, const GPUProgramState* parentProgramState) {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, parentState, *rc->getGPUProgramManager(), parentProgramState);
}

void SGGeometryNode::createGLState() const{
  
  GPUProgramState& progState = *_glState.getGPUProgramState();
  
  progState.setAttributeEnabled(GPUVariable::POSITION, true);
  progState.setAttributeValue(GPUVariable::POSITION,
                              _vertices, 4, //The attribute is a float vector of 4 elements
                              3,            //Our buffer contains elements of 3
                              0,            //Index 0
                              false,        //Not normalized
                              0);           //Stride 0
  
  if (_colors != NULL){
    progState.setAttributeEnabled(GPUVariable::COLOR, true);
    progState.setUniformValue(GPUVariable::EnableColorPerVertex, true);
    progState.setAttributeValue(GPUVariable::COLOR,
                                _colors, 4,   //The attribute is a float vector of 4 elements RGBA
                                4,            //Our buffer contains elements of 4
                                0,            //Index 0
                                false,        //Not normalized
                                0);           //Stride 0
    const float colorsIntensity = 1;
    progState.setUniformValue(GPUVariable::FlatColorIntensity, colorsIntensity);
  } else{
    progState.setAttributeEnabled(GPUVariable::COLOR, false);
    progState.setUniformValue(GPUVariable::EnableColorPerVertex, false);
  }
  
  if (_uv != NULL){
    progState.setAttributeValue(GPUVariable::TEXTURE_COORDS,
                                _uv, 2,
                                2,
                                0,
                                false,
                                0);
    
//    progState.setUniformValue(GPUVariable::SCALE_TEXTURE_COORDS, Vector2D(1.0, 1.0));
//    progState.setUniformValue(GPUVariable::TRANSLATION_TEXTURE_COORDS, Vector2D(0.0, 0.0));
  }
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc, GLState* glState){
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, glState, *rc->getGPUProgramManager());
}
