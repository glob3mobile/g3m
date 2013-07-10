//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GLFeatureGroup.hpp"

GLFeatureGroup* GLFeatureGroup::_noGroup = NULL;
GLFeatureGroup* GLFeatureGroup::_cameraGroup = NULL;
GLFeatureGroup* GLFeatureGroup::_colorGroup = NULL;

GLFeatureGroup* GLFeatureGroup::getGroup(int i){
  switch (i) {
    case -1:
      return getGroup(UNRECOGNIZED_GROUP);
    case 0:
      return getGroup(NO_GROUP);
    case 1:
      return getGroup(CAMERA_GROUP);
    case 2:
      return getGroup(COLOR_GROUP);
    default:
      return NULL;
  }
}

GLFeatureGroup* GLFeatureGroup::getGroup(GLFeatureGroupName name){
  switch (name) {
    case UNRECOGNIZED_GROUP:
      return NULL;
    case NO_GROUP:
      if (_noGroup == NULL){
        _noGroup = new GLFeatureNoGroup();
      }
      return _noGroup;
    case CAMERA_GROUP:
      if (_cameraGroup == NULL){
        _cameraGroup = new GLFeatureCameraGroup();
      }
      return _cameraGroup;

    case COLOR_GROUP:
      if (_colorGroup == NULL){
        _colorGroup = new GLFeatureColorGroup();
      }
      return _colorGroup;
    default:
      return NULL;
  }
}


GPUVariableValueSet* GLFeatureNoGroup::applyAndCreateGPUVariableSet(const GLFeatureSet* features){
  return NULL;
}

GPUVariableValueSet* GLFeatureCameraGroup::applyAndCreateGPUVariableSet(const GLFeatureSet* features){
  return NULL;
}

GPUVariableValueSet* GLFeatureColorGroup::applyAndCreateGPUVariableSet(const GLFeatureSet* features){
  return NULL;
}
