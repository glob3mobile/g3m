package org.glob3.mobile.generated; 
public interface INativeGL
{

  public void dispose()

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void useProgram(int program) const = 0;
  void useProgram(int program);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getAttribLocation(int program, const sbyte name[]) const = 0;
  int getAttribLocation(int program, RefObject<String> name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getUniformLocation(int program, const sbyte name[]) const = 0;
  int getUniformLocation(int program, RefObject<String> name);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform2f(int loc, float x, float y) const = 0;
  void uniform2f(int loc, float x, float y);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1f(int loc, float x) const = 0;
  void uniform1f(int loc, float x);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform1i(int loc, int v) const = 0;
  void uniform1i(int loc, int v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniformMatrix4fv(int location, int count, boolean transpose, const float value[]) const = 0;
  void uniformMatrix4fv(int location, int count, boolean transpose, float[] value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clearColor(float red, float green, float blue, float alpha) const = 0;
  void clearColor(float red, float green, float blue, float alpha);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void clear(int nBuffer, GLBufferType buffers[]) const = 0;
  void clear(int nBuffer, GLBufferType[] buffers);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void uniform4f(int location, float v0, float v1, float v2, float v3) const = 0;
  void uniform4f(int location, float v0, float v1, float v2, float v3);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enable(GLFeature feature) const = 0;
  void enable(GLFeature feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disable(GLFeature feature) const = 0;
  void disable(GLFeature feature);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void polygonOffset(float factor, float units) const = 0;
  void polygonOffset(float factor, float units);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void vertexAttribPointer(int index, int size, GLType type, boolean normalized, int stride, const Object* pointer) const = 0;
  void vertexAttribPointer(int index, int size, GLType type, boolean normalized, int stride, Object pointer);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawElements(GLPrimitive mode, int count, GLType type, const Object* indices) const = 0;
  void drawElements(GLPrimitive mode, int count, GLType type, Object indices);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void lineWidth(float width) const = 0;
  void lineWidth(float width);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual GLError getError() const = 0;
  GLError getError();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor) const = 0;
  void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void bindTexture(GLTextureType target, int texture) const = 0;
  void bindTexture(GLTextureType target, int texture);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void deleteTextures(int n, const int textures[]) const = 0;
  void deleteTextures(int n, int[] textures);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void enableVertexAttribArray(int location) const = 0;
  void enableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void disableVertexAttribArray(int location) const = 0;
  void disableVertexAttribArray(int location);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void pixelStorei(GLAlignment pname, int param) const = 0;
  void pixelStorei(GLAlignment pname, int param);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<int> genTextures(int n) const = 0;
  java.util.ArrayList<Integer> genTextures(int n);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texParameteri(GLTextureType target, GLTextureParameter par, GLTextureParameterValue v) const = 0;
  void texParameteri(GLTextureType target, GLTextureParameter par, GLTextureParameterValue v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void texImage2D(GLTextureType target, int level, int internalFormat, int width, int height, int border, GLFormat format, GLType type, const Object* data) const = 0;
  void texImage2D(GLTextureType target, int level, int internalFormat, int width, int height, int border, GLFormat format, GLType type, Object data);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void drawArrays(GLPrimitive mode, int first, int count) const = 0;
  void drawArrays(GLPrimitive mode, int first, int count);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void cullFace(CullFace c) const = 0;
  void cullFace(CullFace c);



}