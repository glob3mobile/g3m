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

void GLState::linkAndApplyToGPUProgram(GL* gl, GPUProgram* prog) const{
  if (_parentGLState != NULL){
    _parentGLState->linkAndApplyToGPUProgram(gl, prog);
  }
  
  _programState->linkToProgram(prog);
  _programState->applyValuesToLinkedProgram();
  
  _globalState->applyChanges(gl, _currentGPUGlobalState);
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{
  
  GPUProgram* prog = _programState->getLinkedProgram();
  if (prog != NULL){
#ifdef C_CODE
    const GLState* parent = _parentGLState;
#endif
#ifdef JAVA_CODE
    GLState parent = _parentGLState;
#endif
    while (parent != NULL) {
      if (prog != parent->_programState->getLinkedProgram()){
        int ____TALK_WITH_JM;
        prog = NULL;
        break;
      }
      parent = parent->_parentGLState;
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
    
    linkAndApplyToGPUProgram(gl, prog);
    prog->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }

}