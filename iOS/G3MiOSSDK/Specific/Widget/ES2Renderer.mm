//
//  ES2Renderer.m
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
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
      _gl = new GL(nGL,false);
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
    
    // Create the depthbuffer
    glGenRenderbuffers(1, &_depthRenderbuffer);
    glBindRenderbuffer(GL_RENDERBUFFER, _depthRenderbuffer);
    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, _depthRenderbuffer);
  }
  
  return self;
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

//- (BOOL)loadShaders {
//  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
//                                                                 ofType: @"vsh"];
//  if (!vertShaderPathname) {
//    NSLog(@"Can't load Shader.vsh");
//    return FALSE;
//  }
//  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
//                                                             encoding: NSUTF8StringEncoding
//                                                                error: nil] UTF8String]);
//
//  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
//                                                                 ofType: @"fsh"];
//  if (!fragShaderPathname) {
//    NSLog(@"Can't load Shader.fsh");
//    return FALSE;
//  }
//
//  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
//                                                               encoding: NSUTF8StringEncoding
//                                                                  error: nil] UTF8String]);
////  
////  try {
////    _gpuProgram = GPUProgram::createProgram(_gl->getNative(), "", vertexSource, fragmentSource);
////    if (_gpuProgram != NULL) {
////      NSLog(@"GPU Program Loaded");
////      try {
////        _gpuProgram->setUniform(_gl, "Modelview", MutableMatrix44D::identity());
////      } catch (G3MError* e) {
////        NSLog(@"%s", e->getMessage().c_str());
////      }
////    }
////  } catch (G3MError* e) {
////    NSLog(@"%s", e->getMessage().c_str());
////  }
//
//  return TRUE;
//}

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer {
  _firstRender = true;
  
  // Allocate color buffer backing based on the current layer size
  glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
  [_context renderbufferStorage:GL_RENDERBUFFER fromDrawable:layer];
  glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_WIDTH, &_width);
  glGetRenderbufferParameteriv(GL_RENDERBUFFER, GL_RENDERBUFFER_HEIGHT, &_height);

  // damos tamaño al buffer de profundidad
  glBindRenderbuffer(GL_RENDERBUFFER, _depthRenderbuffer);
  glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, _width, _height);
  
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
