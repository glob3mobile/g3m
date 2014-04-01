//
//  EAGLView.m
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "G3MWidget_iOS.h"

#import "ES2Renderer.h"

#include "G3MWidget.hpp"
#include "MathUtils_iOS.hpp"
#include "Logger_iOS.hpp"
#include "Factory_iOS.hpp"
#include "StringUtils_iOS.hpp"
#include "JSONParser_iOS.hpp"
#include "StringBuilder_iOS.hpp"
#include "TextUtils_iOS.hpp"
#include "GPUProgramManager.hpp"
#include "SceneLighting.hpp"
#include "Planet.hpp"
#include "Sector.hpp"
#include "InitialCameraPositionProvider.hpp"

@interface G3MWidget_iOS ()
@property(nonatomic, getter=isAnimating) BOOL animating;
@end



@implementation G3MWidget_iOS

@synthesize animating              = _animating;
@synthesize animationFrameInterval = _animationFrameInterval;
@synthesize displayLink            = _displayLink;
@synthesize animationTimer         = _animationTimer;
@synthesize renderer               = _renderer;


// You must implement this method
+ (Class)layerClass {
  return [CAEAGLLayer class];
}


- (void)          initWidget: (IStorage*) storage
                  downloader: (IDownloader*) downloader
                 threadUtils: (IThreadUtils*) threadUtils
      cameraActivityListener: (ICameraActivityListener*) cameraActivityListener
                      planet: (const Planet*) planet
           cameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
              cameraRenderer: (CameraRenderer*) cameraRenderer
                mainRenderer: (Renderer*) mainRenderer
                busyRenderer: (ProtoRenderer*) busyRenderer
               errorRenderer: (ErrorRenderer*) errorRenderer
                 hudRenderer: (Renderer*) hudRenderer
             backgroundColor: (Color) backgroundColor
                      logFPS: (bool) logFPS
     logDownloaderStatistics: (bool) logDownloaderStatistics
          initializationTask: (GInitializationTask*) initializationTask
autoDeleteInitializationTask: (bool) autoDeleteInitializationTask
             periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks
                    userData: (WidgetUserData*) userData
       initialCameraPosition: (Geodetic3D) initialCameraPosition;
{
  GPUProgramFactory * gpuProgramFactory = new GPUProgramFactory();
  GPUProgramManager * gpuProgramManager = new GPUProgramManager(gpuProgramFactory);

  SceneLighting* sceneLighting = new CameraFocusSceneLighting(Color::fromRGBA(0.3f, 0.3f, 0.3f, 1.0f),
                                                              Color::white());

  InitialCameraPositionProvider* icpp = new SimpleInitialCameraPositionProvider();

  _widgetVP = G3MWidget::create([_renderer getGL],
                                storage,
                                downloader,
                                threadUtils,
                                cameraActivityListener,
                                planet,
                                cameraConstraints,
                                cameraRenderer,
                                mainRenderer,
                                busyRenderer,
                                errorRenderer,
                                hudRenderer,
                                backgroundColor,
                                logFPS,
                                logDownloaderStatistics,
                                initializationTask,
                                autoDeleteInitializationTask,
                                periodicalTasks,
                                gpuProgramManager,
                                sceneLighting,
                                icpp);

  [self widget]->setUserData(userData);
}

- (GL*)getGL {
  return [_renderer getGL];
}

