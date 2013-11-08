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

  bool hasLight = GPUVariable::codeContainsUniform(uniformsCode, AMBIENT_LIGHT);

  if (billboard) {
    return getProgram(gl, "Billboard");
  }
  if (flatColor && !texture && !color) {

    if (hasLight) {
      return getProgram(gl, "FlatColorMesh+DirectionLight");
    }

    return getProgram(gl, "FlatColorMesh");
  }

  if (!flatColor && texture && !color) {
    if (transformTC) {
      return getProgram(gl, "TransformedTexCoorTexturedMesh");
    }

    if (hasLight) {
      return getProgram(gl, "TexturedMesh+DirectionLight");
    }

    return getProgram(gl, "TexturedMesh");
  }

  if (!flatColor && !texture && color) {
    return getProgram(gl, "ColorMesh");
  }

  if (!flatColor && !texture && !color) {
    return getProgram(gl, "NoColorMesh");
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
