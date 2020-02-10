
#ifndef NativeGL_Emscripten_hpp
#define NativeGL_Emscripten_hpp

#include "G3M/INativeGL.hpp"

#include <emscripten/val.h>


class NativeGL_Emscripten : public INativeGL {
private:
  emscripten::val _gl;

  const int GL_FLOAT;
  const int GL_ARRAY_BUFFER;
  const int GL_STATIC_DRAW;
  const int GL_ELEMENT_ARRAY_BUFFER;
  const int GL_UNSIGNED_SHORT;
  const int GL_NO_ERROR;
  const int GL_INVALID_ENUM;
  const int GL_INVALID_VALUE;
  const int GL_INVALID_OPERATION;
  const int GL_INVALID_FRAMEBUFFER_OPERATION;
  const int GL_OUT_OF_MEMORY;
  const int GL_CONTEXT_LOST_WEBGL;
  const int GL_FRONT;
  const int GL_BACK;
  const int GL_FRONT_AND_BACK;
  const int GL_COLOR_BUFFER_BIT;
  const int GL_DEPTH_BUFFER_BIT;
  const int GL_POLYGON_OFFSET_FILL;
  const int GL_DEPTH_TEST;
  const int GL_BLEND;
  const int GL_CULL_FACE;
  const int GL_TRIANGLES;
  const int GL_TRIANGLE_STRIP;
  const int GL_TRIANGLE_FAN;
  const int GL_LINES;
  const int GL_LINE_STRIP;
  const int GL_LINE_LOOP;
  const int GL_POINTS;
  const int GL_UNSIGNED_BYTE;
  const int GL_UNSIGNED_INT;
  const int GL_INT;
  const int GL_FLOAT_VEC2;
  const int GL_FLOAT_VEC3;
  const int GL_FLOAT_VEC4;
  const int GL_BOOL;
  const int GL_FLOAT_MAT4;
  const int GL_ONE;
  const int GL_ZERO;
  const int GL_SRC_ALPHA;
  const int GL_ONE_MINUS_SRC_ALPHA;
  const int GL_TEXTURE_2D;
  const int GL_TEXTURE_MIN_FILTER;
  const int GL_TEXTURE_MAG_FILTER;
  const int GL_TEXTURE_WRAP_S;
  const int GL_TEXTURE_WRAP_T;
  const int GL_NEAREST;
  const int GL_LINEAR;
  const int GL_NEAREST_MIPMAP_NEAREST;
  const int GL_NEAREST_MIPMAP_LINEAR;
  const int GL_LINEAR_MIPMAP_NEAREST;
  const int GL_LINEAR_MIPMAP_LINEAR;
  const int GL_REPEAT;
  const int GL_CLAMP_TO_EDGE;
  const int GL_MIRRORED_REPEAT;
  const int GL_PACK_ALIGNMENT;
  const int GL_UNPACK_ALIGNMENT;
  const int GL_RGBA;
  const int GL_VIEWPORT;
  const int GL_ACTIVE_ATTRIBUTES;
  const int GL_ACTIVE_UNIFORMS;
  const int GL_TEXTURE0;
  const int GL_VERTEX_SHADER;
  const int GL_FRAGMENT_SHADER;
  const int GL_COMPILE_STATUS;
  const int GL_LINK_STATUS;
  const int GL_SAMPLER_2D;

  mutable std::vector<emscripten::val> _shaderList;


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
  bool compileShader(int shader, const std::string& source) const;
  bool deleteShader(int shader) const;
  void printShaderInfoLog(int shader) const;
  bool linkProgram(int program) const;
  void printProgramInfoLog(int program) const;

  void bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const;

  int getProgramiv(const GPUProgram* program, int param) const;

  GPUUniform* getActiveUniform(const GPUProgram* program, int i) const;
  GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const;

  void depthMask(bool depthMask) const;

  void setActiveTexture(int i) const;

  void viewport(int x, int y, int width, int height) const;


  emscripten::val createBuffer() const;

  void bindBuffer(const emscripten::val& webGLBuffer) const;

  void bufferData(const emscripten::val& webGLBuffer) const;

  void deleteBuffer(const emscripten::val& webGLBuffer) const;

};

#endif
