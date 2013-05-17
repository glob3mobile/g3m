//
//  GLGlobalState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

#include "GLGlobalState.hpp"

#include "IFloatBuffer.hpp"
#include "IGLTextureId.hpp"

#include "GPUProgram.hpp"


void GLGlobalState::applyChanges(GL* gl, GLGlobalState& currentState) const{

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
  
  
  
}