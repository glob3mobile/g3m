//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include <list>

#include "GL.hpp"

#include "IImage.hpp"
#include "Vector3D.hpp"
#include "Vector2D.hpp"
#include "INativeGL.hpp"
#include "IShortBuffer.hpp"
#include "IFactory.hpp"
#include "IGLTextureId.hpp"

#include "GPUProgram.hpp"
#include "GPUUniform.hpp"
#include "GPUProgramManager.hpp"

#include "GLState.hpp"

void GL::clearScreen(const Color& color) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::clearScreen()");
  //  }
  _clearScreenState->setClearColor(color);
  _clearScreenState->applyChanges(this, *_currentGLGlobalState);

  _nativeGL->clear(GLBufferType::colorBuffer() | GLBufferType::depthBuffer());
}

void GL::drawElements(int mode, IShortBuffer* indices, const GLState* state,
                      GPUProgramManager& progManager) {

  state->applyOnGPU(this, progManager);

  _nativeGL->drawElements(mode,
                          indices->size(),
                          indices);
}

void GL::drawArrays(int mode,
                    int first,
                    int count, const GLState* state,
                    GPUProgramManager& progManager) {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
  //                                 mode,
  //                                 first,
  //                                 count);
  //  }

  state->applyOnGPU(this, progManager);

  _nativeGL->drawArrays(mode,
                        first,
                        count);
}

int GL::getError() {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::getError()");
  //  }

  return _nativeGL->getError();
}

bool GL::isPowerOfTwo(int x) {
  return (
          (x >= 0) &&
          (
           (x ==          1) ||
           (x ==          2) ||
           (x ==          4) ||
           (x ==          8) ||
           (x ==         16) ||
           (x ==         32) ||
           (x ==         64) ||
           (x ==        128) ||
           (x ==        256) ||
           (x ==        512) ||
           (x ==       1024) ||
           (x ==       2048) ||
           (x ==       4096) ||
           (x ==       8192) ||
           (x ==      16384) ||
           (x ==      32768) ||
           (x ==      65536) ||
           (x ==     131072) ||
           (x ==     262144) ||
           (x ==     524288) ||
           (x ==    1048576) ||
           (x ==    2097152) ||
           (x ==    4194304) ||
           (x ==    8388608) ||
           (x ==   16777216) ||
           (x ==   33554432) ||
           (x ==   67108864) ||
           (x ==  134217728) ||
           (x ==  268435456) ||
           (x ==  536870912) ||
           (x == 1073741824)
           //(x == 2147483648)
           )
          );
}

const IGLTextureId* GL::uploadTexture(const IImage* image,
                                      int format,
                                      bool generateMipmap) {

  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::uploadTexture()");
  //  }

  const IGLTextureId* texId = getGLTextureId();
  if (texId != NULL) {
    GLGlobalState newState;

    newState.setPixelStoreIAlignmentUnpack(1);
    newState.bindTexture(texId);

    newState.applyChanges(this, *_currentGLGlobalState);

    const int texture2D = GLTextureType::texture2D();
    const int linear    = GLTextureParameterValue::linear();

    if (generateMipmap) {
      _nativeGL->texParameteri(texture2D,
                               GLTextureParameter::minFilter(),
                               GLTextureParameterValue::linearMipmapNearest());
    }
    else {
      _nativeGL->texParameteri(texture2D,
                               GLTextureParameter::minFilter(),
                               linear);
    }

    _nativeGL->texParameteri(texture2D,
                             GLTextureParameter::magFilter(),
                             linear);

    const int clampToEdge = GLTextureParameterValue::clampToEdge();
    _nativeGL->texParameteri(texture2D,
                             GLTextureParameter::wrapS(),
                             clampToEdge);
    _nativeGL->texParameteri(texture2D,
                             GLTextureParameter::wrapT(),
                             clampToEdge);

    _nativeGL->texImage2D(image, format);

    if (generateMipmap) {
      if (
          isPowerOfTwo(image->getWidth()) &&
          isPowerOfTwo(image->getHeight())
          ) {
        _nativeGL->generateMipmap(texture2D);
      }
      else {
        ILogger::instance()->logError("Can't generate bitmap. Texture dimensions are not power of two.");
      }
    }
  }
  else {
    ILogger::instance()->logError("can't get a valid texture id\n");
    return NULL;
  }

  return texId;
}

const IGLTextureId* GL::getGLTextureId() {
  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::getGLTextureId()");
  //  }

  if (_texturesIdBag.size() == 0) {
    //const int bugdetSize = 256;
    const int bugdetSize = 1024;
    //const int bugdetSize = 10240;

    const std::vector<IGLTextureId*> ids = _nativeGL->genTextures(bugdetSize);
    const int idsCount = ids.size();
    for (int i = 0; i < idsCount; i++) {
      // ILogger::instance()->logInfo("  = Created textureId=%s", ids[i]->description().c_str());
      _texturesIdBag.push_front(ids[i]);
    }

    _texturesIdAllocationCounter += idsCount;

    ILogger::instance()->logInfo("= Created %d texturesIds (accumulated %d).",
                                 idsCount,
                                 _texturesIdAllocationCounter);
  }

  //  _texturesIdGetCounter++;

  if (_texturesIdBag.size() == 0) {
    ILogger::instance()->logError("TextureIds bag exhausted");
    return NULL;
  }

  const IGLTextureId* result = _texturesIdBag.back();
  _texturesIdBag.pop_back();

  //  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
  //         result.getGLTextureId(),
  //         _texturesIdBag.size(),
  //         _texturesIdGetCounter,
  //         _texturesIdTakeCounter,
  //         _texturesIdGetCounter - _texturesIdTakeCounter);

  return result;
}

void GL::deleteTexture(const IGLTextureId* textureId) {

  //  if (_verbose) {
  //    ILogger::instance()->logInfo("GL::deleteTexture()");
  //  }

  if (textureId != NULL) {
    if ( _nativeGL->deleteTexture(textureId) ) {
      _texturesIdBag.push_back(textureId);
    }
    else {
      delete textureId;
    }

    if (_currentGLGlobalState->getBoundTexture() == textureId) {
      _currentGLGlobalState->bindTexture(NULL);
    }

    //    GLState::textureHasBeenDeleted(textureId);

    //    if (GLState::getCurrentGLGlobalState()->getBoundTexture() == textureId) {
    //      GLState::getCurrentGLGlobalState()->bindTexture(NULL);
    //    }

    //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
  }
}

void GL::useProgram(GPUProgram* program) {
  if (program != NULL) {
    if (_currentGPUProgram != program) {

      if (_currentGPUProgram != NULL) {
        _currentGPUProgram->onUnused(this);
        _currentGPUProgram->removeReference();
      }

      _nativeGL->useProgram(program);
      program->onUsed();
      _currentGPUProgram = program;
      _currentGPUProgram->addReference();
    }

//    if (!_nativeGL->isProgram(program->getProgramID())) {
//      ILogger::instance()->logError("INVALID PROGRAM.");
//    }
  }

}

//void GL::applyGLGlobalStateAndGPUProgramState(const GLGlobalState& state, GPUProgramManager& progManager, const GPUProgramState& progState) {
//  state.applyChanges(this, *_currentState);
//  setProgramState(progManager, progState);
//}
