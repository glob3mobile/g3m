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
#include "IGLTextureId.hpp"

#include "GPUProgram.hpp"



void GLState::applyChanges(GL* gl, GLState& currentState, const AttributesStruct& attributes,const UniformsStruct& uniforms) const{
  
  //Program
//  if (_program != NULL){
//    if( currentState._program != _program){
//      gl->useProgram(_program);
//      currentState._program = _program;
//    }
//    _program->applyChanges(gl);
//  }
  
  INativeGL* nativeGL = gl->getNative();
  
  // Depth Test
  if (_depthTest != currentState._depthTest) {
    if (_depthTest) {
      nativeGL->enable(GLFeature::depthTest());
    }
    else {
      nativeGL->disable(GLFeature::depthTest());
    }
    currentState._depthTest = _depthTest;
  }
  
  // Blending
  if (_blend != currentState._blend) {
    if (_blend) {
      nativeGL->enable(GLFeature::blend());
    }
    else {
      nativeGL->disable(GLFeature::blend());
    }
    currentState._blend = _blend;
  }
  
//  // Textures
//  if (_textures != currentState._textures) {
//    if (_textures) {
//      nativeGL->enableVertexAttribArray(attributes.TextureCoord);
//    }
//    else {
//      nativeGL->disableVertexAttribArray(attributes.TextureCoord);
//    }
//    currentState._textures = _textures;
//  }
  
//  // Texture2D
//  if (_texture2D != currentState._texture2D) {
//    if (_texture2D) {
//      nativeGL->uniform1i(uniforms.EnableTexture, 1);
//    }
//    else {
//      nativeGL->uniform1i(uniforms.EnableTexture, 0);
//    }
//    currentState._texture2D = _texture2D;
//  }
  
//  // VertexColor
//  if (_vertexColor != currentState._vertexColor) {
//    if (_vertexColor) {
//      nativeGL->uniform1i(uniforms.EnableColorPerVertex, 1);
//      nativeGL->enableVertexAttribArray(attributes.Color);
//    }else {
//      nativeGL->disableVertexAttribArray(attributes.Color);
//      nativeGL->uniform1i(uniforms.EnableColorPerVertex, 0);
//    }
//    currentState._vertexColor = _vertexColor;
//  }
//  if (_vertexColor) {
//    if ((_colors != currentState._colors) || (_colorsTimeStamp != currentState._colorsTimeStamp)) {
//      nativeGL->vertexAttribPointer(attributes.Color, 4, false, 0, _colors);
//      currentState._colors = _colors;
//      currentState._colorsTimeStamp = _colorsTimeStamp;
//    }
//  }
  
  
  // Vertex
//  if (_vertices != NULL){
////    if ((_vertices != currentState._vertices) || (_verticesTimestamp != currentState._verticesTimestamp) ||
////        (_verticesSize != currentState._verticesSize) || (_verticesStride != currentState._verticesStride)  ) {
//      nativeGL->vertexAttribPointer(attributes.Position, _verticesSize, false, _verticesStride, _vertices);
////      currentState._vertices = _vertices;
////      currentState._verticesTimestamp = _verticesTimestamp;
////      currentState._verticesSize = _verticesSize;
////      currentState._verticesStride = _verticesStride;
////    }
//  }
  
  // Vertices Position
//  if (_verticesPosition != currentState._verticesPosition) {
//    if (_verticesPosition) {
//      nativeGL->enableVertexAttribArray(attributes.Position);
//    }
//    else {
//      nativeGL->disableVertexAttribArray(attributes.Position);
//    }
//    currentState._verticesPosition = _verticesPosition;
//  }
  
  //Texture Coordinates
//  if (_textureCoordinates != NULL){
//    if (_textureCoordinates != currentState._textureCoordinates ||
//        _textureCoordinatesTimestamp != currentState._textureCoordinatesTimestamp ||
//        _textureCoordinatesSize != currentState._textureCoordinatesSize ||
//        _textureCoordinatesStride != currentState._textureCoordinatesStride){
//      nativeGL->vertexAttribPointer(attributes.TextureCoord,
//                                    _textureCoordinatesSize, false,
//                                    _textureCoordinatesStride, _textureCoordinates);
//      
//      currentState._textureCoordinates = _textureCoordinates;
//      currentState._textureCoordinatesTimestamp = _textureCoordinatesTimestamp;
//      currentState._textureCoordinatesSize = _textureCoordinatesSize;
//      currentState._textureCoordinatesStride = _textureCoordinatesStride;
//    }
//  }
  
  if (_textureCoordinatesScaleX != currentState._textureCoordinatesScaleX ||
      _textureCoordinatesScaleY != currentState._textureCoordinatesScaleY){
    nativeGL->uniform2f(uniforms.ScaleTexCoord, _textureCoordinatesScaleX, _textureCoordinatesScaleY);
    
    currentState._textureCoordinatesScaleX = _textureCoordinatesScaleX;
    currentState._textureCoordinatesScaleY = _textureCoordinatesScaleY;
  }
  
  if (_textureCoordinatesTranslationX != currentState._textureCoordinatesTranslationX ||
      _textureCoordinatesTranslationY != currentState._textureCoordinatesTranslationY){
    nativeGL->uniform2f(uniforms.TranslationTexCoord,
                        _textureCoordinatesTranslationX,
                        _textureCoordinatesTranslationY);
    
    currentState._textureCoordinatesTranslationX = _textureCoordinatesTranslationX;
    currentState._textureCoordinatesTranslationY = _textureCoordinatesTranslationY;
  }
  
  
  // Flat Color
//  if (_flatColor != currentState._flatColor) {
//    if (_flatColor) {
//      nativeGL->uniform1i(uniforms.EnableFlatColor, 1);
//    }
//    else {
//      nativeGL->uniform1i(uniforms.EnableFlatColor, 0);
//    }
//    currentState._flatColor = _flatColor;
//  }
  
//  if (_flatColor) {
//    if ((_flatColorR != currentState._flatColorR) ||
//        (_flatColorG != currentState._flatColorG) ||
//        (_flatColorB != currentState._flatColorB) ||
//        (_flatColorA != currentState._flatColorA)
//        ) {
//      nativeGL->uniform4f(uniforms.FlatColor, _flatColorR, _flatColorG, _flatColorB,_flatColorA);
//      
//      currentState._flatColorR = _flatColorR;
//      currentState._flatColorG = _flatColorG;
//      currentState._flatColorB = _flatColorB;
//      currentState._flatColorA = _flatColorA;
//    }
//    
//    if (_intensity != currentState._intensity) {
//      nativeGL->uniform1f(uniforms.FlatColorIntensity, _intensity);
//      
//      currentState._intensity = _intensity;
//    }
//  }
  
  // Cull Face
  if (_cullFace != currentState._cullFace) {
    currentState._cullFace = _cullFace;
    if (_cullFace) {
      nativeGL->enable(GLFeature::cullFace());
      if (_culledFace != currentState._culledFace) {
        nativeGL->cullFace(_culledFace);
        currentState._culledFace = _culledFace;
      }
    }
    else {
      nativeGL->disable(GLFeature::cullFace());
    }
  }
  
  if (_lineWidth != currentState._lineWidth) {
    nativeGL->lineWidth(_lineWidth);
    currentState._lineWidth = _lineWidth;
  }
  
  if (_pointSize != currentState._pointSize) {
    nativeGL->uniform1f(uniforms.PointSize, _pointSize);
    currentState._pointSize = _pointSize;
  }
  
  
  //Polygon Offset
  if (_polygonOffsetFill != currentState._polygonOffsetFill){
    currentState._polygonOffsetFill = _polygonOffsetFill;
    if (_polygonOffsetFill){
      nativeGL->enable(GLFeature::polygonOffsetFill());
      
      if (_polygonOffsetFactor != currentState._polygonOffsetFactor ||
          _polygonOffsetUnits != currentState._polygonOffsetUnits){
        nativeGL->polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);
        
        currentState._polygonOffsetUnits = _polygonOffsetUnits;
        currentState._polygonOffsetFactor = _polygonOffsetFactor;
      }
      
    } else{
      nativeGL->disable(GLFeature::polygonOffsetFill());
    }
  }
  
  //Blending Factors
  if (_blendDFactor != currentState._blendDFactor || _blendSFactor != currentState._blendSFactor){
    nativeGL->blendFunc(_blendSFactor, _blendDFactor);
    currentState._blendDFactor = _blendDFactor;
    currentState._blendSFactor = _blendSFactor;
  }
  
  //Texture (After blending factors)
  if (_boundTextureId != NULL){
    if (currentState._boundTextureId == NULL ||
        !_boundTextureId->isEqualsTo(currentState._boundTextureId)){
      nativeGL->bindTexture(GLTextureType::texture2D(), _boundTextureId);
      
      currentState._boundTextureId = _boundTextureId;
    } else{
      //ILogger::instance()->logInfo("Texture already bound.\n");
    }
  }
  
