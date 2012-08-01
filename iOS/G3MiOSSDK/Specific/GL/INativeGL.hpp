//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_INativeGL_hpp
#define G3MiOSSDK_INativeGL_hpp

#include <vector>

#include "IGL.hpp"

enum GLBufferType { ColorBuffer, DepthBuffer };

enum GLFeature { PolygonOffsetFill, DepthTest, Blend, CullFacing };

enum GLType { Float, UnsignedByte , UnsignedInt, Int};

enum GLPrimitive { TriangleStrip, Lines, LineLoop, Points };

enum GLError { NoError, InvalidEnum, InvalidValue, InvalidOperation, OutOfMemory, UnknownError};

enum GLBlendFactor { SrcAlpha, OneMinusSrcAlpha};

enum GLTextureType { Texture2D };

enum GLTextureParameter { MinFilter, MagFilter, WrapS, WrapT };

enum GLTextureParameterValue { Linear, ClampToEdge };

enum GLAlignment { Unpack, Pack };

enum GLFormat { RGBA };

class INativeGL
{
public:
  
  virtual ~INativeGL() = 0;
  
  virtual void useProgram(int program) const = 0;
  
  virtual int getAttribLocation(int program, const char name[]) const = 0;
  
  virtual int getUniformLocation(int program, const char name[]) const = 0;
  
  virtual void uniform2f(int loc, float x, float y) const = 0;
  
  virtual void uniform1f(int loc, float x) const = 0;
  
  virtual void uniform1i(int loc, int v) const = 0;
  
  virtual void uniformMatrix4fv(int location, int count, bool transpose, const float value[]) const;
  
  virtual void clearColor(float red, float green, float blue, float alpha) const = 0;
  
  virtual void clear(int nBuffer, GLBufferType buffers[]) const = 0;
  
  virtual void uniform4f(int location, float v0, float v1, float v2, float v3) const = 0;
  
  virtual void enable(GLFeature feature) const = 0;
  
  virtual void disable(GLFeature feature) const = 0;
  
  virtual void polygonOffset(float factor, float units) const = 0;
  
  virtual void vertexAttribPointer(int index, int size, GLType type, 
                                   bool normalized, int stride, const void*	pointer) const = 0;
  
  virtual void drawElements(GLPrimitive mode, int count, GLType type, const void* indices) const = 0;
  
  virtual void lineWidth(float width) const = 0;
  
  virtual GLError getError() const = 0;
  
  virtual void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor) const = 0;
  
  virtual void bindTexture(GLTextureType target, int texture) const = 0;

  virtual void deleteTextures(int n, const int textures[]) const = 0;
  
  virtual void enableVertexAttribArray(int location) const = 0;
  
  virtual void disableVertexAttribArray(int location) const = 0;
  
  virtual void pixelStorei(GLAlignment pname, int param) const = 0;
  
  std::vector<int> genTextures(int	n) const;
  
  virtual void texParameteri(GLTextureType target, GLTextureParameter par, GLTextureParameterValue v) const = 0;
  
  virtual void texImage2D(GLTextureType target,
                            int         level,
                            int         internalFormat,
                            int         width,
                            int         height,
                            int         border,
                            GLFormat    format,
                            GLType      type,
                            const void* data) const = 0;
  
  virtual void drawArrays(GLPrimitive mode, int first, int count) const = 0;
  
  virtual void cullFace(CullFace c) const = 0;



};


#endif
