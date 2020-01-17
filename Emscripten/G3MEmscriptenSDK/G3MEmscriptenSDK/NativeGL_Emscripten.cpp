

#include "NativeGL_Emscripten.hpp"

#include <emscripten/emscripten.h>

#include "FloatBuffer_Emscripten.hpp"
#include "ShortBuffer_Emscripten.hpp"
#include "GLUniformID_Emscripten.hpp"
#include "GLTextureID_Emscripten.hpp"
#include "Image_Emscripten.hpp"

#include "GPUProgram.hpp"
#include "Matrix44D.hpp"
#include "GPUUniform.hpp"
#include "GPUAttributeVec3Float.hpp"
#include "GPUAttributeVec4Float.hpp"
#include "GPUAttributeVec2Float.hpp"

using namespace emscripten;


NativeGL_Emscripten::NativeGL_Emscripten(const val& gl) :
_gl(gl),
GL_FLOAT                        ( gl["FLOAT"                         ].as<int>() ),
GL_ARRAY_BUFFER                 ( gl["ARRAY_BUFFER"                  ].as<int>() ),
GL_STATIC_DRAW                  ( gl["STATIC_DRAW"                   ].as<int>() ),
GL_ELEMENT_ARRAY_BUFFER         ( gl["ELEMENT_ARRAY_BUFFER"          ].as<int>() ),
GL_UNSIGNED_SHORT               ( gl["UNSIGNED_SHORT"                ].as<int>() ),
GL_NO_ERROR                     ( gl["NO_ERROR"                      ].as<int>() ),
GL_INVALID_ENUM                 ( gl["INVALID_ENUM"                  ].as<int>() ),
GL_INVALID_VALUE                ( gl["INVALID_VALUE"                 ].as<int>() ),
GL_INVALID_OPERATION            ( gl["INVALID_OPERATION"             ].as<int>() ),
GL_INVALID_FRAMEBUFFER_OPERATION( gl["INVALID_FRAMEBUFFER_OPERATION" ].as<int>() ),
GL_OUT_OF_MEMORY                ( gl["OUT_OF_MEMORY"                 ].as<int>() ),
GL_CONTEXT_LOST_WEBGL           ( gl["CONTEXT_LOST_WEBGL"            ].as<int>() ),
GL_FRONT                        ( gl["FRONT"                         ].as<int>() ),
GL_BACK                         ( gl["BACK"                          ].as<int>() ),
GL_FRONT_AND_BACK               ( gl["FRONT_AND_BACK"                ].as<int>() ),
GL_COLOR_BUFFER_BIT             ( gl["COLOR_BUFFER_BIT"              ].as<int>() ),
GL_DEPTH_BUFFER_BIT             ( gl["DEPTH_BUFFER_BIT"              ].as<int>() ),
GL_POLYGON_OFFSET_FILL          ( gl["POLYGON_OFFSET_FILL"           ].as<int>() ),
GL_DEPTH_TEST                   ( gl["DEPTH_TEST"                    ].as<int>() ),
GL_BLEND                        ( gl["BLEND"                         ].as<int>() ),
GL_CULL_FACE                    ( gl["CULL_FACE"                     ].as<int>() ),
GL_TRIANGLES                    ( gl["TRIANGLES"                     ].as<int>() ),
GL_TRIANGLE_STRIP               ( gl["TRIANGLE_STRIP"                ].as<int>() ),
GL_TRIANGLE_FAN                 ( gl["TRIANGLE_FAN"                  ].as<int>() ),
GL_LINES                        ( gl["LINES"                         ].as<int>() ),
GL_LINE_STRIP                   ( gl["LINE_STRIP"                    ].as<int>() ),
GL_LINE_LOOP                    ( gl["LINE_LOOP"                     ].as<int>() ),
GL_POINTS                       ( gl["POINTS"                        ].as<int>() ),
GL_UNSIGNED_BYTE                ( gl["UNSIGNED_BYTE"                 ].as<int>() ),
GL_UNSIGNED_INT                 ( gl["UNSIGNED_INT"                  ].as<int>() ),
GL_INT                          ( gl["INT"                           ].as<int>() ),
GL_FLOAT_VEC2                   ( gl["FLOAT_VEC2"                    ].as<int>() ),
GL_FLOAT_VEC3                   ( gl["FLOAT_VEC3"                    ].as<int>() ),
GL_FLOAT_VEC4                   ( gl["FLOAT_VEC4"                    ].as<int>() ),
GL_BOOL                         ( gl["BOOL"                          ].as<int>() ),
GL_FLOAT_MAT4                   ( gl["FLOAT_MAT4"                    ].as<int>() ),
GL_ONE                          ( gl["ONE"                           ].as<int>() ),
GL_ZERO                         ( gl["ZERO"                          ].as<int>() ),
GL_SRC_ALPHA                    ( gl["SRC_ALPHA"                     ].as<int>() ),
GL_ONE_MINUS_SRC_ALPHA          ( gl["ONE_MINUS_SRC_ALPHA"           ].as<int>() ),
GL_TEXTURE_2D                   ( gl["TEXTURE_2D"                    ].as<int>() ),
GL_TEXTURE_MIN_FILTER           ( gl["TEXTURE_MIN_FILTER"            ].as<int>() ),
GL_TEXTURE_MAG_FILTER           ( gl["TEXTURE_MAG_FILTER"            ].as<int>() ),
GL_TEXTURE_WRAP_S               ( gl["TEXTURE_WRAP_S"                ].as<int>() ),
GL_TEXTURE_WRAP_T               ( gl["TEXTURE_WRAP_T"                ].as<int>() ),
GL_NEAREST                      ( gl["NEAREST"                       ].as<int>() ),
GL_LINEAR                       ( gl["LINEAR"                        ].as<int>() ),
GL_NEAREST_MIPMAP_NEAREST       ( gl["NEAREST_MIPMAP_NEAREST"        ].as<int>() ),
GL_NEAREST_MIPMAP_LINEAR        ( gl["NEAREST_MIPMAP_LINEAR"         ].as<int>() ),
GL_LINEAR_MIPMAP_NEAREST        ( gl["LINEAR_MIPMAP_NEAREST"         ].as<int>() ),
GL_LINEAR_MIPMAP_LINEAR         ( gl["LINEAR_MIPMAP_LINEAR"          ].as<int>() ),
GL_REPEAT                       ( gl["REPEAT"                        ].as<int>() ),
GL_CLAMP_TO_EDGE                ( gl["CLAMP_TO_EDGE"                 ].as<int>() ),
GL_MIRRORED_REPEAT              ( gl["MIRRORED_REPEAT"               ].as<int>() ),
GL_PACK_ALIGNMENT               ( gl["PACK_ALIGNMENT"                ].as<int>() ),
GL_UNPACK_ALIGNMENT             ( gl["UNPACK_ALIGNMENT"              ].as<int>() ),
GL_RGBA                         ( gl["RGBA"                          ].as<int>() ),
GL_VIEWPORT                     ( gl["VIEWPORT"                      ].as<int>() ),
GL_ACTIVE_ATTRIBUTES            ( gl["ACTIVE_ATTRIBUTES"             ].as<int>() ),
GL_ACTIVE_UNIFORMS              ( gl["ACTIVE_UNIFORMS"               ].as<int>() ),
GL_TEXTURE0                     ( gl["TEXTURE0"                      ].as<int>() ),
GL_VERTEX_SHADER                ( gl["VERTEX_SHADER"                 ].as<int>() ),
GL_FRAGMENT_SHADER              ( gl["FRAGMENT_SHADER"               ].as<int>() ),
GL_COMPILE_STATUS               ( gl["COMPILE_STATUS"                ].as<int>() ),
GL_LINK_STATUS                  ( gl["LINK_STATUS"                   ].as<int>() ),
GL_SAMPLER_2D                   ( gl["SAMPLER_2D"                    ].as<int>() )
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

