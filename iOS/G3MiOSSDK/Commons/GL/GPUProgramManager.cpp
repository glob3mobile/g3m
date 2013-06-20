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
    std::vector<std::string>* ui = thisGLState->getGPUProgramState()->getUniformsNames();
    int sizeI = ui->size();
    for (int j = 0; j < sizeI; j++) {
      std::string& name = ui->at(j);
      
      if (name.compare("uViewPortExtent") == 0){
        billboard = true;
      }
      
      if (name.compare("uFlatColor") == 0){
        flatColor = true;
      }
      
      if (name.compare("TranslationTexCoord")==0){
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