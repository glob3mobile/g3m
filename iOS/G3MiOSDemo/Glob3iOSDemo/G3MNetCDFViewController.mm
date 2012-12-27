//
//  G3MNetCDFViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 26/12/12.
//
//

#import "G3MNetCDFViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "ShapesRenderer.hpp"


@implementation G3MNetCDFViewController
@synthesize glob3;

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
//  builder.setInitializationTask([self createSampleInitializationTask: shapeRenderer], true);
  
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

- (void)viewDidUnload {
  [self setGlob3:nil];
  [super viewDidUnload];
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

- (GInitializationTask*) createInitializationTask: (ShapesRenderer*) shapesRenderer
{
  class MyInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;
  public:
    MyInitializationTask(G3MWidget_iOS*  iosWidget,
                         ShapesRenderer* shapesRenderer):
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer)
    {
      
    }
    
    void run(const G3MContext* context) {
      NSLog ( @"Running initialization Task\n");
      
      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(37.78333333),
                                                                Angle::fromDegrees(-122.41666666666667),
                                                                1000),
                                                     TimeInterval::fromSeconds(10));
    }
    
    bool isDone(const G3MContext* context) {
      return true;
    }
  };
  
  GInitializationTask* initializationTask = new MyInitializationTask([self glob3], shapesRenderer);
  
  return initializationTask;  
}

- (PeriodicalTask*) createPaintGeometriesTask
{
  class PaintGeometriesTask : public GTask {
  private:
    G3MWidget_iOS*  _iosWidget;
  public:
    void run(const G3MContext* context) {
      MyUserData* userData = (MyUserData*) [_iosWidget widget]->getUserData();
      if (userData->getIsDone()) {
        if (userData->getPeriod() == 18) {
        }
      }
    }
  };
  
  PeriodicalTask* periodicalTask = new PeriodicalTask(TimeInterval::fromSeconds(2),
                                                      new PaintGeometriesTask());
  
  return periodicalTask;  
}

class MyUserData : public UserData {
private:
  bool _isDone;
  int _period;
  ShapesRenderer* _shapesRenderer;
public:
  MyUserData(bool isDone, int period, ShapesRenderer* shapesRenderer) {
    _isDone = isDone;
    _period = period;
    _shapesRenderer = shapesRenderer;
  }
  bool getIsDone() {
    return _isDone;
  }
  void setPeriod(int period) {
    _period = period;
  }
  int getPeriod() {
    return _period;
  }
  ShapesRenderer* getShapesRenderer() {
    return _shapesRenderer;
  }
};

@end
