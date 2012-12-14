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
  // compile vertex shader
  _vertexShader= _gl->createShader(VERTEX_SHADER);
  if (!compileShader(_vertexShader, vertexSource)) {
    printf ("ERROR compiling vertex shader\n");
    _gl->deleteShader(_vertexShader);
    return false;
  } 
  
  // compile fragment shader
  _fragmentShader = _gl->createShader(FRAGMENT_SHADER);
  if (!compileShader(_fragmentShader, fragmentSource)) {
    printf ("ERROR compiling fragment shader\n");
    _gl->deleteShader(_fragmentShader);
    return false;
  }
  
  // link program
  if (!linkProgram()) {
    printf ("ERROR linking graphic program\n");
    _gl->deleteShader(_vertexShader);
    _gl->deleteShader(_fragmentShader);
    _gl->deleteProgram(_program);
    return false;
  }

  // free shaders
  _gl->deleteShader(_vertexShader);
  _gl->deleteShader(_fragmentShader);  
  return true;
}
  

bool ShaderProgram::compileShader(int shader,
                                  const std::string& source)
{
  bool result = _gl->compileShader(shader, source);
  
#if defined(DEBUG)
  _gl->printShaderInfoLog(shader);
#endif
  
  if (result) _gl->attachShader(_program, shader);
     
  return result;
}
      

bool ShaderProgram::linkProgram()
{
  bool result = _gl->linkProgram(_program);

#if defined(DEBUG)
  _gl->printProgramInfoLog(_program);
#endif
  
  return result;
}
      