val NativeGL_Emscripten::createBuffer() const {
  return _gl.call<val>("createBuffer");
}

void NativeGL_Emscripten::bindBuffer(const val& webGLBuffer) const {
  _gl.call<void>("bindBuffer", GL_ARRAY_BUFFER, webGLBuffer);
}

void NativeGL_Emscripten::bufferData(const val& webGLBuffer) const {
  _gl.call<void>("bufferData", GL_ARRAY_BUFFER, webGLBuffer, GL_STATIC_DRAW);
}

void NativeGL_Emscripten::deleteBuffer(const val& webGLBuffer) const {
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
  _gl.call<void>("enableVertexAttribArray", location);
}

void NativeGL_Emscripten::disableVertexAttribArray(int location) const {
  _gl.call<void>("disableVertexAttribArray", location);
}

void NativeGL_Emscripten::pixelStorei(int pname,
                                      int param) const {
  _gl.call<void>("pixelStorei", pname, param);
}

std::vector<IGLTextureID*> NativeGL_Emscripten::genTextures(int  n) const {
  std::vector<IGLTextureID*> result;
//  result.reserve(n);
  for (size_t i = 0; i < n; i++) {
    val texture = _gl.call<val>("createTexture");
    GLTextureID_Emscripten* textureID = new GLTextureID_Emscripten(texture);
    result.push_back(textureID);
  }
  return result;
}

