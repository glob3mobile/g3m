//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "GLState.hpp"


//GLGlobalState GLState::_currentGPUGlobalState;
//GPUProgram* GLState::_currentGPUProgram = NULL;

GLState::~GLState(){
  //  if (_lastParentModelview != NULL){
  //    //      _lastParentModelview->removeListener(&_parentMatrixListener);
  //    _lastParentModelview->_release();
  //  }
  //  if (_modelview != NULL){
  //    _modelview->_release();
  //  }
  //  if (_accumulatedModelview != NULL){
  //    _accumulatedModelview->_release();
  //  }
  //
  //  if (_modelviewUniformValue != NULL){
  //    _modelviewUniformValue->_release();
  //  }

  //  if (_owner){
  //    delete _programState;
  //    delete _globalState;
  //  }

  for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
    delete _featuresGroups[i];
    delete _accumulatedGroups[i];
  }

  delete _valuesSet;
  delete _globalState;
}

void GLState::setParent(const GLState* parent) const{

  if (parent != _parentGLState || _parentsTimeStamp != parent->getTimeStamp()){

    _parentGLState = parent;
    _parentsTimeStamp = parent->getTimeStamp();

    for (int i = 0; i < N_GLFEATURES_GROUPS; i++){
      delete _accumulatedGroups[i];
      _accumulatedGroups[i] = GLFeatureGroup::createGroup((GLFeatureGroupName)i);
      GLFeatureSet* pfs = parent->getAccumulatedGroup(i);
      if (pfs != NULL){                                 //PARENT'S FEATURES
        _accumulatedGroups[i]->add(pfs);
      }
      if (_featuresGroups[i] != NULL){                  //THIS'S FEATURES
        _accumulatedGroups[i]->add(_featuresGroups[i]);
      }
    }

    hasChangedStructure();

  } else{
    //ILogger::instance()->logInfo("Reusing GLState Parent");
  }

  //  if (parent != NULL){
  //    //UNIFORMS AND ATTRIBUTES CODES
  ////    const int newUniformsCode   = parent->getUniformsCode()   | _programState->getUniformsCode();
  //////    const int newAttributesCode = parent->getAttributesCode() | _programState->getAttributesCode();
  ////
  ////    _totalGPUProgramStateChanged = ((newAttributesCode != _attributesCode) ||
  ////                                    (newUniformsCode   != _uniformsCode));
  ////    _uniformsCode   = newUniformsCode;
  ////    _attributesCode = newAttributesCode;
  //
  ////    //MODELVIEW
  ////    if (_multiplyModelview){
  ////      if (_modelview != NULL){
  ////        const Matrix44D* parentsM = parent->getAccumulatedModelView();
  ////        if (parentsM == NULL){
  ////          ILogger::instance()->logError("CAN'T MODIFY PARENTS MODELVIEW");
  ////        } else{
  ////
  ////          if (_lastParentModelview != parentsM){
  ////
  ////            if (_accumulatedModelview != NULL){
  ////              _accumulatedModelview->_release();
  ////            }
  ////            _accumulatedModelview = parentsM->createMultiplication(*_modelview);
  ////
  ////            if (_lastParentModelview != NULL){
  ////              //              _lastParentModelview->removeListener(&_parentMatrixListener);
  ////              _lastParentModelview->_release();
  ////            }
  ////
  ////            _lastParentModelview = parentsM;
  ////            _lastParentModelview->_retain();
  ////
  ////            //            if (_modelviewUniformValue != NULL){
  ////            //              _modelviewUniformValue->_release();
  ////            //              _modelviewUniformValue = NULL;
  ////            //            }
  ////
  ////            //            _lastParentModelview->addListener(&_parentMatrixListener);
  ////          }
  ////          //          else{
  ////          //            ILogger::instance()->logInfo("REUSING MODELVIEW");
  ////          //          }
  ////
  ////        }
  ////      }
  ////    }
  //  }
}

void GLState::applyGlobalStateOnGPU(GL* gl) const{

  if (_parentGLState != NULL){
    _parentGLState->applyGlobalStateOnGPU(gl);
  }

  //  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
}

