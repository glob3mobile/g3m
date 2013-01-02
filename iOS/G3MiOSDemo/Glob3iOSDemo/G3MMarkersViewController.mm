//
//  G3MMarkersViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 19/12/12.
//
//

#import "G3MMarkersViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "MarksRenderer.hpp"
#include "Downloader_iOS.hpp"
#include "GEOJSONParser.hpp"

@interface G3MMarkersViewController ()

@end

@implementation G3MMarkersViewController

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
  
  const bool readyWhenMarksReady = false;
  MarksRenderer* markerRenderer = new MarksRenderer(readyWhenMarksReady);
  // Add markers renderer
  builder.addRenderer([self createMarksRenderer]);
  
  // Set a initialization task to go to the markers's position
  builder.setInitializationTask([self createInitializationTask:markerRenderer], true);
  
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

- (MarksRenderer*) createMarksRenderer
{
  
  class TestMarkTouchListener : public MarkTouchListener {
  public:
    bool touchedMark(Mark* mark) {
      NSString* message = [NSString stringWithFormat: @"Touched on mark \"%s\"", mark->getName().c_str()];
      
      UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Glob3 Demo"
                                                      message:message
                                                     delegate:nil
                                            cancelButtonTitle:@"OK"
                                            otherButtonTitles:nil];
      [alert show];
      
      return true;
    }
  };
  
  const bool readyWhenMarksReady = false;
  MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
  
  marksRenderer->setMarkTouchListener(new TestMarkTouchListener(), true);
  
  Mark* m1 = new Mark("Fuerteventura",
                      URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
  marksRenderer->addMark(m1);
  
  
  Mark* m2 = new Mark("Las Palmas",
                      URL("file:///plane.png", false),
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
  marksRenderer->addMark(m2);
  
  return marksRenderer;
  
}

- (GInitializationTask*) createInitializationTask: (MarksRenderer*) marksRenderer
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _widget;
    MarksRenderer* _marksRenderer;
      
    public:
      SampleInitializationTask(G3MWidget_iOS*  widget, MarksRenderer* mRenderer) :
      _widget(widget),
      _marksRenderer(mRenderer)
      {
        
      }
      
      void run(const G3MContext* context) {
        Downloader_iOS* downloader = (Downloader_iOS*) context->getDownloader();
        MarkersDemoBufferDownloadListener* listener = new MarkersDemoBufferDownloadListener(this, _marksRenderer);
        downloader->requestBuffer(URL("http://poiproxy.mapps.es/browseByLonLat?service=wikilocation&lon=-122.415985&lat=37.766372&dist=10000", false),
                                  200000,
                                  TimeInterval::forever(),
                                  listener,
                                  true);
        
        [_widget widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(28.05),
                                                                  Angle::fromDegrees(-15),
                                                                  1000000),
                                                       TimeInterval::fromSeconds(5));
      }
      
      bool isDone(const G3MContext* context) {
        return true;
      }
    };
    
    GInitializationTask* initializationTask = new SampleInitializationTask([self glob3], marksRenderer);
    
    return initializationTask;
}

class MarkersDemoBufferDownloadListener : public IBufferDownloadListener {
private:
  GInitializationTask* _initTask;
  MarksRenderer* _markRenderer;
public:
  MarkersDemoBufferDownloadListener(GInitializationTask* initTask, MarksRenderer* markRenderer) {
    _initTask = initTask;
    _markRenderer = markRenderer;
  }
  void onDownload(const URL& url,
                  const IByteBuffer* buffer) {

  }
  
  void onError(const URL& url) {
    NSString* message = [NSString stringWithFormat: @"We are really sorry.\nThere was a problem getting markers info"];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"glob3 mobile"
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
  }
  
  void onCancel(const URL& url) {
    
  }
  
  void onCanceledDownload(const URL& url,
                          const IByteBuffer* data) {
    
  }
};

@end
