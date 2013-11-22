//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

#include "GPUProgramManager.hpp"

#include "GLState.hpp"

GPUProgramManager::~GPUProgramManager() {
#ifdef C_CODE
  delete _factory;
  for (std::map<std::string, GPUProgram*>::iterator it = _programs.begin(); it != _programs.end(); ++it) {
    delete it->second;
  }
#endif
}

GPUProgram* GPUProgramManager::getProgram(GL* gl, int uniformsCode, int attributesCode) {
  GPUProgram* p = getCompiledProgram(uniformsCode, attributesCode);
  if (p == NULL) {
    p = getNewProgram(gl, uniformsCode, attributesCode);

    if (p->getAttributesCode() != attributesCode ||
        p->getUniformsCode() != uniformsCode){
      ILogger::instance()->logError("New compiled program does not match GL state.");
    }

  }
  return p;
}

GPUProgram* GPUProgramManager::getNewProgram(GL* gl, int uniformsCode, int attributesCode) {

  bool texture = GPUVariable::codeContainsAttribute(attributesCode, TEXTURE_COORDS);
  bool flatColor = GPUVariable::codeContainsUniform(uniformsCode, FLAT_COLOR);
  bool billboard = GPUVariable::codeContainsUniform(uniformsCode, VIEWPORT_EXTENT);
  bool color = GPUVariable::codeContainsAttribute(attributesCode, COLOR);
  bool transformTC = GPUVariable::codeContainsUniform(uniformsCode, TRANSLATION_TEXTURE_COORDS) ||
  GPUVariable::codeContainsUniform(uniformsCode, SCALE_TEXTURE_COORDS);

  bool hasLight = GPUVariable::codeContainsUniform(uniformsCode, AMBIENT_LIGHT_COLOR);

  if (billboard) {
    return compileProgramWithName(gl, "Billboard");
  }
  if (flatColor && !texture && !color) {

    if (hasLight) {
      return compileProgramWithName(gl, "FlatColorMesh_DirectionLight");
    }

    return compileProgramWithName(gl, "FlatColorMesh");
  }

  if (!flatColor && texture && !color) {
    if (hasLight) {
      if (transformTC) {
        return compileProgramWithName(gl, "TransformedTexCoorTexturedMesh_DirectionLight");
      }
      return compileProgramWithName(gl, "TexturedMesh_DirectionLight");
    }

    if (transformTC) {
      return compileProgramWithName(gl, "TransformedTexCoorTexturedMesh");
    }
    return compileProgramWithName(gl, "TexturedMesh");
  }

  if (!flatColor && !texture && color) {
    return compileProgramWithName(gl, "ColorMesh");
  }

  if (!flatColor && !texture && !color) {
    return compileProgramWithName(gl, "NoColorMesh");
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

GPUProgram* GPUProgramManager::compileProgramWithName(GL* gl, const std::string& name) {

  GPUProgram* prog = getCompiledProgram(name);
  if (prog == NULL) {
    const GPUProgramSources* ps = _factory->get(name);

    //Compile new Program
    if (ps != NULL) {
      prog = GPUProgram::createProgram(gl,
                                       ps->_name,
                                       ps->_vertexSource,
                                       ps->_fragmentSource);
      if (prog == NULL) {
        ILogger::instance()->logError("Problem at creating program named %s.", name.c_str());
        return NULL;
      }

      _programs[name] = prog;
    }

  }
  return prog;
}

GPUProgram* GPUProgramManager::getCompiledProgram(const std::string& name) {
#ifdef C_CODE
  std::map<std::string, GPUProgram*>::iterator it = _programs.find(name);
  if (it != _programs.end()) {
    return it->second;
  } else{
    return NULL;
  }
#endif
#ifdef JAVA_CODE
  return _programs.get(name);
#endif
}
