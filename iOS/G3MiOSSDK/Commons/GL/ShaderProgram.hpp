//
//  ShaderProgram.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_ShaderProgram_hpp
#define G3MiOSSDK_ShaderProgram_hpp

#include <string>

class GL;

enum ShaderType {
  VERTEX_SHADER,
  FRAGMENT_SHADER
};


class ShaderProgram {

private:
  int   _program;
  GL*   _gl;

  bool compileShader(int shader,
                     const std::string& source);
  bool linkProgram();



public:
  ShaderProgram(GL* gl);
  ~ShaderProgram();

  bool loadShaders(const std::string& vertexSource,
                   const std::string& fragmentSource);


  // TEMP
  int getProgram() const {
    return _program;
  }

  const std::string description() const;

};

#endif
