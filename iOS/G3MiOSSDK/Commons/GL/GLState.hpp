//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"
#include "GPUVariableValueSet.hpp"

#include "RCObject.hpp"

class GLState: public RCObject{
private:
  GLFeatureSet _features;
  mutable GLFeatureSet* _accumulatedFeatures;

  mutable int _timeStamp;
  mutable int _parentsTimeStamp;

  mutable GPUVariableValueSet* _valuesSet;
  mutable GLGlobalState*   _globalState;

  mutable GPUProgram* _linkedProgram;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif

  GLState(const GLState& state);

  void hasChangedStructure() const;

  ~GLState();

public:

  GLState():
  _parentGLState(NULL),
  _linkedProgram(NULL),
  _parentsTimeStamp(-1),
  _timeStamp(0),
  _valuesSet(NULL),
  _globalState(NULL),
  _accumulatedFeatures(NULL)
  {
  }

  int getTimeStamp() const { return _timeStamp;}

  GLFeatureSet* getAccumulatedFeatures() const;
//  GLFeatureSet* createAccumulatedFeatures() const;


  void setParent(const GLState* p) const;

  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;

  void addGLFeature(GLFeature* f, bool mustRetain);

  void clearGLFeatureGroup(GLFeatureGroupName g);

  void clearAllGLFeatures();

  int getNumberOfGLFeatures() const;

  GLFeature* getGLFeature(GLFeatureID id) const;

  GLFeatureSet getGLFeatures(GLFeatureID id) const;
};

#endif
