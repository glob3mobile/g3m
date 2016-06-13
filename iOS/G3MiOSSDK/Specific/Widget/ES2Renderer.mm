//
//  ES2Renderer.m
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//

#include <string>

#import "ES2Renderer.h"

#include "G3MWidget.hpp"
#include "GL.hpp"
#include "NativeGL2_iOS.hpp"

// uniform index
enum {
  UNIFORM_TRANSLATE,
  NUM_UNIFORMS
};
GLint uniforms[NUM_UNIFORMS];

// attribute index
enum {
  ATTRIB_VERTEX,
  ATTRIB_COLOR,
  NUM_ATTRIBUTES
};

@interface ES2Renderer (PrivateMethods)
//- (BOOL)loadShaders;

- (BOOL)validateProgram:(GLuint)prog;
@end

@implementation ES2Renderer

// Create an OpenGL ES 2.0 context
- (id)init {
  self = [super init];
  
  if (self) {
    NativeGL2_iOS* nGL = new NativeGL2_iOS();
    _gl = new GL(nGL);
    _firstRender = true;
    _context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
    
    if (!_context || ![EAGLContext setCurrentContext:_context]
        //|| ![self loadShaders]
        ) {
      return nil;
    }
    
    // Create default framebuffer object. The backing will be allocated for the current layer in -resizeFromLayer
    glGenFramebuffers(1, &_defaultFramebuffer);
    glGenRenderbuffers(1, &_colorRenderbuffer);
    glBindFramebuffer(GL_FRAMEBUFFER, _defaultFramebuffer);
    glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, _colorRenderbuffer);
    
    //https://www.khronos.org/registry/gles/extensions/OES/OES_depth_texture.txt
    glGenTextures(1, &_depthRenderbuffer);
    glBindTexture(GL_TEXTURE_2D, _depthRenderbuffer);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, _width, _height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, 0);
    
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, _depthRenderbuffer, 0);
    
    if (glGetError() != GL_NO_ERROR){
      printf("PROBLEMO");
    }
    
    //    // Create the depthbuffer
    //    glGenRenderbuffers(1, &_depthRenderbuffer);
    //    glBindRenderbuffer(GL_RENDERBUFFER, _depthRenderbuffer);
    //    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, _depthRenderbuffer);
  }
  
  return self;
}

GLuint program = 9999;

