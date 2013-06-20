//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#include "GPUProgramManager.hpp"

#include "GLState.hpp"

GPUProgram* GPUProgramManager::getProgram(GL* gl, GLState* const glState) {
  
  bool texture = false;
  bool flatColor = false;
  bool billboard = false;
  
  GLState* thisGLState = glState;
  while (thisGLState != NULL) {
    std::vector<int>* ui = thisGLState->getGPUProgramState()->getUniformsKeys();
    int sizeI = ui->size();
    for (int j = 0; j < sizeI; j++) {
      int key = ui->at(j);
      
      if (key == GPUVariable::VIEWPORT_EXTENT){
        billboard = true;
      }
      
      if (key == GPUVariable::FLAT_COLOR){
        flatColor = true;
      }
      
      if (key == GPUVariable::TRANSLATION_TEXTURE_COORDS){
        texture = true;
      }
    }
    
    thisGLState = thisGLState->getParent();
  }
  
  if (billboard){
    return getProgram(gl, "Billboard");
  } else{
    if (flatColor && !texture){
      return getProgram(gl, "FlatColorMesh");
    } else{
      return getProgram(gl, "Default"); 
    }
  }
  
}