- (void)setWidget:(G3MWidget*) widget {
  _widgetVP = widget;
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
//    // for retina display
//    eaglLayer.contentsScale = 2;
//    if ([[UIScreen mainScreen] respondsToSelector:@selector(displayLinkWithTarget:selector:)]) {
//      eaglLayer.contentsScale = [UIScreen mainScreen].scale;
//    }

    // create GL object
    _renderer = [[ES2Renderer alloc] init];
    if (!_renderer) {
      printf("**** ERROR: G3MWidget_iOS Mobile needs OpenGL ES 2.0\n");
      return nil;
    }
    else {
      printf("*** Using Opengl ES 2.0\n\n");
    }

    bool showOpenGLExtensions = false;
    if (showOpenGLExtensions) {
      NSLog(@"----------------------------------------------------------------------------");
      NSLog(@"OpenGL Extensions:");
      NSString *extensionString = [[NSString stringWithUTF8String:(char*)glGetString(GL_EXTENSIONS)] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
      NSArray *extensions = [extensionString componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
      for (NSString *extension in extensions) {
        NSLog(@"  %@", extension);
      }
      NSLog(@"----------------------------------------------------------------------------");
    }

    _lastTouchEvent = NULL;

    // rest of initialization
    _animating = FALSE;
    _displayLinkSupported = FALSE;
    _animationFrameInterval = 1;
    _displayLink = nil;
    _animationTimer = nil;

    self.multipleTouchEnabled = YES; //NECESSARY FOR PROPER PINCH EVENT

    // A system version of 3.1 or greater is required to use CADisplayLink. The NSTimer
    // class is used as fallback when it isn't available.
    NSString *reqSysVer = @"3.1";
    NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
    if ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending) {
      _displayLinkSupported = TRUE;
    }

    //Detecting LongPress
    UILongPressGestureRecognizer *longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self
                                                                                                      action:@selector(handleLongPress:)];
    longPressRecognizer.minimumPressDuration = 1.0;
    [self addGestureRecognizer:longPressRecognizer];
  }
  return self;
}

- (IBAction)handleLongPress:(UIGestureRecognizer *)sender {
  //  printf ("Longpress. state=%d\n", sender.state);
  //
  //  if (sender.state == UIGestureRecognizerStateEnded) {
  //    NSLog(@"LONG PRESS");
  //  }

  if (sender.state == 1) {
    CGPoint tapPoint = [sender locationInView:sender.view];

    std::vector<const Touch*> pointers = std::vector<const Touch*>();
    Touch *touch = new Touch(Vector2I((int) tapPoint.x,
                                      (int) tapPoint.y),
                             Vector2I(0, 0),
                             1);
    pointers.push_back(touch);

    delete _lastTouchEvent;
    _lastTouchEvent = TouchEvent::create(LongPress, pointers);
    [self widget]->onTouchEvent(_lastTouchEvent);
  }

}

- (void)drawView:(id)sender {
  if (_animating) {
    /*int timeToRedraw = */[_renderer render: [self widget]];
  }
}

- (void)layoutSubviews {
  [super layoutSubviews];

  CGSize size = [self frame].size;
  const int width  = (int) size.width;
  const int height = (int) size.height;
  //NSLog(@"ResizeViewportEvent: %dx%d", width, height);

  G3MWidget* widget = [self widget];
  if (widget) {
    widget->onResizeViewportEvent(width, height);

    [_renderer resizeFromLayer:(CAEAGLLayer *) self.layer];

    [self drawView:nil];
  }
  else {
    NSLog(@"Widget is not set");
  }
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
    if (_displayLinkSupported) {
      self.displayLink = [CADisplayLink displayLinkWithTarget:self
                                                     selector:@selector(drawView:)];
      [_displayLink setFrameInterval:_animationFrameInterval];
      [_displayLink addToRunLoop:[NSRunLoop currentRunLoop]
                         forMode:NSDefaultRunLoopMode];
    }
    else {
      self.animationTimer = [NSTimer scheduledTimerWithTimeInterval:(NSTimeInterval) ((1.0 / 60.0) * _animationFrameInterval)
                                                             target:self
                                                           selector:@selector(drawView:)
                                                           userInfo:nil
                                                            repeats:TRUE];
    }

    self.animating = TRUE;
  }
}

