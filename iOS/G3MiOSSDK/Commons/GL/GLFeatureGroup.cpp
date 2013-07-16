//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"

//GLFeatureGroup* GLFeatureGroup::_noGroup = NULL;
//GLFeatureGroup* GLFeatureGroup::_cameraGroup = NULL;
//GLFeatureGroup* GLFeatureGroup::_colorGroup = NULL;
//
//GLFeatureGroup* GLFeatureGroup::getGroup(int i){
//  switch (i) {
//    case -1:
//      return getGroup(UNRECOGNIZED_GROUP);
//    case 0:
//      return getGroup(NO_GROUP);
//    case 1:
//      return getGroup(CAMERA_GROUP);
//    case 2:
//      return getGroup(COLOR_GROUP);
//    default:
//      return NULL;
//  }
//}

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

//GLFeatureGroup* GLFeatureGroup::getGroup(GLFeatureGroupName name){
//  switch (name) {
//    case UNRECOGNIZED_GROUP:
//      return NULL;
//    case NO_GROUP:
//      if (_noGroup == NULL){
//        _noGroup = new GLFeatureNoGroup();
//      }
//      return _noGroup;
//    case CAMERA_GROUP:
//      if (_cameraGroup == NULL){
//        _cameraGroup = new GLFeatureCameraGroup();
//      }
//      return _cameraGroup;
//
//    case COLOR_GROUP:
//      if (_colorGroup == NULL){
//        _colorGroup = new GLFeatureColorGroup();
//      }
//      return _colorGroup;
//    default:
//      return NULL;
//  }
//}

GLFeatureGroup* GLFeatureGroup::createGroup(GLFeatureGroupName name){
  switch (name) {
    case UNRECOGNIZED_GROUP:
      return NULL;
    case NO_GROUP:
      return new GLFeatureNoGroup();
    case CAMERA_GROUP:
      return new GLFeatureCameraGroup();
    case COLOR_GROUP:
      return new GLFeatureColorGroup();
    default:
      return NULL;
  }
}


//GPUVariableValueSet* GLFeatureNoGroup::applyAndCreateGPUVariableSet(GL* gl){
//  GPUVariableValueSet* vs = new GPUVariableValueSet();
//  for(int i = 0; i < _nFeatures; i++){
//    const GLFeature* f = _features[i];
//    if (f != NULL){
////      f->applyGLGlobalState(gl);
//      vs->combineWith(f->getGPUVariableValueSet());
//    }
//  }
//  return vs;
//}

void GLFeatureNoGroup::applyOnGlobalGLState(GLGlobalState* state){
  for(int i = 0; i < _nFeatures; i++){
    const GLFeature* f = _features[i];
    if (f != NULL){
      f->applyOnGlobalGLState(state);
    }
  }
}

//GPUVariableValueSet* GLFeatureCameraGroup::applyAndCreateGPUVariableSet(GL* gl){
///*
//  const Matrix44D* m = ((GLCameraGroupFeature*) _features[0])->getMatrix();
//  m->_retain();
//  _features[0]->applyGLGlobalState(gl);
//
//  for (int i = 1; i < _nFeatures; i++){
//    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
//    //f->applyGLGlobalState(gl);
//    const Matrix44D* m2 = f->getMatrix();
//
//    Matrix44D* m3 = m->createMultiplication(*m2);
//
//    m->_release();
//    m = m3;
//  }
//
//  GPUVariableValueSet* fs = new GPUVariableValueSet();
//  fs->addUniformValue(MODELVIEW, new GPUUniformValueMatrix4Float(*m));
//  m->_release();
//
//  return fs;
// */
//
//  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
//  for (int i = 0; i < _nFeatures; i++){
//    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
//    matrixHolders[i] = f->getMatrixHolder();
//    if (matrixHolders[i] == NULL){
//      ILogger::instance()->logError("MatrixHolder NULL");
//    }
//  }
//  
//  GPUVariableValueSet* fs = new GPUVariableValueSet();
//  fs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures));
//
//  return fs;
//}

//GPUVariableValueSet* GLFeatureColorGroup::applyAndCreateGPUVariableSet(GL* gl){
//
//  int priority = -1;
//  GLColorGroupFeature* topPriorityFeature = NULL;
//  for (int i = 0; i < _nFeatures; i++){
//    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
//    if (f->getPriority() > priority){
//      topPriorityFeature = f;
//      priority = f->getPriority();
//    }
//  }
//
//  if (topPriorityFeature != NULL){
//    GPUVariableValueSet* fs = new GPUVariableValueSet();
//    fs->combineWith(topPriorityFeature->getGPUVariableValueSet());
////    topPriorityFeature->applyGLGlobalState(gl);
//    return fs;
//  } else{
//    return NULL;
//  }
//}

