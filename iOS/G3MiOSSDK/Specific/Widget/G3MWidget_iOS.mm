//
//  EAGLView.m
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "G3MWidget_iOS.h"

#import "ES2Renderer.h"

#include "G3MWidget.hpp"
#include "CompositeRenderer.hpp"
#include "Planet.hpp"
#include "CameraRenderer.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "CameraConstraints.hpp"
#include "TileRenderer.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "BusyMeshRenderer.hpp"
#include "CPUTextureBuilder.hpp"
#include "LayerSet.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "INativeGL.hpp"
#include "GL.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "TilesRenderParameters.hpp"
#include "IStringBuilder.hpp"
#include "Box.hpp"
#include "TexturesHandler.hpp"
#include "WMSLayer.hpp"
#include "MathUtils_iOS.hpp"
#include "ThreadUtils_iOS.hpp"
#include "Logger_iOS.hpp"
#include "Factory_iOS.hpp"
#include "NativeGL2_iOS.hpp"
#include "StringUtils_iOS.hpp"
#include "JSONParser_iOS.hpp"
#include "StringBuilder_iOS.hpp"

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

- (void) initWidgetWithCameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
                                layerSet: (LayerSet*) layerSet
                  incrementalTileQuality: (bool) incrementalTileQuality
                               renderers: (std::vector<Renderer*>) renderers
                                userData: (UserData*) userData
                      initializationTask: (GTask *) initializationTask
                         periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks
{
  // creates default camera-renderer and camera-handlers
  CameraRenderer *cameraRenderer = new CameraRenderer();
  
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  
  const bool processRotation = true;
  const bool processZoom = true;
  cameraRenderer->addHandler(new CameraDoubleDragHandler(processRotation,
                                                         processZoom));
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceTopLevelTilesRenderOnStart = true;
  
  TilesRenderParameters* parameters = TilesRenderParameters::createDefault(renderDebug,
                                                                           useTilesSplitBudget,
                                                                           forceTopLevelTilesRenderOnStart,
                                                                           incrementalTileQuality);
  
  [self initWidgetWithCameraRenderer: cameraRenderer
                   cameraConstraints: cameraConstraints
                            layerSet: layerSet
               tilesRenderParameters: parameters
                           renderers: renderers
                            userData: userData
                  initializationTask: initializationTask
                     periodicalTasks: periodicalTasks];
}

- (void) initWidgetWithCameraRenderer: (CameraRenderer*) cameraRenderer
                    cameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
                             layerSet: (LayerSet*) layerSet
                tilesRenderParameters: (TilesRenderParameters*) parameters
                            renderers: (std::vector<Renderer*>) renderers
                             userData: (UserData*) userData
                   initializationTask: (GTask*) initializationTask
                      periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks
{
  
  const int width  = (int) [self frame].size.width;
  const int height = (int) [self frame].size.height;
  
  NativeGL2_iOS* nativeGL = new NativeGL2_iOS();
  
  CompositeRenderer* mainRenderer = new CompositeRenderer();
  
  if (layerSet != NULL) {
    TileTexturizer* texturizer = new MultiLayerTileTexturizer();
    
    const bool showStatistics = false;
    TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
                                        texturizer,
                                        layerSet,
                                        parameters,
                                        showStatistics);
    mainRenderer->addRenderer(tr);
  }
  
  for (int i = 0; i < renderers.size(); i++) {
    mainRenderer->addRenderer(renderers[i]);
  }
  
  const Planet* planet = Planet::createEarth();
  
  Renderer* busyRenderer = new BusyMeshRenderer();
  
  _widgetVP = G3MWidget::create(nativeGL,
                                planet,
                                cameraConstraints,
                                cameraRenderer,
                                mainRenderer,
                                busyRenderer,
                                width, height,
                                Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
                                true,
                                false,
                                initializationTask,
                                true,
                                periodicalTasks);
  [self widget]->setUserData(userData);
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
    
    // create GL object
    _renderer = [[ES2Renderer alloc] init];
    if (!_renderer) {
      printf("**** ERROR: G3MWidget_iOS Mobile needs Opengl ES 2.0\n");
      return nil;
    }
    else {
      printf("*** Using Opengl ES 2.0\n\n");
      glver = OpenGL_2;
    }
    
    
    NSLog(@"----------------------------------------------------------------------------");
    NSLog(@"OpenGL Extensions:");
    NSString *extensionString = [[NSString stringWithUTF8String:(char*)glGetString(GL_EXTENSIONS)] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    NSArray *extensions = [extensionString componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    for (NSString *extension in extensions) {
      NSLog(@"  %@", extension);
    }
    NSLog(@"----------------------------------------------------------------------------");
    
    
    lastTouchEvent = NULL;
    
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
    if ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending)
      _displayLinkSupported = TRUE;
    
    //Detecting LongPress
    UILongPressGestureRecognizer *longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
    longPressRecognizer.minimumPressDuration = 1.0;
    [self addGestureRecognizer:longPressRecognizer];
  }
  return self;
}

