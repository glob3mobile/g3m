//
//  ShaderProgram.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "ShaderProgram.hpp"
#include "GL.hpp"


ShaderProgram::ShaderProgram(GL* gl):_gl(gl) 
{
  _program = _gl->createProgram();
}


ShaderProgram::~ShaderProgram()
{
  _gl->deleteProgram(_program);
}


bool ShaderProgram::loadShaders(const std::string& vertexSource, const std::string& fragmentSource)
{
  _vertexShader= _gl->createShader(VERTEX_SHADER);
  if (!compileShader(_vertexShader, vertexSource)) {
    _gl->deleteShader(_vertexShader);
    return false;
  } 
  
  _fragmentShader = _gl->createShader(FRAGMENT_SHADER);
  if (!compileShader(_fragmentShader, fragmentSource)) {
    _gl->deleteShader(_fragmentShader);
    return false;
  }

  
  return true;
}
  

bool ShaderProgram::compileShader(int shader, const std::string source)
{
  bool result = _gl->compileShader(shader, source);

/*  
#if defined(DEBUG)
  GLint logLength;
  glGetShaderiv(*shader, GL_INFO_LOG_LENGTH, &logLength);
  if (logLength > 0) {
    GLchar* log = (GLchar* ) malloc(logLength);
    glGetShaderInfoLog(*shader, logLength, &logLength, log);
    NSLog(@"Shader compile log:\n%s", log);
    free(log);
  }
#endif
 */
  
  if (result) _gl->attachShader(_program, shader);
     
  return result;
}





