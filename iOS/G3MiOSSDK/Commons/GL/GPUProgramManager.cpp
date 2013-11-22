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
  for (std::map<std::string, GPUProgramData>::iterator it = _programs.begin(); it != _programs.end(); ++it) {
    delete it->second._program;
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
  for (std::map<std::string, GPUProgramData>::iterator it = _programs.begin(); it != _programs.end(); ++it) {
    GPUProgram* p = it->second._program;
    if (p->getUniformsCode() == uniformsCode && p->getAttributesCode() == attributesCode) {
      it->second._usedSinceLastCleanUp = true; //Marked as used
      return p;
    }
  }
#endif
#ifdef JAVA_CODE
  for (final GPUProgramData pd : _programs.values()) {
    GPUProgram p = pd._program;
    if ((p.getUniformsCode() == uniformsCode) && (p.getAttributesCode() == attributesCode)) {
      pd._usedSinceLastCleanUp = true; //Marked as used
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

      GPUProgramData pd;
      pd._program = prog;
      pd._usedSinceLastCleanUp = true;

      _programs[name] = pd;
    }

  }
  return prog;
}

GPUProgram* GPUProgramManager::getCompiledProgram(const std::string& name) {
#ifdef C_CODE
  std::map<std::string, GPUProgramData>::iterator it = _programs.find(name);
  if (it != _programs.end()) {
    return it->second._program;
  } else{
    return NULL;
  }
#endif
#ifdef JAVA_CODE
  return _programs.get(name);
#endif
}

void GPUProgramManager::deleteUnusedPrograms(){
#ifdef C_CODE

  std::map<std::string, GPUProgramData>::iterator it = _programs.begin();
  while (it != _programs.end()) {
    bool shouldRemove = !(it->second._usedSinceLastCleanUp);
    if (shouldRemove){
      ILogger::instance()->logInfo("Removing program %s because it hasn't been used in last frames.",
                                   it->second._program->getName().c_str());
      delete it->second._program;
      _programs.erase(it++);
    } else{
      it->second._usedSinceLastCleanUp = false; //Marking as unused
      ++it;
    }
  }

#endif
#ifdef JAVA_CODE
  Iterator<Object> it = _programs.EntrySet().iterator();
  while (it.hasNext())
  {
    GPUProgramData pd = it.next();
    bool shouldRemove = !(pd._usedSinceLastCleanUp);
    if (shouldRemove){
      ILogger.instance().logInfo("Removing program %s because it hasn't been used in last frames.",
                                   it.second._program.getName());
      pd._program.dispose();
      it.remove();
    } else{
      pd._usedSinceLastCleanUp = false; //Marking as unused
    }
  }
#endif
}