void NativeGL_Emscripten::texParameteri(int target,
                                        int par,
                                        int v) const {
  _gl.call<void>("texParameteri", target, par, v);
}

void NativeGL_Emscripten::texImage2D(const IImage* image, int format) const {
  Image_Emscripten* imageEM = (Image_Emscripten*) image;

  val domImage = imageEM->getDOMImage();

  _gl.call<void>("texImage2D", GL_TEXTURE_2D, 0, format, format, GL_UNSIGNED_BYTE, domImage);
}

void NativeGL_Emscripten::generateMipmap(int target) const {
  _gl.call<void>("generateMipmap", target);
}

void NativeGL_Emscripten::drawArrays(int mode,
                                     int first,
                                     int count) const {
  _gl.call<void>("drawArrays", mode, first, count);
}

void NativeGL_Emscripten::cullFace(int c) const {
  _gl.call<void>("cullFace", c);
}

void NativeGL_Emscripten::getIntegerv(int v, int i[]) const {
  // TODO Warning: getIntegerv is not implemented in WebGL.

  val result = _gl.call<val>("getParameter", v);

  const int length = result["length"].as<int>();
  for (int index = 0; index < length; index++) {
    i[index] = result[index].as<int>();
  }
}

int NativeGL_Emscripten::CullFace_Front() const {
  return GL_FRONT;
}
int NativeGL_Emscripten::CullFace_Back() const {
  return GL_BACK;
}
int NativeGL_Emscripten::CullFace_FrontAndBack() const {
  return GL_FRONT_AND_BACK;
}

int NativeGL_Emscripten::BufferType_ColorBuffer() const {
  return GL_COLOR_BUFFER_BIT;
}
int NativeGL_Emscripten::BufferType_DepthBuffer() const {
  return GL_DEPTH_BUFFER_BIT;
}

int NativeGL_Emscripten::Feature_PolygonOffsetFill() const {
  return GL_POLYGON_OFFSET_FILL;
}
int NativeGL_Emscripten::Feature_DepthTest() const {
  return GL_DEPTH_TEST;
}
int NativeGL_Emscripten::Feature_Blend() const {
  return GL_BLEND;
}
int NativeGL_Emscripten::Feature_CullFace() const {
  return GL_CULL_FACE;
}

int NativeGL_Emscripten::Type_Float() const {
  return GL_FLOAT;
}
int NativeGL_Emscripten::Type_UnsignedByte() const {
  return GL_UNSIGNED_BYTE;
}
int NativeGL_Emscripten::Type_UnsignedInt() const {
  return GL_UNSIGNED_INT;
}
int NativeGL_Emscripten::Type_Int() const {
  return GL_INT;
}
int NativeGL_Emscripten::Type_Vec2Float() const {
  return GL_FLOAT_VEC2;
}
int NativeGL_Emscripten::Type_Vec3Float() const {
  return GL_FLOAT_VEC3;
}
int NativeGL_Emscripten::Type_Vec4Float() const {
  return GL_FLOAT_VEC4;
}
int NativeGL_Emscripten::Type_Bool() const {
  return GL_BOOL;
}
int NativeGL_Emscripten::Type_Matrix4Float() const {
  return GL_FLOAT_MAT4;
}

int NativeGL_Emscripten::Primitive_Triangles() const {
  return GL_TRIANGLES;
}
int NativeGL_Emscripten::Primitive_TriangleStrip() const {
  return GL_TRIANGLE_STRIP;
}
int NativeGL_Emscripten::Primitive_TriangleFan() const {
  return GL_TRIANGLE_FAN;
}
int NativeGL_Emscripten::Primitive_Lines() const {
  return GL_LINES;
}
int NativeGL_Emscripten::Primitive_LineStrip() const {
  return GL_LINE_STRIP;
}
int NativeGL_Emscripten::Primitive_LineLoop() const {
  return GL_LINE_LOOP;
}
int NativeGL_Emscripten::Primitive_Points() const {
  return GL_POINTS;
}

int NativeGL_Emscripten::BlendFactor_One() const {
  return GL_ONE;
}
int NativeGL_Emscripten::BlendFactor_Zero() const {
  return GL_ZERO;
}
int NativeGL_Emscripten::BlendFactor_SrcAlpha() const {
  return GL_SRC_ALPHA;
}
int NativeGL_Emscripten::BlendFactor_OneMinusSrcAlpha() const {
  return GL_ONE_MINUS_SRC_ALPHA;
}

