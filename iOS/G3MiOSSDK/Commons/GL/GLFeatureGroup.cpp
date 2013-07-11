//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"

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

GLFeatureGroupName GLFeatureGroup::getGroupName(int i){
  switch (i) {
    case 0:
      return NO_GROUP;
    case 1:
      return CAMERA_GROUP;
    case 2:
      return COLOR_GROUP;
    default:
      return UNRECOGNIZED_GROUP;
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


GPUVariableValueSet* GLFeatureNoGroup::applyAndCreateGPUVariableSet(GL* gl, const GLFeatureSet* features){
  GPUVariableValueSet* vs = new GPUVariableValueSet();
  int size = features->size();
  for(int i = 0; i < size; i++){
    const GLFeature* f = features->get(i);
    if (f != NULL){
      f->applyGLGlobalState(gl);
      vs->combineWith(f->getGPUVariableValueSet());
    }
  }
  return vs;
}

GPUVariableValueSet* GLFeatureCameraGroup::applyAndCreateGPUVariableSet(GL* gl, const GLFeatureSet* features){
  const Matrix44D* m = ((GLCameraGroupFeature*) features->get(0))->getMatrix();
  m->_retain();

  const int size = features->size();
  for (int i = 1; i < size; i++){
    const Matrix44D* m2 = ((GLCameraGroupFeature*) features->get(i))->getMatrix();

    Matrix44D* m3 = m->createMultiplication(*m2);

    m->_release();
    m = m3;
  }

  GPUVariableValueSet* fs = new GPUVariableValueSet();
  fs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4Float(*m));
  m->_release();

  return fs;
}

GPUVariableValueSet* GLFeatureColorGroup::applyAndCreateGPUVariableSet(GL* gl, const GLFeatureSet* features){
  return NULL;
}
