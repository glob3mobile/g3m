//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"

GLFeatureGroupName GLFeatureGroup::getGroupName(int i) {
  switch (i) {
    case 0:
      return NO_GROUP;
    case 1:
      return CAMERA_GROUP;
    case 2:
      return COLOR_GROUP;
    case 3:
      return LIGHTING_GROUP;
    default:
      return UNRECOGNIZED_GROUP;
  }
}

GLFeatureGroup* GLFeatureGroup::createGroup(GLFeatureGroupName name) {
  switch (name) {
    case UNRECOGNIZED_GROUP:
      return NULL;
    case NO_GROUP:
      return new GLFeatureNoGroup();
    case CAMERA_GROUP:
      return new GLFeatureCameraGroup();
    case COLOR_GROUP:
      return new GLFeatureColorGroup();
    case LIGHTING_GROUP:
      return new GLFeatureLightingGroup();
    default:
      return NULL;
  }
}



void GLFeatureSet::add(const GLFeature* f) {
  _features[_nFeatures++] = f;
  f->_retain();

  if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
    ILogger::instance()->logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
  }
}

void GLFeatureSet::clearFeatures(GLFeatureGroupName g) {

  for (int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    if (f->getGroup() == g) {
      f->_release();

      for (int j = i; j < _nFeatures; j++) {
        if (j+1 >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
          _features[j] = NULL;
        } else{
          _features[j] = _features[j+1];
        }
      }
      i--;
      _nFeatures--;
    }
  }

}

void GLFeatureSet::clearFeatures() {

  for (int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    f->_release();

    for (int j = i; j < _nFeatures; j++) {
      if (j+1 >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
        _features[j] = NULL;
      } else{
        _features[j] = _features[j+1];
      }
    }
    i--;
    _nFeatures--;
  }

}

void GLFeatureSet::add(const GLFeatureSet* fs) {
  const int size = fs->size();
  for (int i = 0; i < size; i++) {
    add(fs->get(i));
  }
}

GLFeatureSet::~GLFeatureSet() {
  for (int i = 0; i < _nFeatures; i++) {
    _features[i]->_release();
  }
}

GLFeatureGroup** GLFeatureGroup::_groups = NULL;

void GLFeatureGroup::applyToAllGroups(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state){

  if (_groups == NULL){
    _groups = new GLFeatureGroup*[N_GLFEATURES_GROUPS];

    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
      GLFeatureGroupName groupName = GLFeatureGroup::getGroupName(i);
      _groups[i] = GLFeatureGroup::createGroup(groupName);
    }
  }

  for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
    _groups[i]->apply(features, vs, state);
  }

}

void GLFeatureNoGroup::apply(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state){

  for(int i = 0; i < features.size(); i++) {
    const GLFeature* f = features.get(i);
    if (f->getGroup() == NO_GROUP){
      f->applyOnGlobalGLState(&state);
      vs.combineWith(f->getGPUVariableValueSet());
    }
  }
}

void GLFeatureColorGroup::apply(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state){

  int priority = -1;
  for (int i = 0; i < features.size(); i++) {
    const GLFeature* f = features.get(i);
    if (f->getGroup() == COLOR_GROUP){
      PriorityGLFeature* pf = ((PriorityGLFeature*) f);
      if (pf->getPriority() > priority) {
        priority = pf->getPriority();
      }
    }
  }

  for (int i = 0; i < features.size(); i++) {
    const GLFeature* f = features.get(i);
    if (f->getGroup() == COLOR_GROUP){
      PriorityGLFeature* pf = ((PriorityGLFeature*) f);
      if (pf->getPriority() == priority) {
        pf->applyOnGlobalGLState(&state);
        vs.combineWith(f->getGPUVariableValueSet());
      }
    }
  }

  if (vs.containsUniform(FLAT_COLOR) && vs.containsAttribute(TEXTURE_COORDS)){
    int a = 0;
    a++;
  }

}


void GLFeatureCameraGroup::apply(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state){

  const int size = features.size();
  const Matrix44DProvider** modelTransformHolders = new const Matrix44DProvider*[size];

  int modelViewCount = 0;
  for (int i = 0; i < size; i++){
    const GLFeature* f = features.get(i);
    if (f->getGroup() == CAMERA_GROUP){
      GLCameraGroupFeature* cf = ((GLCameraGroupFeature*) f);
      modelTransformHolders[modelViewCount++] = cf->getMatrixHolder();
    }
  }

  Matrix44DProvider* modelViewProvider = new Matrix44DMultiplicationHolder(modelTransformHolders,modelViewCount);
  vs.addUniformValue(MODELVIEW,
                     new GPUUniformValueMatrix4(modelViewProvider, true),
                     false);

  delete [] modelTransformHolders;
}

void GLFeatureLightingGroup::apply(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state){
  bool normalsAvailable = false;
  for(int i = 0; i < features.size(); i++){
    const GLFeature* f = features.get(i);
    if (f->getID() == GLF_VERTEX_NORMAL){
      normalsAvailable = true;
      break;
    }
  }


  if (normalsAvailable){

    int modelTransformCount = 0;

    for(int i = 0; i < features.size(); i++){
      const GLFeature* f = features.get(i);

      if (f->getID() == GLF_MODEL_TRANSFORM){
        modelTransformCount++;
      }
      
      if (f->getGroup() == LIGHTING_GROUP){
        f->applyOnGlobalGLState(&state);
        vs.combineWith(f->getGPUVariableValueSet());
      }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    const Matrix44DProvider** modelTransformHolders = new const Matrix44DProvider*[modelTransformCount];

    modelTransformCount = 0;
    for (int i = 0; i < features.size(); i++){
      const GLFeature* f = features.get(i);
      if (f->getID() == GLF_MODEL_TRANSFORM){
        GLCameraGroupFeature* cf = ((GLCameraGroupFeature*) f);
        const Matrix44D* m = cf->getMatrixHolder()->getMatrix();

        if (!m->isScaleMatrix() && !m->isTranslationMatrix()){
          modelTransformHolders[modelTransformCount++] = cf->getMatrixHolder();
        }
      }

    }

    Matrix44DProvider* modelProvider = NULL;
    if (modelTransformCount > 0){
      modelProvider = new Matrix44DMultiplicationHolder(modelTransformHolders, modelTransformCount);

      vs.addUniformValue(MODEL,
                         new GPUUniformValueMatrix4(modelProvider, true),
                         false);
    }
    
    delete [] modelTransformHolders;
    
    
  }
}
