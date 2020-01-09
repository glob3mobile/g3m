
#ifndef NativeGL_Emscripten_hpp
#define NativeGL_Emscripten_hpp

#include "INativeGL.hpp"

#include <emscripten/val.h>


class NativeGL_Emscripten : public INativeGL {
private:
  emscripten::val _gl;

  const unsigned long GL_FLOAT;
  const unsigned long GL_ARRAY_BUFFER;
  const unsigned long GL_STATIC_DRAW;
  const unsigned long GL_ELEMENT_ARRAY_BUFFER;
  const unsigned long GL_UNSIGNED_SHORT;
  const unsigned long GL_NO_ERROR;
  const unsigned long GL_INVALID_ENUM;
  const unsigned long GL_INVALID_VALUE;
  const unsigned long GL_INVALID_OPERATION;
  const unsigned long GL_INVALID_FRAMEBUFFER_OPERATION;
  const unsigned long GL_OUT_OF_MEMORY;
  const unsigned long GL_CONTEXT_LOST_WEBGL;

  std::vector<emscripten::val> _shaderList;


public:
  NativeGL_Emscripten(const emscripten::val& gl);

  void useProgram(GPUProgram* program) const;

  void uniform2f(const IGLUniformID* location,
                 float x,
                 float y) const;

  void uniform1f(const IGLUniformID* location,
                 float x) const;

  void uniform1i(const IGLUniformID* location,
                 int v) const;

  void uniformMatrix4fv(const IGLUniformID* location,
                        bool transpose,
                        const Matrix44D* matrix) const;

  void clearColor(float red,
                  float green,
                  float blue,
                  float alpha) const;

  void clear(int buffers) const;

  void uniform4f(const IGLUniformID* location,
                 float v0,
                 float v1,
                 float v2,
                 float v3) const;

  void uniform3f(const IGLUniformID* location,
                 float v0,
                 float v1,
                 float v2) const;

  void enable(int feature) const;

  void disable(int feature) const;

  void polygonOffset(float factor,
                     float units) const;

  void vertexAttribPointer(int index,
                           int size,
                           bool normalized,
                           int stride,
                           const IFloatBuffer* buffer) const;

  void drawElements(int mode,
                    int count,
                    IShortBuffer* indices) const;

  void lineWidth(float width) const;

  int getError() const;

  void blendFunc(int sfactor,
                 int dfactor) const;

  void bindTexture(int target,
                   const IGLTextureID* texture) const;

  /* Delete Texture from GPU, and answer if the TextureID can be reused */
  bool deleteTexture(const IGLTextureID* texture) const;

  void enableVertexAttribArray(int location) const;

  void disableVertexAttribArray(int location) const;

  void pixelStorei(int pname,
                   int param) const;

  std::vector<IGLTextureID*> genTextures(int  n) const;

  void texParameteri(int target,
                     int par,
                     int v) const;

  void texImage2D(const IImage* image, int format) const;

  void generateMipmap(int target) const;

  void drawArrays(int mode,
                  int first,
                  int count) const;

  void cullFace(int c) const;

  void getIntegerv(int v, int i[]) const;


  int CullFace_Front() const;
  int CullFace_Back() const;
  int CullFace_FrontAndBack() const;

  int BufferType_ColorBuffer() const;
  int BufferType_DepthBuffer() const;

  int Feature_PolygonOffsetFill() const;
  int Feature_DepthTest() const;
  int Feature_Blend() const;
  int Feature_CullFace() const;

  int Type_Float() const;
  int Type_UnsignedByte() const;
  int Type_UnsignedInt() const;
  int Type_Int() const;
  int Type_Vec2Float() const;
  int Type_Vec3Float() const;
  int Type_Vec4Float() const;
  int Type_Bool() const;
  int Type_Matrix4Float() const;

  int Primitive_Triangles() const;
  int Primitive_TriangleStrip() const;
  int Primitive_TriangleFan() const;
  int Primitive_Lines() const;
  int Primitive_LineStrip() const;
  int Primitive_LineLoop() const;
  int Primitive_Points() const;

  int BlendFactor_One() const;
  int BlendFactor_Zero() const;
  int BlendFactor_SrcAlpha() const;
  int BlendFactor_OneMinusSrcAlpha() const;

  int TextureType_Texture2D() const;

  int TextureParameter_MinFilter() const;
  int TextureParameter_MagFilter() const;
  int TextureParameter_WrapS() const;
  int TextureParameter_WrapT() const;

  int TextureParameterValue_Nearest() const;
  int TextureParameterValue_Linear() const;
  int TextureParameterValue_NearestMipmapNearest() const;
  int TextureParameterValue_NearestMipmapLinear() const;
  int TextureParameterValue_LinearMipmapNearest() const;
  int TextureParameterValue_LinearMipmapLinear() const;

  /* TextureWrapMode */
  int TextureParameterValue_Repeat() const;
  int TextureParameterValue_ClampToEdge() const;
  int TextureParameterValue_MirroredRepeat() const;

  int Alignment_Pack() const;
  int Alignment_Unpack() const;

  int Format_RGBA() const;

  int Variable_Viewport() const;
  int Variable_ActiveAttributes() const;
  int Variable_ActiveUniforms() const;

  int Error_NoError() const;

  int createProgram() const;
  bool deleteProgram(int program) const;
  void attachShader(int program, int shader) const;
  int createShader(ShaderType type) const;
  bool compileShader (int shader, const std::string& source) const;
  bool deleteShader(int shader) const;
  void printShaderInfoLog(int shader) const;
  bool linkProgram(int program) const;
  void printProgramInfoLog(int program) const;

  void bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const;

  int getProgramiv(const GPUProgram* program, int param) const;

  GPUUniform* getActiveUniform(const GPUProgram* program, int i) const;
  GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const;

  void depthMask(bool v) const;

  void setActiveTexture(int i) const;

  void viewport(int x, int y, int width, int height) const;


  emscripten::val createBuffer() const;

  void bindBuffer(const emscripten::val& webGLBuffer) const;

  void bufferData(const emscripten::val& webGLBuffer) const;

  void deleteBuffer(const emscripten::val& webGLBuffer) const;

};

#endif
