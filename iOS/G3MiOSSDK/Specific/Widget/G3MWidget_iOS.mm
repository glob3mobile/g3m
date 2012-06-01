//
//  EAGLView.m
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "G3MWidget_iOS.h"

#import "ES2Renderer.h"

#include "G3MWidget.h"
#include "CompositeRenderer.h"
#include "Planet.hpp"
#include "Logger_iOS.h"
#include "Factory_iOS.h"

IFactory *factory = (IFactory *) new Factory_iOS();
ILogger *logger = (ILogger *) new Logger_iOS(InfoLevel);

@interface G3MWidget_iOS ()
@property(nonatomic, getter=isAnimating) BOOL animating;
@end


@implementation G3MWidget_iOS

@synthesize animating              = _animating;
@synthesize animationFrameInterval = _animationFrameInterval;
@synthesize displayLink            = _displayLink;
@synthesize animationTimer         = _animationTimer;
@synthesize renderer               = _renderer;
@synthesize widget                 = _widget;
/*@synthesize  multipleTouchEnabled*/


// You must implement this method
+ (Class)layerClass {
  return [CAEAGLLayer class];
}

//The EAGL view is stored in the nib file. When it's unarchived it's sent -initWithCoder:
- (id)initWithCoder:(NSCoder *)coder {
  self = [super initWithCoder:coder];
  
  if (self) {
    // Get the layer
    CAEAGLLayer *eaglLayer = (CAEAGLLayer *) self.layer;
    
    eaglLayer.opaque = TRUE;
    eaglLayer.drawableProperties = [NSDictionary dictionaryWithObjectsAndKeys:
                                    [NSNumber numberWithBool:FALSE], kEAGLDrawablePropertyRetainedBacking, kEAGLColorFormatRGBA8, kEAGLDrawablePropertyColorFormat, nil];
    
    // create IGL object
    _renderer = [[ES2Renderer alloc] init];
    if (!_renderer) {
      printf("**** ERROR: G3MWidget_iOS Mobile needs Opengl ES 2.0\n");
    }
    else {
      printf("*** Using Opengl ES 2.0\n\n");
      glver = OpenGL_2;
    }
    
    // create GLOB3M WIDGET
    //int w = [self frame].size.width;
    //int h = [self frame].size.height;
    
    CompositeRenderer* comp = new CompositeRenderer();
    _widget = G3MWidget::create(Planet::createEarth(), comp); 
    // testing Logger
    logger->logInfo("testing Logger...\n");
    
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    
    // rest of initialization
    _animating = FALSE;
    displayLinkSupported = FALSE;
    _animationFrameInterval = 1;
    _displayLink = nil;
    _animationTimer = nil;
    
    self.multipleTouchEnabled = YES; //NECESSARY FOR PROPER PINCH EVENT
    
    // A system version of 3.1 or greater is required to use CADisplayLink. The NSTimer
    // class is used as fallback when it isn't available.
    NSString *reqSysVer = @"3.1";
    NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
    if ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending)
      displayLinkSupported = TRUE;
    
    //Detecting LongPress
    UILongPressGestureRecognizer *longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
    [self addGestureRecognizer:longPressRecognizer];
  }
  return self;
}

- (IBAction)handleLongPress:(UIGestureRecognizer *)sender {
  
  if (sender.state == UIGestureRecognizerStateEnded) {
    NSLog(@"LONG PRESS");
  }
  
}


- (void)drawView:(id)sender {
  if (_animating) {
    [_renderer render: [self widget]];
  }
}

- (void)layoutSubviews {
  int w = [self frame].size.width;
  int h = [self frame].size.height;
  //SceneController::GetInstance()->ResizeCanvas(w, h);
  printf("RESIZING CANVAS: %d, %d\n", w, h);
  //SceneController::GetInstance()->ForceRedrawNextFrame();
  
  [_renderer resizeFromLayer:(CAEAGLLayer *) self.layer];
  [self drawView:nil];
}

//- (NSInteger)animationFrameInterval {
//    return _animationFrameInterval;
//}

- (void)setAnimationFrameInterval:(NSInteger)frameInterval {
  // Frame interval defines how many display frames must pass between each time the
  // display link fires. The display link will only fire 30 times a second when the
  // frame internal is two on a display that refreshes 60 times a second. The default
  // frame interval setting of one will fire 60 times a second when the display refreshes
  // at 60 times a second. A frame interval setting of less than one results in undefined
  // behavior.
  if (frameInterval >= 1) {
    _animationFrameInterval = frameInterval;
    
    if (_animating) {
      [self stopAnimation];
      [self startAnimation];
    }
  }
}

- (void)startAnimation {
  if (!_animating) {
    if (displayLinkSupported) {
      // CADisplayLink is API new to iPhone SDK 3.1. Compiling against earlier versions will result in a warning, but can be dismissed
      // if the system version runtime check for CADisplayLink exists in -initWithCoder:. The runtime check ensures this code will
      // not be called in system versions earlier than 3.1.
      
      self.displayLink = [NSClassFromString(@"CADisplayLink") displayLinkWithTarget:self selector:@selector(drawView:)];
      [_displayLink setFrameInterval:_animationFrameInterval];
      [_displayLink addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
    }
    else
      self.animationTimer = [NSTimer scheduledTimerWithTimeInterval:(NSTimeInterval) ((1.0 / 60.0) * _animationFrameInterval) target:self selector:@selector(drawView:) userInfo:nil repeats:TRUE];
    
    self.animating = TRUE;
  }
}

- (void)stopAnimation {
  if (_animating) {
    if (displayLinkSupported) {
      [_displayLink invalidate];
      self.displayLink = nil;
    }
    else {
      [_animationTimer invalidate];
      self.animationTimer = nil;
    }
    
    self.animating = FALSE;
  }
}


- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
  UITouch *touch = [touches anyObject];
  CGPoint current = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  //TOUCH EVENT
  Vector2D pos(current.x, current.y);
  Vector2D prevPos(previous.x, previous.y);
  TouchEvent te(TouchEvent::create(Down, new Pointer(pos, prevPos)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(te);
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
  NSSet *allTouches = [event allTouches];
  
  NSUInteger pointersCount = [allTouches count];

  std::vector<const Pointer*> pointers = std::vector<const Pointer*>(pointersCount);
  
  for (int n = 0; n < pointersCount; n++) {
    UITouch *touch = [[allTouches allObjects] objectAtIndex:n];
    CGPoint previous = [touch previousLocationInView:self];
    CGPoint current = [touch locationInView:self];
    
    //1 POINTER
    Vector2D pos(current.x, current.y);
    Vector2D prevPos(previous.x, previous.y);
    pointers.push_back(new Pointer(pos, prevPos));
  }

  //TOUCH EVENT
  TouchEvent te( TouchEvent::create(Move, pointers) );
  
  ((G3MWidget*)[self widget])->onTouchEvent(te);
}


- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
  UITouch *touch = [touches anyObject];
  CGPoint current = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  //TOUCH EVENT
  Vector2D pos(current.x, current.y);
  Vector2D prevPos(previous.x, previous.y);
  TouchEvent te( TouchEvent::create(Up, new Pointer(pos, prevPos)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(te);
}

- (void)dealloc {
}

@end
