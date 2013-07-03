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

GLState::~GLState(){
  if (_lastParentModelview != NULL){
    //      _lastParentModelview->removeListener(&_parentMatrixListener);
    _lastParentModelview->_release();
  }
  if (_modelview != NULL){
    _modelview->_release();
  }
  if (_accumulatedModelview != NULL){
    _accumulatedModelview->_release();
  }
  if (_owner){
    delete _programState;
    delete _globalState;
  }
}

void GLState::setParent(const GLState* p) const{
  _parentGLState = p;
  if (p != NULL){
    //UNIFORMS AND ATTRIBUTES CODES
    const int newUniformsCode = p->getUniformsCode() | _programState->getUniformsCode();
    const int newAttributesCode = p->getAttributesCode() | _programState->getAttributesCode();

    _totalGPUProgramStateChanged = (newAttributesCode != _attributesCode) || (newUniformsCode != _uniformsCode);
    _uniformsCode = newUniformsCode;
    _attributesCode = newAttributesCode;

    //MODELVIEW
    if (_multiplyModelview){
      if (_modelview != NULL){
        const Matrix44D* parentsM = p->getAccumulatedModelView();
        if (parentsM == NULL){
          ILogger::instance()->logError("CAN'T MODIFY PARENTS MODELVIEW");
        } else{

          if (_lastParentModelview != parentsM){

            if (_accumulatedModelview != NULL){
              _accumulatedModelview->_release();
            }
            _accumulatedModelview = parentsM->createMultiplication(*_modelview);

            if (_lastParentModelview != NULL){
//              _lastParentModelview->removeListener(&_parentMatrixListener);
              _lastParentModelview->_release();
            }

            _lastParentModelview = parentsM;
            _lastParentModelview->_retain();

//            _lastParentModelview->addListener(&_parentMatrixListener);
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
    const Matrix44D* modelview = getAccumulatedModelView();
    if (modelview != NULL){
      GPUUniformValueMatrix4Float valueModelview(*modelview);
      _lastGPUProgramUsed->getGPUUniform(MODELVIEW)->set(&valueModelview);
    }

    _lastGPUProgramUsed->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }

}

void GLState::setModelView(const Matrix44D* modelview, bool multiply){

  _multiplyModelview = multiply;

  if (_modelview == NULL || _modelview != modelview){
//    delete _modelview;
//    _modelview = new Matrix44D(modelview);

    if (_modelview != NULL){
      _modelview->_release();
    }

    _modelview = modelview;
    _modelview->_retain();


    //Forcing matrix multiplication next time even when parent's modelview is the same
    if (_lastParentModelview != NULL){
//      _lastParentModelview->removeListener(&_parentMatrixListener);
      _lastParentModelview->_release();
    }

    _lastParentModelview = NULL;
  }
  //  else{
  //    ILogger::instance()->logInfo("Same modelview set.");
  //  }
}

const Matrix44D* GLState::getAccumulatedModelView() const{

  if (!_multiplyModelview && _modelview != NULL){
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