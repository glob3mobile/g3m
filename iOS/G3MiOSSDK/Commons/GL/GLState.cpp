//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "GLState.hpp"
#include "GPUVariableValueSet.hpp"

//GLGlobalState GLState::_currentGPUGlobalState;
//GPUProgram* GLState::_currentGPUProgram = NULL;

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

  if (_modelviewUniformValue != NULL){
    _modelviewUniformValue->_release();
  }

  if (_owner){
    delete _programState;
    delete _globalState;
  }

  for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
    GLFeatureSet* fs = _featuresSets[i];
    if (fs != NULL){
      delete _featuresSets[i];
    }
  }
}

void GLState::setParent(const GLState* parent) const{
  _parentGLState = parent;
  if (parent != NULL){
    //UNIFORMS AND ATTRIBUTES CODES
    const int newUniformsCode   = parent->getUniformsCode()   | _programState->getUniformsCode();
    const int newAttributesCode = parent->getAttributesCode() | _programState->getAttributesCode();

    _totalGPUProgramStateChanged = ((newAttributesCode != _attributesCode) ||
                                    (newUniformsCode   != _uniformsCode));
    _uniformsCode   = newUniformsCode;
    _attributesCode = newAttributesCode;

    //MODELVIEW
    if (_multiplyModelview){
      if (_modelview != NULL){
        const Matrix44D* parentsM = parent->getAccumulatedModelView();
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

            //            if (_modelviewUniformValue != NULL){
            //              _modelviewUniformValue->_release();
            //              _modelviewUniformValue = NULL;
            //            }

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

  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
}

void GLState::applyStates(GL* gl, GPUProgram* prog) const{
  if (_parentGLState != NULL){
    _parentGLState->applyStates(gl, prog);
  }

  //  _programState->linkToProgram(prog);
  //  _programState->applyValuesToLinkedProgram();
  _programState->applyValuesToProgram(prog);

  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{


  int uniformsCode = getUniformsCode();
  int attributesCode = getAttributesCode();

  /////////////////////////// FEATURES
  GPUVariableValueSet values;
  for (int i = 0; i < N_GLFEATURES_GROUPS; i++){

    GLFeatureSet* fs = createAccumulatedGLFeaturesForGroup(GLFeatureGroup::getGroupName(i));
    if (fs != NULL){

      GLFeatureGroup* group = GLFeatureGroup::getGroup(i);
      GPUVariableValueSet* variables = group->applyAndCreateGPUVariableSet(gl, fs);
      delete fs;
      if (variables != NULL){
        values.combineWith(variables);
        delete variables;
      }
    }
  }

//  const GLFeatureGroup* _groups[] = {&_featureNoGroup, &_featureCameraGroup, &_featureColorGroup};
//  for (int i = 0; i < 3; i++){
//
//    GLFeatureSet* fs = createAccumulatedGLFeaturesForGroup(GLFeatureGroup::getGroupName(i));
//    if (fs != NULL){
//
//      GLFeatureGroup* group = GLFeatureGroup::getGroup(i);
//      GPUVariableValueSet* variables = group->applyAndCreateGPUVariableSet(gl, fs);
//      delete fs;
//      if (variables != NULL){
//        values.combineWith(variables);
//        delete variables;
//      }
//    }
//  }

  uniformsCode = uniformsCode | values.getUniformsCode();
  attributesCode = attributesCode | values.getAttributesCode();

  /////////////////////////// FEATURES



  //if (_lastGPUProgramUsed == NULL || _totalGPUProgramStateChanged){
    //ILogger::instance()->logInfo("Total State for GPUProgram has changed since last apply");
    _lastGPUProgramUsed = progManager.getProgram(gl, uniformsCode, attributesCode);
  //}

  if (_lastGPUProgramUsed != NULL){
    gl->useProgram(_lastGPUProgramUsed);
    applyStates(gl, _lastGPUProgramUsed);

    //APPLY TO GPU STATE MODELVIEW
    //    const Matrix44D* modelview = getAccumulatedModelView();
    GPUUniformValueMatrix4Float* modelviewValue = getModelviewUniformValue();
    if (modelviewValue != NULL){
      //      GPUUniformValueMatrix4Float valueModelview(*modelview);
#ifdef C_CODE
      _lastGPUProgramUsed->getGPUUniform(MODELVIEW)->set(modelviewValue);
#endif
#ifdef JAVA_CODE
      _lastGPUProgramUsed.getGPUUniform(GPUUniformKey.MODELVIEW.getValue()).set(modelviewValue);
#endif
    }


    /////////////////////////// FEATURES
    values.applyValuesToProgram(_lastGPUProgramUsed);
    /////////////////////////// FEATURES


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

    if (_modelviewUniformValue != NULL){
      _modelviewUniformValue->_release();
      _modelviewUniformValue = NULL;
    }
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
  }
  if (_parentGLState != NULL){
    return _parentGLState->getAccumulatedModelView();
  }
  return NULL;

};

GPUUniformValueMatrix4Float* GLState::getModelviewUniformValue() const{

  const Matrix44D* mv = getAccumulatedModelView();

  if (_modelviewUniformValue == NULL){
    if (mv == NULL){
      return NULL;
    }
    _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
  } else{
    if (mv != _modelviewUniformValue->getMatrix()){
      _modelviewUniformValue->_release();
      _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
    }
  }
  return _modelviewUniformValue;
  
}

GLFeatureSet* GLState::createAccumulatedGLFeaturesForGroup(GLFeatureGroupName g) const{

  GLFeatureSet* pfs = NULL;
  if (_parentGLState != NULL){
    pfs = _parentGLState->createAccumulatedGLFeaturesForGroup(g);
  }
  
  const int index = g;
  const GLFeatureSet* const thisFS = _featuresSets[index];
  if (thisFS == NULL){
    return pfs;
  } else{
    GLFeatureSet* fs = new GLFeatureSet();
    if (pfs != NULL){
      fs->add(pfs);
      delete pfs;
    }
    fs->add(thisFS);
    return fs;
  }
  
}

void GLState::clearGLFeatureGroup(GLFeatureGroupName g){
  GLFeatureSet* fs = _featuresSets[g];
  if (fs != NULL){
    delete fs;
    _featuresSets[g] = NULL;
  }
}

