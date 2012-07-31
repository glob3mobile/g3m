//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_INativeGL_hpp
#define G3MiOSSDK_INativeGL_hpp

//Enums for handling OpenGL
enum Buffer{ ColorBuffer, DepthBuffer};



class INativeGL
{
public:
  
  virtual ~INativeGL() = 0;
  
  virtual void useProgram(int program) const = 0;
  
  virtual int getAttribLocation(int program, char name[]) const = 0;
  
  virtual void uniform2f(int loc, float x, float y) const = 0;
  
  virtual void uniform1i(int loc, int v) const = 0;
  
  virtual void uniformMatrix4fv(int location, int count, bool transpose, const float value[]);
  
  virtual void clearColor(float red, float green, float blue, float alpha) const = 0;
  
  virtual void clear(Buffer buffer) const = 0;
};


#endif