int NativeGL_Emscripten::TextureType_Texture2D() const {
  return GL_TEXTURE_2D;
}

int NativeGL_Emscripten::TextureParameter_MinFilter() const {
  return GL_TEXTURE_MIN_FILTER;
}
int NativeGL_Emscripten::TextureParameter_MagFilter() const {
  return GL_TEXTURE_MAG_FILTER;
}
int NativeGL_Emscripten::TextureParameter_WrapS() const {
  return GL_TEXTURE_WRAP_S;
}
int NativeGL_Emscripten::TextureParameter_WrapT() const {
  return GL_TEXTURE_WRAP_T;
}

int NativeGL_Emscripten::TextureParameterValue_Nearest() const {
  return GL_NEAREST;
}
int NativeGL_Emscripten::TextureParameterValue_Linear() const {
  return GL_LINEAR;
}
int NativeGL_Emscripten::TextureParameterValue_NearestMipmapNearest() const {
  return GL_NEAREST_MIPMAP_NEAREST;
}
int NativeGL_Emscripten::TextureParameterValue_NearestMipmapLinear() const {
  return GL_NEAREST_MIPMAP_LINEAR;
}
int NativeGL_Emscripten::TextureParameterValue_LinearMipmapNearest() const {
  return GL_LINEAR_MIPMAP_NEAREST;
}
int NativeGL_Emscripten::TextureParameterValue_LinearMipmapLinear() const {
  return GL_LINEAR_MIPMAP_LINEAR;
}

int NativeGL_Emscripten::TextureParameterValue_Repeat() const {
  return GL_REPEAT;
}
int NativeGL_Emscripten::TextureParameterValue_ClampToEdge() const {
  return GL_CLAMP_TO_EDGE;
}
int NativeGL_Emscripten::TextureParameterValue_MirroredRepeat() const {
  return GL_MIRRORED_REPEAT;
}

int NativeGL_Emscripten::Alignment_Pack() const {
  return GL_PACK_ALIGNMENT;
}
int NativeGL_Emscripten::Alignment_Unpack() const {
  return GL_UNPACK_ALIGNMENT;
}

int NativeGL_Emscripten::Format_RGBA() const {
  return GL_RGBA;
}

int NativeGL_Emscripten::Variable_Viewport() const {
  return GL_VIEWPORT;
}
int NativeGL_Emscripten::Variable_ActiveAttributes() const {
  return GL_ACTIVE_ATTRIBUTES;
}
int NativeGL_Emscripten::Variable_ActiveUniforms() const {
  return GL_ACTIVE_UNIFORMS;
}

int NativeGL_Emscripten::Error_NoError() const {
  return GL_NO_ERROR;
}

int NativeGL_Emscripten::createProgram() const {
  const int id = _shaderList.size();

  const val jsoProgram = _gl.call<val>("createProgram");
  _shaderList.push_back(jsoProgram);

  return id;
}

bool NativeGL_Emscripten::deleteProgram(int program) const {
  const val jsoProgram = _shaderList[program];
  _gl.call<void>("deleteProgram", jsoProgram);
  return true;
}

void NativeGL_Emscripten::attachShader(int program, int shader) const {
  const val jsoProgram = _shaderList[program];
  const val jsoShader  = _shaderList[shader];

  _gl.call<void>("attachShader", jsoProgram, jsoShader);
}

int NativeGL_Emscripten::createShader(ShaderType type) const {

  int shaderType;
  switch (type) {
    case ShaderType::VERTEX_SHADER:
      shaderType = GL_VERTEX_SHADER;
      break;

    case ShaderType::FRAGMENT_SHADER:
      shaderType = GL_FRAGMENT_SHADER;
      break;

    default:
      emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR, "Unknown shader type");
      return 0;
  }

  const int id = _shaderList.size();

  val shader = _gl.call<val>("createShader", shaderType);
  _shaderList.push_back(shader);

  return id;
}

bool NativeGL_Emscripten::compileShader(int shader, const std::string& source) const {
  const val jsoShader = _shaderList[shader];

  _gl.call<void>("shaderSource", jsoShader, source);
  _gl.call<void>("compileShader", jsoShader);

  return _gl.call<bool>("getShaderParameter", jsoShader, GL_COMPILE_STATUS);
}

bool NativeGL_Emscripten::deleteShader(int shader) const {
#warning TODO: deleteShader(int shader) implementation fails
  const val jsoShader = _shaderList[shader];
  return true;
  // return _gl.call<bool>("deleteShader", jsoShader);
}

