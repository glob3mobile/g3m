//
//  DemosViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import "DemosViewController.h"
#import "G3MWebViewController.h"

#import "G3MWidget_iOS.h"
#import "G3MBuilder_iOS.hpp"
#import "LayerBuilder.hpp"
#import "Mark.hpp"
#import "MarkTouchListener.hpp"
#import "MarksRenderer.hpp"
#import "ShapesRenderer.hpp"
#import "MeshRenderer.hpp"
#import "Downloader_iOS.hpp"
#import "JSONBaseObject.hpp"
#import "IJSONParser.hpp"
#import "JSONObject.hpp"
#import "JSONArray.hpp"
#import "JSONNumber.hpp"
#import "JSONString.hpp"
#import "SceneJSShapesParser.hpp"
#import "FloatBufferBuilderFromGeodetic.hpp"
#import "FloatBufferBuilderFromColor.hpp"
#import "DirectMesh.hpp"
#import "ByteBuffer_iOS.hpp"
#import "BSONParser.hpp"
#import "JSONGenerator.hpp"
#import "G3MToolbar.h"


@interface DemosViewController () 

@end

@implementation DemosViewController 

@synthesize demoSelector, demoMenu, toolbar, layerSwitcher, playMarkersDemo, playModelDemo, playMeshDemo;
@synthesize G3MWidget, layerSet, satelliteLayerEnabled, markerRenderer, shapeRenderer, meshRenderer;


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
  builder.setUserData(new DemoUserData());
  
  // Initialize widget
  builder.initializeWidget();
  [self resetWidget];
  
  [self initDropDownMenu];
  [self initToolbar];
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
  [self setToolbar: nil];
  [self setLayerSwitcher: nil];
  [self setDemoSelector:nil];
  [self setDemoMenu: nil];
  
  [self setMarkerRenderer: nil];
  [self setShapeRenderer: nil];
  [self setMeshRenderer: nil];
  [self setLayerSet: nil];
  
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
    ShapesRenderer* _shapeRenderer;
    
  public:
    SampleInitializationTask(G3MWidget_iOS*  widget, MarksRenderer* mRenderer, ShapesRenderer* shapeRenderer) :
    _done(false),
    _widget(widget),
    _marksRenderer(mRenderer),
    _shapeRenderer(shapeRenderer)
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
//      NSString *bsonFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
//                                                               ofType: @"bson"];
//      if (bsonFilePath) {
//        NSData* data = [NSData dataWithContentsOfFile: bsonFilePath];
//        const int length = [data length];
//        unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
//        [data getBytes: bytes
//                length: length];
//        
//        IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);
//        if (buffer) {
//          Shape* plane = SceneJSShapesParser::parseFromBSON(buffer, "file://textures-A320/");
//          if (plane) {
//            // Washington, DC
//            plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                              Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
//                                              1000) );
//            plane->setScale(500, 500, 500);
//            plane->setPitch(Angle::fromDegrees(90));
//            _shapesRenderer->addShape(plane);
//          }
//          delete buffer;
//        }
//      }

      NSString *jsonFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
                                                               ofType: @"json"];
      if (jsonFilePath) {
        NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: jsonFilePath
                                                          encoding: NSUTF8StringEncoding
                                                             error: nil];
        if (nsPlaneJSON) {
          std::string planeJSON = [nsPlaneJSON UTF8String];
          Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, "file:///textures-A320/");
          if (plane) {
            ((DemoUserData*) [_widget widget]->getUserData())->setPlane(plane);
            plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                              Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                              10000) );
            const double scale = 200;
            plane->setScale(scale, scale, scale);
            plane->setPitch(Angle::fromDegrees(90));
            
            _shapeRenderer->addShape(plane);
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

- (void) resetWidget {
  [self markerRenderer]->setEnable(false);
  [self shapeRenderer]->setEnable(false);
  [self meshRenderer]->setEnable(false);
  
  [[self G3MWidget] widget]->stopCameraAnimation();
  [[self G3MWidget] widget]->resetCameraPosition();
  [[self G3MWidget] widget]->setCameraPosition(Geodetic3D(Angle::fromDegrees(0),
                                                          Angle::fromDegrees(0),
                                                          25000000));
  
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(0),
                                                                  Angle::fromDegrees(0),
                                                                  25000000),
                                                       TimeInterval::fromSeconds(3));
}

- (void) showSimpleGlob3 {
  if (![self satelliteLayerEnabled]) {
    [self switchLayer];
  }
}

