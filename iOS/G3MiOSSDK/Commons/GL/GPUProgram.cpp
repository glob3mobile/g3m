//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#include "GPUProgram.hpp"

#include "GL.hpp"

GPUProgram::GPUProgram(INativeGL* nativeGL, const std::string& vertexSource,
           const std::string& fragmentSource){
  
  _programCreated = false;
  
  _nativeGL = nativeGL;
  _programID = _nativeGL->createProgram();
  
  
  // compile vertex shader
  int vertexShader= _nativeGL->createShader(VERTEX_SHADER);
  if (!compileShader(vertexShader, vertexSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader\n");
    deleteShader(vertexShader);
    deleteProgram(_programID);
    return;
  }
  
  // compile fragment shader
  int fragmentShader = _nativeGL->createShader(FRAGMENT_SHADER);
  if (!compileShader(fragmentShader, fragmentSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader\n");
    deleteShader(fragmentShader);
    deleteProgram(_programID);
    return;
  }
  
  _nativeGL->bindAttribLocation(this, 0, "Position");
  
  // link program
  if (!linkProgram()) {
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
    deleteShader(vertexShader);
    deleteShader(fragmentShader);
    deleteProgram(_programID);
    return;
  }
  
  // free shaders
  deleteShader(vertexShader);
  deleteShader(fragmentShader);
  
  getVariables();
  
  _programCreated = true; //Program fully created
  
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
    _uniforms.push_back(u);
  }
  
  //Attributes
  n = _nativeGL->getProgramiv(this, GLVariable::activeAttributes());
  for (int i = 0; i < n; i++) {
    Attribute* a = _nativeGL->getActiveAttribute(this, i);
    _attributes.push_back(a);
  }
  
}

Uniform* GPUProgram::getUniform(const std::string name) const{
  for (int i = 0; i < _uniforms.size(); i++) {
    Uniform *u = _uniforms[i];
    if (u != NULL && u->getName() == name){
      return u;
    }
  }
  return NULL;
}

Attribute* GPUProgram::getAttribute(const std::string name) const{
  for (int i = 0; i < _uniforms.size(); i++) {
    Attribute *a = _attributes[i];
    if (a != NULL && a->getName() == name){
      return a;
    }
  }
  return NULL;
}