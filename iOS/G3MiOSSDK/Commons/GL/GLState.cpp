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

void GLState::applyGlobalStateOnGPU(GL* gl) const{
  
  if (_parentGLState != NULL){
    _parentGLState->applyGlobalStateOnGPU(gl);
  }
  
  _globalState->applyChanges(gl, _currentGPUGlobalState);
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) {
  applyGlobalStateOnGPU(gl);
  setProgramState(gl, progManager);
}

void GLState::linkAndApplyToGPUProgram(GPUProgram* prog) const{
  if (_parentGLState != NULL){
    _parentGLState->linkAndApplyToGPUProgram(prog);
  }
  
  _programState->linkToProgram(*prog);
  _programState->applyValuesToLinkedProgram();
}

void GLState::setProgramState(GL* gl, GPUProgramManager& progManager) {
  
  GPUProgram* prog = _programState->getLinkedProgram();
  if (prog != NULL){
    const GLState* parent = _parentGLState;
    while (parent != NULL && prog != NULL) {
      if (prog != parent->getGPUProgramState()->getLinkedProgram()){
        prog = NULL;
      }
      parent = parent->getParent();
    }
  }
  
  if (prog == NULL){
    prog = progManager.getProgram(gl, this);
  }
  
  if (prog != NULL){
    if (prog != _currentGPUProgram){
      if (_currentGPUProgram != NULL){
        _currentGPUProgram->onUnused(gl);
      }
      _currentGPUProgram = prog;
      gl->useProgram(prog);
    }
    
    linkAndApplyToGPUProgram(prog);
    prog->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }

}