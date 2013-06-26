//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#include "GPUProgram.hpp"

#include "GL.hpp"
#include "GPUAttribute.hpp"
#include "GPUUniform.hpp"

GPUProgram* GPUProgram::createProgram(GL* gl, const std::string name, const std::string& vertexSource,
                                      const std::string& fragmentSource){
  
  GPUProgram* p = new GPUProgram();
  
  p->_name = name;
  
  p->_programCreated = false;
  //p->_nativeGL = gl->getNative();
  p->_programID = gl->createProgram();
  
  // compile vertex shader
  int vertexShader= gl->createShader(VERTEX_SHADER);
  if (!p->compileShader(gl, vertexShader, vertexSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader\n");
    p->deleteShader(gl, vertexShader);
    p->deleteProgram(gl, p->_programID);
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader");
    return NULL;
  }
  
  ILogger::instance()->logInfo("VERTEX SOURCE: \n %s", vertexSource.c_str());
  
  // compile fragment shader
  int fragmentShader = gl->createShader(FRAGMENT_SHADER);
  if (!p->compileShader(gl, fragmentShader, fragmentSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader\n");
    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p->_programID);
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader");
    return NULL;
  }
  
  ILogger::instance()->logInfo("FRAGMENT SOURCE: \n %s", fragmentSource.c_str());
  
  //gl->bindAttribLocation(p, 0, GPUVariable::POSITION);
  
  // link program
  if (!p->linkProgram(gl)) {
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
    p->deleteShader(gl, vertexShader);
    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p->_programID);
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program");
    return NULL;
  }
  
  // free shaders
  p->deleteShader(gl, vertexShader);
  p->deleteShader(gl, fragmentShader);
  
  p->getVariables(gl);
  
  if (gl->getError() != GLError::noError()){
    ILogger::instance()->logError("Error while compiling program");
  }
  
  return p;
}


GPUProgram::~GPUProgram(){
}

bool GPUProgram::linkProgram(GL* gl) const {
  bool result = gl->linkProgram(_programID);
  //#if defined(DEBUG)
  //  _nativeGL->printProgramInfoLog(_programID);
  //#endif
  return result;
}

bool GPUProgram::compileShader(GL* gl, int shader, const std::string& source) const{
  bool result = gl->compileShader(shader, source);
  
  //#if defined(DEBUG)
  //  _nativeGL->printShaderInfoLog(shader);
  //#endif
  
  if (result){
    gl->attachShader(_programID, shader);
  } else{
    ILogger::instance()->logError("GPUProgram: Problem encountered while compiling shader.");
  }
  
  return result;
}

void GPUProgram::deleteShader(GL* gl, int shader) const{
  if (!gl->deleteShader(shader)){
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting shader.");
  }
}

void GPUProgram::deleteProgram(GL* gl, int p){
  if (!gl->deleteProgram(p)){
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
  }
  _programCreated = false;
}

void GPUProgram::getVariables(GL* gl){
  
  //Uniforms
  int n = gl->getProgramiv(this, GLVariable::activeUniforms());
  for (int i = 0; i < n; i++) {
    GPUUniform* u = gl->getActiveUniform(this, i);
    if (u != NULL) _uniforms[u->getKey()] = u;
  }
  
  //Attributes
  n = gl->getProgramiv(this, GLVariable::activeAttributes());
  for (int i = 0; i < n; i++) {
    GPUAttribute* a = gl->getActiveAttribute(this, i);
    if (a != NULL) _attributes[a->getKey()] = a;
  }
  
}

GPUUniform* GPUProgram::getGPUUniform(const std::string name) const{
  int key = GPUVariable::getKeyForName(name, UNIFORM);
  
#ifdef C_CODE
  std::map<int, GPUUniform*>::const_iterator it = _uniforms.find(key);
//  if (it != _uniforms.end()){
//    return it->second;
//  } else{
//    return NULL;
//  }
  
  return (it == _uniforms.end()) ? NULL : it->second;
  
#endif
#ifdef JAVA_CODE
  return _uniforms.get(name);
#endif
}

GPUUniformBool* GPUProgram::getGPUUniformBool(const std::string name) const {
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->getType() == GLType::glBool()){
    return (GPUUniformBool*)u;
  } else{
    return NULL;
  }
}

GPUUniformVec2Float* GPUProgram::getGPUUniformVec2Float(const std::string name) const {
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec2Float()){
    return (GPUUniformVec2Float*)u;
  } else{
    return NULL;
  }
}

GPUUniformVec4Float* GPUProgram::getGPUUniformVec4Float(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec4Float()){
    return (GPUUniformVec4Float*)u;
  } else{
    return NULL;
  }
}

GPUUniformFloat* GPUProgram::getGPUUniformFloat(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->getType() == GLType::glFloat()){
    return (GPUUniformFloat*)u;
  } else{
    return NULL;
  }
}

GPUUniformMatrix4Float* GPUProgram::getGPUUniformMatrix4Float(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->getType() == GLType::glMatrix4Float()){
    return (GPUUniformMatrix4Float*)u;
  } else{
    return NULL;
  }
}

GPUAttribute* GPUProgram::getGPUAttribute(const std::string name) const{
  const int key = GPUVariable::getKeyForName(name, ATTRIBUTE);
#ifdef C_CODE
  std::map<int, GPUAttribute*>::const_iterator it = _attributes.find(key);
  if (it != _attributes.end()){
    return it->second;
  } else{
    return NULL;
  }
#endif
#ifdef JAVA_CODE
  return _attributes.get(name);
#endif
}

