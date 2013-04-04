//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#include "GPUProgram.hpp"

#include "GL.hpp"

#include "G3MError.hpp"

#include "ShaderProgram.hpp"

GPUProgram* GPUProgram::createProgram(GL* gl, const std::string name, const std::string& vertexSource,
                                      const std::string& fragmentSource){
  
  GPUProgram* p = new GPUProgram();
  
  p->_name = name;
  
  p->_programCreated = false;
  p->_nativeGL = gl->getNative();
  p->_programID = p->_nativeGL->createProgram();
  
  // compile vertex shader
  int vertexShader= p->_nativeGL->createShader(VERTEX_SHADER);
  if (!p->compileShader(vertexShader, vertexSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader\n");
    p->deleteShader(vertexShader);
    p->deleteProgram(p->_programID);
    throw new G3MError("GPUProgram: ERROR compiling vertex shader");
    return NULL;
  }
  
  // compile fragment shader
  int fragmentShader = p->_nativeGL->createShader(FRAGMENT_SHADER);
  if (!p->compileShader(fragmentShader, fragmentSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader\n");
    p->deleteShader(fragmentShader);
    p->deleteProgram(p->_programID);
    throw new G3MError("GPUProgram: ERROR compiling fragment shader");
    return NULL;
  }
  
  p->_nativeGL->bindAttribLocation(p, 0, "Position");
  
  // link program
  if (!p->linkProgram()) {
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
    p->deleteShader(vertexShader);
    p->deleteShader(fragmentShader);
    p->deleteProgram(p->_programID);
    throw new G3MError("GPUProgram: ERROR linking graphic program");
    return NULL;
  }
  
  // free shaders
  p->deleteShader(vertexShader);
  p->deleteShader(fragmentShader);
  
  p->getVariables();
  
  return p;
}


GPUProgram::~GPUProgram(){
}

bool GPUProgram::linkProgram() const {
  bool result = _nativeGL->linkProgram(_programID);
#if defined(DEBUG)
  _nativeGL->printProgramInfoLog(_programID);
#endif
  return result;
}

bool GPUProgram::compileShader(int shader, const std::string& source) const{
  bool result = _nativeGL->compileShader(shader, source);
  
#if defined(DEBUG)
  _nativeGL->printShaderInfoLog(shader);
#endif
  
  if (result){
    _nativeGL->attachShader(_programID, shader);
  } else{
    ILogger::instance()->logError("GPUProgram: Problem encountered while compiling shader.");
  }
  
  return result;
}

void GPUProgram::deleteShader(int shader) const{
  if (!_nativeGL->deleteShader(shader)){
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting shader.");
  }
}

void GPUProgram::deleteProgram(int p){
  if (!_nativeGL->deleteProgram(p)){
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
  }
  _programCreated = false;
}

void GPUProgram::getVariables(){
  
  //Uniforms
  int n = _nativeGL->getProgramiv(this, GLVariable::activeUniforms());
  for (int i = 0; i < n; i++) {
    Uniform* u = _nativeGL->getActiveUniform(this, i);
    if (u != NULL) _uniforms[u->getName()] = u;
  }
  
  //Attributes
  n = _nativeGL->getProgramiv(this, GLVariable::activeAttributes());
  for (int i = 0; i < n; i++) {
    Attribute* a = _nativeGL->getActiveAttribute(this, i);
    if (a != NULL) _attributes[a->getName()] = a;
  }
  
}

Uniform* GPUProgram::getUniform(const std::string name) const{
  std::map<std::string, Uniform*> ::const_iterator it = _uniforms.find(name);
  if (it != _uniforms.end()){
    return it->second;
  } else{
    return NULL;
  }
}

UniformBool* GPUProgram::getUniformBool(const std::string name) const{
  Uniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glBool()){
    return (UniformBool*)u;
  } else{
    return NULL;
  }
}

UniformVec2Float* GPUProgram::getUniformVec2Float(const std::string name) const {
  Uniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec2Float()){
    return (UniformVec2Float*)u;
  } else{
    return NULL;
  }
}
UniformVec4Float* GPUProgram::getUniformVec4Float(const std::string name) const{
  Uniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec4Float()){
    return (UniformVec4Float*)u;
  } else{
    return NULL;
  }
}
UniformFloat* GPUProgram::getUniformFloat(const std::string name) const{
  Uniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glFloat()){
    return (UniformFloat*)u;
  } else{
    return NULL;
  }
}

UniformMatrix4Float* GPUProgram::getUniformMatrix4Float(const std::string name) const{
  Uniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glMatrix4Float()){
    return (UniformMatrix4Float*)u;
  } else{
    return NULL;
  }
}

Attribute* GPUProgram::getAttribute(const std::string name) const{
  std::map<std::string, Attribute*> ::const_iterator it = _attributes.find(name);
  if (it != _attributes.end()){
    return it->second;
  } else{
    return NULL;
  }
}

/**
 Must be called when the program is used
 */
void GPUProgram::onUsed(){
  ILogger::instance()->logInfo("GPUProgram %s being used", _name.c_str());
}
/**
 Must be called when the program is no longer used
 */
void GPUProgram::onUnused(){
    ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());
}

/**
 Must be called before drawing to apply Uniforms and Attributes new values
 */
void GPUProgram::applyChanges(GL* gl){
  //ILogger::instance()->logInfo("GPUProgram %s applying changes", _name.c_str());
  
  std::map<std::string, Uniform*>::iterator iter;
  for (iter = _uniforms.begin(); iter != _uniforms.end(); iter++) {
    iter->second->applyChanges(gl);
  }
}