void renderTexture(){
  
  if (program == 9999){
    const std::string vs = "attribute vec2 aPosition2D;\n"
    "attribute vec2 aTextureCoord;\n"
    "varying mediump vec2 TextureCoordOut;\n"
    "void main() {"
    "gl_Position = vec4(aPosition2D.x, aPosition2D.y, 0, 1);\n"
    //"gl_PointSize = aTextureCoord.x;\n"
    "TextureCoordOut =aTextureCoord;\n"
    "}\n";
    
    const std::string fs =
    "varying mediump vec2 TextureCoordOut;\n"
    "void main() {"
    "  gl_FragColor = vec4(TextureCoordOut.x, 0.0, 0.0, 1.0); //RED\n"
    "}\n";
    
    program = glCreateProgram();
    GLuint vertex = glCreateShader(GL_VERTEX_SHADER);
    const char *s = vs.c_str();
    glShaderSource(vertex, 1, &s, NULL);
    glCompileShader(vertex);
    int status;
    glGetShaderiv(vertex, GL_COMPILE_STATUS, &status);
    if (status == GL_FALSE){
      printf("PROBLEM VERTEX");
    }
    
    
    GLuint fragment = glCreateShader(GL_FRAGMENT_SHADER);
    s = fs.c_str();
    glShaderSource(fragment, 1, &s, NULL);
    glCompileShader(fragment);
    glGetShaderiv(fragment, GL_COMPILE_STATUS, &status);
    if (status == GL_FALSE){
      printf("PROBLEM FRAGMENT");
    }
    
    glAttachShader(program, vertex);
    glAttachShader(program, fragment);
    glLinkProgram(program);
    glGetProgramiv(program, GL_LINK_STATUS, &status);
    if (status == GL_FALSE){
      printf("PROBLEM PROGRAM");
    }
  }
  
  GLint oldProgram;
  glGetIntegerv(GL_CURRENT_PROGRAM,&oldProgram);
  
  glUseProgram(program);
  
  //VERTICES
  int apos2D = glGetAttribLocation(program, "aPosition2D");
  glEnableVertexAttribArray(apos2D);
  GLfloat vertices[] = {-0.1, -0.1, // bottom left corner
        -0.1,  0.1, // top right corner
    0.1,  -0.1, // top left corner
  0.1, 0.1}; // bottom right corner
  GLint verticesSize = 8 * sizeof(GLfloat);
  glVertexAttribPointer(apos2D, 2, GL_FLOAT, false, 0, vertices);
  
  //TEX COORDS
  int aTexCoords = glGetAttribLocation(program, "aTextureCoord");
  glEnableVertexAttribArray(aTexCoords);
  GLfloat tc[] = {-1, -1, // bottom left corner
    -1,  1, // top right corner
    1,  -1, // top left corner
    1, 1}; // bottom right corner
  GLint tcSize = 8 * sizeof(GLfloat);
  glVertexAttribPointer(aTexCoords, 2, GL_FLOAT, false, 0, tc);
  
  glDisable(GL_DEPTH_TEST);
//  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  
  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  
  glUseProgram(oldProgram);
  
  //  GPUProgram* p = new GPUProgram();
  //
  //  p->_name = name;
  //  p->_programID = gl->createProgram();
  //  p->_gl = gl;
  //
  //  // compile vertex shader
  //  int vertexShader= gl->createShader(VERTEX_SHADER);
  //  if (!p->compileShader(gl, vertexShader, vertexSource)) {
  //    ILogger::instance()->logError("GPUProgram: ERROR compiling vertex shader :\n %s\n", vertexSource.c_str());
  //    gl->printShaderInfoLog(vertexShader);
  //
  //    p->deleteShader(gl, vertexShader);
  //    p->deleteProgram(gl, p);
  //    return NULL;
  //  }
  //
  //  //  ILogger::instance()->logInfo("VERTEX SOURCE: \n %s", vertexSource.c_str());
  //
  //  // compile fragment shader
  //  int fragmentShader = gl->createShader(FRAGMENT_SHADER);
  //  if (!p->compileShader(gl, fragmentShader, fragmentSource)) {
  //    ILogger::instance()->logError("GPUProgram: ERROR compiling fragment shader :\n %s\n", fragmentSource.c_str());
  //    gl->printShaderInfoLog(fragmentShader);
  //
  //    p->deleteShader(gl, fragmentShader);
  //    p->deleteProgram(gl, p);
  //    return NULL;
  //  }
  //
  //  //  ILogger::instance()->logInfo("FRAGMENT SOURCE: \n %s", fragmentSource.c_str());
  //
  //  //gl->bindAttribLocation(p, 0, POSITION);
  //
  //  // link program
  //  if (!p->linkProgram(gl)) {
  //    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program\n");
  //    p->deleteShader(gl, vertexShader);
  //    p->deleteShader(gl, fragmentShader);
  //    p->deleteProgram(gl, p);
  //    ILogger::instance()->logError("GPUProgram: ERROR linking graphic program");
  //    return NULL;
  //  }
  //
  //  //Mark shaders for deleting when program is deleted
  //  p->deleteShader(gl, vertexShader);
  //  p->deleteShader(gl, fragmentShader);
  //
  //  p->getVariables(gl);
  //
  //  if (gl->getError() != GLError::noError()) {
  //    ILogger::instance()->logError("Error while compiling program");
  //  }
  //
  //  return p;
  
  //  GLfloat vertices[] = {-1, -1, 0, // bottom left corner
  //    -1,  1, 0, // top left corner
  //    1,  1, 0, // top right corner
  //    1, -1, 0}; // bottom right corner
  //
  //  GLubyte indices[] = {0,1,2, // first triangle (bottom left - top left - top right)
  //    0,2,3}; // second triangle (bottom left - top right - bottom right)
  //
  //  glVertexPointer(3, GL_FLOAT, 0, vertices);
  //  glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indices);
  
  
}

