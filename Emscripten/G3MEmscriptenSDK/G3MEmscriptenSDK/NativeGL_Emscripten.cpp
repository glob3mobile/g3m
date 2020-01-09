

#include "NativeGL_Emscripten.hpp"

#include <emscripten/emscripten.h>

#include "FloatBuffer_Emscripten.hpp"
#include "ShortBuffer_Emscripten.hpp"
#include "GLUniformID_Emscripten.hpp"
#include "GLTextureID_Emscripten.hpp"

#include "GPUProgram.hpp"
#include "Matrix44D.hpp"


using namespace emscripten;


NativeGL_Emscripten::NativeGL_Emscripten(const emscripten::val& gl) :
_gl(gl),
GL_FLOAT                        ( gl["FLOAT"                         ].as<unsigned long>() ),
GL_ARRAY_BUFFER                 ( gl["ARRAY_BUFFER"                  ].as<unsigned long>() ),
GL_STATIC_DRAW                  ( gl["STATIC_DRAW"                   ].as<unsigned long>() ),
GL_ELEMENT_ARRAY_BUFFER         ( gl["ELEMENT_ARRAY_BUFFER"          ].as<unsigned long>() ),
GL_UNSIGNED_SHORT               ( gl["UNSIGNED_SHORT"                ].as<unsigned long>() ),
GL_NO_ERROR                     ( gl["NO_ERROR"                      ].as<unsigned long>() ),
GL_INVALID_ENUM                 ( gl["INVALID_ENUM"                  ].as<unsigned long>() ),
GL_INVALID_VALUE                ( gl["INVALID_VALUE"                 ].as<unsigned long>() ),
GL_INVALID_OPERATION            ( gl["INVALID_OPERATION"             ].as<unsigned long>() ),
GL_INVALID_FRAMEBUFFER_OPERATION( gl["INVALID_FRAMEBUFFER_OPERATION" ].as<unsigned long>() ),
GL_OUT_OF_MEMORY                ( gl["OUT_OF_MEMORY"                 ].as<unsigned long>() ),
GL_CONTEXT_LOST_WEBGL           ( gl["CONTEXT_LOST_WEBGL"            ].as<unsigned long>() )
{
  
}

void NativeGL_Emscripten::useProgram(GPUProgram* program) const {
  //Check: On WEBGL FloatBuffers do not check if they are already bound
  
  val jsoProgram = _shaderList[ program->getProgramID() ];
  _gl.call<void>("useProgram", jsoProgram);
}

void NativeGL_Emscripten::uniform2f(const IGLUniformID* location,
                                    float x,
                                    float y) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  _gl.call<void>("uniform2f", id, x, y);
}

void NativeGL_Emscripten::uniform1f(const IGLUniformID* location,
                                    float x) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  _gl.call<void>("uniform1f", id, x);
}

void NativeGL_Emscripten::uniform1i(const IGLUniformID* location,
                                    int v) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  _gl.call<void>("uniform1i", id, v);
}

void NativeGL_Emscripten::uniformMatrix4fv(const IGLUniformID* location,
                                           bool transpose,
                                           const Matrix44D* matrix) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  
  const FloatBuffer_Emscripten* floatBuffer = (FloatBuffer_Emscripten*) matrix->getColumnMajorFloatBuffer();
  val buffer = floatBuffer->getBuffer();
  _gl.call<void>("uniformMatrix4fv", id, transpose, buffer);
}

void NativeGL_Emscripten::clearColor(float red,
                                     float green,
                                     float blue,
                                     float alpha) const {
  _gl.call<void>("clearColor", red, green, blue, alpha);
}

void NativeGL_Emscripten::clear(int buffers) const {
  _gl.call<void>("clear", buffers);
}

void NativeGL_Emscripten::uniform4f(const IGLUniformID* location,
                                    float v0,
                                    float v1,
                                    float v2,
                                    float v3) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  _gl.call<void>("uniform4f", id, v0, v1, v2, v3);
}

void NativeGL_Emscripten::uniform3f(const IGLUniformID* location,
                                    float v0,
                                    float v1,
                                    float v2) const {
  GLUniformID_Emscripten* locEM = (GLUniformID_Emscripten*) location;
  val id = locEM->getId();
  _gl.call<void>("uniform3f", id, v0, v1, v2);
}

void NativeGL_Emscripten::enable(int feature) const {
  _gl.call<void>("enable", feature);
}

void NativeGL_Emscripten::disable(int feature) const {
  _gl.call<void>("disable", feature);
}

void NativeGL_Emscripten::polygonOffset(float factor,
                                        float units) const {
  _gl.call<void>("polygonOffset", factor, units);
}

emscripten::val NativeGL_Emscripten::createBuffer() const {
  return _gl.call<emscripten::val>("createBuffer");
}

