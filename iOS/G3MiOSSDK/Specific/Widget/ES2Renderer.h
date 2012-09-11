//
//  ES2Renderer.h
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>

#import <OpenGLES/EAGL.h>
#import <OpenGLES/EAGLDrawable.h>

#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>

@interface ES2Renderer :NSObject  {
@private
  EAGLContext *context;
  
  // The pixel dimensions of the CAEAGLLayer
  GLint backingWidth;
  GLint backingHeight;
  
  // The OpenGL ES names for the framebuffer and renderbuffer used to render to this view
  GLuint defaultFramebuffer, colorRenderbuffer, depthRenderbuffer;
  
  GLuint program;
  
  BOOL _firstRender;
}

- (void)render: (void*) widget;

- (BOOL)resizeFromLayer:(CAEAGLLayer *)layer;

@end

