//
//  ShaderProgram.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include "ShaderProgram.hpp"
#include "GL.hpp"
#include "IStringBuilder.hpp"

ShaderProgram::ShaderProgram(GL* gl) :
_gl(gl)
{
  _program = _gl->createProgram();
}


ShaderProgram::~ShaderProgram() {
  _gl->deleteProgram(_program);
}


bool ShaderProgram::loadShaders(const std::string& vertexSource,
                                const std::string& fragmentSource) {
  // compile vertex shader
  int vertexShader= _gl->createShader(VERTEX_SHADER);
  if (!compileShader(vertexShader, vertexSource)) {
    printf ("ERROR compiling vertex shader\n");
    _gl->deleteShader(vertexShader);
    return false;
  }

  // compile fragment shader
  int fragmentShader = _gl->createShader(FRAGMENT_SHADER);
  if (!compileShader(fragmentShader, fragmentSource)) {
    printf ("ERROR compiling fragment shader\n");
    _gl->deleteShader(fragmentShader);
    return false;
  }

  // link program
  if (!linkProgram()) {
    printf ("ERROR linking graphic program\n");
    _gl->deleteShader(vertexShader);
    _gl->deleteShader(fragmentShader);
    _gl->deleteProgram(_program);
    return false;
  }

  // free shaders
  _gl->deleteShader(vertexShader);
  _gl->deleteShader(fragmentShader);
  return true;
}

bool ShaderProgram::compileShader(int shader,
                                  const std::string& source) {
  bool result = _gl->compileShader(shader, source);

#if defined(DEBUG)
  _gl->printShaderInfoLog(shader);
#endif

  if (result) _gl->attachShader(_program, shader);

  return result;
}

bool ShaderProgram::linkProgram() {
  bool result = _gl->linkProgram(_program);

#if defined(DEBUG)
  _gl->printProgramInfoLog(_program);
#endif

  return result;
}

const std::string ShaderProgram::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(ShaderProgram ");
  isb->addInt(_program);
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