GPUAttribute* GPUProgram::getGPUAttributeVecXFloat(const std::string name, int x) const{
  switch (x) {
    case 1:
      return getGPUAttributeVec1Float(name);
    case 2:
      return getGPUAttributeVec2Float(name);
    case 3:
      return getGPUAttributeVec3Float(name);
    case 4:
      return getGPUAttributeVec4Float(name);
    default:
      return NULL;
  }
}

GPUAttributeVec1Float* GPUProgram::getGPUAttributeVec1Float(const std::string name) const{
  GPUAttributeVec1Float* a = (GPUAttributeVec1Float*)getGPUAttribute(name);
  if (a!= NULL && a->getSize() == 1 && a->getType() == GLType::glFloat()){
    return (GPUAttributeVec1Float*)a;
  } else{
    return NULL;
  }
}

GPUAttributeVec2Float* GPUProgram::getGPUAttributeVec2Float(const std::string name) const{
  GPUAttributeVec2Float* a = (GPUAttributeVec2Float*)getGPUAttribute(name);
  if (a!= NULL && a->getSize() == 2 && a->getType() == GLType::glFloat()){
    return (GPUAttributeVec2Float*)a;
  } else{
    return NULL;
  }
}

GPUAttributeVec3Float* GPUProgram::getGPUAttributeVec3Float(const std::string name) const{
  GPUAttributeVec3Float* a = (GPUAttributeVec3Float*)getGPUAttribute(name);
  if (a!= NULL && a->getSize() == 3 && a->getType() == GLType::glFloat()){
    return (GPUAttributeVec3Float*)a;
  } else{
    return NULL;
  }
}

GPUAttributeVec4Float* GPUProgram::getGPUAttributeVec4Float(const std::string name) const{
  GPUAttributeVec4Float* a = (GPUAttributeVec4Float*)getGPUAttribute(name);
  if (a!= NULL && a->getSize() == 4 && a->getType() == GLType::glFloat()){
    return (GPUAttributeVec4Float*)a;
  } else{
    return NULL;
  }
}

/**
 Must be called when the program is used
 */
void GPUProgram::onUsed(){
  //  ILogger::instance()->logInfo("GPUProgram %s being used", _name.c_str());
}
/**
 Must be called when the program is no longer used
 */
void GPUProgram::onUnused(GL* gl){
  //ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());
#ifdef C_CODE
  for (std::map<int, GPUUniform*>::iterator iter = _uniforms.begin(); iter != _uniforms.end(); iter++) {
    GPUUniform* u = iter->second;
    u->unset();
  }
  
  for (std::map<int, GPUAttribute*>::iterator iter = _attributes.begin(); iter != _attributes.end(); iter++) {
    GPUAttribute* a = iter->second;
    a->unset(gl);
  }
#endif
#ifdef JAVA_CODE
  for (final GPUUniform uni : _uniforms.values()) {
    uni.unset();
  }
  
  for (final GPUAttribute att : _attributes.values()) {
    att.unset(gl);
  }
#endif
}

/**
 Must be called before drawing to apply Uniforms and Attributes new values
 */
void GPUProgram::applyChanges(GL* gl){
  //ILogger::instance()->logInfo("GPUProgram %s applying changes", _name.c_str());
#ifdef C_CODE
  for (std::map<int, GPUUniform*>::iterator iter = _uniforms.begin(); iter != _uniforms.end(); iter++) {
    GPUUniform* u = iter->second;
    u->applyChanges(gl);
  }
  
  for (std::map<int, GPUAttribute*>::iterator iter = _attributes.begin(); iter != _attributes.end(); iter++) {
    GPUAttribute* a = iter->second;
    a->applyChanges(gl);
  }
#endif
#ifdef JAVA_CODE
  for (final GPUUniform u : _uniforms.values()){
    if (u.wasSet()){
      u.applyChanges(gl);
    } else{
      ILogger.instance().logError("Uniform " + u.getName() + " was not set.");
    }
  }
  
  for (final GPUAttribute a : _attributes.values()) {
    if (a.wasSet()){
      a.applyChanges(gl);
    } else{
      if (a.isEnabled()){
        ILogger.instance().logError("Attribute " + a.getName() + " was not set but it is enabled.");
      }
    }
  }
#endif
}

GPUUniform* GPUProgram::getUniformOfType(const std::string& name, int type) const{
  GPUUniform* u = NULL;
  if (type == GLType::glBool()){
    u = getGPUUniformBool(name);
  } else {
    if (type == GLType::glVec2Float()){
      u = getGPUUniformVec2Float(name);
    } else{
      if (type == GLType::glVec4Float()){
        u = getGPUUniformVec4Float(name);
      } else{
        if (type == GLType::glFloat()){
          u = getGPUUniformFloat(name);
        } else
          if (type == GLType::glMatrix4Float()){
            u = getGPUUniformMatrix4Float(name);
          }
      }
    }
  }
  return u;
}

GPUUniform* GPUProgram::getGPUUniform(int key) const{
#ifdef C_CODE
  std::map<int, GPUUniform*>::const_iterator it = _uniforms.find(key);
  if (it != _uniforms.end()){
    return it->second;
  } else{
    return NULL;
  }
#endif
#ifdef JAVA_CODE
  return _uniforms.get(key);
#endif
}

GPUAttribute* GPUProgram::getGPUAttribute(int key) const{
#ifdef C_CODE
  std::map<int, GPUAttribute*>::const_iterator it = _attributes.find(key);
  if (it != _attributes.end()){
    return it->second;
  } else{
    return NULL;
  }
#endif
#ifdef JAVA_CODE
  return _attributes.get(key);
#endif
}

GPUAttribute* GPUProgram::getGPUAttributeVecXFloat(int key, int x) const{
  
  GPUAttribute* a = getGPUAttribute(key);
  if (a->getType() == GLType::glFloat() && a->getSize() == x){
    return a;
  }
  return NULL;
}