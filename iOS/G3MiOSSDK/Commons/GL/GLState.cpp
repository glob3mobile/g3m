//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "GLState.hpp"

GLGlobalState GLState::_currentGPUGlobalState;
GPUProgram* GLState::_currentGPUProgram = NULL;

void GLState::applyGlobalStateOnGPU(GL* gl){
  
  if (_parentGLState != NULL){
    _parentGLState->applyGlobalStateOnGPU(gl);
  }
  
  _globalState->applyChanges(gl, _currentGPUGlobalState);
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) {
  applyGlobalStateOnGPU(gl);
  setProgramState(gl, progManager);
}

void GLState::linkAndApplyToGPUProgram(GPUProgram* prog){
  
  if (_parentGLState != NULL){
    _parentGLState->linkAndApplyToGPUProgram(prog);
  }
  
  _programState->linkToProgram(*prog);
  _programState->applyValuesToLinkedProgram();
}

void GLState::setProgramState(GL* gl, GPUProgramManager& progManager) {
  
  GPUProgram* prog = progManager.getProgram(this);
  if (prog != NULL){
    if (prog != _currentGPUProgram){
      if (_currentGPUProgram != NULL){
        _currentGPUProgram->onUnused();
      }
      _currentGPUProgram = prog;
      gl->useProgram(prog);
    }
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }
  
  linkAndApplyToGPUProgram(prog);
  prog->applyChanges(gl);
}