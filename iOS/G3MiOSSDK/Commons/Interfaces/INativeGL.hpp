//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_INativeGL_hpp
#define G3MiOSSDK_INativeGL_hpp

class IGLProgramId;

class IFloatBuffer;
class IIntBuffer;
class IImage;

class IGLUniformID;

class IGLTextureId;

#include <vector>
#include <string>

class MutableMatrix44D;

class INativeGL {
public:
  
  virtual ~INativeGL() { };
  
  virtual void useProgram(IGLProgramId* program) const = 0;

  virtual int getAttribLocation(IGLProgramId* program,
                                const std::string& name) const = 0;
  
  virtual IGLUniformID* getUniformLocation(IGLProgramId* program,
                                 const std::string& name) const = 0;

  virtual void uniform2f(IGLUniformID* loc,
                         float x,
                         float y) const = 0;
  
  virtual void uniform1f(IGLUniformID* loc,
                         float x) const = 0;
  
  virtual void uniform1i(IGLUniformID* loc,
                         int v) const = 0;
  
//  virtual void uniformMatrix4fv(IGLUniformID* location,
//                                bool transpose,
//                                const IFloatBuffer* buffer) const = 0;

  virtual void uniformMatrix4fv(IGLUniformID* location,
                                bool transpose,
                                const MutableMatrix44D* matrix) const = 0;

  
//  virtual void uniformMatrix4fv(IGLUniformID* location,
//                                int count,
//                                bool transpose,
//                                const float value[]) const = 0;

  virtual void clearColor(float red,
                          float green,
                          float blue,
                          float alpha) const = 0;
  
  virtual void clear(int buffers) const = 0;
  
  virtual void uniform4f(IGLUniformID* location,
                         float v0,
                         float v1,
                         float v2,
                         float v3) const = 0;
  
  virtual void enable(int feature) const = 0;
  
  virtual void disable(int feature) const = 0;
  
  virtual void polygonOffset(float factor,
                             float units) const = 0;
  
  virtual void vertexAttribPointer(int index,
                                   int size,
                                   bool normalized,
                                   int stride,
                                   IFloatBuffer* buffer) const = 0;
  
  virtual void drawElements(int mode,
                            int count,
                            IIntBuffer* indices) const = 0;
  
  virtual void lineWidth(float width) const = 0;
  
  virtual int getError() const = 0;
  
  virtual void blendFunc(int sfactor,
                         int dfactor) const = 0;
  
  virtual void bindTexture(int target,
                           const IGLTextureId* texture) const = 0;
  
  /* delete texture and answer if the textureId can be recycled */
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
  
  //  virtual void texImage2D(GLTextureType target,
  //                          int         level,
  //                          GLFormat    internalFormat,
  //                          int         width,
  //                          int         height,
  //                          int         border,
  //                          GLFormat    format,
  //                          GLType      type,
  //                          const void* data) const = 0;
  
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
  
  virtual int Primitive_Triangles() const = 0;
  virtual int Primitive_TriangleStrip() const = 0;
  virtual int Primitive_TriangleFan() const = 0;
  virtual int Primitive_Lines() const = 0;
  virtual int Primitive_LineStrip() const = 0;
  virtual int Primitive_LineLoop() const = 0;
  virtual int Primitive_Points() const = 0;
  
  virtual int BlendFactor_SrcAlpha() const = 0;
  virtual int BlendFactor_OneMinusSrcAlpha() const = 0;
  
  virtual int TextureType_Texture2D() const = 0;
  
  virtual int TextureParameter_MinFilter() const = 0;
  virtual int TextureParameter_MagFilter() const = 0;
  virtual int TextureParameter_WrapS() const = 0;
  virtual int TextureParameter_WrapT() const = 0;
  
  virtual int TextureParameterValue_Linear() const = 0;
  virtual int TextureParameterValue_ClampToEdge() const = 0;
  
  virtual int Alignment_Pack() const = 0;
  virtual int Alignment_Unpack() const = 0;
  
  virtual int Format_RGBA() const = 0;
  
  virtual int Variable_Viewport() const = 0;
  
  virtual int Error_NoError() const = 0;

};

#endif
