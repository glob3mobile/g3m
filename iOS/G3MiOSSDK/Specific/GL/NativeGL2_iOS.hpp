//
//  NativeGL_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_NativeGL_iOS
#define G3MiOSSDK_NativeGL_iOS

#include <OpenGLES/ES2/gl.h>

#include "INativeGL.hpp"

#include "GLUniformID_iOS.hpp"
#include "GLTextureId_iOS.hpp"
#include "FloatBuffer_iOS.hpp"
#include "ShortBuffer_iOS.hpp"
#include "Image_iOS.hpp"
#include "GPUProgram.hpp"
#include "GPUAttribute.hpp"
#include "GPUUniform.hpp"


class NativeGL2_iOS: public INativeGL {
public:

  void useProgram(GPUProgram* program) const {
    glUseProgram(program->getProgramID());
  }

  void uniform2f(const IGLUniformID* loc,
                 float x, float y) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform2f(location, x, y);
  }

  void uniform1f(const IGLUniformID* loc,
                 float x) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform1f(location, x);
  }

  void uniform1i(const IGLUniformID* loc,
                 int v) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform1i(location, v);
  }

  void uniformMatrix4fv(const IGLUniformID* location,
                        bool transpose,
                        const Matrix44D* matrix) const {
    const int loc = ((GLUniformID_iOS*)location)->getID();

    GLfloat* pointer = matrix->getColumnMajorFloatArray();

    glUniformMatrix4fv(loc, 1, transpose, pointer);
  }

  void clearColor(float red, float green, float blue, float alpha) const {
    glClearColor(red, green, blue, alpha);
  }

  void clear(int buffers) const {
    glClear(buffers);
  }

  void uniform4f(const IGLUniformID* location,
                 float v0, float v1, float v2, float v3) const {
    const int loc = ((GLUniformID_iOS*)location)->getID();
    glUniform4f(loc, v0, v1, v2, v3);
  }

  void uniform3f(const IGLUniformID* location,
                 float v0, float v1, float v2) const {
    const int loc = ((GLUniformID_iOS*)location)->getID();
    glUniform3f(loc, v0, v1, v2);
  }

  void enable(int feature) const {
    glEnable(feature);
  }

  void disable(int feature) const {
    glDisable(feature);
  }

  void polygonOffset(float factor, float units) const {
    glPolygonOffset(factor, units);
  }

  void vertexAttribPointer(int index,
                           int size,
                           bool normalized,
                           int stride,
                           const IFloatBuffer* buffer) const {
    const FloatBuffer_iOS* buffer_iOS = (FloatBuffer_iOS*) buffer;

    buffer_iOS->bindAsVBOToGPU();
    glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);

//#warning uncoment for no VBO
//    const float* pointer = buffer_iOS->getPointer();
//    glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, pointer);
  }

