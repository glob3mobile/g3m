//
//  GLGlobalState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

#include "GLGlobalState.hpp"

#include "IFloatBuffer.hpp"
#include "IGLTextureID.hpp"

#include "GPUProgram.hpp"
#include "INativeGL.hpp"
#include "GL.hpp"
#include "ILogger.hpp"


bool GLGlobalState::_initializationAvailable = false;

GLGlobalState::GLGlobalState() :
_depthTest(false),
_blend(false),
_cullFace(false),
_culledFace(GLCullFace::back()),
_lineWidth(1),
_polygonOffsetFactor(0),
_polygonOffsetUnits(0),
_polygonOffsetFill(false),
_blendDFactor(GLBlendFactor::zero()),
_blendSFactor(GLBlendFactor::one()),
_pixelStoreIAlignmentUnpack(-1),
_clearColorR(0.0),
_clearColorG(0.0),
_clearColorB(0.0),
_clearColorA(0.0)
{
  if (!_initializationAvailable) {
    ILogger::instance()->logError("GLGlobalState creation before it is available.");
  }

  for (int i = 0; i < MAX_N_TEXTURES; i++) {
    _boundTextureID[i] = NULL;
  }
}


void GLGlobalState::bindTexture(const int target,
                                const IGLTextureID* textureID) {
  if (target > MAX_N_TEXTURES) {
    ILogger::instance()->logError("WRONG TARGET FOR TEXTURE");
    return;
  }

  _boundTextureID[target] = textureID;
}


void GLGlobalState::applyChanges(GL* gl, GLGlobalState& currentState) const {

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
 // if (_polygonOffsetFill != currentState._polygonOffsetFill) {
//    currentState._polygonOffsetFill = _polygonOffsetFill;
    if (_polygonOffsetFill) {
      nativeGL->enable(GLStage::polygonOffsetFill());

     /* if (_polygonOffsetFactor != currentState._polygonOffsetFactor ||
          _polygonOffsetUnits != currentState._polygonOffsetUnits) {*/
        nativeGL->polygonOffset(_polygonOffsetFactor, _polygonOffsetUnits);

        currentState._polygonOffsetUnits = _polygonOffsetUnits;
        currentState._polygonOffsetFactor = _polygonOffsetFactor;
   //   }
    }
    else {
      nativeGL->disable(GLStage::polygonOffsetFill());
    }
//  }

  //Blending Factors
  if (_blendDFactor != currentState._blendDFactor || _blendSFactor != currentState._blendSFactor) {
    nativeGL->blendFunc(_blendSFactor, _blendDFactor);
    currentState._blendDFactor = _blendDFactor;
    currentState._blendSFactor = _blendSFactor;
  }

  //Texture (After blending factors)

  for (int i = 0; i < MAX_N_TEXTURES; i++) {

    if (_boundTextureID[i] != NULL) {
      if (currentState._boundTextureID[i] == NULL ||
          !_boundTextureID[i]->isEquals(currentState._boundTextureID[i])) {
        nativeGL->setActiveTexture(i);
        nativeGL->bindTexture(GLTextureType::texture2D(), _boundTextureID[i]);

        currentState._boundTextureID[i] = _boundTextureID[i];
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

