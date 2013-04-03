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

GPUProgram* GPUProgram::createProgram(INativeGL* nativeGL, const std::string name, const std::string& vertexSource,
                                 const std::string& fragmentSource){
  
  GPUProgram* p = new GPUProgram();
  
  p->_programCreated = false;
  p->_nativeGL = nativeGL;
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
  
  p->_name = name; //NAME
  
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

Attribute* GPUProgram::getAttribute(const std::string name) const{
  std::map<std::string, Attribute*> ::const_iterator it = _attributes.find(name);
  if (it != _attributes.end()){
    return it->second;
  } else{
    return NULL;
  }
}