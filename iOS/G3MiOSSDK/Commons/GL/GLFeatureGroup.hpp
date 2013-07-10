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

#include "GLFeature.hpp"
#include "GPUVariableValueSet.hpp"

class GLFeatureSet{
#define MAX_CONCURRENT_FEATURES_PER_GROUP 10
  GLFeature* _features[MAX_CONCURRENT_FEATURES_PER_GROUP];
  int _nFeatures;

public:

  GLFeatureSet():_nFeatures(0){
  }

  GLFeature* get(int i){
    if (_nFeatures >= i){
      return NULL;
    }
    return _features[i];
  }

  void add(GLFeature* f){
    _features[_nFeatures++] = f;
  }

};

enum GLFeatureGroupName{
  UNRECOGNIZED_GROUP = -1,
  NO_GROUP = 0,
  CAMERA_GROUP = 1,
  COLOR_GROUP = 2,
};

class GLFeatureGroup{
public:

  virtual ~GLFeatureGroup(){}

  static GLFeatureGroup* _noGroup;
  static GLFeatureGroup* _cameraGroup;
  static GLFeatureGroup* _colorGroup;

  static GLFeatureGroup* getGroup(GLFeatureGroupName name);

  virtual GPUVariableValueSet* applyAndCreateGPUVariableSet(GLFeatureSet* features) = 0;


  

};

class GLFeatureNoGroup: public GLFeatureGroup{
  ~GLFeatureNoGroup();
  GPUVariableValueSet* applyAndCreateGPUVariableSet(GLFeatureSet* features){}
};

class GLFeatureCameraGroup: public GLFeatureGroup{
  ~GLFeatureCameraGroup();
  GPUVariableValueSet* applyAndCreateGPUVariableSet(GLFeatureSet* features){}
};


class GLFeatureColorGroup: public GLFeatureGroup{
  ~GLFeatureColorGroup();
  GPUVariableValueSet* applyAndCreateGPUVariableSet(GLFeatureSet* features){}
};

#endif /* defined(__G3MiOSSDK__GLFeatureGroup__) */
