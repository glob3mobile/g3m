//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

#include "GLState.hpp"

#include "GLShaderAttributes.hpp"
#include "GLShaderUniforms.hpp"
#include "IFloatBuffer.hpp"


void GLState::applyChanges(const INativeGL* nativeGL, const GLState& currentState, const AttributesStruct& attributes, const UniformsStruct& uniforms) const{
  
  // Depth Testh
  if (_depthTest != currentState._depthTest) {
    if (_depthTest) {
      nativeGL->enable(GLFeature::depthTest());
    }
    else {
      nativeGL->disable(GLFeature::depthTest());
    }
  }
  
  // Blending
  if (_blend != currentState._blend) {
    if (_blend) {
      nativeGL->enable(GLFeature::blend());
    }
    else {
      nativeGL->disable(GLFeature::blend());
    }
  }
  
  // Textures
  if (_textures != currentState._textures) {
    if (_textures) {
      nativeGL->enableVertexAttribArray(attributes.TextureCoord);
    }
    else {
      nativeGL->disableVertexAttribArray(attributes.TextureCoord);
    }
  }
  
  // Texture2D
  if (_texture2D != currentState._texture2D) {
    if (_texture2D) {
      nativeGL->uniform1i(uniforms.EnableTexture, 1);
    }
    else {
      nativeGL->uniform1i(uniforms.EnableTexture, 0);
    }
  }
  
  // VertexColor
  if (_vertexColor != currentState._vertexColor) {
    if (_vertexColor) {
      nativeGL->uniform1i(uniforms.EnableColorPerVertex, 1);
      nativeGL->enableVertexAttribArray(attributes.Color);
      
      int WITHOUT_TIMESTAMP;
      if ((_colors != currentState._colors) /* ||
                                             (_colorsTimestamp != colors->timestamp()) */) {
                                               nativeGL->vertexAttribPointer(attributes.Color, 4, false, 0, _colors);
                                               /*_colorsTimestamp = _colors->timestamp();*/
                                             }
    }
    else {
      nativeGL->disableVertexAttribArray(attributes.Color);
      nativeGL->uniform1i(uniforms.EnableColorPerVertex, 0);
    }
  }
  
  // Vertices Position
  if (_verticesPosition != currentState._verticesPosition) {
    if (_verticesPosition) {
      nativeGL->enableVertexAttribArray(attributes.Position);
    }
    else {
      nativeGL->disableVertexAttribArray(attributes.Position);
    }
  }
  
  // Flat Color
  if (_flatColor != currentState._flatColor) {
    if (_flatColor) {
      nativeGL->uniform1i(uniforms.EnableFlatColor, 1);
    }
    else {
      nativeGL->uniform1i(uniforms.EnableFlatColor, 0);
    }
  }
  
  if (_flatColor) {
    if ((_flatColorR != currentState._flatColorR) ||
        (_flatColorG != currentState._flatColorG) ||
        (_flatColorB != currentState._flatColorB) ||
        (_flatColorA != currentState._flatColorA)
        ) {
      nativeGL->uniform4f(uniforms.FlatColor, _flatColorR, _flatColorG, _flatColorB,_flatColorA);
    }
    
    if (_intensity != currentState._intensity) {
      nativeGL->uniform1f(uniforms.FlatColorIntensity, _intensity);
    }
  }
  
  // Cull Face
  if (_cullFace != currentState._cullFace) {
    if (_cullFace) {
      nativeGL->enable(GLFeature::cullFace());
      if (_culledFace != currentState._culledFace) {
        nativeGL->cullFace(_culledFace);
      }
    }
    else {
      nativeGL->disable(GLFeature::cullFace());
    }
  }
  
  if (_lineWidth != currentState._lineWidth) {
    nativeGL->lineWidth(_lineWidth);
  }
  
  if (_pointSize != currentState._pointSize) {
    nativeGL->uniform1f(uniforms.PointSize, _pointSize);
  }
  
  
  //Polygon Offset
  if (_polygonOffsetFill != currentState._polygonOffsetFill){
    if (_polygonOffsetFill){
      nativeGL->enable(GLFeature::polygonOffsetFill());
      
      if (_polygonOffsetFactor != currentState._polygonOffsetFactor ||
          _polygonOffsetUnits != currentState._polygonOffsetUnits){
        nativeGL->polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);
      }
      
    } else{
      nativeGL->disable(GLFeature::polygonOffsetFill());
    }
  }
  
  //Blending Factors
  if (_blendDFactor != currentState._blendDFactor || _blendSFactor != currentState._blendSFactor){
    nativeGL->blendFunc(_blendSFactor, _blendDFactor);
  }

}