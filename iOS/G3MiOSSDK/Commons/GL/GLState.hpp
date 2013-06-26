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
  
  static GLGlobalState _currentGPUGlobalState;
  static GPUProgram*   _currentGPUProgram;
  
  GPUProgramState* _programState;
  GLGlobalState*   _globalState;
  const bool _owner;
  
#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif
  
  void linkAndApplyToGPUProgram(GL* gl, GPUProgram* prog) const;
  
  explicit GLState(const GLState& state):
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL){}
  
public:
  
  GLState():
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()),
  _owner(true),
  _parentGLState(NULL){}
  
  //For debugging purposes only
  GLState(GLGlobalState*   globalState,
          GPUProgramState* programState):
  _programState(programState), _globalState(globalState), _owner(false), _parentGLState(NULL){}
  
  ~GLState(){
    if (_owner){
      delete _programState;
      delete _globalState;
    }
  }
  
  const GLState* getParent() const{
    return _parentGLState;
  }
  
  void setParent(const GLState* p) const{
    _parentGLState = p;
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
};

#endif /* defined(__G3MiOSSDK__GLState__) */
