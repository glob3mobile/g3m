//
//  ES2Renderer.h
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>

#import <OpenGLES/EAGL.h>
#import <OpenGLES/EAGLDrawable.h>

#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>

//#include "GLProgramId_iOS.hpp"
#include "ShaderProgram.hpp"

class GL;


@interface ES2Renderer :NSObject  {
@private
  EAGLContext *context;
  
  // The pixel dimensions of the CAEAGLLayer
  GLint _width;
  GLint _height;
  
  // The OpenGL ES names for the framebuffer and renderbuffer used to render to this view
  GLuint defaultFramebuffer, colorRenderbuffer, depthRenderbuffer;
  
  //IGLProgramId* program;
  ShaderProgram* _shaderProgram;
//  ShaderProgram* _shaderProgram2;

  BOOL _firstRender;
  
  GL* _gl;
}

- (void)render: (void*) widget;

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer;

- (GL*)getGL;

@end

