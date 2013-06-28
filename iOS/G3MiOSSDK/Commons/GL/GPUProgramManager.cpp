//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#include "GPUProgramManager.hpp"

#include "GLState.hpp"

GPUProgram* GPUProgramManager::getProgram(GL* gl, const GLState* glState) {
  
  bool texture = false;
  bool flatColor = false;
  bool billboard = false;
  bool color = false;
  bool transformTC = false;
  
#ifdef C_CODE
  const GLState* thisGLState = glState;
#endif
#ifdef JAVA_CODE
  GLState thisGLState = glState;
#endif
  while (thisGLState != NULL) {
    std::vector<int>* ui = thisGLState->getGPUProgramState()->getUniformsKeys();
    int sizeI = ui->size();
    for (int j = 0; j < sizeI; j++) {
      int key = ui->at(j);
      
      if (key == VIEWPORT_EXTENT){
        billboard = true;
      }
      
      if (key == FLAT_COLOR){
        flatColor = true;
      }
      
//      if (key == TRANSLATION_TEXTURE_COORDS){
//        texture = true;
//      }
      
      if (key == TRANSLATION_TEXTURE_COORDS || key == SCALE_TEXTURE_COORDS){
        transformTC = true;
      }
    }
    
    std::vector<int>* ai = thisGLState->getGPUProgramState()->getAttributeKeys();
    sizeI = ai->size();
    for (int j = 0; j < sizeI; j++) {
      int key = ai->at(j);
      
      if (key == TEXTURE_COORDS){
        texture = true;
      }
      
      if (key == COLOR){
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
      if (transformTC){
        return getProgram(gl, "TransformedTexCoorTexturedMesh");
      } else{
        return getProgram(gl, "TexturedMesh");
      }
    }
    
    if (!flatColor && !texture && color){
      return getProgram(gl, "ColorMesh");
    }
    
  }
  
  
  return NULL;
  
}