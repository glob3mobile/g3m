//
//  NativeGL2_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_NativeGL2_iOS_hpp
#define G3MiOSSDK_NativeGL2_iOS_hpp

#include <OpenGLES/ES2/gl.h>

#include "INativeGL.hpp"

class NativeGL2_iOS: public INativeGL
{
public:
  void useProgram(int program) const{
    glUseProgram(program);
  }
  
  int getAttribLocation(int program, char name[]) const{
    return glGetAttribLocation(program, name);
  }
  
  void uniform2f(int loc, float x, float y) const{
    glUniform2f(loc, x, y);
  }
  
  void uniform1i(int loc, int v) const{
    glUniform1i(loc, v);
  }
  
  void uniformMatrix4fv(int location, int count, bool transpose, const float value[]){
    glUniformMatrix4fv(location, count, transpose, value);
  }
  
  void clearColor(float red, float green, float blue, float alpha) const{
    glClearColor(red, green, blue, alpha);
  }
  
  void clear(Buffer buffer) const{
    GLbitfield b = 0x000000;
    if (buffer & 0x01) {
      b |= GL_COLOR_BUFFER_BIT;
    }
    if (buffer & 0x02) {
      b |= GL_DEPTH_BUFFER_BIT;
    }
    glClear(b);
  }
};

#endif