void NativeGL_Emscripten::printShaderInfoLog(int shader) const {
  const val jsoShader = _shaderList[shader];

  if (!_gl.call<bool>("getShaderParameter", jsoShader, GL_COMPILE_STATUS)) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR,
                   "Error compiling shaders: %s",
                   _gl.call<std::string>("getShaderInfoLog", jsoShader).c_str());
  }
}

bool NativeGL_Emscripten::linkProgram(int program) const {
  const val jsoProgram = _shaderList[program];
  _gl.call<void>("linkProgram", jsoProgram);
  return _gl.call<bool>("getProgramParameter", jsoProgram, GL_LINK_STATUS);
}

void NativeGL_Emscripten::printProgramInfoLog(int program) const {
  const val jsoProgram = _shaderList[program];

  if (!_gl.call<bool>("getProgramParameter", jsoProgram, GL_LINK_STATUS)) {
    emscripten_log(EM_LOG_CONSOLE | EM_LOG_ERROR,
                   "Error linking program: %s",
                   _gl.call<std::string>("getProgramInfoLog", jsoProgram).c_str());
  }
}

void NativeGL_Emscripten::bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const {
  const int programID = program->getProgramID();
  const val jsoProgram = _shaderList[programID];
  
  _gl.call<void>("bindAttribLocation", jsoProgram, loc, name);
}

int NativeGL_Emscripten::getProgramiv(const GPUProgram* program, int param) const {
  const int programID = program->getProgramID();
  const val jsoProgram = _shaderList[programID];

  // Return the value for the passed pname given the passed program. The type returned is the natural type for the requested pname
  return _gl.call<int>("getProgramParameter", jsoProgram, param);
}

GPUUniform* NativeGL_Emscripten::getActiveUniform(const GPUProgram* program, int i) const {
  const int programID = program->getProgramID();
  const val jsoProgram = _shaderList[programID];

  const val info = _gl.call<val>("getActiveUniform", jsoProgram, i);

  const std::string infoName = info["name"].as<std::string>();

  const val id = _gl.call<val>("getUniformLocation", jsoProgram, infoName);

  GLUniformID_Emscripten* glUniformID = new GLUniformID_Emscripten(id);

  const int infoType = info["type"].as<int>();
  if (infoType == GL_FLOAT_MAT4) {
    return new GPUUniformMatrix4Float(infoName, glUniformID);
  }
  else if (infoType == GL_FLOAT_VEC4) {
    return new GPUUniformVec4Float(infoName, glUniformID);
  }
  else if (infoType == GL_FLOAT) {
    return new GPUUniformFloat(infoName, glUniformID);
  }
  else if (infoType == GL_FLOAT_VEC2) {
    return new GPUUniformVec2Float(infoName, glUniformID);
  }
  else if (infoType == GL_FLOAT_VEC3) {
    return new GPUUniformVec3Float(infoName, glUniformID);
  }
  else if (infoType == GL_BOOL) {
    return new GPUUniformBool(infoName, glUniformID);
  }
  else if (infoType == GL_SAMPLER_2D) {
    return new GPUUniformSampler2D(infoName, glUniformID);
  }
  else {
    return NULL;
  }
}

GPUAttribute* NativeGL_Emscripten::getActiveAttribute(const GPUProgram* program, int i) const {
  const int programID = program->getProgramID();
  const val jsoProgram = _shaderList[programID];

  const val info = _gl.call<val>("getActiveAttrib", jsoProgram, i);

  const std::string infoName = info["name"].as<std::string>();

  const int id = _gl.call<int>("getAttribLocation", jsoProgram, infoName);

  const int infoType = info["type"].as<int>();
  if (infoType == GL_FLOAT_VEC3) {
    return new GPUAttributeVec3Float(infoName, id);
  }
  else if (infoType == GL_FLOAT_VEC4) {
    return new GPUAttributeVec4Float(infoName, id);
  }
  else if (infoType == GL_FLOAT_VEC2) {
    return new GPUAttributeVec2Float(infoName, id);
  }
  else {
    return NULL;
  }
}

void NativeGL_Emscripten::depthMask(bool depthMask) const {
  _gl.call<void>("depthMask", depthMask);
}

void NativeGL_Emscripten::setActiveTexture(int i) const {
  _gl.call<void>("activeTexture", GL_TEXTURE0 + i);
}

void NativeGL_Emscripten::viewport(int x, int y, int width, int height) const {
  _gl.call<void>("viewport", x, y, width, height);
}
