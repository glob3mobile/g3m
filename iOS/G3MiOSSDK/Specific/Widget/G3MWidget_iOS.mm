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
    int width = [self frame].size.width;
    int height = [self frame].size.height;
    
    
    IFactory *factory = new Factory_iOS();
    ILogger *logger = new Logger_iOS(ErrorLevel);
    IGL* gl  = new GL2();
    
    //Testing downloads
    int test_download_code = 0;
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    DummyDownload *dummyDownload = new DummyDownload(factory, [documentsDirectory cStringUsingEncoding:NSUTF8StringEncoding] );
    dummyDownload->run();
    
    // all the creation of renderers must be move to common source code, instead of specific
    int __to_move_to_common_source_code;
    
    // composite renderer is the father of the rest of renderers
    CompositeRenderer* comp = new CompositeRenderer();
    
    // camera renderer
    CameraRenderer *cameraRenderer = new CameraRenderer();
    comp->addRenderer(cameraRenderer);
    
    // very basic tile renderer
    if (true) {
      TileTessellator* tessellator = new EllipsoidalTileTessellator("world.jpg");
      
      TileRenderer* tr = new TileRenderer(tessellator);
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
    if (false) {
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
      
      
      for (int i = 0; i < 500; i++) {
        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
        
        marks->addMark(new Mark("Random",
                                "mark.png",
                                Geodetic3D(latitude, longitude, 0)
                                )
                       );
      } 
    }
    
    if (false) {
      EffectsScheduler* scheduler = new EffectsScheduler();
      scheduler->startEffect(new DummyEffect(TimeInterval::fromSeconds(3)));
      comp->addRenderer(scheduler);
    }
    
    if (false) {
      SceneGraphRenderer* sgr = new SceneGraphRenderer();
      SGCubeNode* cube = new SGCubeNode();
//      cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
      sgr->getRootNode()->addChild(cube);
      comp->addRenderer(sgr);
    }
    
    comp->addRenderer(new GLErrorRenderer());
    
    TexturesHandler* texturesHandler = new TexturesHandler();
    
    const Planet* planet = Planet::createEarth();
    
    _widget = G3MWidget::create(factory,
                                logger,
                                gl,
                                texturesHandler,
                                planet, 
                                comp,
                                width, height,
                                Color::fromRGB((float)0, (float)0.1, (float)0.2, (float)1),
                                true);
    
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
    int timeToRedraw = [_renderer render: [self widget]];
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


- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
  UITouch *touch = [touches anyObject];
  CGPoint current = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  //TOUCH EVENT
  Vector2D pos(current.x, current.y);
  Vector2D prevPos(previous.x, previous.y);
  TouchEvent te(TouchEvent::create(Down, new Touch(pos, prevPos)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(&te);
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
  NSSet *allTouches = [event allTouches];
  
  NSUInteger pointersCount = [allTouches count];
  
  std::vector<const Touch*> pointers = std::vector<const Touch*>();
  
  for (int n = 0; n < pointersCount; n++) {
    UITouch *touch = [[allTouches allObjects] objectAtIndex:n];
    CGPoint previous = [touch previousLocationInView:self];
    CGPoint current = [touch locationInView:self];
    
    //1 POINTER
    Vector2D pos(current.x, current.y);
    Vector2D prevPos(previous.x, previous.y);
    Touch *to = new Touch(pos, prevPos);
    
    pointers.push_back(to);
  }
  
  //TOUCH EVENT
  TouchEvent te( TouchEvent::create(Move, pointers) );
  
  ((G3MWidget*)[self widget])->onTouchEvent(&te);
}


- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
  UITouch *touch = [touches anyObject];
  CGPoint current = [touch locationInView:self];
  CGPoint previous = [touch previousLocationInView:self];
  
  //TOUCH EVENT
  Vector2D pos(current.x, current.y);
  Vector2D prevPos(previous.x, previous.y);
  TouchEvent te( TouchEvent::create(Up, new Touch(pos, prevPos)));
  
  ((G3MWidget*)[self widget])->onTouchEvent(&te);
}

- (void)dealloc {
}

@end
