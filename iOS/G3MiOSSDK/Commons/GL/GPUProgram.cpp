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
  //p->_nativeGL = gl->getNative();
  p->_programID = gl->createProgram();
  
  // compile vertex shader
  int vertexShader= gl->createShader(VERTEX_SHADER);
  if (!p->compileShader(gl, vertexShader, vertexSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader\n");
    p->deleteShader(gl, vertexShader);
    p->deleteProgram(gl, p->_programID);
    throw new G3MError("GPUProgram: ERROR compiling vertex shader");
    return NULL;
  }
  
  // compile fragment shader
  int fragmentShader = gl->createShader(FRAGMENT_SHADER);
  if (!p->compileShader(gl, fragmentShader, fragmentSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader\n");
    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p->_programID);
    throw new G3MError("GPUProgram: ERROR compiling fragment shader");
    return NULL;
  }
  
  gl->bindAttribLocation(p, 0, "Position");
  
  // link program
  if (!p->linkProgram(gl)) {
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
    p->deleteShader(gl, vertexShader);
    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p->_programID);
    throw new G3MError("GPUProgram: ERROR linking graphic program");
    return NULL;
  }
  
  // free shaders
  p->deleteShader(gl, vertexShader);
  p->deleteShader(gl, fragmentShader);
  
  p->getVariables(gl);
  
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
    if (u != NULL) _uniforms[u->getName()] = u;
  }
  
  //Attributes
  n = gl->getProgramiv(this, GLVariable::activeAttributes());
  for (int i = 0; i < n; i++) {
    Attribute* a = gl->getActiveAttribute(this, i);
    if (a != NULL) _attributes[a->getName()] = a;
  }
  
}

GPUUniform* GPUProgram::getUniform(const std::string name) const{
  std::map<std::string, GPUUniform*> ::const_iterator it = _uniforms.find(name);
  if (it != _uniforms.end()){
    return it->second;
  } else{
    return NULL;
  }
}

GPUUniformBool* GPUProgram::getGPUUniformBool(const std::string name) const {
  GPUUniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glBool()){
    return (GPUUniformBool*)u;
  } else{
    return NULL;
  }
}

GPUUniformVec2Float* GPUProgram::getGPUUniformVec2Float(const std::string name) const {
  GPUUniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec2Float()){
    return (GPUUniformVec2Float*)u;
  } else{
    return NULL;
  }
}

GPUUniformVec4Float* GPUProgram::getGPUUniformVec4Float(const std::string name) const{
  GPUUniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glVec4Float()){
    return (GPUUniformVec4Float*)u;
  } else{
    return NULL;
  }
}

GPUUniformFloat* GPUProgram::getGPUUniformFloat(const std::string name) const{
  GPUUniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glFloat()){
    return (GPUUniformFloat*)u;
  } else{
    return NULL;
  }
}

GPUUniformMatrix4Float* GPUProgram::getGPUUniformMatrix4Float(const std::string name) const{
  GPUUniform* u = getUniform(name);
  if (u!= NULL && u->getType() == GLType::glMatrix4Float()){
    return (GPUUniformMatrix4Float*)u;
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

AttributeVec1Float* GPUProgram::getAttributeVec1Float(const std::string name) const{
  AttributeVecFloat* a = (AttributeVecFloat*)getAttribute(name);
  if (a!= NULL && a->getSize() == 1){
    return (AttributeVec1Float*)a;
  } else{
    return NULL;
  }
}

AttributeVec2Float* GPUProgram::getAttributeVec2Float(const std::string name) const{
  AttributeVecFloat* a = (AttributeVecFloat*)getAttribute(name);
  if (a!= NULL && a->getSize() == 2){
    return (AttributeVec2Float*)a;
  } else{
    return NULL;
  }
}

AttributeVec3Float* GPUProgram::getAttributeVec3Float(const std::string name) const{
  AttributeVecFloat* a = (AttributeVecFloat*)getAttribute(name);
  if (a!= NULL && a->getSize() == 3){
    return (AttributeVec3Float*)a;
  } else{
    return NULL;
  }
}

AttributeVec4Float* GPUProgram::getAttributeVec4Float(const std::string name) const{
  AttributeVecFloat* a = (AttributeVecFloat*)getAttribute(name);
  if (a!= NULL && a->getSize() == 4){
    return (AttributeVec4Float*)a;
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
  
  std::map<std::string, GPUUniform*>::iterator iter;
  for (iter = _uniforms.begin(); iter != _uniforms.end(); iter++) {
    iter->second->applyChanges(gl);
  }
  
  std::map<std::string, Attribute*>::iterator iter2;
  for (iter2 = _attributes.begin(); iter2 != _attributes.end(); iter2++) {
    iter2->second->applyChanges(gl);
  }
}