package org.glob3.mobile.generated; 
//
//  INativeGL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class IFloatBuffer;
//class IIntBuffer;
//class IShortBuffer;
//class IImage;
//class IGLUniformID;
//class IGLTextureId;
//class Matrix44D;



//class GPUProgram;

//class GPUUniform;
//class GPUAttribute;

public abstract class INativeGL
{

  public void dispose()
  {
  }

  public abstract void useProgram(GPUProgram program);

  public abstract void uniform2f(IGLUniformID loc, float x, float y);

  public abstract void uniform1f(IGLUniformID loc, float x);

  public abstract void uniform1i(IGLUniformID loc, int v);

  public abstract void uniformMatrix4fv(IGLUniformID location, boolean transpose, Matrix44D matrix);

  public abstract void clearColor(float red, float green, float blue, float alpha);

  public abstract void clear(int buffers);

  public abstract void uniform4f(IGLUniformID location, float v0, float v1, float v2, float v3);

  public abstract void uniform3f(IGLUniformID location, float v0, float v1, float v2);

  public abstract void enable(int feature);

  public abstract void disable(int feature);

  public abstract void polygonOffset(float factor, float units);

  public abstract void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer buffer);

  public abstract void drawElements(int mode, int count, IShortBuffer indices);

  public abstract void lineWidth(float width);

  public abstract int getError();

  public abstract void blendFunc(int sfactor, int dfactor);

  public abstract void bindTexture(int target, IGLTextureId texture);

  /* Delete Texture from GPU, and answer if the TextureId can be reused */
  public abstract boolean deleteTexture(IGLTextureId texture);

  public abstract void enableVertexAttribArray(int location);

  public abstract void disableVertexAttribArray(int location);

  public abstract void pixelStorei(int pname, int param);

  public abstract java.util.ArrayList<IGLTextureId> genTextures(int n);

  public abstract void texParameteri(int target, int par, int v);

  public abstract void texImage2D(IImage image, int format);

  public abstract void generateMipmap(int target);

  public abstract void drawArrays(int mode, int first, int count);

  public abstract void cullFace(int c);

  public abstract void getIntegerv(int v, int[] i);


  public abstract int CullFace_Front();
  public abstract int CullFace_Back();
  public abstract int CullFace_FrontAndBack();

  public abstract int BufferType_ColorBuffer();
  public abstract int BufferType_DepthBuffer();

  public abstract int Feature_PolygonOffsetFill();
  public abstract int Feature_DepthTest();
  public abstract int Feature_Blend();
  public abstract int Feature_CullFace();

  public abstract int Type_Float();
  public abstract int Type_UnsignedByte();
  public abstract int Type_UnsignedInt();
  public abstract int Type_Int();
  public abstract int Type_Vec2Float();
  public abstract int Type_Vec3Float();
  public abstract int Type_Vec4Float();
  public abstract int Type_Bool();
  public abstract int Type_Matrix4Float();

  public abstract int Primitive_Triangles();
  public abstract int Primitive_TriangleStrip();
  public abstract int Primitive_TriangleFan();
  public abstract int Primitive_Lines();
  public abstract int Primitive_LineStrip();
  public abstract int Primitive_LineLoop();
  public abstract int Primitive_Points();

  public abstract int BlendFactor_One();
  public abstract int BlendFactor_Zero();
  public abstract int BlendFactor_SrcAlpha();
  public abstract int BlendFactor_OneMinusSrcAlpha();

  public abstract int TextureType_Texture2D();

  public abstract int TextureParameter_MinFilter();
  public abstract int TextureParameter_MagFilter();
  public abstract int TextureParameter_WrapS();
  public abstract int TextureParameter_WrapT();

  public abstract int TextureParameterValue_Nearest();
  public abstract int TextureParameterValue_Linear();
  public abstract int TextureParameterValue_NearestMipmapNearest();
  public abstract int TextureParameterValue_NearestMipmapLinear();
  public abstract int TextureParameterValue_LinearMipmapNearest();
  public abstract int TextureParameterValue_LinearMipmapLinear();

  public abstract int TextureParameterValue_ClampToEdge();

  public abstract int Alignment_Pack();
  public abstract int Alignment_Unpack();

  public abstract int Format_RGBA();

  public abstract int Variable_Viewport();
  public abstract int Variable_ActiveAttributes();
  public abstract int Variable_ActiveUniforms();

  public abstract int Error_NoError();

  public abstract int createProgram();
  public abstract boolean deleteProgram(int program);
  public abstract void attachShader(int program, int shader);
  public abstract int createShader(ShaderType type);
  public abstract boolean compileShader (int shader, String source);
  public abstract boolean deleteShader(int shader);
  public abstract void printShaderInfoLog(int shader);
  public abstract boolean linkProgram(int program);
  public abstract void printProgramInfoLog(int program);

  public abstract void bindAttribLocation(GPUProgram program, int loc, String name);

  public abstract int getProgramiv(GPUProgram program, int param);

  public abstract GPUUniform getActiveUniform(GPUProgram program, int i);
  public abstract GPUAttribute getActiveAttribute(GPUProgram program, int i);

  public abstract void depthMask(boolean v);

  public abstract void setActiveTexture(int i);

}