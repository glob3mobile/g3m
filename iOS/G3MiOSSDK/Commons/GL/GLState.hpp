//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include <iostream>

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"
#include "GPUVariableValueSet.hpp"

class GLState{

  GLFeatureSet _features;
  mutable GLFeatureSet* _accumulatedFeatures;

//  GLFeatureGroup* _featuresGroups[N_GLFEATURES_GROUPS]; //1 set for group of features
//  mutable GLFeatureGroup* _accumulatedGroups[N_GLFEATURES_GROUPS]; //1 set for group of features

  mutable int _timeStamp;
  mutable int _parentsTimeStamp;

  mutable GPUVariableValueSet* _valuesSet;
  mutable GLGlobalState*   _globalState;

  mutable GPUProgram* _lastGPUProgramUsed;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif

//  void applyStates(GL* gl, GPUProgram* prog) const;

  GLState(const GLState& state);

  void hasChangedStructure() const {
    _timeStamp++;
    delete _valuesSet;
    _valuesSet = NULL;
    delete _globalState;
    _globalState = NULL;
    _lastGPUProgramUsed = NULL;

//    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
//      delete _accumulatedGroups[i];
//      _accumulatedGroups[i] = NULL;
//    }

    delete _accumulatedFeatures;
    _accumulatedFeatures = NULL;
  }

public:

  GLState():
  _parentGLState(NULL),
  _lastGPUProgramUsed(NULL),
  _parentsTimeStamp(0),
  _timeStamp(0),
  _valuesSet(NULL),
  _globalState(NULL),
  _accumulatedFeatures(NULL)
  {

//    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
//      _featuresGroups[i] = NULL;
//      _accumulatedGroups[i] = NULL;
//    }

  }

  int getTimeStamp() const { return _timeStamp;}

//  GLFeatureGroup* getAccumulatedGroup(int i) const{
//    if (_accumulatedGroups[i] == NULL) {
//
//      _accumulatedGroups[i] = GLFeatureGroup::createGroup(GLFeatureGroup::getGroupName(i));
//      if (_parentGLState != NULL) {
//        GLFeatureGroup* pg = _parentGLState->getAccumulatedGroup(i);
//        if (pg != NULL) {
//          _accumulatedGroups[i]->add(pg);
//        }
//      }
//      if (_featuresGroups[i] != NULL) {
//        _accumulatedGroups[i]->add(_featuresGroups[i]);
//      }
//    }
//    return _accumulatedGroups[i];
//  }

  GLFeatureSet* getAccumulatedFeatures() const{
    if (_accumulatedFeatures == NULL) {

      _accumulatedFeatures = new GLFeatureSet();

      if (_parentGLState != NULL){
        GLFeatureSet* parents = _parentGLState->getAccumulatedFeatures();
        if (parents != NULL){
          _accumulatedFeatures->add(parents);
        }
      }
      _accumulatedFeatures->add(&_features);

    }
    return _accumulatedFeatures;
  }

  ~GLState();

//  const GLState* getParent() const{
//    return _parentGLState;
//  }

  void setParent(const GLState* p) const;

//  void applyGlobalStateOnGPU(GL* gl) const;

  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;

  void addGLFeature(const GLFeature* f, bool mustRetain) {
//    GLFeatureGroupName g = f->getGroup();
//#ifdef C_CODE
//    const int index = g;
//#endif
//#ifdef JAVA_CODE
//    final int index = g.getValue();
//#endif

    _features.add(f);

//    if (_featuresGroups[index] == NULL) {
//      _featuresGroups[index] = GLFeatureGroup::createGroup(g);
//    }
//
//    _featuresGroups[index]->add(f);
    if (!mustRetain) {
      f->_release();
    }

    hasChangedStructure();
  }

  void clearGLFeatureGroup(GLFeatureGroupName g);

//  int getGLFeatureSize(GLFeatureGroupName g) const{
//#ifdef C_CODE
//    const int index = g;
//#endif
//#ifdef JAVA_CODE
//    final int index = g.getValue();
//#endif
//
//    if (_featuresGroups[index] == NULL) {
//      return 0;
//    }
//    return _featuresGroups[index]->size();
//  }
};

#endif /* defined(__G3MiOSSDK__GLState__) */
