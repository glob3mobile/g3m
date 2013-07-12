//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include <iostream>

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramState.hpp"
#include "GPUProgramManager.hpp"

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"

class GLState{

  GLFeatureSet* _featuresSets[N_GLFEATURES_GROUPS]; //1 set for group of features

  
  GLFeatureNoGroup _featureNoGroup;
  GLFeatureCameraGroup _featureCameraGroup;
  GLFeatureColorGroup _featureColorGroup;


  GPUProgramState* _programState;
  GLGlobalState*   _globalState;
  const bool _owner;

  mutable int _uniformsCode;
  mutable int _attributesCode;
  mutable bool _totalGPUProgramStateChanged;
  mutable GPUProgram* _lastGPUProgramUsed;

#ifdef C_CODE
  const Matrix44D* _modelview;
  mutable const Matrix44D* _accumulatedModelview;
  mutable const Matrix44D* _lastParentModelview;
#else
  Matrix44D* _modelview;
  mutable Matrix44D* _accumulatedModelview;
  mutable Matrix44D* _lastParentModelview;
#endif
  bool _multiplyModelview;
  mutable GPUUniformValueMatrix4Float* _modelviewUniformValue;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif

  void applyStates(GL* gl, GPUProgram* prog) const;

  GLState(const GLState& state);

public:

  GLState():
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(NULL),
  _lastGPUProgramUsed(NULL),
  _accumulatedModelview(NULL),
  _multiplyModelview(false),
  _lastParentModelview(NULL),
  _modelviewUniformValue(NULL){

    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
      _featuresSets[i] = NULL;
    }

  }
  /*
   //For debugging purposes only
   GLState(GLGlobalState*   globalState,
   GPUProgramState* programState):
   _programState(programState),
   _globalState(globalState),
   _owner(false),
   _parentGLState(NULL),
   _uniformsCode(0),
   _attributesCode(0),
   _totalGPUProgramStateChanged(true),
   _modelview(NULL),
   _accumulatedModelview(NULL),
   _multiplyModelview(false),
   //    _parentMatrixListener(this),
   _lastParentModelview(NULL),
   _modelviewUniformValue(NULL){}
   */
  ~GLState();

  const GLState* getParent() const{
    return _parentGLState;
  }

  void setParent(const GLState* p) const;

  int getUniformsCode() const{
    if (_parentGLState == NULL){
      return _programState->getUniformsCode();
    }
    return _uniformsCode;
  }

  int getAttributesCode() const{
    if (_parentGLState == NULL){
      return _programState->getAttributesCode();
    }
    return _attributesCode;
  }

  void applyGlobalStateOnGPU(GL* gl) const;

  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;

  GPUProgramState* getGPUProgramState() const{
    return _programState;
  }

  GLGlobalState* getGLGlobalState() const{
    return _globalState;
  }

  //  static void textureHasBeenDeleted(const IGLTextureId* textureId){
  //    if (_currentGPUGlobalState.getBoundTexture() == textureId){
  //      _currentGPUGlobalState.bindTexture(NULL);
  //    }
  //  }
  //
  //  static GLGlobalState* createCopyOfCurrentGLGlobalState(){
  //    return _currentGPUGlobalState.createCopy();
  //  }

  void setModelView(const Matrix44D* modelview, bool modifiesParents);
  const Matrix44D* getAccumulatedModelView() const;

  GPUUniformValueMatrix4Float* getModelviewUniformValue() const;


  void addGLFeature(const GLFeature* f){
    GLFeatureGroupName g = f->getGroup();
    if (_featuresSets[g] == NULL){
      _featuresSets[g] = new GLFeatureSet();
    }

    _featuresSets[g]->add(f);
  }

  void addGLFeatureAndRelease(const GLFeature* f){
    GLFeatureGroupName g = f->getGroup();
    if (_featuresSets[g] == NULL){
      _featuresSets[g] = new GLFeatureSet();
    }

    _featuresSets[g]->add(f);
    f->_release();
  }

  GLFeatureSet* createAccumulatedGLFeaturesForGroup(GLFeatureGroupName g) const;

  void clearGLFeatureGroup(GLFeatureGroupName g);
};

#endif /* defined(__G3MiOSSDK__GLState__) */
