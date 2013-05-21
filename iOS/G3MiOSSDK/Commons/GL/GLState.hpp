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
  
  void setProgramState(GL* gl, GPUProgramManager& progManager);
  
public:
  
  GLState():
  _programState(new GPUProgramState()),
  _globalState(new GLGlobalState()){}
  
  //For debugging purposes only
  GLState(GLGlobalState*   globalState,
          GPUProgramState* programState):_programState(programState), _globalState(globalState){}
  
  void applyGlobalStateOnGPU(GL* gl);
  
  void applyOnGPU(GL* gl, GPUProgramManager& progManager);
  
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
  
  static GLGlobalState createCopyOfCurrentGLGlobalState(){
    return GLGlobalState(_currentGPUGlobalState);
  }
  
//  static const GLGlobalState* getCurrentGLGlobalState() {
//    return &_currentGPUGlobalState;
//  }
  
//  static const GPUProgram* getGPUProgram() {
//    return _currentGPUProgram;
//  }
};

#endif /* defined(__G3MiOSSDK__GLState__) */