void GLState::applyStates(GL* gl, GPUProgram* prog) const{
  if (_parentGLState != NULL){
    _parentGLState->applyStates(gl, prog);
  }

  //  _programState->linkToProgram(prog);
  //  _programState->applyValuesToLinkedProgram();
  //  _programState->applyValuesToProgram(prog);
  //
  //  _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{


  //  int uniformsCode = getUniformsCode();
  //  int attributesCode = getAttributesCode();

  /////////////////////////// FEATURES
  if (_valuesSet == NULL){
    _valuesSet = new GPUVariableValueSet();
    for (int i = 0; i < N_GLFEATURES_GROUPS; i++){

      GLFeatureGroup* group = _accumulatedGroups[i];
      if (group != NULL){
        GPUVariableValueSet* variables = group->createGPUVariableSet();
        if (variables != NULL){
          _valuesSet->combineWith(variables);
          delete variables;
        }
      }
    }


    int uniformsCode = _valuesSet->getUniformsCode();
    int attributesCode = _valuesSet->getAttributesCode();
    if (uniformsCode != _uniformsCode || attributesCode != _attributesCode){
      _uniformsCode = uniformsCode;
      _attributesCode = attributesCode;
      _totalGPUProgramStateChanged = true;
    }
  }


  if (_globalState == NULL){
    _globalState = new GLGlobalState();
    for (int i = 0; i < N_GLFEATURES_GROUPS; i++){

      GLFeatureGroup* group = _accumulatedGroups[i];
      if (group != NULL){
        //      group->applyGlobalGLState(gl);
        group->applyOnGlobalGLState(_globalState);
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


  /////////////////////////// FEATURES



  if (_lastGPUProgramUsed == NULL || _totalGPUProgramStateChanged){
    //ILogger::instance()->logInfo("Total State for GPUProgram has changed since last apply");
    _lastGPUProgramUsed = progManager.getProgram(gl, _uniformsCode, _attributesCode);
    _totalGPUProgramStateChanged = false;
  }

  if (_lastGPUProgramUsed != NULL){
    gl->useProgram(_lastGPUProgramUsed);
    applyStates(gl, _lastGPUProgramUsed);

    //APPLY TO GPU STATE MODELVIEW
    //    const Matrix44D* modelview = getAccumulatedModelView();
    //    GPUUniformValueMatrix4Float* modelviewValue = getModelviewUniformValue();
    //    if (modelviewValue != NULL){
    //      //      GPUUniformValueMatrix4Float valueModelview(*modelview);
    //#ifdef C_CODE
    //      _lastGPUProgramUsed->getGPUUniform(MODELVIEW)->set(modelviewValue);
    //#endif
    //#ifdef JAVA_CODE
    //      _lastGPUProgramUsed.getGPUUniform(GPUUniformKey.MODELVIEW.getValue()).set(modelviewValue);
    //#endif
    //    }


    /////////////////////////// FEATURES
    _valuesSet->applyValuesToProgram(_lastGPUProgramUsed);
    _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());

//    delete _valuesSet;
//    _valuesSet = NULL;
    /////////////////////////// FEATURES


    _lastGPUProgramUsed->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  } else{
    ILogger::instance()->logError("No GPUProgram found.");
  }




}

//void GLState::setModelView(const Matrix44D* modelview, bool multiply){
//
//  _multiplyModelview = multiply;
//
//  if (_modelview == NULL || _modelview != modelview){
//    //    delete _modelview;
//    //    _modelview = new Matrix44D(modelview);
//
//    if (_modelview != NULL){
//      _modelview->_release();
//    }
//
//    _modelview = modelview;
//    _modelview->_retain();
//
//
//    //Forcing matrix multiplication next time even when parent's modelview is the same
//    if (_lastParentModelview != NULL){
//      //      _lastParentModelview->removeListener(&_parentMatrixListener);
//      _lastParentModelview->_release();
//    }
//
//    _lastParentModelview = NULL;
//
//    if (_modelviewUniformValue != NULL){
//      _modelviewUniformValue->_release();
//      _modelviewUniformValue = NULL;
//    }
//  }
//  //  else{
//  //    ILogger::instance()->logInfo("Same modelview set.");
//  //  }
//}

//const Matrix44D* GLState::getAccumulatedModelView() const{
//
//  if (!_multiplyModelview && _modelview != NULL){
//    return _modelview;
//  }
//
//  if (_accumulatedModelview != NULL){
//    return _accumulatedModelview;
//  }
//  if (_parentGLState != NULL){
//    return _parentGLState->getAccumulatedModelView();
//  }
//  return NULL;
//
//};

//GPUUniformValueMatrix4Float* GLState::getModelviewUniformValue() const{
//
//  const Matrix44D* mv = getAccumulatedModelView();
//
//  if (_modelviewUniformValue == NULL){
//    if (mv == NULL){
//      return NULL;
//    }
//    _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
//  } else{
//    if (mv != _modelviewUniformValue->getMatrix()){
//      _modelviewUniformValue->_release();
//      _modelviewUniformValue = new GPUUniformValueMatrix4Float(*mv);
//    }
//  }
//  return _modelviewUniformValue;
//
//}

//GLFeatureSet* GLState::createAccumulatedGLFeaturesForGroup(GLFeatureGroupName g) const{
////TODO: WHY THIS DOES NOT WORK???? -> SOLVED: NOT TOP-BOTTOM
////  GLFeatureSet* fs = NULL;
////  const GLState* state = this;
////  const int index = g;
////  while (state != NULL) {
////    const GLFeatureSet* const thisFS = state->_featuresSets[index];
////    if (thisFS != NULL){
////      if (fs == NULL){
////        fs = new GLFeatureSet();
////      }
////      fs->add(thisFS);
////    }
////    state = state->getParent();
////  }
////  return fs;
//
//
//
//  GLFeatureSet* pfs = NULL;
//  if (_parentGLState != NULL){
//    pfs = _parentGLState->createAccumulatedGLFeaturesForGroup(g);
//  }
//
//  const int index = g;
//  const GLFeatureSet* const thisFS = _featuresSets[index];
//  if (thisFS == NULL){
//    return pfs;
//  } else{
//    GLFeatureSet* fs = new GLFeatureSet();
//    if (pfs != NULL){
//      fs->add(pfs);
//      delete pfs;
//    }
//    fs->add(thisFS);
//    return fs;
//  }
//
//}

void GLState::clearGLFeatureGroup(GLFeatureGroupName g){
  GLFeatureGroup* group = _featuresGroups[g];
  if (group != NULL){
    delete group;
    _featuresGroups[g] = NULL;
  }

  GLFeatureGroup* aGroup = _accumulatedGroups[g];
  if (aGroup != NULL){
    delete aGroup;
    _accumulatedGroups[g] = NULL;
  }
  
  hasChangedStructure();
}

