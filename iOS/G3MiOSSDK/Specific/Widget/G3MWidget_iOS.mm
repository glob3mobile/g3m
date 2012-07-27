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
#include "Logger_iOS.hpp"
#include "Factory_iOS.hpp"
#include "GL2.hpp"

#include "CameraRenderer.hpp"
#include "TileRenderer.hpp"
#include "DummyRenderer.hpp"
#include "MarksRenderer.hpp"
#include "SimplePlanetRenderer.hpp"
#include "Effects.hpp"
#include "SceneGraphRenderer.hpp"
#include "GLErrorRenderer.hpp"
#include "EllipsoidalTileTessellator.hpp"

#include "DummyDownload.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "FileSystemStorage.hpp"
#include "NullStorage.hpp"
#include "TileImagesTileTexturizer.hpp"
#include "SingleImageTileTexturizer.hpp"
#include "BusyRenderer.hpp"
#include "CPUTextureBuilder.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"
#include "StaticImageLayer.hpp"

#include "Downloader_iOS.hpp"

#include <stdlib.h>

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

- (void) initWidgetCSIRO
{
  // create GLOB3M WIDGET
  int width = [self frame].size.width;
  int height = [self frame].size.height;

  IFactory *factory = new Factory_iOS();
  ILogger *logger = new Logger_iOS(ErrorLevel);
  IGL* gl  = new GL2();

  // composite renderer is the father of the rest of renderers
  CompositeRenderer* comp = new CompositeRenderer();
  
  // camera renderer
  CameraRenderer *cameraRenderer = new CameraRenderer();
  comp->addRenderer(cameraRenderer);
  
  //STORAGE
  NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
  FileSystemStorage * fss = new FileSystemStorage([documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding]);
  Downloader* downloaderOLD = new Downloader(fss, 5, factory->createNetwork());
  IDownloader* downloader = new Downloader_iOS();

  //LAYERS
  LayerSet* layerSet = new LayerSet();
  WMSLayer* baseLayer = new WMSLayer("bmng200405", "http://www.nasa.network.com/wms?", 
                                     "1.3", "image/jpeg", Sector::fullSphere(), "EPSG:4326", "");
  layerSet->add(baseLayer);

  Sector s = Sector::fromDegrees(-60, 50, 10, 185);
  //Sector s = Sector::fullSphere();
  WMSLayer *wmsl = new WMSLayer("test:contourGSLA","http://imos2.ersa.edu.au/geo2/test/wms","1.1.1", "image/png", s, "EPSG:4326", "sla_test");
    
  WMSLayer *wms_sst = new WMSLayer("sea_surface_temperature","http://opendap-vpac.arcs.org.au/thredds/wms/IMOS/SRS/GHRSST-SSTsubskin/2012/20120626-ABOM-L3P_GHRSST-SSTsubskin-AVHRR_MOSAIC_01km-AO_DAAC-v01-fv01_0.nc?","1.3.0", "image/png", s, "EPSG:4326&COLORSCALERANGE=273.8%2C302.8&NUMCOLORBANDS=50&LOGSCALE=false", "boxfill%2Fsst_36");
  
  //layerSet->add(wmsl);
  //layerSet->add(wms_sst);
  
  int testing_SIL;
  if (false){
    IImage *image = factory->createImageFromFileName("20120720_cintp1.png");
    StaticImageLayer * imageLayer = new StaticImageLayer("SIL",
                                                         image,
                                                         Sector::fromDegrees(-60, 50, 10, 185), 
                                                         fss);
    layerSet->add(imageLayer);
  }
  
  // very basic tile renderer
  if (true) {
    TileParameters* parameters = TileParameters::createDefault(true);
    
    TileTexturizer* texturizer = NULL;
    if (true) {
      texturizer = new TileImagesTileTexturizer(parameters, downloaderOLD, layerSet); //WMS
    }
    else {
      //SINGLE IMAGE
      IImage *singleWorldImage = factory->createImageFromFileName("world.jpg");
      texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
    }
    
    const bool showStatistics = false;
    TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
                                        texturizer,
                                        parameters,
                                        showStatistics);
    comp->addRenderer(tr);
  }
  
  if (false) {
    // dummy renderer with a simple box
    DummyRenderer* dum = new DummyRenderer();
    comp->addRenderer(dum);
  }
  
  if (false) {
    // simple planet renderer, with a basic world image
    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
    comp->addRenderer(spr);
  }

  TextureBuilder* texBuilder = new CPUTextureBuilder();
  TexturesHandler* texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
  
  const Planet* planet = Planet::createEarth();
  
  Renderer* busyRenderer = new BusyRenderer();
  
  
  _widget = G3MWidget::create(factory,
                              logger,
                              gl,
                              texturesHandler,
                              downloaderOLD,
                              downloader,
                              planet, 
                              comp,
                              busyRenderer,
                              width, height,
                              Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
                              true);
  
  Geodetic3D australia = Geodetic3D::fromDegrees(-26.91, 133.94, 1.1e7);
  ((G3MWidget*)_widget)->getCamera()->setPosition(*planet, australia);

}

