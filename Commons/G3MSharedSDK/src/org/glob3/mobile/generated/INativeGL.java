package org.glob3.mobile.generated; 
//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLProgramId;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLUniformID;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableMatrix44D;

public abstract class INativeGL
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void useProgram(IGLProgramId* program) const = 0;
  public abstract void useProgram(IGLProgramId program);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getAttribLocation(IGLProgramId* program, const String& name) const = 0;
  public abstract int getAttribLocation(IGLProgramId program, String name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IGLUniformID* getUniformLocation(IGLProgramId* program, const String& name) const = 0;
  public abstract IGLUniformID getUniformLocation(IGLProgramId program, String name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform2f(IGLUniformID* loc, float x, float y) const = 0;
  public abstract void uniform2f(IGLUniformID loc, float x, float y);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1f(IGLUniformID* loc, float x) const = 0;
  public abstract void uniform1f(IGLUniformID loc, float x);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1i(IGLUniformID* loc, int v) const = 0;
  public abstract void uniform1i(IGLUniformID loc, int v);

//  virtual void uniformMatrix4fv(IGLUniformID* location,
//                                bool transpose,
//                                const IFloatBuffer* buffer) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniformMatrix4fv(IGLUniformID* location, boolean transpose, const MutableMatrix44D* matrix) const = 0;
  public abstract void uniformMatrix4fv(IGLUniformID location, boolean transpose, MutableMatrix44D matrix);


//  virtual void uniformMatrix4fv(IGLUniformID* location,
//                                int count,
//                                bool transpose,
//                                const float value[]) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clearColor(float red, float green, float blue, float alpha) const = 0;
  public abstract void clearColor(float red, float green, float blue, float alpha);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clear(int buffers) const = 0;
  public abstract void clear(int buffers);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform4f(IGLUniformID* location, float v0, float v1, float v2, float v3) const = 0;
  public abstract void uniform4f(IGLUniformID location, float v0, float v1, float v2, float v3);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enable(int feature) const = 0;
  public abstract void enable(int feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disable(int feature) const = 0;
  public abstract void disable(int feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void polygonOffset(float factor, float units) const = 0;
  public abstract void polygonOffset(float factor, float units);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer* buffer) const = 0;
  public abstract void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer buffer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawElements(int mode, int count, IIntBuffer* indices) const = 0;
  public abstract void drawElements(int mode, int count, IIntBuffer indices);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void lineWidth(float width) const = 0;
  public abstract void lineWidth(float width);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getError() const = 0;
  public abstract int getError();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void blendFunc(int sfactor, int dfactor) const = 0;
  public abstract void blendFunc(int sfactor, int dfactor);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void bindTexture(int target, const IGLTextureId* texture) const = 0;
  public abstract void bindTexture(int target, IGLTextureId texture);

  /* delete texture and answer if the textureId can be recycled */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean deleteTexture(const IGLTextureId* texture) const = 0;
  public abstract boolean deleteTexture(IGLTextureId texture);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enableVertexAttribArray(int location) const = 0;
  public abstract void enableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disableVertexAttribArray(int location) const = 0;
  public abstract void disableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void pixelStorei(int pname, int param) const = 0;
  public abstract void pixelStorei(int pname, int param);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<IGLTextureId*> genTextures(int n) const = 0;
  public abstract java.util.ArrayList<IGLTextureId> genTextures(int n);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texParameteri(int target, int par, int v) const = 0;
  public abstract void texParameteri(int target, int par, int v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texImage2D(const IImage* image, int format) const = 0;
  public abstract void texImage2D(IImage image, int format);

  //  virtual void texImage2D(GLTextureType target,
  //                          int         level,
  //                          GLFormat    internalFormat,
  //                          int         width,
  //                          int         height,
  //                          int         border,
  //                          GLFormat    format,
  //                          GLType      type,
  //                          const void* data) const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void generateMipmap(int target) const = 0;
  public abstract void generateMipmap(int target);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawArrays(int mode, int first, int count) const = 0;
  public abstract void drawArrays(int mode, int first, int count);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void cullFace(int c) const = 0;
  public abstract void cullFace(int c);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void getIntegerv(int v, int i[]) const = 0;
  public abstract void getIntegerv(int v, int[] i);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int CullFace_Front() const = 0;
  public abstract int CullFace_Front();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int CullFace_Back() const = 0;
  public abstract int CullFace_Back();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int CullFace_FrontAndBack() const = 0;
  public abstract int CullFace_FrontAndBack();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int BufferType_ColorBuffer() const = 0;
  public abstract int BufferType_ColorBuffer();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int BufferType_DepthBuffer() const = 0;
  public abstract int BufferType_DepthBuffer();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Feature_PolygonOffsetFill() const = 0;
  public abstract int Feature_PolygonOffsetFill();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Feature_DepthTest() const = 0;
  public abstract int Feature_DepthTest();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Feature_Blend() const = 0;
  public abstract int Feature_Blend();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Feature_CullFace() const = 0;
  public abstract int Feature_CullFace();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Type_Float() const = 0;
  public abstract int Type_Float();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Type_UnsignedByte() const = 0;
  public abstract int Type_UnsignedByte();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Type_UnsignedInt() const = 0;
  public abstract int Type_UnsignedInt();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Type_Int() const = 0;
  public abstract int Type_Int();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_Triangles() const = 0;
  public abstract int Primitive_Triangles();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_TriangleStrip() const = 0;
  public abstract int Primitive_TriangleStrip();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_TriangleFan() const = 0;
  public abstract int Primitive_TriangleFan();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_Lines() const = 0;
  public abstract int Primitive_Lines();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_LineStrip() const = 0;
  public abstract int Primitive_LineStrip();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_LineLoop() const = 0;
  public abstract int Primitive_LineLoop();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Primitive_Points() const = 0;
  public abstract int Primitive_Points();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int BlendFactor_SrcAlpha() const = 0;
  public abstract int BlendFactor_SrcAlpha();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int BlendFactor_OneMinusSrcAlpha() const = 0;
  public abstract int BlendFactor_OneMinusSrcAlpha();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureType_Texture2D() const = 0;
  public abstract int TextureType_Texture2D();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameter_MinFilter() const = 0;
  public abstract int TextureParameter_MinFilter();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameter_MagFilter() const = 0;
  public abstract int TextureParameter_MagFilter();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameter_WrapS() const = 0;
  public abstract int TextureParameter_WrapS();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameter_WrapT() const = 0;
  public abstract int TextureParameter_WrapT();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameterValue_Linear() const = 0;
  public abstract int TextureParameterValue_Linear();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int TextureParameterValue_ClampToEdge() const = 0;
  public abstract int TextureParameterValue_ClampToEdge();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Alignment_Pack() const = 0;
  public abstract int Alignment_Pack();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Alignment_Unpack() const = 0;
  public abstract int Alignment_Unpack();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Format_RGBA() const = 0;
  public abstract int Format_RGBA();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Variable_Viewport() const = 0;
  public abstract int Variable_Viewport();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int Error_NoError() const = 0;
  public abstract int Error_NoError();

}