- (void)render: (void*) widgetV
{
  if (widgetV == NULL) {
    return;
  }
  
  G3MWidget* widget = (G3MWidget*) widgetV;
  @autoreleasepool {
    if (_firstRender) {
      // This application only creates a single context which is already set current at this point.
      // This call is redundant, but needed if dealing with multiple contexts.
      [EAGLContext setCurrentContext:_context];
      
      // This application only creates a single default framebuffer which is already bound at this point.
      // This call is redundant, but needed if dealing with multiple framebuffers.
      glBindFramebuffer(GL_FRAMEBUFFER, _defaultFramebuffer);
      glViewport(0, 0, _width, _height);
    }
    
    // Use shader program
    widget->render(_width, _height);
    
    
    renderTexture();
    
    //TEST READING DEPTH
    //HAY Q RENDERIZAR LA TEXTURA PARA PODER LEERLA http://roxlu.com/2014/036/rendering-the-depth-buffer
    
    
    if (_firstRender) {
      // This application only creates a single color renderbuffer which is already bound at this point.
      // This call is redundant, but needed if dealing with multiple renderbuffers.
      glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
      _firstRender = false;
      
    }
    [_context presentRenderbuffer:GL_RENDERBUFFER];
  }
}

- (BOOL)validateProgram:(GLuint)prog {
  GLint logLength, status;
  
  glValidateProgram(prog);
  glGetProgramiv(prog, GL_INFO_LOG_LENGTH, &logLength);
  if (logLength > 0) {
    GLchar* log = (GLchar*) malloc(logLength);
    glGetProgramInfoLog(prog, logLength, &logLength, log);
    NSLog(@"Program validate log:\n%s", log);
    free(log);
  }
  
  glGetProgramiv(prog, GL_VALIDATE_STATUS, &status);
  if (status == 0)
    return FALSE;
  
  return TRUE;
}

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer {
  _firstRender = true;
  
  // Allocate color buffer backing based on the current layer size
  glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
  [_context renderbufferStorage:GL_RENDERBUFFER fromDrawable:layer];
  glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_WIDTH, &_width);
  glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_HEIGHT, &_height);
  
  // damos tamaño al buffer de profundidad
  //  glBindRenderbuffer(GL_RENDERBUFFER, _depthRenderbuffer);
  //  glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, _width, _height);
  
  //We create another texture for storing the depth
  glGenTextures(1, &_depthRenderbuffer);
  glBindTexture(GL_TEXTURE_2D, _depthRenderbuffer);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, _width, _height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, 0);
  
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  
  glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, _depthRenderbuffer, 0);
  
  if (glGetError() != GL_NO_ERROR){
    printf("PROBLEMO");
  }
  
  
  if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
    NSLog(@"Failed to make complete framebuffer object %x", glCheckFramebufferStatus(GL_FRAMEBUFFER));
    return NO;
  }
  
  return YES;
}

- (void)dealloc {
  // Tear down GL
  if (_defaultFramebuffer) {
    glDeleteFramebuffers(1, &_defaultFramebuffer);
    _defaultFramebuffer = 0;
  }
  
  if (_colorRenderbuffer) {
    glDeleteRenderbuffers(1, &_colorRenderbuffer);
    _colorRenderbuffer = 0;
  }
  
  if (_depthRenderbuffer) {
    glDeleteRenderbuffers(1, &_depthRenderbuffer);
    _depthRenderbuffer = 0;
  }
  
  // Tear down context
  if ([EAGLContext currentContext] == _context)
    [EAGLContext setCurrentContext:nil];
  
  _context = nil;
  
}

- (GL*)getGL {
  return _gl;
}

@end
