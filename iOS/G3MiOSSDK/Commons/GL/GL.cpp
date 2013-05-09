//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
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
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "IGLTextureId.hpp"

#include "GPUProgram.hpp"
#include "GPUUniform.hpp"
#include "GPUProgramState.hpp"
#include "GPUProgramManager.hpp"

void GL::clearScreen(const GLState& state) {
  if (_verbose) {
    ILogger::instance()->logInfo("GL::clearScreen()");
  }
  setGLState(state);
  _nativeGL->clear(GLBufferType::colorBuffer() | GLBufferType::depthBuffer());
}

void GL::drawElements(int mode,
                      IShortBuffer* indices, const GLState& state,
                      GPUProgramManager& progManager,
                      const GPUProgramState* gpuState) {
  if (_verbose) {
    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
                                 mode,
                                 indices->description().c_str());
  }
  
  applyGLStateAndGPUProgramState(state, progManager, *gpuState);
  
  _nativeGL->drawElements(mode,
                          indices->size(),
                          indices);
  
  //TODO: CHECKING GPU STATUS BY DELETING ALL
  //progManager.getProgram(*gpuState)->onUnused();
}

void GL::drawArrays(int mode,
                    int first,
                    int count, const GLState& state,
                    GPUProgramManager& progManager,
                    const GPUProgramState* gpuState) {
  if (_verbose) {
    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
                                 mode,
                                 first,
                                 count);
  }
  
  applyGLStateAndGPUProgramState(state, progManager, *gpuState);

  
  _nativeGL->drawArrays(mode,
                        first,
                        count);
  
  //TODO: CHECKING GPU STATUS BY DELETING ALL
  //progManager.getProgram(*gpuState)->onUnused();
}

int GL::getError() {
  if (_verbose) {
    ILogger::instance()->logInfo("GL::getError()");
  }
  
  return _nativeGL->getError();
}

const IGLTextureId* GL::uploadTexture(const IImage* image,
                                      int format,
                                      bool generateMipmap){
  if (_verbose) {
    ILogger::instance()->logInfo("GL::uploadTexture()");
  }
  
  const IGLTextureId* texId = getGLTextureId();
  if (texId != NULL) {
    int texture2D = GLTextureType::texture2D();
    
    GLState state(*_currentState);
    state.setPixelStoreIAlignmentUnpack(1);
    state.bindTexture(texId);
    setGLState(state);
    
    int linear = GLTextureParameterValue::linear();
    int clampToEdge = GLTextureParameterValue::clampToEdge();
    _nativeGL->texParameteri(texture2D, GLTextureParameter::minFilter(), linear);
    _nativeGL->texParameteri(texture2D, GLTextureParameter::magFilter(),linear);
    _nativeGL->texParameteri(texture2D, GLTextureParameter::wrapS(),clampToEdge);
    _nativeGL->texParameteri(texture2D, GLTextureParameter::wrapT(),clampToEdge);
    _nativeGL->texImage2D(image, format);
    
    if (generateMipmap) {
      _nativeGL->generateMipmap(texture2D);
    }
    
  }
  else {
    ILogger::instance()->logError("can't get a valid texture id\n");
    return NULL;
  }
  
  return texId;
}

const IGLTextureId* GL::getGLTextureId() {
  if (_verbose) {
    ILogger::instance()->logInfo("GL::getGLTextureId()");
  }
  
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
  if (_verbose) {
    ILogger::instance()->logInfo("GL::deleteTexture()");
  }
  
  if (textureId != NULL) {
    if ( _nativeGL->deleteTexture(textureId) ) {
      _texturesIdBag.push_back(textureId);
    }
    else {
      delete textureId;
    }
    
    if (_currentState->getBoundTexture() == textureId){
      _currentState->bindTexture(NULL);
    }
    
    //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
  }
}

void GL::setGLState(const GLState& state) {
  state.applyChanges(this, *_currentState);
}

void GL::setProgramState(GPUProgramManager& progManager, const GPUProgramState& progState) {
  GPUProgram* prog = progManager.getProgram(progState);
  if (prog != _currentGPUProgram){
    _currentGPUProgram = prog;
    useProgram(prog);
  }
  
  progState.applyChanges(this, *prog);
}

void GL::useProgram(GPUProgram* program) {
  _nativeGL->useProgram(program);
  program->onUsed();
}

void GL::applyGLStateAndGPUProgramState(const GLState& state, GPUProgramManager& progManager, const GPUProgramState& progState){
  state.applyChanges(this, *_currentState);
  setProgramState(progManager, progState);
}