//  if (_billboarding != currentState._billboarding){
//    if (_billboarding){
//      nativeGL->uniform1i(uniforms.BillBoard, 1);
//    } else{
//      nativeGL->uniform1i(uniforms.BillBoard, 0);
//    }
//    currentState._billboarding = _billboarding;
//  }
  
//  //Viewport
//  if (_viewportHeight != currentState._viewportHeight || _viewportWidth != currentState._viewportWidth){
//    nativeGL->uniform2f(uniforms.ViewPortExtent, _viewportWidth, _viewportHeight);
//    
//    currentState._viewportWidth = _viewportWidth;
//    currentState._viewportHeight = _viewportHeight;
//  }
  
  if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack){
    nativeGL->pixelStorei(GLAlignment::unpack(), _pixelStoreIAlignmentUnpack);
    currentState._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
  }
  
  if (_clearColorR != currentState._clearColorR ||
      _clearColorG != currentState._clearColorG ||
      _clearColorB != currentState._clearColorB ||
      _clearColorA != currentState._clearColorA){
    nativeGL->clearColor(_clearColorR, _clearColorG, _clearColorB, _clearColorA);
    currentState._clearColorR = _clearColorR;
    currentState._clearColorG = _clearColorG;
    currentState._clearColorB = _clearColorB;
    currentState._clearColorA = _clearColorA;
  }
  
//  //Projection
  if (!_projectionMatrix.isEqualsTo(currentState._projectionMatrix)){
    nativeGL->uniformMatrix4fv(uniforms.Projection, false, &_projectionMatrix);
    currentState._projectionMatrix = _projectionMatrix;
  }
  
//  //Modelview
  if (!_modelViewMatrix.isEqualsTo(currentState._modelViewMatrix)){
    nativeGL->uniformMatrix4fv(uniforms.Modelview, false, &_modelViewMatrix);
    currentState._modelViewMatrix = _modelViewMatrix;
  }
  
  //Texture Extent
  if (_textureWidth != currentState._textureWidth || _textureHeight != currentState._textureHeight){
    nativeGL->uniform2f(uniforms.TextureExtent, _textureWidth, _textureHeight);
    currentState._textureHeight = _textureHeight;
    currentState._textureWidth = _textureWidth;
  }
  
  
}