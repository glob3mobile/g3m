package org.glob3.mobile.generated; 
public abstract class INativeGL
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void useProgram(int program) const = 0;
  public abstract void useProgram(int program);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getAttribLocation(int program, const String& name) const = 0;
  public abstract int getAttribLocation(int program, String name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getUniformLocation(int program, const String& name) const = 0;
  public abstract int getUniformLocation(int program, String name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform2f(int loc, float x, float y) const = 0;
  public abstract void uniform2f(int loc, float x, float y);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1f(int loc, float x) const = 0;
  public abstract void uniform1f(int loc, float x);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1i(int loc, int v) const = 0;
  public abstract void uniform1i(int loc, int v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniformMatrix4fv(int location, int count, boolean transpose, const float value[]) const = 0;
  public abstract void uniformMatrix4fv(int location, int count, boolean transpose, float[] value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clearColor(float red, float green, float blue, float alpha) const = 0;
  public abstract void clearColor(float red, float green, float blue, float alpha);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clear(int nBuffer, GLBufferType buffers[]) const = 0;
  public abstract void clear(int nBuffer, GLBufferType[] buffers);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform4f(int location, float v0, float v1, float v2, float v3) const = 0;
  public abstract void uniform4f(int location, float v0, float v1, float v2, float v3);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enable(GLFeature feature) const = 0;
  public abstract void enable(GLFeature feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disable(GLFeature feature) const = 0;
  public abstract void disable(GLFeature feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void polygonOffset(float factor, float units) const = 0;
  public abstract void polygonOffset(float factor, float units);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer* buffer) const = 0;
  public abstract void vertexAttribPointer(int index, int size, boolean normalized, int stride, IFloatBuffer buffer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawElements(GLPrimitive mode, int count, IIntBuffer* indices) const = 0;
  public abstract void drawElements(GLPrimitive mode, int count, IIntBuffer indices);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void lineWidth(float width) const = 0;
  public abstract void lineWidth(float width);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual GLError getError() const = 0;
  public abstract GLError getError();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor) const = 0;
  public abstract void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void bindTexture(GLTextureType target, int texture) const = 0;
  public abstract void bindTexture(GLTextureType target, int texture);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteTextures(int n, const int textures[]) const = 0;
  public abstract void deleteTextures(int n, int[] textures);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enableVertexAttribArray(int location) const = 0;
  public abstract void enableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disableVertexAttribArray(int location) const = 0;
  public abstract void disableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void pixelStorei(GLAlignment pname, int param) const = 0;
  public abstract void pixelStorei(GLAlignment pname, int param);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GLTextureId> genTextures(int n) const = 0;
  public abstract java.util.ArrayList<GLTextureId> genTextures(int n);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texParameteri(GLTextureType target, GLTextureParameter par, GLTextureParameterValue v) const = 0;
  public abstract void texParameteri(GLTextureType target, GLTextureParameter par, GLTextureParameterValue v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texImage2D(const GLImage* glImage) const = 0;
  public abstract void texImage2D(GLImage glImage);

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
//ORIGINAL LINE: virtual void generateMipmap(GLTextureType target) const = 0;
  public abstract void generateMipmap(GLTextureType target);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawArrays(GLPrimitive mode, int first, int count) const = 0;
  public abstract void drawArrays(GLPrimitive mode, int first, int count);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void cullFace(GLCullFace c) const = 0;
  public abstract void cullFace(GLCullFace c);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void getIntegerv(GLVariable v, int i[]) const = 0;
  public abstract void getIntegerv(GLVariable v, int[] i);

}