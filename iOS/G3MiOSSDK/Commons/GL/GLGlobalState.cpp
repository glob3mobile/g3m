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
#include "INativeGL.hpp"
#include "GL.hpp"

bool GLGlobalState::_initializationAvailable = false;


void GLGlobalState::applyChanges(GL* gl, GLGlobalState& currentState) const{

  INativeGL* nativeGL = gl->getNative();

  // Depth Test
  if (_depthTest != currentState._depthTest) {
    if (_depthTest) {
      nativeGL->enable(GLStage::depthTest());
    }
    else {
      nativeGL->disable(GLStage::depthTest());
    }
    currentState._depthTest = _depthTest;
  }

  // Blending
  if (_blend != currentState._blend) {
    if (_blend) {
      nativeGL->enable(GLStage::blend());
    }
    else {
      nativeGL->disable(GLStage::blend());
    }
    currentState._blend = _blend;
  }

  // Cull Face
  if (_cullFace != currentState._cullFace) {
    currentState._cullFace = _cullFace;
    if (_cullFace) {
      nativeGL->enable(GLStage::cullFace());
      if (_culledFace != currentState._culledFace) {
        nativeGL->cullFace(_culledFace);
        currentState._culledFace = _culledFace;
      }
    }
    else {
      nativeGL->disable(GLStage::cullFace());
    }
  }

  if (_lineWidth != currentState._lineWidth) {
    nativeGL->lineWidth(_lineWidth);
    currentState._lineWidth = _lineWidth;
  }

  //Polygon Offset
  if (_polygonOffsetFill != currentState._polygonOffsetFill) {
    currentState._polygonOffsetFill = _polygonOffsetFill;
    if (_polygonOffsetFill) {
      nativeGL->enable(GLStage::polygonOffsetFill());

      if (_polygonOffsetFactor != currentState._polygonOffsetFactor ||
          _polygonOffsetUnits != currentState._polygonOffsetUnits) {
        nativeGL->polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);

        currentState._polygonOffsetUnits = _polygonOffsetUnits;
        currentState._polygonOffsetFactor = _polygonOffsetFactor;
      }

    } else{
      nativeGL->disable(GLStage::polygonOffsetFill());
    }
  }

  //Blending Factors
  if (_blendDFactor != currentState._blendDFactor || _blendSFactor != currentState._blendSFactor) {
    nativeGL->blendFunc(_blendSFactor, _blendDFactor);
    currentState._blendDFactor = _blendDFactor;
    currentState._blendSFactor = _blendSFactor;
  }

  //Texture (After blending factors)

  for (int i = 0; i < MAX_N_TEXTURES; i++) {

    if (_boundTextureId[i] != NULL) {
      if (currentState._boundTextureId[i] == NULL ||
          !_boundTextureId[i]->isEquals(currentState._boundTextureId[i])) {
        nativeGL->setActiveTexture(i);
        nativeGL->bindTexture(GLTextureType::texture2D(), _boundTextureId[i]);

        currentState._boundTextureId[i] = _boundTextureId[i];
      }
      //else {
      //  ILogger::instance()->logInfo("Texture already bound.\n");
      //}
    }
  }

  if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack) {
    nativeGL->pixelStorei(GLAlignment::unpack(), _pixelStoreIAlignmentUnpack);
    currentState._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
  }

  if (_pixelStoreIAlignmentUnpack != -1 && _pixelStoreIAlignmentUnpack != currentState._pixelStoreIAlignmentUnpack) {
    nativeGL->pixelStorei(GLAlignment::unpack(), _pixelStoreIAlignmentUnpack);
    currentState._pixelStoreIAlignmentUnpack = _pixelStoreIAlignmentUnpack;
  }

  if (_clearColorR != currentState._clearColorR ||
      _clearColorG != currentState._clearColorG ||
      _clearColorB != currentState._clearColorB ||
      _clearColorA != currentState._clearColorA) {
    nativeGL->clearColor(_clearColorR, _clearColorG, _clearColorB, _clearColorA);
    currentState._clearColorR = _clearColorR;
    currentState._clearColorG = _clearColorG;
    currentState._clearColorB = _clearColorB;
    currentState._clearColorA = _clearColorA;
  }

}

