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
#include "GPUProgramManager.hpp"

GPUProgram* GPUProgram::createProgram(GL* gl,
                                      const std::string& name,
                                      const std::string& vertexSource,
                                      const std::string& fragmentSource) {

  GPUProgram* p = new GPUProgram();

  p->_name = name;
  p->_programID = gl->createProgram();
  p->_gl = gl;

  // compile vertex shader
  int vertexShader= gl->createShader(VERTEX_SHADER);
  if (!p->compileShader(gl, vertexShader, vertexSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader :\n %s\n", vertexSource.c_str());
    gl->printShaderInfoLog(vertexShader);

    p->deleteShader(gl, vertexShader);
    p->deleteProgram(gl, p);
    return NULL;
  }

//  ILogger::instance()->logInfo("VERTEX SOURCE: \n %s", vertexSource.c_str());

  // compile fragment shader
  int fragmentShader = gl->createShader(FRAGMENT_SHADER);
  if (!p->compileShader(gl, fragmentShader, fragmentSource)) {
    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader :\n %s\n", fragmentSource.c_str());
    gl->printShaderInfoLog(fragmentShader);

    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p);
    return NULL;
  }

//  ILogger::instance()->logInfo("FRAGMENT SOURCE: \n %s", fragmentSource.c_str());

  //gl->bindAttribLocation(p, 0, POSITION);

  // link program
  if (!p->linkProgram(gl)) {
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
    p->deleteShader(gl, vertexShader);
    p->deleteShader(gl, fragmentShader);
    p->deleteProgram(gl, p);
    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program");
    return NULL;
  }

  //Mark shaders for deleting when program is deleted
  p->deleteShader(gl, vertexShader);
  p->deleteShader(gl, fragmentShader);

  p->getVariables(gl);

  if (gl->getError() != GLError::noError()) {
    ILogger::instance()->logError("Error while compiling program");
  }

  return p;
}


GPUProgram::~GPUProgram() {

  //ILogger::instance()->logInfo("Deleting program %s", _name.c_str());

//  if (_manager != NULL) {
//    _manager->compiledProgramDeleted(this->_name);
//  }

  for (int i = 0; i < _nUniforms; i++) {
    delete _createdUniforms[i];
  }

  for (int i = 0; i < _nAttributes; i++) {
    delete _createdAttributes[i];
  }

  delete[] _createdAttributes;
  delete[] _createdUniforms;

  if (!_gl->deleteProgram(this)) {
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
  }
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

  if (result) {
    gl->attachShader(_programID, shader);
  } else{
    ILogger::instance()->logError("GPUProgram: Problem encountered while compiling shader.");
  }

  return result;
}

void GPUProgram::deleteShader(GL* gl, int shader) const{
  if (!gl->deleteShader(shader)) {
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting shader.");
  }
}

void GPUProgram::deleteProgram(GL* gl, const GPUProgram* p) {
  if (!gl->deleteProgram(p)) {
    ILogger::instance()->logError("GPUProgram: Problem encountered while deleting program.");
  }
}

void GPUProgram::getVariables(GL* gl) {

  for (int i = 0; i < 32; i++) {
    _uniforms[i] = NULL;
    _attributes[i] = NULL;
  }

  //Uniforms
  _uniformsCode = 0;
  _nUniforms = gl->getProgramiv(this, GLVariable::activeUniforms());

  int counter = 0;
  _createdUniforms = new GPUUniform*[_nUniforms];

  for (int i = 0; i < _nUniforms; i++) {
    GPUUniform* u = gl->getActiveUniform(this, i);
    if (u != NULL) {
      _uniforms[u->getIndex()] = u;

      const int code = GPUVariable::getUniformCode(u->_key);
      _uniformsCode = _uniformsCode | code;
    }

    _createdUniforms[counter++] = u; //Adding to created uniforms array
  }

  //Attributes
  _attributesCode = 0;
  _nAttributes = gl->getProgramiv(this, GLVariable::activeAttributes());

  counter = 0;
  _createdAttributes = new GPUAttribute*[_nAttributes];

  for (int i = 0; i < _nAttributes; i++) {
    GPUAttribute* a = gl->getActiveAttribute(this, i);
    if (a != NULL) {
      _attributes[a->getIndex()] = a;

      const int code = GPUVariable::getAttributeCode(a->_key);
      _attributesCode = _attributesCode | code;
    }

    _createdAttributes[counter++] = a;
  }

  //ILogger::instance()->logInfo("Program with Uniforms Bitcode: %d and Attributes Bitcode: %d", _uniformsCode, _attributesCode);
}

GPUUniform* GPUProgram::getGPUUniform(const std::string name) const{
#ifdef C_CODE
  const int key = GPUVariable::getUniformKey(name);
#endif
#ifdef JAVA_CODE
  final int key = GPUVariable.getUniformKey(name).getValue();
#endif
  return _uniforms[key];
}

GPUUniformBool* GPUProgram::getGPUUniformBool(const std::string name) const {
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->_type == GLType::glBool()) {
    return (GPUUniformBool*)u;
  }
  return NULL;
}

GPUUniformVec2Float* GPUProgram::getGPUUniformVec2Float(const std::string name) const {
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->_type == GLType::glVec2Float()) {
    return (GPUUniformVec2Float*)u;
  }
  return NULL;
}

