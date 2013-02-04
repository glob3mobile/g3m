//
//  G3M3DModelViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 20/12/12.
//
//

#import "G3M3DModelViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "ShapesRenderer.hpp"
#include "SceneJSShapesParser.hpp"

@interface G3M3DModelViewController ()

@end

@implementation G3M3DModelViewController
@synthesize glob3;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
  self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
  if (self) {
    // Custom initialization
  }
  return self;
}

- (void)viewDidLoad
{
  [super viewDidLoad];
  
  // Create a builder
  G3MBuilder_iOS builder([self glob3]);
  
  // Create and add a shape renderer
  ShapesRenderer* shapeRenderer = new ShapesRenderer();
  builder.addRenderer(shapeRenderer);
  
  // Set a initialization task.
  // This task reads and parses the file containing the 3D model and add the model to the renderer.
  // It also moves the camera to the model's position
  builder.setInitializationTask([self createSampleInitializationTask: shapeRenderer], true);
  
  // Initialize widget
  builder.initializeWidget();
  
  // Let's get the show on the road!
  [[self glob3] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [[self glob3] stopAnimation];
  
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  [self setGlob3:nil];
  
  [super viewDidUnload];
  // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  } else {
    return YES;
  }
}

- (GInitializationTask*) createSampleInitializationTask: (ShapesRenderer*) shapesRenderer
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;
  public:
    SampleInitializationTask(G3MWidget_iOS*  iosWidget,
                             ShapesRenderer* shapesRenderer):
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer)
    {
      
    }
    
    void run(const G3MContext* context) {
      printf("Running initialization Task\n");
      
      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(37.78333333),
                                                                Angle::fromDegrees(-122.41666666666667),
                                                                1000),
                                                     TimeInterval::fromSeconds(10));
       NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
                                                                 ofType: @"json"];
//      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"3dmodels/Macba_Google_Earth-1"
//                                                                ofType: @"json"];
      if (planeFilePath) {
         NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
                                                           encoding: NSUTF8StringEncoding
                                                              error: nil];
         if (nsPlaneJSON) {
           std::string planeJSON = [nsPlaneJSON UTF8String];
           Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, "file:///");
           if (plane) {
             plane->setPosition(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                               Angle::fromDegrees(-122.41666666666667),
                                               500));
             plane->setScale(200, 200, 200);
             plane->setPitch(Angle::fromDegrees(90));
             _shapesRenderer->addShape(plane);
           }
         }
      }
    }
    
    bool isDone(const G3MContext* context) {
      return true;
    }
  };
  
  GInitializationTask* initializationTask = new SampleInitializationTask([self glob3], shapesRenderer);
  
  return initializationTask;
}


@end
