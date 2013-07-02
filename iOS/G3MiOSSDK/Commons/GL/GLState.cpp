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

void GLState::setParent(const GLState* p) const{
  _parentGLState = p;
  if (p != NULL){
    //UNIFORMS AND ATTRIBUTES CODES
    int newUniformsCode = p->getUniformsCode() | _programState->getUniformsCode();
    int newAttributesCode = p->getAttributesCode() | _programState->getAttributesCode();

    _totalGPUProgramStateChanged = (newAttributesCode != _attributesCode) || (newUniformsCode != _uniformsCode);
    _uniformsCode = newUniformsCode;
    _attributesCode = newAttributesCode;

    //MODELVIEW
    if (_modelviewModifiesParents){
      if (_modelview != NULL){
        Matrix44D* parentsM = p->getAccumulatedModelView();
        if (parentsM == NULL){
          ILogger::instance()->logError("CAN'T MODIFY PARENTS MODELVIEW");
        } else{

          if (_lastParentsModelview != parentsM){
            delete _accumulatedModelview;
            _accumulatedModelview = parentsM->multiply(*_modelview);

            if (_lastParentsModelview != NULL){
              _lastParentsModelview->removeListener(&_parentMatrixListener);
            }

            _lastParentsModelview = parentsM;

            _lastParentsModelview->addListener(&_parentMatrixListener);
          }
//          else{
//            ILogger::instance()->logInfo("REUSING MODELVIEW");
//          }

        }
      }
    }
  }
}

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

  _modelviewModifiesParents = modifiesParents;

  if (_modelview == NULL || !_modelview->isEqualsTo(modelview)){
    delete _modelview;
    _modelview = new Matrix44D(modelview);

    if (_lastParentsModelview != NULL){
      _lastParentsModelview->removeListener(&_parentMatrixListener);
    }

    _lastParentsModelview = NULL;
  }
  //  else{
  //    ILogger::instance()->logInfo("Same modelview set.");
  //  }
}

Matrix44D* GLState::getAccumulatedModelView() const{

  if (!_modelviewModifiesParents && _modelview != NULL){
    return _modelview;
  }

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