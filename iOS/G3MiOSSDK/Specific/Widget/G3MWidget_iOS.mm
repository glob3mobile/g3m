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
#include "Effects.hpp"
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
#include "FrameTasksExecutor.hpp"

#include "IStringBuilder.hpp"
#include "StringBuilder_iOS.hpp"

#include "Box.hpp"

#include "TexturesHandler.hpp"

#include "Logger_iOS.hpp"
#include "Factory_iOS.hpp"
#include "NativeGL2_iOS.hpp"
#include "StringUtils_iOS.hpp"
#include "SingleImageTileTexturizer.hpp"
#include "WMSLayer.hpp"

#include "MathUtils_iOS.hpp"
#include "ThreadUtils_iOS.hpp"

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
                               renderers: (std::vector<Renderer*>) renderers
                                userData: (UserData*) userData
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
                                                                           forceTopLevelTilesRenderOnStart);
  
  [self initWidgetWithCameraRenderer: cameraRenderer
                   cameraConstraints: cameraConstraints
                            layerSet: layerSet
               tilesRenderParameters: parameters
                           renderers: renderers
                            userData: userData];
}

- (void) initWidgetWithCameraRenderer: (CameraRenderer*) cameraRenderer
                    cameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
                             layerSet: (LayerSet*) layerSet
                tilesRenderParameters: (TilesRenderParameters*) parameters
                            renderers: (std::vector<Renderer*>) renderers
                             userData: (UserData*) userData
{
  
  // create GLOB3M WIDGET
  int width = (int) [self frame].size.width;
  int height = (int) [self frame].size.height;
  
  IStringBuilder* stringBuilder = new StringBuilder_iOS();
  IMathUtils*     mathUtils     = new MathUtils_iOS();
  IFactory*       factory       = new Factory_iOS();
  ILogger*        logger        = new Logger_iOS(ErrorLevel);
  NativeGL2_iOS*  nGL           = new NativeGL2_iOS();

  GL* gl = new GL(nGL);
  
  IStorage* storage = new SQLiteStorage_iOS("g3m.cache");
  const bool saveInBackground = true;
  IDownloader* downloader = new CachedDownloader(new Downloader_iOS(8),
                                                 storage,
                                                 saveInBackground);
  
  CompositeRenderer* composite = new CompositeRenderer();
  
  composite->addRenderer(cameraRenderer);
  
  if (layerSet != NULL) {
    if (layerSet->size() > 0) {
      TileTexturizer* texturizer = new MultiLayerTileTexturizer(layerSet);
      //IImage *singleWorldImage = factory->createImageFromFileName("mercator.jpg");
      //TileTexturizer* texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, false);
      
      //Single Mercator image
      //IImage *singleWorldImage = factory->createImageFromFileName("tissot.png");
      //TileTexturizer* texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage, true);
      
      const bool showStatistics = false;
      TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
                                          texturizer,
                                          parameters,
                                          showStatistics);
      composite->addRenderer(tr);
    }
  }
  
  for (int i = 0; i < renderers.size(); i++) {
    composite->addRenderer(renderers[i]);
  }
  
  
  
  
  TextureBuilder* textureBuilder = new CPUTextureBuilder();
  TexturesHandler* texturesHandler = new TexturesHandler(gl, false);
  
  const Planet* planet = Planet::createEarth();
  
  Renderer* busyRenderer = new BusyMeshRenderer();
  
  EffectsScheduler* scheduler = new EffectsScheduler();
  
  FrameTasksExecutor* frameTasksExecutor = new FrameTasksExecutor();
  
  const IStringUtils* stringUtils = new StringUtils_iOS();
  //  if (true) {
  //    int __REMOVE_STRING_UTILS_TESTS;
  //
  //    std::vector<std::string> lines = stringUtils->splitLines("line1\nline2");
  //
  //    printf("%s\n", stringUtils->left("Diego", 1).c_str());
  //    printf("%s\n", stringUtils->substring("Diego", 1).c_str());
  //
  //    std::string line = "name=value";
  //    int equalsPosition = stringUtils->indexOf(line, "=");
  //    std::string name = stringUtils->left(line, equalsPosition);
  //    std::string value = stringUtils->substring(line, equalsPosition+1);
  //    printf("\"%s\"=\"%s\"\n", name.c_str(), value.c_str());
  //
  //    printf("\n");
  //  }
  
  IThreadUtils* threadUtils = new ThreadUtils_iOS();
  
  _widgetVP = G3MWidget::create(frameTasksExecutor,
                                factory,
                                stringUtils,
                                threadUtils,
                                stringBuilder,
                                mathUtils,
                                logger,
                                gl,
                                texturesHandler,
                                textureBuilder,
                                downloader,
                                planet,
                                cameraConstraints,
                                composite,
                                busyRenderer,
                                scheduler,
                                width, height,
                                Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
                                true,
                                false);
  
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
    Touch *touch = new Touch(Vector2D(tapPoint.x, tapPoint.y), Vector2D(0.0, 0.0), 1);
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
    
    Touch *touch = new Touch(Vector2D(current.x, current.y),
                             Vector2D(previous.x, previous.y),
                             tapCount);
    
    pointers.push_back(touch);
  }
  
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }
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
    
    Touch *touch = new Touch(Vector2D(current.x, current.y),
                             Vector2D(previous.x, previous.y));
    
    pointers.push_back(touch);
  }
  
  // test if finger orders are the same that in the previous gesture
  if (lastTouchEvent!=NULL) {
    if (pointers.size()==2 && lastTouchEvent->getTouchCount()==2) {
      Vector2D current0 = pointers[0]->getPrevPos();
      Vector2D last0 = lastTouchEvent->getTouch(0)->getPos();
      Vector2D last1 = lastTouchEvent->getTouch(1)->getPos();
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
    
    Touch *touch = new Touch(Vector2D(current.x, current.y),
                             Vector2D(previous.x, previous.y));
    
    pointers.push_back(touch);
  }
  
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }
  lastTouchEvent = TouchEvent::create(Up, pointers);
  [self widget]->onTouchEvent(lastTouchEvent);
}

- (void)dealloc {
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }
}

- (G3MWidget*) widget {
  return (G3MWidget*) _widgetVP;
}

@end
