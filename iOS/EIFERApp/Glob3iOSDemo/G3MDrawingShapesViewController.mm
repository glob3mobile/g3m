//
//  G3MDrawingShapesViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 19/12/12.
//
//

#import "G3MDrawingShapesViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "ShapesRenderer.hpp"
#include "Angle.hpp"

@interface G3MDrawingShapesViewController ()

@end

@implementation G3MDrawingShapesViewController

@synthesize glob3;
@synthesize playButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
  self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
  if (self) {
   
  }
  return self;
}

- (void)viewDidLoad
{
  [super viewDidLoad];
  
  // Create a builder
  G3MBuilder_iOS builder(self.glob3);

  // Set up some data, like positions
  SAN_FRANCISCO_POSITION = new Geodetic2D(Angle::fromDegreesMinutesSeconds(37, 47, 00),
                                          Angle::fromDegreesMinutesSeconds(-122, 25, 00));
  LOS_ANGELES_POSITION = new Geodetic2D(Angle::fromDegreesMinutes(34, 3),
                                        Angle::fromDegreesMinutes(-118, 15));

  
  // Create a shape renderer and add some shapes. Add the renderer to the builder
  ShapesRenderer* shapesRenderer = new ShapesRenderer();
  [self createShapes: shapesRenderer];
  builder.addRenderer(shapesRenderer);
  
  // Add a periodical task to randomly move the shapes
  builder.addPeriodicalTask([self createPeriodicalTask]);
  
  // Set a initialization task to go to the shapes's position
  builder.setInitializationTask([self createInitializationTask], true);
  
  // Create and set UserData. Some data to be shared between the glob3 widget and UI (like the animation state flag)
  data = new SampleUserData(false);
  builder.setUserData(data);
  
  // Initialize the glob3
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
  [self setPlayButton:nil];
  
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

- (IBAction)toggleShapesAnimation:(id)sender
{
  ((SampleUserData*) data)->setAnimRunning(!((SampleUserData*) data)->getAnimRunning());
  if (((SampleUserData*) data)->getAnimRunning()) {
    [[self playButton] setImage:[UIImage imageNamed:@"stop.png"] forState:UIControlStateNormal];
  }
  else {
    [[self playButton] setImage:[UIImage imageNamed:@"play.png"] forState:UIControlStateNormal];
  }
}

- (void) createShapes:(ShapesRenderer*) shapeRenderer
{
  circle = new CircleShape(new Geodetic3D(LOS_ANGELES_POSITION->latitude(),
                                          LOS_ANGELES_POSITION->longitude(),
                                          8000),
                                50000,
                                Color::newFromRGBA(1, 1, 0, 0.5));
  shapeRenderer->addShape(circle);
  
  box = new BoxShape(new Geodetic3D(SAN_FRANCISCO_POSITION->latitude(),
                                    SAN_FRANCISCO_POSITION->longitude(),
                                    45000),
                          Vector3D(20000, 30000, 50000),
                          2,
                          Color::newFromRGBA(0,    1, 0, 0.5),
                          Color::newFromRGBA(0, 0.75, 0, 0.75));
  box->setAnimatedScale(1, 1, 20);
  shapeRenderer->addShape(box);
}

- (PeriodicalTask*) createPeriodicalTask
{
  class AnimShapesTask : public GTask {
  private:
    G3MWidget_iOS* _widget;
    BoxShape* _box;
    CircleShape* _circle;
    Geodetic2D* _sfPosition;
  public:
    AnimShapesTask(G3MWidget_iOS* widget,
                   BoxShape* box,
                   CircleShape* circle,
                   Geodetic2D* sfPosition) {
      _widget = widget;
      _box = box;
      _circle = circle;
      _sfPosition = sfPosition;
    }
    void run(const G3MContext* context) {
      SampleUserData* userData = (SampleUserData*) [_widget widget]->getUserData();
      if (userData->getAnimRunning()) {
        int r = (arc4random() % 51) - 50;
        
        _box->setAnimatedScale(1, 1, r);
        _circle->setPosition(new Geodetic3D(_sfPosition->latitude().add(Angle::fromDegrees(r / 5)),
                                           _sfPosition->longitude().add(Angle::fromDegrees(r / 5)),
                                           abs(r) * 5000));
      }
    }
  };
  
  PeriodicalTask* periodicalTask = new PeriodicalTask(TimeInterval::fromSeconds(2),
                                                      new AnimShapesTask([self glob3], box, circle, SAN_FRANCISCO_POSITION));
  
  return periodicalTask;
}

- (GInitializationTask*) createInitializationTask
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _widget;
    Geodetic2D* _sfPosition;
    
  public:
    SampleInitializationTask(G3MWidget_iOS*  widget,
                             Geodetic2D* sfPosition) {
      _widget = widget;
      _sfPosition = sfPosition;
    }
    
    void run(const G3MContext* context) {
      [_widget widget]->setAnimatedCameraPosition(Geodetic3D(_sfPosition->latitude(),
                                                             _sfPosition->longitude(),
                                                             3500000),
                                                  TimeInterval::fromSeconds(5));
    }
    
    bool isDone(const G3MContext* context) {
      return true;
    }
  };
  
  GInitializationTask* initializationTask = new SampleInitializationTask([self glob3], SAN_FRANCISCO_POSITION);
  
  return initializationTask;
}


class SampleUserData : public WidgetUserData {
private:
  bool _animRunning;
public:
  SampleUserData(bool animRunning) {
    _animRunning = animRunning;
  }
  void setAnimRunning(bool animRunning) {
    _animRunning = animRunning;
  }
  bool getAnimRunning() {
    return _animRunning;
  }
};


@end
