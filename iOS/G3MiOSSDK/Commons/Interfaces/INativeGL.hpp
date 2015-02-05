//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_INativeGL
#define G3MiOSSDK_INativeGL

class IFloatBuffer;
//class IIntBuffer;
class IShortBuffer;
class IImage;
class IGLUniformID;
class IGLTextureId;
class Matrix44D;

#include <vector>
#include <string>

#include "GPUProgram.hpp"

class GPUProgram;

class GPUUniform;
class GPUAttribute;

class INativeGL {
public:

  virtual ~INativeGL() {
  }

  virtual void useProgram(GPUProgram* program) const = 0;

  virtual void uniform2f(const IGLUniformID* loc,
                         float x,
                         float y) const = 0;

  virtual void uniform1f(const IGLUniformID* loc,
                         float x) const = 0;

  virtual void uniform1i(const IGLUniformID* loc,
                         int v) const = 0;

  virtual void uniformMatrix4fv(const IGLUniformID* location,
                                bool transpose,
                                const Matrix44D* matrix) const = 0;

  virtual void clearColor(float red,
                          float green,
                          float blue,
                          float alpha) const = 0;

  virtual void clear(int buffers) const = 0;

  virtual void uniform4f(const IGLUniformID* location,
                         float v0,
                         float v1,
                         float v2,
                         float v3) const = 0;

  virtual void uniform3f(const IGLUniformID* location,
                         float v0,
                         float v1,
                         float v2) const = 0;

  virtual void enable(int feature) const = 0;

  virtual void disable(int feature) const = 0;

  virtual void polygonOffset(float factor,
                             float units) const = 0;

  virtual void vertexAttribPointer(int index,
                                   int size,
                                   bool normalized,
                                   int stride,
                                   const IFloatBuffer* buffer) const = 0;

  virtual void drawElements(int mode,
                            int count,
                            IShortBuffer* indices) const = 0;

  virtual void lineWidth(float width) const = 0;

  virtual int getError() const = 0;

  virtual void blendFunc(int sfactor,
                         int dfactor) const = 0;

  virtual void bindTexture(int target,
                           const IGLTextureId* texture) const = 0;

  /* Delete Texture from GPU, and answer if the TextureId can be reused */
  virtual bool deleteTexture(const IGLTextureId* texture) const = 0;

  virtual void enableVertexAttribArray(int location) const = 0;

  virtual void disableVertexAttribArray(int location) const = 0;

  virtual void pixelStorei(int pname,
                           int param) const = 0;

  virtual std::vector<IGLTextureId*> genTextures(int	n) const = 0;

  virtual void texParameteri(int target,
                             int par,
                             int v) const = 0;

  virtual void texImage2D(const IImage* image, int format) const = 0;

  virtual void generateMipmap(int target) const = 0;

  virtual void drawArrays(int mode,
                          int first,
                          int count) const = 0;

  virtual void cullFace(int c) const = 0;

  virtual void getIntegerv(int v, int i[]) const = 0;


  virtual int CullFace_Front() const = 0;
  virtual int CullFace_Back() const = 0;
  virtual int CullFace_FrontAndBack() const = 0;

  virtual int BufferType_ColorBuffer() const = 0;
  virtual int BufferType_DepthBuffer() const = 0;

  virtual int Feature_PolygonOffsetFill() const = 0;
  virtual int Feature_DepthTest() const = 0;
  virtual int Feature_Blend() const = 0;
  virtual int Feature_CullFace() const = 0;

  virtual int Type_Float() const = 0;
  virtual int Type_UnsignedByte() const = 0;
  virtual int Type_UnsignedInt() const = 0;
  virtual int Type_Int() const = 0;
  virtual int Type_Vec2Float() const = 0;
  virtual int Type_Vec3Float() const = 0;
  virtual int Type_Vec4Float() const = 0;
  virtual int Type_Bool() const = 0;
  virtual int Type_Matrix4Float() const = 0;

  virtual int Primitive_Triangles() const = 0;
  virtual int Primitive_TriangleStrip() const = 0;
  virtual int Primitive_TriangleFan() const = 0;
  virtual int Primitive_Lines() const = 0;
  virtual int Primitive_LineStrip() const = 0;
  virtual int Primitive_LineLoop() const = 0;
  virtual int Primitive_Points() const = 0;

  virtual int BlendFactor_One() const = 0;
  virtual int BlendFactor_Zero() const = 0;
  virtual int BlendFactor_SrcAlpha() const = 0;
  virtual int BlendFactor_OneMinusSrcAlpha() const = 0;

  virtual int TextureType_Texture2D() const = 0;

  virtual int TextureParameter_MinFilter() const = 0;
  virtual int TextureParameter_MagFilter() const = 0;
  virtual int TextureParameter_WrapS() const = 0;
  virtual int TextureParameter_WrapT() const = 0;

  virtual int TextureParameterValue_Nearest() const = 0;
  virtual int TextureParameterValue_Linear() const = 0;
  virtual int TextureParameterValue_NearestMipmapNearest() const = 0;
  virtual int TextureParameterValue_NearestMipmapLinear() const = 0;
  virtual int TextureParameterValue_LinearMipmapNearest() const = 0;
  virtual int TextureParameterValue_LinearMipmapLinear() const = 0;

  virtual int TextureParameterValue_ClampToEdge() const = 0;

  virtual int Alignment_Pack() const = 0;
  virtual int Alignment_Unpack() const = 0;

  virtual int Format_RGBA() const = 0;

  virtual int Variable_Viewport() const = 0;
  virtual int Variable_ActiveAttributes() const = 0;
  virtual int Variable_ActiveUniforms() const = 0;

  virtual int Error_NoError() const = 0;

  virtual int createProgram() const = 0;
  virtual bool deleteProgram(int program) const = 0;
  virtual void attachShader(int program, int shader) const = 0;
  virtual int createShader(ShaderType type) const = 0;
  virtual bool compileShader (int shader, const std::string& source) const = 0;
  virtual bool deleteShader(int shader) const = 0;
  virtual void printShaderInfoLog(int shader) const = 0;
  virtual bool linkProgram(int program) const = 0;
  virtual void printProgramInfoLog(int program) const = 0;
  
  virtual void bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const = 0;
  
  virtual int getProgramiv(const GPUProgram* program, int param) const = 0;
  
  virtual GPUUniform* getActiveUniform(const GPUProgram* program, int i) const = 0;
  virtual GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const = 0;

  virtual void depthMask(bool v) const = 0;

  virtual void setActiveTexture(int i) const = 0;
  
};

#endif
