//
//  GLState.cpp
//  G3MiOSSDK
//

#include "GLState.hpp"

#include "GPUProgramManager.hpp"
#include "ILogger.hpp"


GLState::GLState():
_parentGLState(NULL),
_linkedProgram(NULL),
_parentsTimestamp(-1),
_timestamp(0),
_valuesSet(NULL),
_globalState(NULL),
_accumulatedFeatures(NULL)
{
  _features = new GLFeatureSet();
}

GLState::~GLState() {
  delete _features;
  delete _accumulatedFeatures;

  delete _valuesSet;
  delete _globalState;

  if (_parentGLState != NULL) {
    _parentGLState->_release();
  }

  if (_linkedProgram != NULL) {
    _linkedProgram->removeReference();
  }
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GLState::hasChangedStructure() const {
  _timestamp++;
  delete _valuesSet;
  _valuesSet = NULL;
  delete _globalState;
  _globalState = NULL;

  if (_linkedProgram != NULL) {
    _linkedProgram->removeReference();
    _linkedProgram = NULL;
  }

  delete _accumulatedFeatures;
  _accumulatedFeatures = NULL;
}

GLFeatureSet* GLState::getAccumulatedFeatures() const {
  if (_accumulatedFeatures == NULL) {

    _accumulatedFeatures = new GLFeatureSet();

    if (_parentGLState != NULL) {
      GLFeatureSet* parents = _parentGLState->getAccumulatedFeatures();
      if (parents != NULL) {
        _accumulatedFeatures->add(parents);
      }
    }
    _accumulatedFeatures->add(_features);

  }
  return _accumulatedFeatures;
}

void GLState::addGLFeature(GLFeature* f,
                           bool mustRetain) {
  _features->add(f);

  if (!mustRetain) {
    f->_release();
  }

  hasChangedStructure();
}

void GLState::setParent(const GLState* parent) const {

  if (parent == NULL) {
    if (parent != _parentGLState) {
      _parentGLState    = NULL;
      _parentsTimestamp = -1;
      hasChangedStructure();
    }
  }
  else {
    const int parentsTimestamp = parent->getTimestamp();
    if ((parent != _parentGLState) ||
        (_parentsTimestamp != parentsTimestamp)) {

      if (_parentGLState != parent) {
        if (_parentGLState != NULL) {
          _parentGLState->_release();
        }
        _parentGLState    = parent;
        _parentGLState->_retain();
      }

      _parentsTimestamp = parentsTimestamp;
      hasChangedStructure();
    }
  }
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const {


  if (_valuesSet == NULL && _globalState == NULL) {

    _valuesSet   = new GPUVariableValueSet();
    _globalState = new GLGlobalState();

    GLFeatureSet* accumulatedFeatures = getAccumulatedFeatures();

    GLFeatureGroup::applyToAllGroups(*accumulatedFeatures, *_valuesSet, *_globalState);

    const int uniformsCode   = _valuesSet->getUniformsCode();
    const int attributesCode = _valuesSet->getAttributesCode();

    _linkedProgram = progManager.getProgram(gl, uniformsCode, attributesCode); //GET RETAINED REFERENCE
  }

  if (_valuesSet == NULL || _globalState == NULL) {
    ILogger::instance()->logError("GLState logic error.");
    return;
  }

  if (_linkedProgram != NULL) {
    gl->useProgram(_linkedProgram);

    _valuesSet->applyValuesToProgram(_linkedProgram);
    _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());

    _linkedProgram->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  }
  else {
    ILogger::instance()->logError("No GPUProgram found.");
  }

}

void GLState::clearGLFeatureGroup(GLFeatureGroupName g) {
  _features->clearFeatures(g);
  hasChangedStructure();
}

void GLState::clearAllGLFeatures() {
  _features->clearFeatures();
  hasChangedStructure();
}

int GLState::getNumberOfGLFeatures() const {
  return _features->size();
}

GLFeature* GLState::getGLFeature(GLFeatureID id) const {
  const int size = _features->size();
  for (int i = 0; i < size; i++) {
    GLFeature* f = _features->get(i);
    if (f->_id == id) {
      return f;
    }
  }

  return NULL;
}

GLFeatureSet* GLState::getGLFeatures(GLFeatureID id) const {
  GLFeatureSet* features = new GLFeatureSet();
  const int size = _features->size();
  for (int i = 0; i < size; i++) {
    GLFeature* f = _features->get(i);
    if (f->_id == id) {
      features->add(f);
    }
  }
  return features;
}