//** Agustin cancelled lonpressgesture because touchedmoved and touchedended event don't work
- (IBAction)handleLongPress:(UIGestureRecognizer *)sender {
  
  //  printf ("Longpress. state=%d\n", sender.state);
  //
  //  if (sender.state == UIGestureRecognizerStateEnded) {
  //    NSLog(@"LONG PRESS");
  //  }
  
  if (sender.state == 1){
    
    CGPoint tapPoint = [sender locationInView:sender.view.superview];
    
    std::vector<const Touch*> pointers = std::vector<const Touch*>();
    Touch *touch = new Touch(Vector2I( GMath.toInt(tapPoint.x), GMath.toInt(tapPoint.y)),
                             Vector2I(0, 0),
                             1);
    pointers.push_back(touch);
    lastTouchEvent = TouchEvent::create(LongPress, pointers);
    [self widget]->onTouchEvent(lastTouchEvent);
  }
  
}

- (void)drawView:(id)sender {
  if (_animating) {
    /*int timeToRedraw = */[_renderer render: [self widget]];
  }
}

- (void)layoutSubviews {
  int w = (int) [self frame].size.width;
  int h = (int) [self frame].size.height;
  NSLog(@"ResizeViewportEvent: %dx%d", w, h);
  [self widget]->onResizeViewportEvent(w,h);
  
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

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
  
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];
  
  std::vector<const Touch*> pointers = std::vector<const Touch*>();
  // pointers.reserve([allTouches count]);
  
  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current         = [touch locationInView:self];
    CGPoint previous        = [touch previousLocationInView:self];
    unsigned char tapCount  = (unsigned char) [touch tapCount];
    
    Touch *touch = new Touch(Vector2I( GMath.toInt(current.x), GMath.toInt(current.y) ),
                             Vector2I( GMath.toInt(previous.x), GMath.toInt(previous.y) ),
                             tapCount);
    
    pointers.push_back(touch);
  }
  
  delete lastTouchEvent;
      
  lastTouchEvent = TouchEvent::create(Down, pointers);
  [self widget]->onTouchEvent(lastTouchEvent);
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
  
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];
  
  std::vector<const Touch*> pointers = std::vector<const Touch*>();
  
  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current  = [touch locationInView:self];
    CGPoint previous = [touch previousLocationInView:self];
    
    Touch *touch = new Touch(Vector2I( GMath.toInt(current.x), GMath.toInt(current.y) ),
                             Vector2I( GMath.toInt(previous.x), GMath.toInt(previous.y) ));
    
    pointers.push_back(touch);
  }
  
  // test if finger orders are the same that in the previous gesture
  if (lastTouchEvent!=NULL) {
    if (pointers.size()==2 && lastTouchEvent->getTouchCount()==2) {
      Vector2I current0 = pointers[0]->getPrevPos();
      Vector2I last0 = lastTouchEvent->getTouch(0)->getPos();
      Vector2I last1 = lastTouchEvent->getTouch(1)->getPos();
      delete lastTouchEvent;
      double dist0 = current0.sub(last0).squaredLength();
      double dist1 = current0.sub(last1).squaredLength();
      
      // swap finger order
      if (dist1<dist0) {
        std::vector<const Touch*> swappedPointers = std::vector<const Touch*>();
        swappedPointers.push_back(pointers[1]);
        swappedPointers.push_back(pointers[0]);
        lastTouchEvent = TouchEvent::create(Move, swappedPointers);
      } else {
        lastTouchEvent = TouchEvent::create(Move, pointers);
      }
    } else {
      delete lastTouchEvent;
      lastTouchEvent = TouchEvent::create(Move, pointers);
    }
  } else {
    lastTouchEvent = TouchEvent::create(Move, pointers);
  }
  
  [self widget]->onTouchEvent(lastTouchEvent);
}



- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
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
    
    Touch *touch = new Touch(Vector2I( GMath.toInt(current.x), GMath.toInt(current.y) ),
                             Vector2I( GMath.toInt(previous.x), GMath.toInt(previous.y) ) );
    
    pointers.push_back(touch);
  }
  
  delete lastTouchEvent;
  
  lastTouchEvent = TouchEvent::create(Up, pointers);
  [self widget]->onTouchEvent(lastTouchEvent);
}

- (void)dealloc {
  delete lastTouchEvent;
}

- (G3MWidget*) widget {
  return (G3MWidget*) _widgetVP;
}

- (void)initSingletons {

    ILogger*            logger          = new Logger_iOS(WarningLevel);
    IFactory*           factory         = new Factory_iOS();
    const IStringUtils* stringUtils     = new StringUtils_iOS();
    IThreadUtils*       threadUtils     = new ThreadUtils_iOS();
    IStringBuilder*     stringBuilder   = new StringBuilder_iOS();
    IMathUtils*         mathUtils       = new MathUtils_iOS();
    IJSONParser*        jsonParser      = new JSONParser_iOS();
    
    IStorage*           storage         = new SQLiteStorage_iOS("g3m.cache");
    const bool          saveInBackground= true;
    IDownloader*        downloader      = new CachedDownloader(new Downloader_iOS(8),
                                                   saveInBackground);
    
    G3MWidget::initSingletons(logger, factory, stringUtils, threadUtils, stringBuilder, mathUtils, jsonParser, storage, downloader);
}

@end
