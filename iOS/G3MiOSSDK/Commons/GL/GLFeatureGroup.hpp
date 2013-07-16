//
//  GLFeatureGroup.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#ifndef __G3MiOSSDK__GLFeatureGroup__
#define __G3MiOSSDK__GLFeatureGroup__

#include <iostream>


#include "GPUVariableValueSet.hpp"

class GLFeature;

class GLFeatureSet{
protected:
#define MAX_CONCURRENT_FEATURES_PER_GROUP 20
#ifdef C_CODE
  GLFeature const* _features[MAX_CONCURRENT_FEATURES_PER_GROUP];
#endif
#ifdef JAVA_CODE
  protected final GLFeature[] _features = new GLFeature[MAX_CONCURRENT_FEATURES_PER_GROUP];
#endif
  int _nFeatures;

public:

  GLFeatureSet():_nFeatures(0){
    for (int i = 0; i < MAX_CONCURRENT_FEATURES_PER_GROUP; i++) {
      _features[i] = NULL;
    }
  }

  virtual ~GLFeatureSet();

#ifdef C_CODE
  GLFeature const* get(int i) const
#else
  GLFeature* get(int i) const
#endif
  {
    if (_nFeatures < i){
      return NULL;
    }
    return _features[i];
  }

  void add(const GLFeature* f);

  void add(const GLFeatureSet* fs);

  int size() const{
    return _nFeatures;
  }

};

enum GLFeatureGroupName{
  UNRECOGNIZED_GROUP = -1,
  NO_GROUP = 0,
  CAMERA_GROUP = 1,
  COLOR_GROUP = 2,
};

#define N_GLFEATURES_GROUPS 3
class GLFeatureGroup: public GLFeatureSet{

  static GLFeatureGroup* _noGroup;
  static GLFeatureGroup* _cameraGroup;
  static GLFeatureGroup* _colorGroup;
public:

  virtual ~GLFeatureGroup(){}

  static GLFeatureGroup* createGroup(GLFeatureGroupName name);
  static GLFeatureGroup* getGroup(GLFeatureGroupName name);
  static GLFeatureGroup* getGroup(int i);
  static GLFeatureGroupName getGroupName(int i);

//  virtual GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl)= 0;

  virtual GPUVariableValueSet* createGPUVariableSet()= 0;
  virtual void applyOnGlobalGLState(GLGlobalState* state)= 0;
};

class GLFeatureNoGroup: public GLFeatureGroup{
public:
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
  GPUVariableValueSet* createGPUVariableSet();
  void applyOnGlobalGLState(GLGlobalState* state);
};

class GLFeatureCameraGroup: public GLFeatureGroup{
public:
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
  GPUVariableValueSet* createGPUVariableSet();
  void applyOnGlobalGLState(GLGlobalState* state){}
};


class GLFeatureColorGroup: public GLFeatureGroup{
public:
//  GPUVariableValueSet* applyAndCreateGPUVariableSet(GL* gl);
  GPUVariableValueSet* createGPUVariableSet();
  void applyOnGlobalGLState(GLGlobalState* state);
};

#endif /* defined(__G3MiOSSDK__GLFeatureGroup__) */
