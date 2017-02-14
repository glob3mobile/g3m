//
//  GLState.hpp
//  G3MiOSSDK
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include "RCObject.hpp"

#include "GLFeature.hpp"

class GLFeatureSet;
class GPUVariableValueSet;
class GLGlobalState;
class GPUProgram;
class GL;
class GPUProgramManager;


class GLState: public RCObject {
private:
  GLFeatureSet* _features;
  mutable GLFeatureSet* _accumulatedFeatures;

  mutable int _timestamp;
  mutable int _parentsTimestamp;

  mutable GPUVariableValueSet* _valuesSet;
  mutable GLGlobalState*   _globalState;

  mutable GPUProgram* _linkedProgram;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif

  GLState(const GLState& state);

  void hasChangedStructure() const;

  ~GLState();

public:

  GLState():
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

  int getTimestamp() const { return _timestamp; }

  GLFeatureSet* getAccumulatedFeatures() const;

  void setParent(const GLState* p) const;

  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;

  void addGLFeature(GLFeature* f,
                    bool mustRetain);

  void clearGLFeatureGroup(GLFeatureGroupName g);

  void clearAllGLFeatures();

  int getNumberOfGLFeatures() const;

  GLFeature* getGLFeature(GLFeatureID id) const;

  GLFeatureSet* getGLFeatures(GLFeatureID id) const;
};

#endif