void NativeGL_Emscripten::bindBuffer(const emscripten::val& webGLBuffer) const {
  _gl.call<void>("bindBuffer", GL_ARRAY_BUFFER, webGLBuffer);
}

void NativeGL_Emscripten::bufferData(const emscripten::val& webGLBuffer) const {
  _gl.call<void>("bufferData", GL_ARRAY_BUFFER, webGLBuffer, GL_STATIC_DRAW);
}

void NativeGL_Emscripten::deleteBuffer(const emscripten::val& webGLBuffer) const {
  _gl.call<void>("deleteBuffer", webGLBuffer);
}

void NativeGL_Emscripten::vertexAttribPointer(int index,
                                              int size,
                                              bool normalized,
                                              int stride,
                                              const IFloatBuffer* buffer) const {
  val webGLBuffer = ((FloatBuffer_Emscripten*) buffer)->bindVBO(this);
  
  _gl.call<void>("vertexAttribPointer", index, size, GL_FLOAT, normalized, stride, 0);
}

void NativeGL_Emscripten::drawElements(int mode,
                                       int count,
                                       IShortBuffer* indices) const {
  ShortBuffer_Emscripten* indicesEM = (ShortBuffer_Emscripten*) indices;
  val webGLBuffer = indicesEM->getWebGLBuffer(this);
  
  _gl.call<void>("bindBuffer", GL_ELEMENT_ARRAY_BUFFER, webGLBuffer);
  
  val array = indicesEM->getBuffer();
  _gl.call<void>("bufferData", GL_ELEMENT_ARRAY_BUFFER, array, GL_STATIC_DRAW);
  
  _gl.call<void>("drawElements", mode, count, GL_UNSIGNED_SHORT, 0);
}

void NativeGL_Emscripten::lineWidth(float width) const {
  _gl.call<void>("lineWidth", width);
}

int NativeGL_Emscripten::getError() const {
  const int e = _gl.call<int>("getError");
  
  if (e == GL_INVALID_ENUM) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "NativeGL_Emscripten: INVALID_ENUM");
  }
  else if (e == GL_INVALID_VALUE) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "NativeGL_Emscripten: INVALID_VALUE");
  }
  else if (e == GL_INVALID_OPERATION) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "NativeGL_Emscripten: INVALID_OPERATION");
  }
  else if (e == GL_OUT_OF_MEMORY) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "NativeGL_Emscripten: INVALID_OPERATION");
  }
  else if (e == GL_CONTEXT_LOST_WEBGL) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "NativeGL_Emscripten: CONTEXT_LOST_WEBGL");
  }
  
  return e;
}

void NativeGL_Emscripten::blendFunc(int sfactor,
                                    int dfactor) const {
  _gl.call<void>("blendFunc", sfactor, dfactor);
}

void NativeGL_Emscripten::bindTexture(int target,
                                      const IGLTextureID* texture) const {
  GLTextureID_Emscripten* textureEM = (GLTextureID_Emscripten*) texture;
  val textureID = textureEM->getWebGLTexture();
  _gl.call<void>("bindTexture", target, textureID);
}

/* Delete Texture from GPU, and answer if the TextureID can be reused */
bool NativeGL_Emscripten::deleteTexture(const IGLTextureID* texture) const {
  GLTextureID_Emscripten* textureEM = (GLTextureID_Emscripten*) texture;
  val textureID = textureEM->getWebGLTexture();
  _gl.call<void>("deleteTexture", textureID);
  return false;
}

void NativeGL_Emscripten::enableVertexAttribArray(int location) const {
#error TODO
}

void NativeGL_Emscripten::disableVertexAttribArray(int location) const {
#error TODO
}

void NativeGL_Emscripten::pixelStorei(int pname,
                                      int param) const {
#error TODO
}

std::vector<IGLTextureID*> NativeGL_Emscripten::genTextures(int  n) const {
#error TODO
}

void NativeGL_Emscripten::texParameteri(int target,
                                        int par,
                                        int v) const {
#error TODO
}

void NativeGL_Emscripten::texImage2D(const IImage* image, int format) const {
#error TODO
}

void NativeGL_Emscripten::generateMipmap(int target) const {
#error TODO
}

void NativeGL_Emscripten::drawArrays(int mode,
                                     int first,
                                     int count) const {
#error TODO
}

void NativeGL_Emscripten::cullFace(int c) const {
#error TODO
}

void NativeGL_Emscripten::getIntegerv(int v, int i[]) const {
#error TODO
}


int NativeGL_Emscripten::CullFace_Front() const {
#error TODO
}
int NativeGL_Emscripten::CullFace_Back() const {
#error TODO
}
int NativeGL_Emscripten::CullFace_FrontAndBack() const {
#error TODO
}

