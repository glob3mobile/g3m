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
#define MAX_CONCURRENT_FEATURES_PER_GROUP 10
  GLFeature const* _features[MAX_CONCURRENT_FEATURES_PER_GROUP];
  int _nFeatures;

public:

  GLFeatureSet():_nFeatures(0){
  }

  GLFeature const* get(int i){
    if (_nFeatures >= i){
      return NULL;
    }
    return _features[i];
  }

  void add(const GLFeature* f){
    _features[_nFeatures++] = f;
  }

};

enum GLFeatureGroupName{
  UNRECOGNIZED_GROUP = -1,
  NO_GROUP = 0,
  CAMERA_GROUP = 1,
  COLOR_GROUP = 2,
};

#define N_GLFEATURES_GROUPS 3
class GLFeatureGroup{

  static GLFeatureGroup* _noGroup;
  static GLFeatureGroup* _cameraGroup;
  static GLFeatureGroup* _colorGroup;
public:

  virtual ~GLFeatureGroup(){}

  static GLFeatureGroup* getGroup(GLFeatureGroupName name);
  static GLFeatureGroup* getGroup(int i);

  virtual GPUVariableValueSet* applyAndCreateGPUVariableSet(const GLFeatureSet* features)= 0;
};

class GLFeatureNoGroup: public GLFeatureGroup{
public:
  ~GLFeatureNoGroup(){}
  GPUVariableValueSet* applyAndCreateGPUVariableSet(const GLFeatureSet* features);
};

class GLFeatureCameraGroup: public GLFeatureGroup{
public:
  ~GLFeatureCameraGroup(){}
  GPUVariableValueSet* applyAndCreateGPUVariableSet(const GLFeatureSet* features);
};


class GLFeatureColorGroup: public GLFeatureGroup{
public:
  ~GLFeatureColorGroup(){}
  GPUVariableValueSet* applyAndCreateGPUVariableSet(const GLFeatureSet* features);
};

#endif /* defined(__G3MiOSSDK__GLFeatureGroup__) */
