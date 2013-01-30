//
//  DemosViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import "DemosViewController.h"
#import "G3MWebViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerBuilder.hpp"
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

@interface DemosViewController () 

@end

@implementation DemosViewController 

@synthesize G3MWidget;
@synthesize demoSwitcher;
@synthesize layerSet;
@synthesize satelliteLayerEnabled;
@synthesize markerRenderer;

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
  
  [self setSatelliteLayerEnabled: true];
  [self setLayerSet: [self createLayerSet]];
  [self setMarkerRenderer: [self createMarkerRenderer]];
  
  // Create a builder
  G3MBuilder_iOS builder([self G3MWidget]);
  // Setup the builder
  builder.getTileRendererBuilder()->setLayerSet([self layerSet]);
  builder.addRenderer([self markerRenderer]);
  builder.setInitializationTask([self createInitializationTask], true);
  // Initialize widget
  builder.initializeWidget();  
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
  // Let's get the show on the road!
  [[self G3MWidget] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [[self G3MWidget] stopAnimation];
  
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  [self setG3MWidget: nil];
  [self setDemoSwitcher:nil];
  
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

- (LayerSet*) createLayerSet {
  LayerSet* layers = new LayerSet();
  
  layers->addLayer(LayerBuilder::createBingLayer([self satelliteLayerEnabled]));
  layers->addLayer(LayerBuilder::createOSMLayer(![self satelliteLayerEnabled]));
  
  return layers;
}

- (MarksRenderer*) createMarkerRenderer {
  
  class TestMarkTouchListener : public MarkTouchListener {
  private:
    DemosViewController* _vc;
  public:
    TestMarkTouchListener(DemosViewController* vc) {
      _vc = vc;
    }
    bool touchedMark(Mark* mark) {
      NSString* message = [NSString stringWithFormat: @"%s", mark->getLabel().c_str()];
      
      UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                      message: message
                                                     delegate: _vc
                                            cancelButtonTitle: @"OK"
                                            otherButtonTitles: @"Learn more...",nil];
      
      URL markUrl = ((MarkerUserData*) mark->getUserData())->getUrl();
      [_vc setValue: [NSString stringWithCString: markUrl.getPath().c_str()
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

- (GInitializationTask*) createInitializationTask {
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
    }
    
    bool isDone(const G3MContext* context) {
      return true;
    }
  };
  
  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget], [self markerRenderer]);
  
  return initializationTask;
}

- (IBAction)switchDemo:(UISegmentedControl*)sender {
  [self resetWidget];
  
  switch ([sender selectedSegmentIndex]) {
    case 0:
      [self showSimpleGlob3];
      break;
    case 1:
      [self switchLayer];
      break;
    case 2:
      [self showMarkers];
      
    default:
      break;
  }
}

- (void) resetWidget {
  [self markerRenderer]->setEnable(false);
  
  [[self G3MWidget] widget]->resetCameraPosition();
  [[self G3MWidget] widget]->setCameraPosition(Geodetic3D(Angle::fromDegrees(0),
                                                          Angle::fromDegrees(0),
                                                          25000000));
}

- (void) showSimpleGlob3 {
  if (![self satelliteLayerEnabled]) {
    [self switchLayer];
  }
}

- (void) switchLayer {
  [self setSatelliteLayerEnabled: ![self satelliteLayerEnabled]];
  
  // bing
  [self layerSet]->getLayer("ve")->setEnable([self satelliteLayerEnabled]);
  // osm
  [self layerSet]->getLayer("osm_auto:all")->setEnable(![self satelliteLayerEnabled]);
}

- (void) showMarkers {
  [self markerRenderer]->setEnable(true);
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(37.7658),
                                                                  Angle::fromDegrees(-122.4185),
                                                                  10000),
                                                       TimeInterval::fromSeconds(4));
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
                              Geodetic3D(Angle::fromDegrees(coordinates->getAsNumber(1)->doubleValue()),
                                         Angle::fromDegrees(coordinates->getAsNumber(0)->doubleValue()),
                                         0));
      
      MarkUserData* mud = new MarkerUserData(URL(urlStr, false));
      marker->setUserData(mud);
      
      _markRenderer->addMark(marker);
    }
    IJSONParser::instance()->deleteJSONData(json);
  }
  
  void onError(const URL& url) {
    NSString* message = [NSString stringWithFormat: @"Oops!\nThere was a problem getting markers info"];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                    message: message
                                                   delegate: nil
                                          cancelButtonTitle: @"OK"
                                          otherButtonTitles: nil];
    [alert show];
  }
  
  void onCancel(const URL& url) {
    
  }
  
  void onCanceledDownload(const URL& url,
                          const IByteBuffer* data) {
    
  }
  
};

@end
