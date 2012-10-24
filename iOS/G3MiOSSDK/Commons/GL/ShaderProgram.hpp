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

//#include "IGLProgramId.hpp"


class ShaderProgram {
  
private:
  //IGLProgramId* _program;
  int           _programNum;
  
  
public:
  ShaderProgram(const std::string& vertexShader, const std::string& fragmentShader);
  ~ShaderProgram();
  
  
  // TEMP
  void attachShader(unsigned int shader); 
  int getProgramNum() { return _programNum; }

};

#endif
