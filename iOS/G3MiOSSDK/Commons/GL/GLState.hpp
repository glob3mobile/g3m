//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include <iostream>

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramState.hpp"
#include "GPUProgramManager.hpp"

class GLState{

  /////////////////////////////////////////////////
  //CURRENT GL STATUS
  static GLGlobalState _currentGPUGlobalState;
  static GPUProgram*   _currentGPUProgram;
  /////////////////////////////////////////////////
  
  GPUProgramState* _programState;
  GLGlobalState*   _globalState;
  const bool _owner;

  mutable int _uniformsCode;
  mutable int _attributesCode;
  mutable bool _totalGPUProgramStateChanged;
  mutable GPUProgram* _lastGPUProgramUsed;

#ifdef C_CODE
  const Matrix44D* _modelview;
  mutable const Matrix44D* _accumulatedModelview;
  mutable const Matrix44D* _lastParentModelview;
#endif
#ifdef JAVA_CODE
  private Matrix44D _modelview;
  private Matrix44D _accumulatedModelview;
  private Matrix44D _lastParentModelview;
#endif
  bool _multiplyModelview;

//  class ParentModelviewListener: public Matrix44DListener{
//    const GLState* _state;
//  public:
//    ParentModelviewListener(GLState* state):_state(state){}
//    void onMatrixBeingDeleted(const Matrix44D* m){
//      //ILogger::instance()->logError("BORRADO");
//      _state->_lastParentModelview = NULL;
//    }
//  };
//  mutable ParentModelviewListener _parentMatrixListener;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif
  
  void applyStates(GL* gl, GPUProgram* prog) const;
 
//  explicit GLState(const GLState& state):
//  _programState(new GPUProgramState()),
//  _globalState(new GLGlobalState()),
//  _owner(true),
//  _parentGLState(NULL),
//  _uniformsCode(0),
//  _attributesCode(0),
//  _totalGPUProgramStateChanged(true),
//  _modelview(new Matrix44D(*state._modelview)),
//  _accumulatedModelview(new Matrix44D(*state._accumulatedModelview)),
//  _multiplyModelview(state._multiplyModelview),
//  _lastParentModelview(new Matrix44D(*state._lastParentModelview))
//  {
//    
//  }
  GLState(const GLState& state);

public:
  
  GLState():
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(NULL),
  _accumulatedModelview(NULL),
  _multiplyModelview(false),
  _lastParentModelview(NULL){}
  
  //For debugging purposes only
  GLState(GLGlobalState*   globalState,
          GPUProgramState* programState):
  _programState(programState),
  _globalState(globalState),
  _owner(false),
  _parentGLState(NULL),
  _uniformsCode(0),
  _attributesCode(0),
  _totalGPUProgramStateChanged(true),
  _modelview(NULL),
  _accumulatedModelview(NULL),
  _multiplyModelview(false),
//    _parentMatrixListener(this),
  _lastParentModelview(NULL){}
  
  ~GLState();
  
  const GLState* getParent() const{
    return _parentGLState;
  }
  
  void setParent(const GLState* p) const;

  int getUniformsCode() const{
    if (_uniformsCode == 0){
      return _programState->getUniformsCode();
    }
    return _uniformsCode;
  }

  int getAttributesCode() const{
    if (_attributesCode == 0){
      return _programState->getAttributesCode();
    }
    return _attributesCode;
  }

  void applyGlobalStateOnGPU(GL* gl) const;
  
  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;
  
  GPUProgramState* getGPUProgramState() const{
    return _programState;
  }
  
  GLGlobalState* getGLGlobalState() const{
    return _globalState;
  }
  
  static void textureHasBeenDeleted(const IGLTextureId* textureId){
    if (_currentGPUGlobalState.getBoundTexture() == textureId){
      _currentGPUGlobalState.bindTexture(NULL);
    }
  }
  
  static GLGlobalState* createCopyOfCurrentGLGlobalState(){
    return _currentGPUGlobalState.createCopy();
  }

  void setModelView(const Matrix44D* modelview, bool modifiesParents);
  const Matrix44D* getAccumulatedModelView() const;
};

#endif /* defined(__G3MiOSSDK__GLState__) */
