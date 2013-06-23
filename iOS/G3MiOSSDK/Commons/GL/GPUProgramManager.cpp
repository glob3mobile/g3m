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
  bool color = false;
  
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
    
    std::vector<int>* ai = thisGLState->getGPUProgramState()->getAttributeKeys();
    sizeI = ai->size();
    for (int j = 0; j < sizeI; j++) {
      int key = ui->at(j);
      
//      if (key == GPUVariable::TEXTURE_COORDS){
//        color = true;
//      }
      
      if (key == GPUVariable::COLOR){
        color = true;
      }
    }
    
    thisGLState = thisGLState->getParent();
  }
  
  if (billboard){
    return getProgram(gl, "Billboard");
  } else{
    if (flatColor && !texture && !color){
      return getProgram(gl, "FlatColorMesh");
    }
   
    if (!flatColor && texture && !color){
      return getProgram(gl, "TexturedMesh");
    }
    
    if (!flatColor && !texture && color){
      return getProgram(gl, "ColorMesh");
    }
    
    if (!flatColor && !texture && !color){
      return NULL;   //Shapes with texture not loaded yet
    }
    
  }
  
  
  return getProgram(gl, "Default"); 
  
}