//
//  ES2Renderer.h
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//

#import <QuartzCore/QuartzCore.h>

#import <OpenGLES/EAGL.h>
#import <OpenGLES/EAGLDrawable.h>

#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>

#include "G3M/GPUProgram.hpp"

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

  // MSAA: samples per pixel (1 = none); when > 1 the scene is drawn into the multisample framebuffer
  // and resolved into the default one on every frame
  GLint  _multisampleCount;
  GLuint _multisampleFramebuffer;
  GLuint _multisampleColorRenderbuffer;
  GLuint _multisampleDepthRenderbuffer;

  BOOL _firstRender;

  GL* _gl;
}

- (void)render: (void*) widget;

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer;

- (GL*)getGL;

// takes effect on the next resizeFromLayer:
- (void)setMultisampleCount:(GLint)count;

- (GLint)multisampleCount;

@end

