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
#include "SceneJSShapesParser.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"


@interface DemosViewController () 

@end

@implementation DemosViewController 

@synthesize G3MWidget;
@synthesize layerSet;
@synthesize satelliteLayerEnabled;
@synthesize markerRenderer;
@synthesize shapeRenderer;
@synthesize meshRenderer;

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
  G3MBuilder_iOS builder([self G3MWidget]);
  
  [self setSatelliteLayerEnabled: true];
  [self setLayerSet: [self createLayerSet]];
  [self setMarkerRenderer: [self createMarkerRenderer]];
  [self markerRenderer]->setEnable(false);
  [self setShapeRenderer: new ShapesRenderer()];
  [self shapeRenderer]->setEnable(false);
  [self setMeshRenderer: [self createMeshRenderer: builder.getPlanet()]];
  [self meshRenderer]->setEnable(false);
  
  // Setup the builder
  builder.getTileRendererBuilder()->setLayerSet([self layerSet]);
  builder.addRenderer([self markerRenderer]);
  builder.addRenderer([self shapeRenderer]);
  builder.addRenderer([self meshRenderer]);
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
      NSString* message = [NSString stringWithFormat: @"%s", ((MarkerUserData*) mark->getUserData())->getTitle().c_str()];
      
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

- (MeshRenderer*) createMeshRenderer: (const Planet*)planet  {
  MeshRenderer* mr = new MeshRenderer();

  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          planet,
                                          Geodetic2D::zero());
  FloatBufferBuilderFromColor colors;
  
  const Angle centerLat = Angle::fromDegreesMinutesSeconds(38, 53, 42);
  const Angle centerLon = Angle::fromDegreesMinutesSeconds(-77, 02, 11);
  
  const Angle deltaLat = Angle::fromDegrees(1).div(16);
  const Angle deltaLon = Angle::fromDegrees(1).div(16);
  
  const int steps = 128;
  const int halfSteps = steps/2;
  for (int i = -halfSteps; i < halfSteps; i++) {
    Angle lat = centerLat.add( deltaLat.times(i) );
    for (int j = -halfSteps; j < halfSteps; j++) {
      Angle lon = centerLon.add( deltaLon.times(j) );
      
      vertices.add( Geodetic3D(lat, lon, 100000) );
      
      const float red   = (float) (i + halfSteps + 1) / steps;
      const float green = (float) (j + halfSteps + 1) / steps;
      colors.add(Color::fromRGBA(red, green, 0, 1));
    }
  }
  
  const float lineWidth = 1;
  const float pointSize = 2;
  Color* flatColor = NULL;
  DirectMesh* pointMesh = new DirectMesh(GLPrimitive::points(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        pointSize,
                        flatColor,
                        colors.create());
  
  
  mr->addMesh(pointMesh);
  
  return mr;
}

- (GInitializationTask*) createInitializationTask {
  class SampleInitializationTask : public GInitializationTask {
  private:
    bool _done;
    G3MWidget_iOS*  _widget;
    MarksRenderer* _marksRenderer;
    ShapesRenderer* _shapesRenderer;
    
  public:
    SampleInitializationTask(G3MWidget_iOS*  widget, MarksRenderer* mRenderer, ShapesRenderer* shapesRenderer) :
    _done(false),
    _widget(widget),
    _marksRenderer(mRenderer),
    _shapesRenderer(shapesRenderer)
    {
      
    }
    
    void run(const G3MContext* context) {
      // Markers
      Downloader_iOS* downloader = (Downloader_iOS*) context->getDownloader();
      WikiMarkerBufferDownloadListener* listener = new WikiMarkerBufferDownloadListener(this, _marksRenderer);
      downloader->requestBuffer(URL("http://poiproxy.mapps.es/browseByLonLat?service=wikilocation&lon=-122.415985&lat=37.766372&dist=50000", false),
                                200000,
                                TimeInterval::forever(),
                                listener,
                                true);
      // 3D model
      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
                                                                ofType: @"json"];
      //      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"citation"
      //                                                                ofType: @"json"];
      if (planeFilePath) {
        NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
                                                          encoding: NSUTF8StringEncoding
                                                             error: nil];
        if (nsPlaneJSON) {
          std::string planeJSON = [nsPlaneJSON UTF8String];
          Shape* plane = SceneJSShapesParser::parse(planeJSON, "file:///textures-A320/");
          //Shape* plane = SceneJSShapesParser::parse(planeJSON, "file:///textures-citation/");
          if (plane) {
            // Washington, DC
            plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                              Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                              1000) );
            plane->setScale(100, 100, 100);
            plane->setPitch(Angle::fromDegrees(90));
            _shapesRenderer->addShape(plane);
          }
        }
      }
      
      _done = true;
    }
    
    bool isDone(const G3MContext* context) {
      return _done;
    }
  };

  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget],
                                                                         [self markerRenderer],
                                                                         [self shapeRenderer]);
  
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
      break;
    case 3:
      [self show3DModel];
      break;
    case 4:
      [self showPointMesh];
      break;
    default:
      break;
  }
}

- (void) resetWidget {
  [self markerRenderer]->setEnable(false);
  [self shapeRenderer]->setEnable(false);
  [self meshRenderer]->setEnable(false);
  
  [[self G3MWidget] widget]->stopCameraAnimation();
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
                                                       TimeInterval::fromSeconds(7));
}

- (void) show3DModel {
  [self shapeRenderer]->setEnable(true);
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                                  Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                                                  2500),
                                                       TimeInterval::fromSeconds(7));
}

- (void) showPointMesh {
  [self meshRenderer]->setEnable(true);
  Geodetic3D position = [[self G3MWidget] widget]->getNextCamera()->getGeodeticCenterOfView();
  if (position.height() < 18000000) {
    [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(position.latitude(),
                                                                    position.longitude(),
                                                                    18000000),
                                                         TimeInterval::fromSeconds(4));
  }
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42),
                                                                  Angle::fromDegreesMinutesSeconds(-77, 2, 11),
                                                                  7000000),
                                                       TimeInterval::fromSeconds(4));
}


class MarkerUserData : public MarkUserData {
  
private:
  const std::string _title;
  const URL _url;
  
public:
  MarkerUserData(std::string title, const URL &url) :
  _title(title),
  _url(url) {
  }
  
  std::string getTitle() {
    return _title;
  }
  
  URL getUrl() {
    return _url;
  }
  
};

class WikiMarkerBufferDownloadListener : public IBufferDownloadListener {

private:
  GInitializationTask* _initTask;
  MarksRenderer* _markRenderer;

public:
  WikiMarkerBufferDownloadListener(GInitializationTask* initTask, MarksRenderer* markRenderer) {
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
      
      std::string markerIcon = "";
      if ([[UIScreen mainScreen] bounds].size.width < 768) {
        markerIcon = "file:///marker-wikipedia-48x48.png"; // iPhone
      }
      else {
        markerIcon = "file:///marker-wikipedia-72x72.png"; // iPad
      }
      
      Mark* marker = new Mark(URL(markerIcon, false),
                              Geodetic3D(Angle::fromDegrees(coordinates->getAsNumber(1)->doubleValue()),
                                         Angle::fromDegrees(coordinates->getAsNumber(0)->doubleValue()),
                                         0));
      
      MarkUserData* mud = new MarkerUserData(title, URL(urlStr, false));
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
