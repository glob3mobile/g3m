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



/*
// ******** ESTO SE DEBE QUITAR y usar la nativa
#include <OpenGLES/ES2/gl.h>*/


ShaderProgram::ShaderProgram(const std::string& vertexShader, const std::string& fragmentShader, GL* gl):_gl(gl)
{
  _programNum = _gl->createProgram();
}

ShaderProgram::~ShaderProgram()
{
  _gl->deleteProgram(_programNum);
}


// TEMP
void ShaderProgram::attachShader(unsigned int shader) 
{
  _gl->attachShader(_programNum, shader);
}





