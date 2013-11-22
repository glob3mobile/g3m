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



void GLFeatureSet::add(GLFeature* f) {
  _features[_nFeatures++] = f;
  f->_retain();

  if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
    ILogger::instance()->logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
  }
}

void GLFeatureSet::clearFeatures(GLFeatureGroupName g) {

  for (int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    if (f->_group == g) {
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

#pragma mark GLFeatureGroup

GLFeatureGroup** GLFeatureGroup::_groups = NULL;

void GLFeatureGroup::applyToAllGroups(const GLFeatureSet& features,
                                      GPUVariableValueSet& vs,
                                      GLGlobalState& state) {

  if (_groups == NULL) {
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

void GLFeatureNoGroup::apply(const GLFeatureSet& features,
                             GPUVariableValueSet& vs,
                             GLGlobalState& state) {

  const int size = features.size();
  for(int i = 0; i < size; i++) {
    const GLFeature* f = features.get(i);
    if (f->_group == NO_GROUP) {
      f->applyOnGlobalGLState(&state);
      vs.combineWith(f->getGPUVariableValueSet());
    }
  }
}

void GLFeatureColorGroup::apply(const GLFeatureSet& features,
                                GPUVariableValueSet& vs,
                                GLGlobalState& state) {

  const int featuresSize = features.size();
  int priority = -1;
  for (int i = 0; i < featuresSize; i++) {
    const GLFeature* f = features.get(i);
    if (f->_group == COLOR_GROUP) {
      PriorityGLFeature* pf = ((PriorityGLFeature*) f);
      if (pf->_priority > priority) {
        if (pf->_id != GLF_BLENDING_MODE) {  //We do not take into account Blending if TexID not set
          priority = pf->_priority;
        }
      }
    }
  }

  for (int i = 0; i < featuresSize; i++) {
    const GLFeature* f = features.get(i);
    if (f->_group == COLOR_GROUP) {
      PriorityGLFeature* pf = ((PriorityGLFeature*) f);
      if (pf->_priority == priority) {
        pf->applyOnGlobalGLState(&state);
        vs.combineWith(f->getGPUVariableValueSet());
      }
    }
  }
}


void GLFeatureCameraGroup::apply(const GLFeatureSet& features,
                                 GPUVariableValueSet& vs,
                                 GLGlobalState& state) {

  Matrix44DMultiplicationHolderBuilder modelViewHolderBuilder;
  Matrix44DMultiplicationHolderBuilder modelTransformHolderBuilder;

  bool normalsAvailable = false;

  const int featuresSize = features.size();
  for (int i = 0; i < featuresSize; i++) {
    const GLFeature* f = features.get(i);
    const GLFeatureGroupName group = f->_group;
    const GLFeatureID id = f->_id;
    if (group == CAMERA_GROUP) {
      GLCameraGroupFeature* cf = ((GLCameraGroupFeature*) f);

      if (id == GLF_MODEL_TRANSFORM) {
        modelTransformHolderBuilder.add(cf->getMatrixHolder());
      }
      else {
        modelViewHolderBuilder.add(cf->getMatrixHolder());
      }
    }
    else {
      if (group == LIGHTING_GROUP) {
        if (id == GLF_VERTEX_NORMAL) {
          normalsAvailable = true;
        }
      }
    }
  }

  if (modelTransformHolderBuilder.size() > 0) {
    Matrix44DProvider* prov = modelTransformHolderBuilder.create();
    modelViewHolderBuilder.add(prov);
    
    if (normalsAvailable) {
      vs.addUniformValue(MODEL,     //FOR LIGHTING
                         new GPUUniformValueMatrix4(prov),
                         false);
    }

    prov->_release();
  }

  Matrix44DProvider* modelViewProvider = modelViewHolderBuilder.create();

  vs.addUniformValue(MODELVIEW,
                     new GPUUniformValueMatrix4(modelViewProvider),
                     false);

  modelViewProvider->_release();
}

void GLFeatureLightingGroup::apply(const GLFeatureSet& features, GPUVariableValueSet& vs, GLGlobalState& state) {

  const int size = features.size();

  bool normalsAvailable = false;
  for(int i = 0; i < size; i++) {
    const GLFeature* f = features.get(i);
    if (f->_id == GLF_VERTEX_NORMAL) {
      normalsAvailable = true;
      break;
    }
  }


  if (normalsAvailable) {

    for(int i = 0; i < size; i++) {
      const GLFeature* f = features.get(i);

      if (f->_group == LIGHTING_GROUP) {
        f->applyOnGlobalGLState(&state);
        vs.combineWith(f->getGPUVariableValueSet());
      }
    }
  }
}
