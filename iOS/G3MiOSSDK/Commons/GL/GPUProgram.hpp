//
//  GPUProgram.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef __G3MiOSSDK__GPUProgram__
#define __G3MiOSSDK__GPUProgram__

#include <iostream.h>
#include <string.h>

#include "Attribute.hpp"
#include "Uniform.hpp"

class IFloatBuffer;

class GL;

class GPUProgram{
  
  INativeGL* _nativeGL;
  int _programID;
  bool _programCreated;
  
  bool compileShader(int shader, const std::string& source) const;
  bool linkProgram() const;
  void deleteShader(int shader) const;
  void deleteProgram(int p) const;
  
public:
  
  GPUProgram(INativeGL* nativeGL, const std::string& vertexSource,
             const std::string& fragmentSource);
  
  ~GPUProgram();
  
  int getProgramID() const{ return _programID;}
  bool isCreated() const{ return _programCreated;}
  
  
};

#endif /* defined(__G3MiOSSDK__GPUProgram__) */
