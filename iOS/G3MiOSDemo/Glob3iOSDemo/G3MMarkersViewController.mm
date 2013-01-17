//
//  G3MMarkersViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 19/12/12.
//
//

#import "G3MMarkersViewController.h"

#import "G3MWebViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "Mark.hpp"
#include "MarkTouchListener.hpp"
#include "MarksRenderer.hpp"
#include "Downloader_iOS.hpp"
#include "JSONBaseObject.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONNumber.hpp"
#include "JSONString.hpp"

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
  
  // Create a markers renderer
  MarksRenderer* markerRenderer = [self createMarksRenderer];
  // Add markers renderer
  builder.addRenderer(markerRenderer);
  
  // Set a initialization task to request markers and go to the markers's position
  builder.setInitializationTask([self createInitializationTask:markerRenderer], true);
  
  // Initialize widget
  builder.initializeWidget();
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];

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
  private:
    G3MMarkersViewController* _vc;
  public:
    TestMarkTouchListener(G3MMarkersViewController* vc) {
      _vc = vc;
    }
    bool touchedMark(Mark* mark) {
      NSString* message = [NSString stringWithFormat: @"%s", mark->getLabel().c_str()];

      UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                      message: message
                                                     delegate: _vc
                                            cancelButtonTitle: @"OK"
                                            otherButtonTitles: @"Learn more...",nil];

      URL* markUrl = (URL*) mark->getUserData();
      [_vc setValue: [NSString stringWithCString: markUrl->getPath().c_str()
                                        encoding: NSUTF8StringEncoding]
             forKey: @"urlMarkString"];
      [alert show];
      
      return true;
    }
  };
  
  const bool readyWhenMarksReady = false;
  MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
  
  marksRenderer->setMarkTouchListener(new TestMarkTouchListener(self), true);
  
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
        downloader->requestBuffer(URL("http://poiproxy.mapps.es/browseByLonLat?service=wikilocation&lon=-122.415985&lat=37.766372&dist=50000", false),
                                  200000,
                                  TimeInterval::forever(),
                                  listener,
                                  true);
        
        [_widget widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(37.7658),
                                                                  Angle::fromDegrees(-122.4185),
                                                                  10000),
                                                       TimeInterval::fromSeconds(10));
      }
      
      bool isDone(const G3MContext* context) {
        return true;
      }
    };
    
    GInitializationTask* initializationTask = new SampleInitializationTask([self glob3], marksRenderer);
    
    return initializationTask;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
  NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
  
  if([title isEqualToString:@"Learn more..."]) {
    G3MWebViewController *webView = [self.storyboard instantiateViewControllerWithIdentifier:@"G3MWebViewController"];
    [self presentModalViewController: webView
                            animated: YES];
    [webView loadUrl: [NSURL URLWithString:urlMarkString]];
  }
}


class MarkerUserData : public MarkUserData {
  
private:
  const URL _url;
  
public:
  MarkerUserData(const URL &url) :
  _url(url) {
  }
  
  URL getUrl() {
    return _url;
  }
  
};


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
    JSONBaseObject* json = IJSONParser::instance()->parse(buffer->getAsString());    
    const JSONArray* features = json->asObject()->getAsArray("features");

    for (int i = 0; i < features->size(); i++) {
      const JSONObject* item = features->getAsObject(i);
      
      const JSONObject* properties = item->asObject()->getAsObject("properties");
      std::string title = properties->getAsString("title")->value();
      std::string urlStr = properties->getAsString("url")->value();
      
      const JSONArray* coordinates = item->asObject()->getAsObject("geometry")->asObject()->getAsArray("coordinates");

      Mark* marker = new Mark(title,
                              URL("file:///marker-wikipedia-72x72.png", false),
                              Geodetic3D(Angle::fromDegrees(coordinates->getAsNumber(1)->doubleValue()), Angle::fromDegrees(coordinates->getAsNumber(0)->doubleValue()), 0));

      MarkUserData* mud = new MarkerUserData(URL(urlStr, false));
      marker->setUserData(mud);
      
      _markRenderer->addMark(marker);
    }
    IJSONParser::instance()->deleteJSONData(json);
  }
  
  void onError(const URL& url) {
    NSString* message = [NSString stringWithFormat: @"Oops!\nThere was a problem getting markers info"];
    
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