int NativeGL_Emscripten::BufferType_ColorBuffer() const {
#error TODO
}
int NativeGL_Emscripten::BufferType_DepthBuffer() const {
#error TODO
}

int NativeGL_Emscripten::Feature_PolygonOffsetFill() const {
#error TODO
}
int NativeGL_Emscripten::Feature_DepthTest() const {
#error TODO
}
int NativeGL_Emscripten::Feature_Blend() const {
#error TODO
}
int NativeGL_Emscripten::Feature_CullFace() const {
#error TODO
}

int NativeGL_Emscripten::Type_Float() const {
#error TODO
}
int NativeGL_Emscripten::Type_UnsignedByte() const {
#error TODO
}
int NativeGL_Emscripten::Type_UnsignedInt() const {
#error TODO
}
int NativeGL_Emscripten::Type_Int() const {
#error TODO
}
int NativeGL_Emscripten::Type_Vec2Float() const {
#error TODO
}
int NativeGL_Emscripten::Type_Vec3Float() const {
#error TODO
}
int NativeGL_Emscripten::Type_Vec4Float() const {
#error TODO
}
int NativeGL_Emscripten::Type_Bool() const {
#error TODO
}
int NativeGL_Emscripten::Type_Matrix4Float() const {
#error TODO
}

int NativeGL_Emscripten::Primitive_Triangles() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_TriangleStrip() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_TriangleFan() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_Lines() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_LineStrip() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_LineLoop() const {
#error TODO
}
int NativeGL_Emscripten::Primitive_Points() const {
#error TODO
}

int NativeGL_Emscripten::BlendFactor_One() const {
#error TODO
}
int NativeGL_Emscripten::BlendFactor_Zero() const {
#error TODO
}
int NativeGL_Emscripten::BlendFactor_SrcAlpha() const {
#error TODO
}
int NativeGL_Emscripten::BlendFactor_OneMinusSrcAlpha() const {
#error TODO
}

int NativeGL_Emscripten::TextureType_Texture2D() const {
#error TODO
}

int NativeGL_Emscripten::TextureParameter_MinFilter() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameter_MagFilter() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameter_WrapS() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameter_WrapT() const {
#error TODO
}

int NativeGL_Emscripten::TextureParameterValue_Nearest() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_Linear() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_NearestMipmapNearest() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_NearestMipmapLinear() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_LinearMipmapNearest() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_LinearMipmapLinear() const {
#error TODO
}

/* TextureWrapMode */
int NativeGL_Emscripten::TextureParameterValue_Repeat() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_ClampToEdge() const {
#error TODO
}
int NativeGL_Emscripten::TextureParameterValue_MirroredRepeat() const {
#error TODO
}

int NativeGL_Emscripten::Alignment_Pack() const {
#error TODO
}
int NativeGL_Emscripten::Alignment_Unpack() const {
#error TODO
}

int NativeGL_Emscripten::Format_RGBA() const {
#error TODO
}

int NativeGL_Emscripten::Variable_Viewport() const {
#error TODO
}
int NativeGL_Emscripten::Variable_ActiveAttributes() const {
#error TODO
}
int NativeGL_Emscripten::Variable_ActiveUniforms() const {
#error TODO
}

int NativeGL_Emscripten::Error_NoError() const {
#error TODO
}

int NativeGL_Emscripten::createProgram() const {
#error TODO
}
bool NativeGL_Emscripten::deleteProgram(int program) const {
#error TODO
}
void NativeGL_Emscripten::attachShader(int program, int shader) const {
#error TODO
}
int NativeGL_Emscripten::createShader(ShaderType type) const {
#error TODO
}
bool NativeGL_Emscripten::compileShader (int shader, const std::string& source) const {
#error TODO
}
bool NativeGL_Emscripten::deleteShader(int shader) const {
#error TODO
}
void NativeGL_Emscripten::printShaderInfoLog(int shader) const {
#error TODO
}
bool NativeGL_Emscripten::linkProgram(int program) const {
#error TODO
}
void NativeGL_Emscripten::printProgramInfoLog(int program) const {
#error TODO
}

void NativeGL_Emscripten::bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const {
#error TODO
}

int NativeGL_Emscripten::getProgramiv(const GPUProgram* program, int param) const {
#error TODO
}

GPUUniform* NativeGL_Emscripten::getActiveUniform(const GPUProgram* program, int i) const {
#error TODO
}
GPUAttribute* NativeGL_Emscripten::getActiveAttribute(const GPUProgram* program, int i) const {
#error TODO
}

void NativeGL_Emscripten::depthMask(bool v) const {
#error TODO
}

void NativeGL_Emscripten::setActiveTexture(int i) const {
#error TODO
}

void NativeGL_Emscripten::viewport(int x, int y, int width, int height) const {
#error TODO
}