- (void) initWidgetDemo
{
  
  // create GLOB3M WIDGET
  int width = [self frame].size.width;
  int height = [self frame].size.height;

  IFactory *factory = new Factory_iOS();
  ILogger *logger = new Logger_iOS(ErrorLevel);
  IGL* gl  = new GL2();
  
  //Testing downloads
  if (false) {
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    
    FileSystemStorage * fss = new FileSystemStorage([documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding]);
    
    DummyDownload *dummyDownload = new DummyDownload(factory, fss );
    dummyDownload->run();
  }
  
  if (false){
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSLog(@"\nDocument Directory: %s;", [documentsDirectory UTF8String]);
    
    SQLiteStorage_iOS *sql = new SQLiteStorage_iOS("test.db", "file");
    DummyDownload *dummyDownload = new DummyDownload(factory, sql);
    
    std::string directory = "";
    std::string file1 = "";
    std::string file2 = "";
    std::string file3 = "";
    
    if(false){
      directory = "/Users/vidalete/Downloads/";
      file1 = "Tic2Vtwo.pdf";
      file2 = "Pantallazo.png";
      file3 = "blobtest.png";
    }else{
      directory = [documentsDirectory UTF8String];
      file1 = "mark.png";
      file2 = "world.jpg";
      file3 = "plane.png";
      
      const NSFileManager *fileManager = [NSFileManager defaultManager];
      NSError *error;
      
      NSString *writableDBPathNS = [documentsDirectory stringByAppendingPathComponent:[[NSString alloc] initWithCString:file1.c_str() encoding:NSUTF8StringEncoding]];
      if (![fileManager fileExistsAtPath:writableDBPathNS]){
        // The writable database does not exist, so copy the default to the appropriate location.
        NSString *defaultDBPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:[[NSString alloc] initWithCString:file1.c_str() encoding:NSUTF8StringEncoding]];
        if (![fileManager copyItemAtPath:defaultDBPath toPath:writableDBPathNS error:&error]) {
          NSLog(@"Failed to create writable file with message '%@'.", [error localizedDescription]);
        } 
      }
      
      writableDBPathNS = [documentsDirectory stringByAppendingPathComponent:[[NSString alloc] initWithCString:file2.c_str() encoding:NSUTF8StringEncoding]];
      if (![fileManager fileExistsAtPath:writableDBPathNS]){
        // The writable database does not exist, so copy the default to the appropriate location.
        NSString *defaultDBPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:[[NSString alloc] initWithCString:file2.c_str() encoding:NSUTF8StringEncoding]];
        if (![fileManager copyItemAtPath:defaultDBPath toPath:writableDBPathNS error:&error]) {
          NSLog(@"Failed to create writable file with message '%@'.", [error localizedDescription]);
        } 
      }
      
      writableDBPathNS = [documentsDirectory stringByAppendingPathComponent:[[NSString alloc] initWithCString:file3.c_str() encoding:NSUTF8StringEncoding]];
      if (![fileManager fileExistsAtPath:writableDBPathNS]){
        // The writable database does not exist, so copy the default to the appropriate location.
        NSString *defaultDBPath = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:[[NSString alloc] initWithCString:file3.c_str() encoding:NSUTF8StringEncoding]];
        if (![fileManager copyItemAtPath:defaultDBPath toPath:writableDBPathNS error:&error]) {
          NSLog(@"Failed to create writable file with message '%@'.", [error localizedDescription]);
        } 
      }
      
    }
    
    
    FileSystemStorage * fssAux = new FileSystemStorage([documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding]);
    
    dummyDownload->runSqlite(directory, file1, fssAux);
    dummyDownload->runSqlite(directory, file2, fssAux);
    dummyDownload->runSqlite(directory, file3, fssAux);
  }

  // composite renderer is the father of the rest of renderers
  CompositeRenderer* comp = new CompositeRenderer();
  
  // camera renderer
  CameraRenderer *cameraRenderer = new CameraRenderer();
  comp->addRenderer(cameraRenderer);
  
  //STORAGE
  NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
  FileSystemStorage * fss = new FileSystemStorage([documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding]);
  Downloader* downloaderOLD = new Downloader(fss, 5, factory->createNetwork());
  IDownloader* downloader = new Downloader_iOS();

  //LAYERS
  LayerSet* layerSet = new LayerSet();
  WMSLayer* baseLayer = new WMSLayer("bmng200405", "http://www.nasa.network.com/wms?", 
                                     "1.3", "image/jpeg", Sector::fullSphere(), "EPSG:4326", "");
  layerSet->add(baseLayer);
  
  WMSLayer *wmsl = new WMSLayer("VIAS",
                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
                                "1.1.0", "image/gif", 
                                Sector::fromDegrees(22.5,-22.5, 33.75, -11.25),
                                "EPSG:4326", "");
  
  layerSet->add(wmsl);
  
  // very basic tile renderer
  if (true) {
    TileParameters* parameters = TileParameters::createDefault(true);
    
    TileTexturizer* texturizer = NULL;
    if (true) {
      texturizer = new TileImagesTileTexturizer(parameters, downloaderOLD, layerSet); //WMS
    }
    else {
      //SINGLE IMAGE
      IImage *singleWorldImage = factory->createImageFromFileName("world.jpg");
      texturizer = new SingleImageTileTexturizer(parameters, singleWorldImage);
    }
    
    const bool showStatistics = false;
    TileRenderer* tr = new TileRenderer(new EllipsoidalTileTessellator(parameters->_tileResolution, true),
                                        texturizer,
                                        parameters,
                                        showStatistics);
    comp->addRenderer(tr);
  }
  
  if (false) {
    // dummy renderer with a simple box
    DummyRenderer* dum = new DummyRenderer();
    comp->addRenderer(dum);
  }
  
  if (false) {
    // simple planet renderer, with a basic world image
    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
    comp->addRenderer(spr);
  }
  
  // marks renderer
  MarksRenderer* marks = new MarksRenderer();
  comp->addRenderer(marks);
  
  Mark* m1 = new Mark("Fuerteventura",
                      "plane.png",
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
  //m1->addTouchListener(listener);
  marks->addMark(m1);
  
  
  Mark* m2 = new Mark("Las Palmas",
                      "plane.png",
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
  //m2->addTouchListener(listener);
  marks->addMark(m2);
  
  if (false) {
    for (int i = 0; i < 500; i++) {
      const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
      //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
      
      marks->addMark(new Mark("Random",
                              "mark.png",
                              Geodetic3D(latitude, longitude, 0)));
    } 
  }
  
  if (false) {
    EffectsScheduler* scheduler = new EffectsScheduler();
    scheduler->startEffect(new DummyEffect(TimeInterval::fromSeconds(4)));
    comp->addRenderer(scheduler);
  }
  
  if (false) {
    SceneGraphRenderer* sgr = new SceneGraphRenderer();
    SGCubeNode* cube = new SGCubeNode();
    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
    sgr->getRootNode()->addChild(cube);
    comp->addRenderer(sgr);
  }
  
  //    comp->addRenderer(new GLErrorRenderer());
  
  
  TextureBuilder* texBuilder = new CPUTextureBuilder();
  TexturesHandler* texturesHandler = new TexturesHandler(gl, factory, texBuilder, false);
  
  const Planet* planet = Planet::createEarth();
  
  Renderer* busyRenderer = new BusyRenderer();


  _widget = G3MWidget::create(factory,
                              logger,
                              gl,
                              texturesHandler,
                              downloaderOLD,
                              downloader,
                              planet, 
                              comp,
                              busyRenderer,
                              width, height,
                              Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1),
                              true);
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
    
    lastTouchEvent = NULL;
    
    // all the creation of renderers must be move to common source code, instead of specific
    int __to_move_to_common_source_code;
    
    bool csiro = false;
    if (csiro){
      [self initWidgetCSIRO];
    } else{
      [self initWidgetDemo];
    }
    
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
    /*int timeToRedraw = */[_renderer render: [self widget]];
  }
}

