//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#include "GPUProgramManager.hpp"

#include "GLState.hpp"

GPUProgram* GPUProgramManager::getProgram(GLState* const glState) {
  GLState* thisGLState = glState;
  while (thisGLState != NULL) {
    std::vector<std::string>* ui = glState->getGPUProgramState()->getUniformsNames();
    int sizeI = ui->size();
    for (int j = 0; j < sizeI; j++) {
      std::string& name = ui->at(j);
      
      if (name.compare("uViewPortExtent") == 0){
        return getProgram("Billboard");
      }
    }
    
    
    thisGLState = thisGLState->getParent();
  }
  
  int WORKING_JM;
  return getProgram("Default");
}