void GLFeatureColorGroup::applyOnGlobalGLState(GLGlobalState* state){

  int priority = -1;
  GLColorGroupFeature* topPriorityFeature = NULL;
  for (int i = 0; i < _nFeatures; i++){
    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
    if (f->getPriority() > priority){
      topPriorityFeature = f;
      priority = f->getPriority();
    }
  }

  if (topPriorityFeature != NULL){
    topPriorityFeature->applyOnGlobalGLState(state);
  }
}



//GPUVariableValueSet* GLFeatureNoGroup::createGPUVariableSet(){
//  GPUVariableValueSet* vs = new GPUVariableValueSet();
//  for(int i = 0; i < _nFeatures; i++){
//    const GLFeature* f = _features[i];
//    if (f != NULL){
//      vs->combineWith(f->getGPUVariableValueSet());
//    }
//  }
//  return vs;
//}


void GLFeatureNoGroup::addToGPUVariableSet(GPUVariableValueSet* vs){
  for(int i = 0; i < _nFeatures; i++){
    const GLFeature* f = _features[i];
    if (f != NULL){
      vs->combineWith(f->getGPUVariableValueSet());
    }
  }
}

//GPUVariableValueSet* GLFeatureCameraGroup::createGPUVariableSet(){
//#ifdef C_CODE
//  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
//#endif
//#ifdef JAVA_CODE
//  final Matrix44DHolder[] matrixHolders = new Matrix44DHolder[_nFeatures];
//#endif
//  for (int i = 0; i < _nFeatures; i++){
//    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
//    matrixHolders[i] = f->getMatrixHolder();
//    if (matrixHolders[i] == NULL){
//      ILogger::instance()->logError("MatrixHolder NULL");
//    }
//  }
//
//  GPUVariableValueSet* fs = new GPUVariableValueSet();
//  fs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures), false);
//
//  return fs;
//}

void GLFeatureCameraGroup::addToGPUVariableSet(GPUVariableValueSet* vs){
#ifdef C_CODE
  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
#endif
#ifdef JAVA_CODE
  final Matrix44DHolder[] matrixHolders = new Matrix44DHolder[_nFeatures];
#endif
  for (int i = 0; i < _nFeatures; i++){
    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
    matrixHolders[i] = f->getMatrixHolder();
    if (matrixHolders[i] == NULL){
      ILogger::instance()->logError("MatrixHolder NULL");
    }
  }

  vs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures), false);
}

//GPUVariableValueSet* GLFeatureColorGroup::createGPUVariableSet(){
//
//  int priority = -1;
//  GLColorGroupFeature* topPriorityFeature = NULL;
//  for (int i = 0; i < _nFeatures; i++){
//    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
//    if (f->getPriority() > priority){
//      topPriorityFeature = f;
//      priority = f->getPriority();
//    }
//  }
//
//  if (topPriorityFeature != NULL){
//    GPUVariableValueSet* fs = new GPUVariableValueSet();
//    fs->combineWith(topPriorityFeature->getGPUVariableValueSet());
//    return fs;
//  } else{
//    return NULL;
//  }
//}

void GLFeatureColorGroup::addToGPUVariableSet(GPUVariableValueSet *vs){

  int priority = -1;
  GLColorGroupFeature* topPriorityFeature = NULL;
  for (int i = 0; i < _nFeatures; i++){
    GLColorGroupFeature* f = ((GLColorGroupFeature*) _features[i]);
    if (f->getPriority() > priority){
      topPriorityFeature = f;
      priority = f->getPriority();
    }
  }

  if (topPriorityFeature != NULL){
    vs->combineWith(topPriorityFeature->getGPUVariableValueSet());
  }
}


void GLFeatureSet::add(const GLFeature* f){
  _features[_nFeatures++] = f;
  f->_retain();

  if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP){
    ILogger::instance()->logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
  }
}

void GLFeatureSet::add(const GLFeatureSet* fs){
  const int size = fs->size();
  for (int i = 0; i < size; i++) {
    add(fs->get(i));
  }
}

GLFeatureSet::~GLFeatureSet(){
  for (int i = 0; i < _nFeatures; i++) {
    _features[i]->_release();
  }
}
