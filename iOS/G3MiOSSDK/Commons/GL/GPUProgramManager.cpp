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
  
  std::vector<std::vector<std::string>*> uniforms;
  GLState* thisGLState = glState;
  while (thisGLState != NULL) {
    std::vector<std::string>* ui = glState->getGPUProgramState()->getUniformsNames();
    //uniforms.insert(uniforms.end(), ui->begin(), ui->end());
    uniforms.push_back(ui);
    thisGLState = thisGLState->getParent();
  }
  
  int WORKING_JM;
  
  int size = uniforms.size();
  for (int i = 0; i < size; i++) {
    
    int sizeI = uniforms[i]->size();
    for (int j = 0; j < sizeI; j++) {
      std::string& name = uniforms[i]->at(j);
      
      if (name.compare("ViewPortExtent") == 0){
        return getProgram("Billboard");
      }
    }
    
    
  }
  
  return getProgram("Default");
}