GPUUniformVec4Float* GPUProgram::getGPUUniformVec4Float(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->_type == GLType::glVec4Float()) {
    return (GPUUniformVec4Float*)u;
  }
  return NULL;
}

GPUUniformFloat* GPUProgram::getGPUUniformFloat(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->_type == GLType::glFloat()) {
    return (GPUUniformFloat*)u;
  }
  return NULL;
}

GPUUniformMatrix4Float* GPUProgram::getGPUUniformMatrix4Float(const std::string name) const{
  GPUUniform* u = getGPUUniform(name);
  if (u!= NULL && u->_type == GLType::glMatrix4Float()) {
    return (GPUUniformMatrix4Float*)u;
  }
  return NULL;

}

GPUAttribute* GPUProgram::getGPUAttribute(const std::string name) const{
#ifdef C_CODE
  const int key = GPUVariable::getAttributeKey(name);
#endif
#ifdef JAVA_CODE
  final int key = GPUVariable.getAttributeKey(name).getValue();
#endif
  return _attributes[key];
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
  if (a!= NULL && a->_size == 1 && a->_type == GLType::glFloat()) {
    return (GPUAttributeVec1Float*)a;
  }
  return NULL;

}

GPUAttributeVec2Float* GPUProgram::getGPUAttributeVec2Float(const std::string name) const{
  GPUAttributeVec2Float* a = (GPUAttributeVec2Float*)getGPUAttribute(name);
  if (a!= NULL && a->_size == 2 && a->_type == GLType::glFloat()) {
    return (GPUAttributeVec2Float*)a;
  }
  return NULL;

}

GPUAttributeVec3Float* GPUProgram::getGPUAttributeVec3Float(const std::string name) const{
  GPUAttributeVec3Float* a = (GPUAttributeVec3Float*)getGPUAttribute(name);
  if (a!= NULL && a->_size == 3 && a->_type == GLType::glFloat()) {
    return (GPUAttributeVec3Float*)a;
  }
  return NULL;

}

GPUAttributeVec4Float* GPUProgram::getGPUAttributeVec4Float(const std::string name) const{
  GPUAttributeVec4Float* a = (GPUAttributeVec4Float*)getGPUAttribute(name);
  if (a!= NULL && a->_size == 4 && a->_type == GLType::glFloat()) {
    return (GPUAttributeVec4Float*)a;
  }
  return NULL;

}

/**
 Must be called when the program is used
 */
void GPUProgram::onUsed() {
  //  ILogger::instance()->logInfo("GPUProgram %s being used", _name.c_str());
}
/**
 Must be called when the program is no longer used
 */
void GPUProgram::onUnused(GL* gl) {
  //ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());

  for (int i = 0; i < _nUniforms; i++) {
    if (_createdUniforms[i] != NULL) { //Texture Samplers return null
      _createdUniforms[i]->unset();
    }
  }

  for (int i = 0; i < _nAttributes; i++) {
    if (_createdAttributes[i] != NULL) {
      _createdAttributes[i]->unset(gl);
    }
  }
}

/**
 Must be called before drawing to apply Uniforms and Attributes new values
 */
void GPUProgram::applyChanges(GL* gl) {

  for (int i = 0; i < _nUniforms; i++) {
    GPUUniform* uniform = _createdUniforms[i];
    if (uniform != NULL) { //Texture Samplers return null
      uniform->applyChanges(gl);
    }
  }

  for (int i = 0; i < _nAttributes; i++) {
    GPUAttribute* attribute = _createdAttributes[i];
    if (attribute != NULL) {
      attribute->applyChanges(gl);
    }
  }
}

GPUUniform* GPUProgram::getUniformOfType(const std::string& name, int type) const{
  GPUUniform* u = NULL;
  if (type == GLType::glBool()) {
    u = getGPUUniformBool(name);
  } else {
    if (type == GLType::glVec2Float()) {
      u = getGPUUniformVec2Float(name);
    } else{
      if (type == GLType::glVec4Float()) {
        u = getGPUUniformVec4Float(name);
      } else{
        if (type == GLType::glFloat()) {
          u = getGPUUniformFloat(name);
        } else
          if (type == GLType::glMatrix4Float()) {
            u = getGPUUniformMatrix4Float(name);
          }
      }
    }
  }
  return u;
}

GPUUniform* GPUProgram::getGPUUniform(int key) const{
  return _uniforms[key];
}

GPUAttribute* GPUProgram::getGPUAttribute(int key) const{
  return _attributes[key];
}

GPUAttribute* GPUProgram::getGPUAttributeVecXFloat(int key, int x) const{
  GPUAttribute* a = getGPUAttribute(key);
  if (a->_type == GLType::glFloat() && a->_size == x) {
    return a;
  }
  return NULL;
}

void GPUProgram::setGPUUniformValue(int key, GPUUniformValue* v) {
  GPUUniform* u = _uniforms[key];
  if (u == NULL) {
    ILogger::instance()->logError("Uniform [key=%d] not found", key);
    return;
  }
  u->set(v);
}

void GPUProgram::setGPUAttributeValue(int key, GPUAttributeValue* v) {
  GPUAttribute* a = _attributes[key];
  if (a == NULL) {
    ILogger::instance()->logError("Attribute [key=%d] not found", key);
    return;
  }
  a->set(v);
}