- (void) switchLayer {
  [self setSatelliteLayerEnabled: ![self satelliteLayerEnabled]];
  
  if ([self satelliteLayerEnabled]) {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"satellite-on-96x48.png"] forState:UIControlStateNormal];
  }
  else {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"map-on-96x48.png"] forState:UIControlStateNormal];    
  }
  
  // bing
  [self layerSet]->getLayer("ve")->setEnable([self satelliteLayerEnabled]);
  // osm
  [self layerSet]->getLayer("osm_auto:all")->setEnable(![self satelliteLayerEnabled]);
}

- (void) gotoMarkersDemo {
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegrees(37.7658),
                                                                  Angle::fromDegrees(-122.4185),
                                                                  12000),
                                                       TimeInterval::fromSeconds(5));
  [[self toolbar] setVisible: FALSE];
}

- (void) gotoModelDemo {
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                                  Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                                                  6000),
                                                       TimeInterval::fromSeconds(5));

  Shape* plane = ((DemoUserData*) [[self G3MWidget] widget]->getUserData())->getPlane();
  plane->setAnimatedPosition(TimeInterval::fromSeconds(26),
                             Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                        Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
                                        10000),
                             true);
  
  const double fromDistance = 50000 * 1.5;
  const double toDistance   = 25000 * 1.5 / 2;
  
  const Angle fromAzimuth = Angle::fromDegrees(-90);
  const Angle toAzimuth   = Angle::fromDegrees(-90 + 360);
  
  const Angle fromAltitude = Angle::fromDegrees(90);
  const Angle toAltitude   = Angle::fromDegrees(15);
  
  plane->orbitCamera(TimeInterval::fromSeconds(20),
                     fromDistance, toDistance,
                     fromAzimuth,  toAzimuth,
                     fromAltitude, toAltitude);
  
  [[self toolbar] setVisible: FALSE];
}

- (void) gotoMeshDemo {
  [[self G3MWidget] widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42),
                                                                  Angle::fromDegreesMinutesSeconds(-77, 2, 11),
                                                                  6700000),
                                                       TimeInterval::fromSeconds(5));
  [[self toolbar] setVisible: FALSE];
}


// UI
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
  NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
  
  if([title isEqualToString:@"Learn more..."]) {
    G3MWebViewController *webView = [self.storyboard instantiateViewControllerWithIdentifier:@"G3MWebViewController"];
    [self presentModalViewController: webView
                            animated: YES];
    [webView loadUrl: [NSURL URLWithString:urlMarkString]];
  }
}

