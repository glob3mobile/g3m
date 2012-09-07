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

  IStringBuilder::setInstance(new StringBuilder_iOS()); //Setting StringBuilder

  IFactory *factory  = new Factory_iOS();
  ILogger *logger    = new Logger_iOS(ErrorLevel);
  NativeGL2_iOS* nGL = new NativeGL2_iOS();
  GL* gl  = new GL(nGL);
  
  IMathUtils::setInstance(new MathUtils_iOS()); //Mathematics utilities
  
  IStorage* storage = new SQLiteStorage_iOS("g3m.cache");
  IDownloader* downloader = new CachedDownloader(new Downloader_iOS(8),
                                                 storage);

  CompositeRenderer* composite = new CompositeRenderer();
  
  composite->addRenderer(cameraRenderer);
  
  if (layerSet != NULL) {
    if (layerSet->size() > 0) {
      TileTexturizer* texturizer = new MultiLayerTileTexturizer(layerSet);
//      IImage *singleWorldImage = factory->createImageFromFileName("world.jpg");
//      TileTexturizer* texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);

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
  TexturesHandler* texturesHandler = new TexturesHandler(gl, factory, textureBuilder, false);
  
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
                                logger,
                                gl,
                                texturesHandler,
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

//- (void) initWidgetDemo
//{
//
//  // create GLOB3M WIDGET
//  int width = (int) [self frame].size.width;
//  int height = (int) [self frame].size.height;
//
//  IFactory *factory = new Factory_iOS();
//  ILogger *logger = new Logger_iOS(ErrorLevel);
//
//  NativeGL2_iOS * nGL = new NativeGL2_iOS();
//  GL* gl  = new GL(nGL);
//
//  //Testing BOX intersection
//  if (true){
//    Box b(Vector3D(-10,-10,-10) , Vector3D(10,10,10) );
//
//    Vector3D v = b.intersectionWithRay(Vector3D(-20,0,0), Vector3D(1.0,0,0));
//    printf("%f, %f, %f\n", v.x(), v.y(), v.z());
//
//    Vector3D v1 = b.intersectionWithRay(Vector3D(-20,20,0), Vector3D(1.0,0,0));
//    printf("%f, %f, %f\n", v1.x(), v1.y(), v1.z());
//
//    Vector3D v2 = b.intersectionWithRay(Vector3D(-20,0,0), Vector3D(1.0,0.1,0));
//    printf("%f, %f, %f\n", v2.x(), v2.y(), v2.z());
//  }
//
//  // composite renderer is the father of the rest of renderers
//  CompositeRenderer* comp = new CompositeRenderer();
//
//  // camera renderer and handlers
//  CameraRenderer *cameraRenderer;
//  cameraRenderer = new CameraRenderer();
//  const bool useInertia = true;
//  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
//  const bool processRotation = true;
//  const bool processZoom = true;
//  cameraRenderer->addHandler(new CameraDoubleDragHandler(processRotation,
//                                                         processZoom));
//  cameraRenderer->addHandler(new CameraRotationHandler());
//  cameraRenderer->addHandler(new CameraDoubleTapHandler());
//  comp->addRenderer(cameraRenderer);
//
//
//  IStorage* storage = new SQLiteStorage_iOS("g3m.cache");
//  IDownloader* downloader = new CachedDownloader(new Downloader_iOS(8),
//                                                 storage);
//
//  if (false) {
//    class Listener : public IDownloadListener {
//    private:
//      int _onDownload;
//      int _onError;
//      int _onCancel;
//
//    public:
//      Listener() :
//      _onDownload(0),
//      _onError(0),
//      _onCancel(0)
//      {
//
//      }
//
//      void onDownload(const Response* response) {
//        _onDownload++;
//        BOOL isMainThread = [NSThread isMainThread];
//        if (isMainThread) {
//          NSLog(@"*** Main-Thread: Downloaded %d bytes ***", response->getByteBuffer()->getLength());
//        }
//        else {
//          NSLog(@"*** NOT IN Main-Thread: Downloaded %d bytes ***", response->getByteBuffer()->getLength());
//        }
//      }
//
//      void onError(const Response* response) {
//        _onError++;
//      }
//
//      void onCancel(const URL* url) {
//        _onCancel++;
//      }
//
//      void onCanceledDownload(const Response* response) {
//      }
//
//      void showInvalidState() const {
//        printf("onDownload=%d, onCancel=%d, onError=%d\n", _onDownload, _onCancel, _onError);
//      }
//
//      void testState() const {
//        if ((_onDownload == 1) && (_onCancel == 0) && (_onError == 0)) {
//          return;
//        }
//        if ((_onDownload == 0) && (_onCancel == 1) && (_onError == 0)) {
//          return;
//        }
//        if ((_onDownload == 0) && (_onCancel == 0) && (_onError == 1)) {
//          return;
//        }
//        showInvalidState();
//      }
//
//      virtual ~Listener() {
//        testState();
//      }
//    };
//
//    const long priority = 999999999;
//    long requestId = downloader->request(URL("http://glob3.sourceforge.net/img/isologo640x160.png"), priority, new Listener(), true);
//    long requestId2 = downloader->request(URL("http://glob3.sourceforge.net/img/isologo640x160.png"), priority, new Listener(), true);
//    downloader->cancelRequest(requestId);
//    downloader->cancelRequest(requestId2);
//
//    printf("break (point) on me 2");
//  }
//
//
//  //LAYERS
//  LayerSet* layerSet = new LayerSet();
//
//  WMSLayer* blueMarble = new WMSLayer("bmng200405",
//                                      "http://www.nasa.network.com/wms?",
//                                      WMS_1_1_0,
//                                      "image/jpeg",
//                                      Sector::fullSphere(),
//                                      "EPSG:4326",
//                                      "",
//                                      false,
//                                      Angle::nan(),
//                                      Angle::nan());
//  layerSet->addLayer(blueMarble);
//
//  //  WMSLayer *pnoa = new WMSLayer("PNOA",
//  //                                "http://www.idee.es/wms/PNOA/PNOA",
//  //                                WMS_1_1_0,
//  //                                "image/png",
//  //                                Sector::fromDegrees(21, -18, 45, 6),
//  //                                "EPSG:4326",
//  //                                "",
//  //                                true,
//  //                                Angle::nan(),
//  //                                Angle::nan());
//  //  layerSet->addLayer(pnoa);
//
//  //  WMSLayer *vias = new WMSLayer("VIAS",
//  //                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
//  //                                WMS_1_1_0,
//  //                                "image/gif",
//  //                                Sector::fromDegrees(22.5,-22.5, 33.75, -11.25),
//  //                                "EPSG:4326",
//  //                                "",
//  //                                true,
//  //                                Angle::nan(),
//  //                                Angle::nan());
//  //  layerSet->addLayer(vias);
//
//  //  WMSLayer *oceans = new WMSLayer(//"igo:bmng200401,igo:sttOZ,igo:cntOZ",
//  //                                  "bmsstcnt",
//  ////                                  "OZ",
//  //                                  "bmsstcnt",
//  ////                                  "OZ",
//  ////                                  "http://igosoftware.dyndns.org:8081/geoserver/igo/wms",
//  //                                  "http://igosoftware.dyndns.org:8081/geowebcache/service/wms",
//  //                                  WMS_1_1_0,
//  //                                  "image/jpeg",
//  //                                  Sector::fullSphere(),
//  //                                  "EPSG:4326",
//  //                                  "",
//  //                                  false,
//  //                                  Angle::nan(),
//  //                                  Angle::nan());
//  //
//  //  oceans->addTerrainTouchEventListener(new OceanTerrainTouchEventListener(factory, downloader));
//  //  layerSet->addLayer(oceans);
//
//  //  WMSLayer *osm = new WMSLayer("bing",
//  //                               "bing",
//  //                               "http://wms.latlon.org/",
//  //                               WMS_1_1_0,
//  //                               "image/jpeg",
//  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
//  //                               "EPSG:4326",
//  //                               "",
//  //                               false,
//  //                               Angle::nan(),
//  //                               Angle::nan());
//  //  layerSet->addLayer(osm);
//
//  //  WMSLayer *osm = new WMSLayer("osm",
//  //                               "osm",
//  //                               "http://wms.latlon.org/",
//  //                               WMS_1_1_0,
//  //                               "image/jpeg",
//  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
//  //                               "EPSG:4326",
//  //                               "",
//  //                               false,
//  //                               Angle::nan(),
//  //                               Angle::nan());
//  //  layerSet->addLayer(osm);
//
//
//  // very basic tile renderer
//  if (true) {
//    const bool renderDebug = false;
//    TilesRenderParameters* parameters = TilesRenderParameters::createDefault(renderDebug);
//    //    TilesRenderParameters* parameters = TilesRenderParameters::createSingleSector(renderDebug);
//
//    TileTexturizer* texturizer = NULL;
//    if (true) {
//      texturizer = new MultiLayerTileTexturizer(layerSet);
//    }
//    else {
//      //SINGLE IMAGE
//      IImage *singleWorldImage = factory->createImageFromFileName("world.jpg");
//      texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
//    }
//
//    const bool showStatistics = false;
//    TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
//                                        texturizer,
//                                        parameters,
//                                        showStatistics);
//    comp->addRenderer(tr);
//  }
//
//  if (false) {
//    // dummy renderer with a simple box
//    DummyRenderer* dum = new DummyRenderer();
//    comp->addRenderer(dum);
//  }
//
//  if (false) {
//    // simple planet renderer, with a basic world image
//    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
//    comp->addRenderer(spr);
//  }
//
//  if (false) {
//    // marks renderer
//    MarksRenderer* marks = new MarksRenderer();
//    comp->addRenderer(marks);
//
//    Mark* m1 = new Mark("Fuerteventura",
//                        "g3m-marker.png",
//                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
//    //m1->addTouchListener(listener);
//    marks->addMark(m1);
//
//
//    Mark* m2 = new Mark("Las Palmas",
//                        "g3m-marker.png",
//                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
//    //m2->addTouchListener(listener);
//    marks->addMark(m2);
//
//    if (false) {
//      for (int i = 0; i < 500; i++) {
//        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
//        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
//        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
//
//        marks->addMark(new Mark("Random",
//                                "g3m-marker.png",
//                                Geodetic3D(latitude, longitude, 0)));
//      }
//    }
//  }
//
//  if (false) {
//    LatLonMeshRenderer *renderer = new LatLonMeshRenderer();
//    comp->addRenderer(renderer);
//  }
//
//  EffectsScheduler* scheduler = new EffectsScheduler();
//
//  if (false) {
//    EffectTarget* target = NULL;
//    scheduler->startEffect(new SampleEffect(TimeInterval::fromSeconds(2)),
//                           target);
//  }
//
//  if (false) {
//    SceneGraphRenderer* sgr = new SceneGraphRenderer();
//    SGCubeNode* cube = new SGCubeNode();
//    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
//    sgr->getRootNode()->addChild(cube);
//    comp->addRenderer(sgr);
//  }
//
//  //  comp->addRenderer(new GLErrorRenderer());
//
//
//  TextureBuilder* texBuilder = new CPUTextureBuilder();
//  TexturesHandler* texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
//
//  const Planet* planet = Planet::createEarth();
//
//  //Renderer* busyRenderer = new BusyQuadRenderer("ProgressWheel.png");
//  Renderer* busyRenderer = new BusyMeshRenderer();
//
//  std::vector <ICameraConstrainer*> cameraConstraint;
//  cameraConstraint.push_back(new SimpleCameraConstrainer);
//
//  const bool logFPS = false;
//  const bool logDownloaderStatistics = false;
//
//  FrameTasksExecutor* frameTasksExecutor = new FrameTasksExecutor();
//
//  _widgetVP = G3MWidget::create(frameTasksExecutor,
//                                factory,
//                                logger,
//                                gl,
//                                texturesHandler,
//                                downloader,
//                                planet,
//                                cameraConstraint,
//                                comp,
//                                busyRenderer,
//                                scheduler,
//                                width, height,
//                                Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
//                                logFPS,
//                                logDownloaderStatistics);
//}

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
