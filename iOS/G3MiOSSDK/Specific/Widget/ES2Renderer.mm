//
//  ES2Renderer.m
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//

#include <string>

#import "ES2Renderer.h"

#include "G3M/G3MWidget.hpp"
#include "G3M/GL.hpp"
#include "G3M/ILogger.hpp"

#import "NativeGL2_iOS.hpp"


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
    _multisampleCount = 1;
    _multisampleFramebuffer = 0;
    _multisampleColorRenderbuffer = 0;
    _multisampleDepthRenderbuffer = 0;
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

      glViewport(0, 0, _width, _height);
    }

    // every frame: the resolve below leaves the default framebuffer bound
    glBindFramebuffer(GL_FRAMEBUFFER, (_multisampleFramebuffer != 0) ? _multisampleFramebuffer : _defaultFramebuffer);

    // Use shader program
    widget->render(_width, _height);

    if (_multisampleFramebuffer != 0) {
      glBindFramebuffer(GL_READ_FRAMEBUFFER_APPLE, _multisampleFramebuffer);
      glBindFramebuffer(GL_DRAW_FRAMEBUFFER_APPLE, _defaultFramebuffer);
      glResolveMultisampleFramebufferAPPLE();
      // the samples are not needed after the resolve, so the GPU need not write them back to memory
      const GLenum discards[] = { GL_COLOR_ATTACHMENT0, GL_DEPTH_ATTACHMENT };
      glDiscardFramebufferEXT(GL_READ_FRAMEBUFFER_APPLE, 2, discards);
      glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
    }

    if (_firstRender) {
      // This application only creates a single color renderbuffer which is already bound at this point.
      // This call is redundant, but needed if dealing with multiple renderbuffers.
      glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
      _firstRender = false;

    }
    if (_frameCaptureHandlers.count > 0) {
      NSArray<void (^)(UIImage* frame)>* handlers = [_frameCaptureHandlers copy];
      [_frameCaptureHandlers removeAllObjects];
      UIImage* frame = [self readFrame];
      for (void (^handler)(UIImage* frame) in handlers) {
        handler(frame);
      }
    }

    [_context presentRenderbuffer:GL_RENDERBUFFER];
  }
}

// requests made before the next frame is drawn share it
- (void)captureNextFrame:(void (^)(UIImage* frame))handler {
  if (_frameCaptureHandlers == nil) {
    _frameCaptureHandlers = [NSMutableArray array];
  }
  [_frameCaptureHandlers addObject:[handler copy]];
}

// the resolved frame in the default framebuffer; GL rows go bottom-up, image rows top-down
- (UIImage*)readFrame {
  glBindFramebuffer(GL_FRAMEBUFFER, _defaultFramebuffer);

  const size_t bytesPerRow = 4 * (size_t) _width;
  NSMutableData* pixels = [NSMutableData dataWithLength:bytesPerRow * (size_t) _height];
  glReadPixels(0, 0, _width, _height, GL_RGBA, GL_UNSIGNED_BYTE, pixels.mutableBytes);
  const GLenum error = glGetError();
  if (error != GL_NO_ERROR) {
    ILogger::instance()->logError("ES2Renderer: GL error 0x%x after glReadPixels", error);
    return nil;
  }

  unsigned char* bytes = (unsigned char*) pixels.mutableBytes;
  NSMutableData* rowData = [NSMutableData dataWithLength:bytesPerRow];
  unsigned char* row = (unsigned char*) rowData.mutableBytes;
  for (GLint top = 0, bottom = _height - 1; top < bottom; top++, bottom--) {
    unsigned char* topRow    = bytes + (size_t) top    * bytesPerRow;
    unsigned char* bottomRow = bytes + (size_t) bottom * bytesPerRow;
    memcpy(row,       topRow,    bytesPerRow);
    memcpy(topRow,    bottomRow, bytesPerRow);
    memcpy(bottomRow, row,       bytesPerRow);
  }

  CGDataProviderRef provider = CGDataProviderCreateWithCFData((__bridge CFDataRef) pixels);
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  // the drawable is opaque: its alpha channel is ignored
  CGImageRef cgImage = CGImageCreate(_width, _height,
                                     8, 32, bytesPerRow,
                                     colorSpace,
                                     kCGBitmapByteOrderDefault | kCGImageAlphaNoneSkipLast,
                                     provider,
                                     NULL, false, kCGRenderingIntentDefault);
  UIImage* image = [UIImage imageWithCGImage:cgImage];
  CGImageRelease(cgImage);
  CGColorSpaceRelease(colorSpace);
  CGDataProviderRelease(provider);
  return image;
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

  glBindFramebuffer(GL_FRAMEBUFFER, _defaultFramebuffer);

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

  [self resizeMultisampleBuffers];

  return YES;
}

- (void)setMultisampleCount:(GLint)count {
  _multisampleCount = count;
}

- (GLint)multisampleCount {
  return _multisampleCount;
}

- (void)deleteMultisampleBuffers {
  if (_multisampleFramebuffer != 0) {
    glDeleteFramebuffers(1, &_multisampleFramebuffer);
    glDeleteRenderbuffers(1, &_multisampleColorRenderbuffer);
    glDeleteRenderbuffers(1, &_multisampleDepthRenderbuffer);
    _multisampleFramebuffer = 0;
    _multisampleColorRenderbuffer = 0;
    _multisampleDepthRenderbuffer = 0;
  }
}

// MSAA smooths the edges of every mesh (trail, plane, planet limb); text is already antialiased by CoreGraphics
- (void)resizeMultisampleBuffers {
  [self deleteMultisampleBuffers];
  if (_multisampleCount <= 1) {
    return;
  }

  GLint maxSamples = 0;
  glGetIntegerv(GL_MAX_SAMPLES_APPLE, &maxSamples);
  GLint samples = _multisampleCount;
  if (samples > maxSamples) {
    NSLog(@"ERROR: %d samples per pixel requested, the device supports up to %d; using %d", samples, maxSamples, maxSamples);
    samples = maxSamples;
  }

  glGenFramebuffers(1, &_multisampleFramebuffer);
  glBindFramebuffer(GL_FRAMEBUFFER, _multisampleFramebuffer);

  glGenRenderbuffers(1, &_multisampleColorRenderbuffer);
  glBindRenderbuffer(GL_RENDERBUFFER, _multisampleColorRenderbuffer);
  glRenderbufferStorageMultisampleAPPLE(GL_RENDERBUFFER, samples, GL_RGBA8_OES, _width, _height);
  glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_RENDERBUFFER, _multisampleColorRenderbuffer);

  glGenRenderbuffers(1, &_multisampleDepthRenderbuffer);
  glBindRenderbuffer(GL_RENDERBUFFER, _multisampleDepthRenderbuffer);
  glRenderbufferStorageMultisampleAPPLE(GL_RENDERBUFFER, samples, GL_DEPTH_COMPONENT16, _width, _height);
  glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, _multisampleDepthRenderbuffer);

  const GLenum status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
  if (status != GL_FRAMEBUFFER_COMPLETE) {
    NSLog(@"ERROR: failed to make complete multisample framebuffer object %x; drawing without MSAA", status);
    [self deleteMultisampleBuffers];
  }

  glBindFramebuffer(GL_FRAMEBUFFER, _defaultFramebuffer);
  glBindRenderbuffer(GL_RENDERBUFFER, _colorRenderbuffer);
}

- (void)dealloc {
  // Tear down GL
  [self deleteMultisampleBuffers];

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