//  void drawElements(int mode,
//                    int count,
//                    IIntBuffer* buffer) const {
//    int has_to_set_GL_UNSIGNED_INT; //???????
//    const int* pointer = ((IntBuffer_iOS*) buffer)->getPointer();
//    glDrawElements(mode, count, GL_UNSIGNED_INT, pointer);
//  }
  void drawElements(int mode,
                    int count,
                    IShortBuffer* buffer) const {

//    printf("-----DRAW\n");
//    ShortBuffer_iOS* bufferIOS = (ShortBuffer_iOS*) buffer; //UNCOMMENT FOR IBO USING
//    bufferIOS->bindAsIBOToGPU();
//    glDrawElements(mode, count, GL_UNSIGNED_SHORT, 0);

    const short* pointer = ((ShortBuffer_iOS*) buffer)->getPointer();
    glDrawElements(mode, count, GL_UNSIGNED_SHORT, pointer);
  }

  void lineWidth(float width) const {
    glLineWidth(width);
  }

  int getError() const {
    return glGetError();
  }

  void blendFunc(int sfactor, int dfactor) const {
    glBlendFunc(sfactor, dfactor);
  }

  void bindTexture(int target, const IGLTextureId* texture) const {
    const int id = ((GLTextureId_iOS*) texture)->getGLTextureId();
    if (id < 0) {
      ILogger::instance()->logError("Trying to bind invalid IGLTextureId");
    }
    else {
      glBindTexture(target, id);
    }
  }

  bool deleteTexture(const IGLTextureId* texture) const {
    const unsigned int textures[] = {
      ((GLTextureId_iOS*) texture)->getGLTextureId()
    };

    glDeleteTextures(1, textures);

    return false;
  }

  void enableVertexAttribArray(int location) const {
    glEnableVertexAttribArray(location);
  }

  void disableVertexAttribArray(int location) const {
    glDisableVertexAttribArray(location);
  }

  void pixelStorei(int pname, int param) const {
    glPixelStorei(pname, param);
  }

  std::vector<IGLTextureId*> genTextures(int n) const {
    GLuint textureIds[n];
    glGenTextures(n, textureIds);
    std::vector<IGLTextureId*> ts;
    for(int i = 0; i < n; i++) {
      GLuint textureId = textureIds[i];
      if (textureId == 0) {
        ILogger::instance()->logError("Can't create a textureId");
      }
      else {
        ts.push_back( new GLTextureId_iOS(textureId) );
      }
    }
    return ts;
  }

  void texParameteri(int target,
                     int par,
                     int v) const {
    glTexParameteri(target, par, v);
  }

  void texImage2D(const IImage* image,
                  int format) const {
    const unsigned char* data = ((Image_iOS*) image)->createByteArrayRGBA8888();

    glTexImage2D(GL_TEXTURE_2D,
                 0,
                 format,
                 image->getWidth(),
                 image->getHeight(),
                 0,
                 format,
                 GL_UNSIGNED_BYTE,
                 data);

    delete [] data;
  }

  void setActiveTexture(int i) const{
    glActiveTexture(GL_TEXTURE0 + i);
  }

  void generateMipmap(int target) const {
    glGenerateMipmap(target);
  }

  void drawArrays(int mode,
                  int first,
                  int count) const {
    glDrawArrays(mode, first, count);
  }

  void cullFace(int c) const {
    glCullFace(c);
  }

  void getIntegerv(int v, int i[]) const {
    glGetIntegerv(v, i);
  }

  int CullFace_Front() const {
    return GL_FRONT;
  }

  int CullFace_Back() const {
    return GL_BACK;
  }

  int CullFace_FrontAndBack() const {
    return GL_FRONT_AND_BACK;
  }

  int BufferType_ColorBuffer() const {
    return GL_COLOR_BUFFER_BIT;
  }

  int BufferType_DepthBuffer() const {
    return GL_DEPTH_BUFFER_BIT;
  }

  int Feature_PolygonOffsetFill() const {
    return GL_POLYGON_OFFSET_FILL;
  }

  int Feature_DepthTest() const {
    return GL_DEPTH_TEST;
  }

  int Feature_Blend() const {
    return GL_BLEND;
  }

  int Feature_CullFace() const {
    return GL_CULL_FACE;
  }

  int Type_Float() const {
    return GL_FLOAT;
  }

  int Type_UnsignedByte() const {
    return GL_UNSIGNED_BYTE;
  }

  int Type_UnsignedInt() const {
    return GL_UNSIGNED_INT;
  }

  int Type_Int() const {
    return GL_INT;
  }
  
  int Type_Vec2Float() const{
    return GL_FLOAT_VEC2;
  }
  int Type_Vec3Float() const{
    return GL_FLOAT_VEC3;
  }
  virtual int Type_Vec4Float() const{
    return GL_FLOAT_VEC4;
  }
  virtual int Type_Bool() const{
    return GL_BOOL;
  }
  virtual int Type_Matrix4Float() const{
    return GL_FLOAT_MAT4;
  }

  int Primitive_Triangles() const {
    return GL_TRIANGLES;
  }

  int Primitive_TriangleStrip() const {
    return GL_TRIANGLE_STRIP;
  }

  int Primitive_TriangleFan() const {
    return GL_TRIANGLE_FAN;
  }

  int Primitive_Lines() const {
    return GL_LINES;
  }

  int Primitive_LineStrip() const {
    return GL_LINE_STRIP;
  }

  int Primitive_LineLoop() const {
    return GL_LINE_LOOP;
  }

  int Primitive_Points() const {
    return GL_POINTS;
  }
  
  int BlendFactor_One() const {
    return GL_ONE;
  }
  
  int BlendFactor_Zero() const {
    return GL_ZERO;
  }

  int BlendFactor_SrcAlpha() const {
    return GL_SRC_ALPHA;
  }

  int BlendFactor_OneMinusSrcAlpha() const {
    return GL_ONE_MINUS_SRC_ALPHA;
  }

  int TextureType_Texture2D() const {
    return GL_TEXTURE_2D;
  }

  int TextureParameter_MinFilter() const {
    return GL_TEXTURE_MIN_FILTER;
  }

  int TextureParameter_MagFilter() const {
    return GL_TEXTURE_MAG_FILTER;
  }

  int TextureParameter_WrapS() const {
    return GL_TEXTURE_WRAP_S;
  }

  int TextureParameter_WrapT() const {
    return GL_TEXTURE_WRAP_T;
  }

  int TextureParameterValue_Nearest()              const { return GL_NEAREST;                }
  int TextureParameterValue_Linear()               const { return GL_LINEAR;                 }
  int TextureParameterValue_NearestMipmapNearest() const { return GL_NEAREST_MIPMAP_NEAREST; }
  int TextureParameterValue_NearestMipmapLinear()  const { return GL_NEAREST_MIPMAP_LINEAR;  }
  int TextureParameterValue_LinearMipmapNearest()  const { return GL_LINEAR_MIPMAP_NEAREST;  }
  int TextureParameterValue_LinearMipmapLinear()   const { return GL_LINEAR_MIPMAP_LINEAR;   }

  int TextureParameterValue_ClampToEdge() const {
    return GL_CLAMP_TO_EDGE;
  }

  int Alignment_Pack() const {
    return GL_PACK_ALIGNMENT;
  }

  int Alignment_Unpack() const {
    return GL_UNPACK_ALIGNMENT;
  }

  int Format_RGBA() const {
    return GL_RGBA;
  }

  int Variable_Viewport() const {
    return GL_VIEWPORT;
  }
  
  int Variable_ActiveAttributes() const{
    return GL_ACTIVE_ATTRIBUTES;
  }
  
  virtual int Variable_ActiveUniforms() const{
    return GL_ACTIVE_UNIFORMS;
  }

  int Error_NoError() const {
    return GL_NO_ERROR;
  }
  
  int createProgram() const {
    return glCreateProgram();
  }
  
  bool deleteProgram(int program) const {
    //ILogger::instance()->logInfo("Deleting program id = %d", program);
    glDeleteProgram(program);

    if (glIsProgram(program) == GL_FALSE) {
      return true;
    } else{
      int markedToBeDeleted;
      glGetProgramiv(program, GL_DELETE_STATUS, &markedToBeDeleted);
      return (markedToBeDeleted == GL_TRUE);
    }
  }
  
  void attachShader(int program, int shader) const {
    glAttachShader(program, shader);
  }
  
  int createShader(ShaderType type) const {
    switch (type) {
      case VERTEX_SHADER:
        return glCreateShader(GL_VERTEX_SHADER);
      case FRAGMENT_SHADER:
        return glCreateShader(GL_FRAGMENT_SHADER);
    }  
  }
  
  bool compileShader(int shader, const std::string& source) const {
    int status;
    const char *s = source.c_str();
    glShaderSource(shader, 1, &s, NULL);
    glCompileShader(shader);
    glGetShaderiv(shader, GL_COMPILE_STATUS, &status);
    return status;
  }

  bool deleteShader(int shader) const {
    glDeleteShader(shader);
    int ds;
    glGetShaderiv(shader, GL_DELETE_STATUS, &ds);
    return (ds == GL_TRUE);
  }
  
  void printShaderInfoLog(int shader) const {
    GLint logLength;
    glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &logLength);
    if (logLength > 0) {
      GLchar* log = (GLchar* ) malloc(logLength);
      glGetShaderInfoLog(shader, logLength, &logLength, log);
      NSLog(@"Shader compile log:\n%s", log);
      free(log);
    }
  }
  
  bool linkProgram(int program) const {
    int status;
    glLinkProgram(program);
    glGetProgramiv(program, GL_LINK_STATUS, &status);
    return status;
  }
  
  void printProgramInfoLog(int program) const {
    GLint logLength;
    glGetProgramiv(program, GL_INFO_LOG_LENGTH, &logLength);
    if (logLength > 0) {
      GLchar* log = (GLchar* ) malloc(logLength);
      glGetProgramInfoLog(program, logLength, &logLength, log);
      NSLog(@"Program link log:\n%s", log);
      free(log);
    }
  }
  
  void bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const{
    glBindAttribLocation(program->getProgramID(), loc, name.c_str());
  }
  
  int getProgramiv(const GPUProgram* program, int pname) const{
    int i = 0;
    glGetProgramiv(program->getProgramID(), pname, &i);
    return i;
  }
  
  GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const{
    GLint maxLength;
    glGetProgramiv(program->getProgramID(), GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, &maxLength);
  
    GLsizei bufsize = maxLength;
    
    GLchar name[maxLength];
    GLsizei length;
    GLint size;
    GLenum type;
    
    glGetActiveAttrib(program->getProgramID(),
                      i,
                      bufsize,
                      &length,
                      &size,
                      &type,
                      name);
    
    
    const int id = glGetAttribLocation(program->getProgramID(), name);
    
    //NSLog(@"Attribute Name: %s - %d, BitCode: %d", name, id, GPUVariable::getAttributeCode(GPUVariable::getAttributeKey(name)));
    switch (type) {
      case GL_FLOAT_VEC3:
        return new GPUAttributeVec3Float(name, id);
      case GL_FLOAT_VEC4:
        return new GPUAttributeVec4Float(name, id);
      case GL_FLOAT_VEC2:
        return new GPUAttributeVec2Float(name, id);
      default:
        return NULL;
        break;
    }
  }
  
  GPUUniform* getActiveUniform(const GPUProgram* program, int i) const{
    GLint maxLength;
    glGetProgramiv(program->getProgramID(), GL_ACTIVE_UNIFORM_MAX_LENGTH, &maxLength);
    
    GLsizei bufsize = maxLength;
    
    GLchar name[maxLength];
    GLsizei length;
    GLint size;
    GLenum type;
    
    glGetActiveUniform(program->getProgramID(),
                       i,
                       bufsize,
                       &length,
                       &size,
                       &type,
                       name);
    
    const int id = glGetUniformLocation(program->getProgramID(), name);
    
    //NSLog(@"Uniform Name: %s - %d, BitCode: %d", name, id, GPUVariable::getUniformCode(GPUVariable::getUniformKey(name))  );
    switch (type) {
      case GL_FLOAT_MAT4:
        return new GPUUniformMatrix4Float(name, new GLUniformID_iOS(id));
      case GL_FLOAT_VEC4:
        return new GPUUniformVec4Float(name, new GLUniformID_iOS(id));
      case GL_FLOAT:
        return new GPUUniformFloat(name, new GLUniformID_iOS(id));
      case GL_FLOAT_VEC2:
        return new GPUUniformVec2Float(name, new GLUniformID_iOS(id));
      case GL_FLOAT_VEC3:
        return new GPUUniformVec3Float(name, new GLUniformID_iOS(id));
      case GL_BOOL:
        return new GPUUniformBool(name, new GLUniformID_iOS(id));
      case GL_SAMPLER_2D:
        return new GPUUniformSampler2D(name, new GLUniformID_iOS(id));
      default:
        return NULL;
        break;
    }
  }

  void depthMask(bool v) const{
    glDepthMask(v);
  }
  
};

#endif