- (void)stopAnimation {
  if (_animating) {
    if (_displayLinkSupported) {
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

- (void) touchesBegan: (NSSet*) touches
            withEvent: (UIEvent*) event
{
  NSSet *allTouches = [event touchesForView:self];

  std::vector<const Touch*> pointers = std::vector<const Touch*>();

  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current         = [touch locationInView:self];
    CGPoint previous        = [touch previousLocationInView:self];
    unsigned char tapCount  = (unsigned char) [touch tapCount];

    Touch *touch = new Touch(Vector2I((int) current.x,
                                      (int) current.y),
                             Vector2I((int) previous.x,
                                      (int) previous.y),
                             tapCount);

    pointers.push_back(touch);
  }

  delete _lastTouchEvent;

  _lastTouchEvent = TouchEvent::create(Down, pointers);
  [self widget]->onTouchEvent(_lastTouchEvent);
}


- (void) touchesMoved: (NSSet*) touches
            withEvent: (UIEvent*) event
{
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];

  std::vector<const Touch*> pointers = std::vector<const Touch*>();

  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current  = [touch locationInView:self];
    CGPoint previous = [touch previousLocationInView:self];

    Touch *touch = new Touch(Vector2I((int) current.x,
                                      (int) current.y),
                             Vector2I((int) previous.x,
                                      (int) previous.y));

    pointers.push_back(touch);
  }

  // test if finger orders are the same that in the previous gesture
  if (_lastTouchEvent == NULL) {
    _lastTouchEvent = TouchEvent::create(Move, pointers);
  }
  else {
    if ((pointers.size() == 2) &&
        (_lastTouchEvent->getTouchCount() == 2)) {

      const Vector2I current0 = pointers[0]->getPrevPos();
      const Vector2I last0 = _lastTouchEvent->getTouch(0)->getPos();
      const Vector2I last1 = _lastTouchEvent->getTouch(1)->getPos();
      delete _lastTouchEvent;
      const double dist0 = current0.sub(last0).squaredLength();
      const double dist1 = current0.sub(last1).squaredLength();

      // swap finger order
      if (dist1 < dist0) {
        std::vector<const Touch*> swappedPointers = std::vector<const Touch*>();
        swappedPointers.push_back(pointers[1]);
        swappedPointers.push_back(pointers[0]);
        _lastTouchEvent = TouchEvent::create(Move, swappedPointers);
      }
      else {
        _lastTouchEvent = TouchEvent::create(Move, pointers);
      }
    }
    else {
      delete _lastTouchEvent;
      _lastTouchEvent = TouchEvent::create(Move, pointers);
    }
  }

  [self widget]->onTouchEvent(_lastTouchEvent);
}


- (void) touchesEnded: (NSSet*) touches
            withEvent: (UIEvent*) event
{
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];

  std::vector<const Touch*> pointers = std::vector<const Touch*>();
  // pointers.reserve([allTouches count]);

  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current  = [touch locationInView:self];
    CGPoint previous = [touch previousLocationInView:self];

    [touch timestamp];

    Touch *touch = new Touch(Vector2I((int) current.x,
                                      (int) current.y),
                             Vector2I((int) previous.x,
                                      (int) previous.y));

    pointers.push_back(touch);
  }

  delete _lastTouchEvent;

  _lastTouchEvent = TouchEvent::create(Up, pointers);
  [self widget]->onTouchEvent(_lastTouchEvent);
}

- (void) dealloc {
  delete _lastTouchEvent;
  [self setRenderer: nil];
  delete (G3MWidget*) _widgetVP;
}

- (G3MWidget*) widget {
  return (G3MWidget*) _widgetVP;
}

- (void)initSingletons {
  ILogger*            logger          = new Logger_iOS(InfoLevel);
  IFactory*           factory         = new Factory_iOS();
  const IStringUtils* stringUtils     = new StringUtils_iOS();
  IStringBuilder*     stringBuilder   = new StringBuilder_iOS();
  IMathUtils*         mathUtils       = new MathUtils_iOS();
  IJSONParser*        jsonParser      = new JSONParser_iOS();
  ITextUtils*         textUtils       = new TextUtils_iOS();

  G3MWidget::initSingletons(logger,
                            factory,
                            stringUtils,
                            stringBuilder,
                            mathUtils,
                            jsonParser,
                            textUtils);
}

- (CameraRenderer*)getCameraRenderer {
  return [self widget]->getCameraRenderer();
}

- (void)setAnimatedCameraPosition: (const Geodetic3D&) position
                     timeInterval: (const TimeInterval&)interval {
  [self widget]->setAnimatedCameraPosition(interval, position);
}

- (void)setAnimatedCameraPosition: (const Geodetic3D&) position {
  [self widget]->setAnimatedCameraPosition(position);
}

- (void)setCameraPosition: (const Geodetic3D&) position {
  [self widget]->setCameraPosition(position);
}

- (void)setCameraHeading: (const Angle&) angle {
  [self widget]->setCameraHeading(angle);
}

- (void)setCameraPitch: (const Angle&) angle {
  [self widget]->setCameraPitch(angle);
}

- (void)cancelCameraAnimation {
  [self widget]->cancelCameraAnimation();
}

- (WidgetUserData*) userData
{
  return [self widget]->getUserData();
}

@end
