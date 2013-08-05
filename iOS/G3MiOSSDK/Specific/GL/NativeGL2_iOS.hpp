//
//  NativeGL_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_NativeGL_iOS_hpp
#define G3MiOSSDK_NativeGL_iOS_hpp

#include <OpenGLES/ES2/gl.h>

#include "INativeGL.hpp"

#include "GLUniformID_iOS.hpp"
#include "GLTextureId_iOS.hpp"
#include "FloatBuffer_iOS.hpp"
#include "ShortBuffer_iOS.hpp"
#include "Image_iOS.hpp"
#include "MutableMatrix44D.hpp"

class NativeGL2_iOS: public INativeGL {
public:

  void useProgram(ShaderProgram* program) const {
//    glUseProgram(((GLProgramId_iOS*)program)->getID());
    glUseProgram(program->getProgram());
  }

  int getAttribLocation(ShaderProgram* program,
                        const std::string& name) const {
//    return glGetAttribLocation(((GLProgramId_iOS*)program)->getID(), name.c_str());
    return glGetAttribLocation(program->getProgram(), name.c_str());
  }

  IGLUniformID* getUniformLocation(ShaderProgram* program,
                                   const std::string& name) const {
//    const int id = glGetUniformLocation(((GLProgramId_iOS*)program)->getID(), name.c_str());
    const int id = glGetUniformLocation(program->getProgram(), name.c_str());
    return (IGLUniformID*) new GLUniformID_iOS(id);
  }

  void uniform2f(IGLUniformID* loc,
                 float x, float y) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform2f(location, x, y);
  }

  void uniform1f(IGLUniformID* loc,
                 float x) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform1f(location, x);
  }

  void uniform1i(IGLUniformID* loc,
                 int v) const {
    const int location = ((GLUniformID_iOS*)loc)->getID();
    glUniform1i(location, v);
  }

  void uniformMatrix4fv(IGLUniformID* location,
                        bool transpose,
                        const MutableMatrix44D* matrix) const {
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

  void uniform4f(IGLUniformID* location,
                 float v0, float v1, float v2, float v3) const {
    const int loc = ((GLUniformID_iOS*)location)->getID();
    glUniform4f(loc, v0, v1, v2, v3);
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
                           IFloatBuffer* buffer) const {
    const FloatBuffer_iOS* buffer_iOS = (FloatBuffer_iOS*) buffer;
    
    const float* pointer = buffer_iOS->getPointer();
    glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, pointer);

//    FloatBuffer_iOS* buffer_iOS = (FloatBuffer_iOS*) buffer;
//
//    GLuint glBuffer = buffer_iOS->getGLBuffer(size);
//    glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
//    
//    glBufferData(GL_ARRAY_BUFFER, size, buffer_iOS->getPointer(), GL_STATIC_DRAW);
//
//    glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, 0);
//
//    /*
//		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
//
//		// var webGLBuffer = gl.createBuffer();
//		var webGLBuffer = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
//		gl.bindBuffer(gl.ARRAY_BUFFER, webGLBuffer);
//
//		var array = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();
//		gl.bufferData(gl.ARRAY_BUFFER, array, gl.STATIC_DRAW);
//
//		gl.vertexAttribPointer(index, size, gl.FLOAT, normalized, stride, 0);
//     */
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

//  void texImage2D(const IImage* image,
//                  int format) const;

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

  int TextureParameterValue_Linear() const {
    return GL_LINEAR;
  }

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

  int Error_NoError() const {
    return GL_NO_ERROR;
  }
  
  int createProgram() const {
    return glCreateProgram();
  }
  
  void deleteProgram(int program) const {
    glDeleteProgram(program);
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

  void deleteShader(int shader) const {
    glDeleteShader(shader);
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
  
};

#endif
