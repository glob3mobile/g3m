//
//  ShaderProgram.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "ShaderProgram.hpp"




// ******** ESTO SE DEBE QUITAR y usar la nativa
#include <OpenGLES/ES2/gl.h>


ShaderProgram::ShaderProgram(const std::string& vertexShader, const std::string& fragmentShader)
{
  _programNum = glCreateProgram();
}

ShaderProgram::~ShaderProgram()
{
  glDeleteProgram(_programNum);
}


// TEMP
void ShaderProgram::attachShader(unsigned int shader) 
{
  glAttachShader(_programNum, shader);
}





