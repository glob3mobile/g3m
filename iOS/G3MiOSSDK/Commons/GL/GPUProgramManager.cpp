//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#include "GPUProgramManager.hpp"

#include "GLState.hpp"

GPUProgram* GPUProgramManager::getNewProgram(GL* gl, int uniformsCode, int attributesCode) {

  bool texture = GPUVariable::codeContainsAttribute(attributesCode, TEXTURE_COORDS);
  bool flatColor = GPUVariable::codeContainsUniform(uniformsCode, FLAT_COLOR);
  bool billboard = GPUVariable::codeContainsUniform(uniformsCode, VIEWPORT_EXTENT);
  bool color = GPUVariable::codeContainsAttribute(attributesCode, COLOR);
  bool transformTC = GPUVariable::codeContainsUniform(uniformsCode, TRANSLATION_TEXTURE_COORDS) ||
  GPUVariable::codeContainsUniform(uniformsCode, SCALE_TEXTURE_COORDS);

  /*
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

   if (key == VIEWPORT_EXTENT) {
   billboard = true;

   if (!GPUVariable::codeContainsUniform(uniformsCode, VIEWPORT_EXTENT)) {
   int a = 0;
   a++;
   }

   }

   if (key == FLAT_COLOR) {
   flatColor = true;
   }

   //      if (key == TRANSLATION_TEXTURE_COORDS) {
   //        texture = true;
   //      }

   if (key == TRANSLATION_TEXTURE_COORDS || key == SCALE_TEXTURE_COORDS) {
   transformTC = true;
   }
   }

   std::vector<int>* ai = thisGLState->getGPUProgramState()->getAttributeKeys();
   sizeI = ai->size();
   for (int j = 0; j < sizeI; j++) {
   int key = ai->at(j);

   //      if (key == TEXTURE_COORDS) {
   //        texture = true;
   //      }

   if (key == COLOR) {
   color = true;

   if (!GPUVariable::codeContainsAttribute(attributesCode, COLOR)) {
   int a = 0;
   a++;
   }
   }
   }

   thisGLState = thisGLState->getParent();
   }
   */
  if (billboard) {
    return getProgram(gl, "Billboard");
  }
  if (flatColor && !texture && !color) {
    return getProgram(gl, "FlatColorMesh");
  }

  if (!flatColor && texture && !color) {
    if (transformTC) {
      return getProgram(gl, "TransformedTexCoorTexturedMesh");
    }
    return getProgram(gl, "TexturedMesh");
  }

  if (!flatColor && !texture && color) {
    return getProgram(gl, "ColorMesh");
  }

  return NULL;
}

GPUProgram* GPUProgramManager::getCompiledProgram(int uniformsCode, int attributesCode) {
#ifdef C_CODE
  for (std::map<std::string, GPUProgram*>::iterator it = _programs.begin(); it != _programs.end(); ++it) {
    GPUProgram* p = it->second;
    if (p->getUniformsCode() == uniformsCode && p->getAttributesCode() == attributesCode) {
      return p;
    }
  }
#endif
#ifdef JAVA_CODE
  for (final GPUProgram p : _programs.values()) {
    if ((p.getUniformsCode() == uniformsCode) && (p.getAttributesCode() == attributesCode)) {
      return p;
    }
  }
#endif
  return NULL;
}