- (void) initDropDownMenu
{
  // left align button text
  [[self demoSelector] setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
  [[self demoSelector] setContentEdgeInsets: UIEdgeInsetsMake(0, 10, 0, 0)];
  [[[self demoSelector] layer] setBorderWidth: 0];
  [[self demoSelector] setBackgroundColor: [UIColor clearColor]];
  
  [self setDemoMenu: [[UIDropDownMenu alloc] initWithIdentifier:@"demoMenu"]];
  
  NSMutableArray *demoNames = [[NSMutableArray alloc] initWithObjects:
                               @"Simple glob3",
                               @"Switch layer",
                               @"Markers",
                               @"3D Model",
                               @"Point Mesh",
                               nil];
  
  [[self demoMenu] setDelegate: self];
  [[self demoMenu] setMenuWidth: 200];
  [[self demoMenu] setBackgroundColor: [UIColor darkGrayColor]];
  [[self demoMenu] setBorderColor: [UIColor blackColor]];
  [[self demoMenu] setTextColor: [UIColor lightGrayColor]];
  [[self demoMenu] setTitleArray: demoNames];
  [[self demoMenu] setValueArray: demoNames];
  [[self demoMenu] makeMenu: [self demoSelector]
                 targetView: [self view]];
}

- (void) initToolbar
{
  [self setToolbar: [[G3MToolbar alloc] init]];
  [[self view] addSubview: toolbar];
  
  // layerSwitcher
  [self setLayerSwitcher: [UIButton buttonWithType: UIButtonTypeCustom]];
  [[self layerSwitcher] addTarget: self
                           action: @selector(switchLayer)
                 forControlEvents: UIControlEventTouchUpInside];
  [[self layerSwitcher] setTitle: @""
                        forState: nil];
  [[[self layerSwitcher] layer] setBorderWidth: 0];
  [[self layerSwitcher] setImage: [UIImage imageNamed:@"satellite-on-96x48.png"]
                        forState: UIControlStateNormal];
  [[self layerSwitcher] setFrame: CGRectMake(10.0, 10.0, 96.0, 48.0)];
  
  // playMarkersDemo
  [self setPlayMarkersDemo: [UIButton buttonWithType: UIButtonTypeCustom]];
  [[self playMarkersDemo] addTarget: self
                           action: @selector(gotoMarkersDemo)
                 forControlEvents: UIControlEventTouchUpInside];
  [[self playMarkersDemo] setTitle: @""
                        forState: nil];
  [[[self playMarkersDemo] layer] setBorderWidth: 0];
  [[self playMarkersDemo] setImage: [UIImage imageNamed:@"play.png"]
                        forState: UIControlStateNormal];
  [[self playMarkersDemo] setFrame: CGRectMake(10.0, 10.0, 48.0, 48.0)];
  
  // playModelDemo
  [self setPlayModelDemo: [UIButton buttonWithType: UIButtonTypeCustom]];
  [[self playModelDemo] addTarget: self
                           action: @selector(gotoModelDemo)
                 forControlEvents: UIControlEventTouchUpInside];
  [[self playModelDemo] setTitle: @""
                        forState: nil];
  [[[self playModelDemo] layer] setBorderWidth: 0];
  [[self playModelDemo] setImage: [UIImage imageNamed:@"play.png"]
                        forState: UIControlStateNormal];
  [[self playModelDemo] setFrame: CGRectMake(10.0, 10.0, 48.0, 48.0)];
  
  // playMeshDemo
  [self setPlayMeshDemo: [UIButton buttonWithType: UIButtonTypeCustom]];
  [[self playMeshDemo] addTarget: self
                           action: @selector(gotoMeshDemo)
                 forControlEvents: UIControlEventTouchUpInside];
  [[self playMeshDemo] setTitle: @""
                        forState: nil];
  [[[self playMeshDemo] layer] setBorderWidth: 0];
  [[self playMeshDemo] setImage: [UIImage imageNamed:@"play.png"]
                        forState: UIControlStateNormal];
  [[self playMeshDemo] setFrame: CGRectMake(10.0, 10.0, 48.0, 48.0)];
}

- (void) updateToolbar: (NSString*) option
{
  [[self toolbar] clear];
  if ([option isEqual: @"Simple glob3"]) {
    [[self toolbar] setVisible: FALSE];
  }
  else if ([option isEqual: @"Switch layer"]) {
    [[self toolbar] addSubview: [self layerSwitcher]];
    [[self toolbar] setVisible: TRUE];
  }
  else if ([option isEqual: @"Markers"]) {
    [[self toolbar] addSubview: [self playMarkersDemo]];
    [[self toolbar] setVisible: TRUE];
  }
  else if ([option isEqual: @"3D Model"]) {
    [[self toolbar] addSubview: [self playModelDemo]];
    [[self toolbar] setVisible: TRUE];
  }
  else if ([option isEqual: @"Point Mesh"]) {
    [[self toolbar] addSubview: [self playMeshDemo]];
    [[self toolbar] setVisible: TRUE];
  }
}

- (void) DropDownMenuDidChange:(NSString *)identifier :(NSString *)returnValue
{
  if ([identifier isEqual: @"demoMenu"]) {
    [self resetWidget];
    [self updateToolbar: returnValue];
    [[self demoSelector] setTitle: returnValue
                         forState: nil];
    
    if ([returnValue isEqual: @"Simple glob3"]) {
      [self showSimpleGlob3];
    }
    else if ([returnValue isEqual: @"Switch layer"]) {
      [self switchLayer];
    }
    else if ([returnValue isEqual: @"Markers"]) {
      [self markerRenderer]->setEnable(true);
    }
    else if ([returnValue isEqual: @"3D Model"]) {
      [self shapeRenderer]->setEnable(true);
    }
    else if ([returnValue isEqual: @"Point Mesh"]) {
      [self meshRenderer]->setEnable(true);
    }
  }
}

// end UI

// Aux classes

class DemoUserData : public WidgetUserData {
private:
  Shape* _plane;
public:
  DemoUserData() {
    
  }
  
  void setPlane(Shape* plane) {
    _plane = plane;
  }
  
  Shape* getPlane() {
    return _plane;
  }
};

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
                              Geodetic3D(Angle::fromDegrees(coordinates->getAsNumber(1)->value()),
                                         Angle::fromDegrees(coordinates->getAsNumber(0)->value()),
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
