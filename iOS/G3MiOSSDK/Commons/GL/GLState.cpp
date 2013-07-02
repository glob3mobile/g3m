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

void GLState::applyStates(GL* gl, GPUProgram* prog) const{
  if (_parentGLState != NULL){
    _parentGLState->applyStates(gl, prog);
  }

//  _programState->linkToProgram(prog);
//  _programState->applyValuesToLinkedProgram();
  _programState->applyValuesToProgram(prog);

  _globalState->applyChanges(gl, _currentGPUGlobalState);
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{

  if (_lastGPUProgramUsed == NULL || _totalGPUProgramStateChanged){
    //ILogger::instance()->logInfo("Total State for GPUProgram has changed since last apply");
    _lastGPUProgramUsed = progManager.getProgram(gl, getUniformsCode(), getAttributesCode());
  }

  if (_lastGPUProgramUsed != NULL){
    if (_lastGPUProgramUsed != _currentGPUProgram){
      if (_currentGPUProgram != NULL){
        _currentGPUProgram->onUnused(gl);
      }
      _currentGPUProgram = _lastGPUProgramUsed;
      gl->useProgram(_lastGPUProgramUsed);
    }

    applyStates(gl, _lastGPUProgramUsed);

    //APPLY TO GPU STATE MODELVIEW
    Matrix44D* modelview = getAccumulatedModelView();
    if (modelview != NULL){
      GPUUniformValueMatrix4Float valueModelview(*getAccumulatedModelView());
      _lastGPUProgramUsed->getGPUUniform(MODELVIEW)->set(&valueModelview);
    }

    _lastGPUProgramUsed->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }

}

void GLState::setModelView(const Matrix44D& modelview, bool modifiesParents){
  _modelview = new Matrix44D(modelview);
  _modelviewModifiesParents = modifiesParents;
}

Matrix44D* GLState::getAccumulatedModelView() const{
  if (_accumulatedModelview != NULL){
    return _accumulatedModelview;
  } else{
    if (_parentGLState != NULL){
      return _parentGLState->getAccumulatedModelView();
    } else{
      return NULL;
    }
  }
};