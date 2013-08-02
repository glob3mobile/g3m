//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "GLState.hpp"

GLState::~GLState() {
  for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
    delete _featuresGroups[i];
    delete _accumulatedGroups[i];
  }

  delete _valuesSet;
  delete _globalState;
}

void GLState::setParent(const GLState* parent) const{

  if ((parent != _parentGLState) ||
      (parent == NULL)           ||
      (_parentsTimeStamp != parent->getTimeStamp())) {

    _parentGLState    = parent;
    _parentsTimeStamp = (_parentGLState == NULL) ? 0 : _parentGLState->getTimeStamp();

    hasChangedStructure();
  }
//  else {
//    ILogger::instance()->logInfo("Reusing GLState Parent");
//  }

}

void GLState::applyStates(GL* gl, GPUProgram* prog) const{
  if (_parentGLState != NULL) {
    _parentGLState->applyStates(gl, prog);
  }
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) const{

  if (_valuesSet == NULL) {
    _valuesSet = new GPUVariableValueSet();
    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
      GLFeatureGroup* group = getAccumulatedGroup(i);
      if (group != NULL) {
        group->addToGPUVariableSet(_valuesSet);
      }
    }

    const int uniformsCode = _valuesSet->getUniformsCode();
    const int attributesCode = _valuesSet->getAttributesCode();

    _lastGPUProgramUsed = progManager.getProgram(gl, uniformsCode, attributesCode);
  }

  if (_globalState == NULL) {
    _globalState = new GLGlobalState();
    for (int i = 0; i < N_GLFEATURES_GROUPS; i++) {
      GLFeatureGroup* group = getAccumulatedGroup(i);
      if (group != NULL) {
        group->applyOnGlobalGLState(_globalState);
      }
    }
  }

  if (_lastGPUProgramUsed != NULL) {
    gl->useProgram(_lastGPUProgramUsed);
    applyStates(gl, _lastGPUProgramUsed);

    _valuesSet->applyValuesToProgram(_lastGPUProgramUsed);
    _globalState->applyChanges(gl, *gl->getCurrentGLGlobalState());

    _lastGPUProgramUsed->applyChanges(gl);

    //prog->onUnused(); //Uncomment to check that all GPUProgramStates are complete
  }
  else {
    ILogger::instance()->logError("No GPUProgram found.");
  }

}

void GLState::clearGLFeatureGroup(GLFeatureGroupName g) {
  int __ASK_JM; // replace clear with replaceFeature
#ifdef C_CODE
  const int index = g;
#endif
#ifdef JAVA_CODE
  final int index = g.getValue();
#endif

  GLFeatureGroup* group = _featuresGroups[index];
  if (group != NULL) {
    delete group;
    _featuresGroups[index] = NULL;
  }

  hasChangedStructure();
}