- (void)layoutSubviews {
  int w = [self frame].size.width;
  int h = [self frame].size.height;
  NSLog(@"ResizeViewportEvent: %dx%d", w, h);
  ((G3MWidget*)_widget)->onResizeViewportEvent(w,h);
  
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

/*
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
  
  printf ("----------------- touch began\n");
  
  UITouch *touch = [touches anyObject];
  
  CGPoint current  = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  TouchEvent* te = TouchEvent::create(Down,
                                      new Touch(Vector2D(current.x, current.y),
                                                Vector2D(previous.x, previous.y)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(te);
  
  delete te;
}*/


- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
  
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];
  
  std::vector<const Touch*> pointers = std::vector<const Touch*>();
  // pointers.reserve([allTouches count]);
  
  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current  = [touch locationInView:self];
    CGPoint previous = [touch previousLocationInView:self];
    
    Touch *touch = new Touch(Vector2D(current.x, current.y), 
                             Vector2D(previous.x, previous.y));
    
    pointers.push_back(touch);
  }
  
  printf ("** touch began %d fingers\n", pointers.size());
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }
  lastTouchEvent = TouchEvent::create(Down, pointers);
  ((G3MWidget*)[self widget])->onTouchEvent(lastTouchEvent);
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
  //NSSet *allTouches = [event allTouches];
  NSSet *allTouches = [event touchesForView:self];
  
  std::vector<const Touch*> pointers = std::vector<const Touch*>();
 // pointers.reserve([allTouches count]);
  
  NSEnumerator *enumerator = [allTouches objectEnumerator];
  UITouch *touch = nil;
  while ((touch = [enumerator nextObject])) {
    CGPoint current  = [touch locationInView:self];
    CGPoint previous = [touch previousLocationInView:self];
    
    Touch *touch = new Touch(Vector2D(current.x, current.y), 
                             Vector2D(previous.x, previous.y));
    
    pointers.push_back(touch);
  }
  
  printf ("------- touch move %d fingers\n", pointers.size());
  
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
  
  ((G3MWidget*)[self widget])->onTouchEvent(lastTouchEvent);
}


/*
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
  printf ("----------------- touch end\n");

  
  
  UITouch *touch = [touches anyObject];
  
  CGPoint current = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  TouchEvent* te = TouchEvent::create(Up,
                                      new Touch(Vector2D(current.x, current.y), 
                                                Vector2D(previous.x, previous.y)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(te);
  
  delete te;
}*/


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
    
    Touch *touch = new Touch(Vector2D(current.x, current.y), 
                             Vector2D(previous.x, previous.y));
    
    pointers.push_back(touch);
  }
  
  printf ("------- touch end %d fingers\n", pointers.size());
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }
  lastTouchEvent = TouchEvent::create(Up, pointers);
  ((G3MWidget*)[self widget])->onTouchEvent(lastTouchEvent);
}


- (void)dealloc {
  if (lastTouchEvent!=NULL) {
    delete lastTouchEvent;
  }

}

@end
