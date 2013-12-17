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

#include "GPUProgram.hpp"

class GL;


@interface ES2Renderer :NSObject  {
@private
  EAGLContext* _context;
  
  // The pixel dimensions of the CAEAGLLayer
  GLint _width;
  GLint _height;
  
  // The OpenGL ES names for the framebuffer and renderbuffer used to render to this view
  GLuint _defaultFramebuffer;
  GLuint _colorRenderbuffer;
  GLuint _depthRenderbuffer;

  BOOL _firstRender;
  
  GL* _gl;
}

- (void)render: (void*) widget;

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer;

- (GL*)getGL;

@end

