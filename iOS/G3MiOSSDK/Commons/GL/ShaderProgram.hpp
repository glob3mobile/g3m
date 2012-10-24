//
//  ShaderProgram.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 24/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_ShaderProgram_hpp
#define G3MiOSSDK_ShaderProgram_hpp

#include <string>

class GL;


class ShaderProgram {
  
private:
  int   _programNum;
  GL*   _gl;
  
  
public:
  ShaderProgram(const std::string& vertexShader, const std::string& fragmentShader, GL* gl);
  ~ShaderProgram();
  
  
  // TEMP
  void attachShader(unsigned int shader); 
  int getProgramNum() { return _programNum; }

};

#endif
