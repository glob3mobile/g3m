//
//  ViewController.m
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"

#include "LayerSet.hpp"
#include "WMSLayer.hpp"
#include "Factory_iOS.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "TileRenderer.hpp"
#include "TilesRenderParameters.hpp"
#include "MarksRenderer.hpp"
#include "CameraConstraints.hpp"
//#include "GLErrorRenderer.hpp"
#include "LevelTileCondition.hpp"
#include "BingLayer.hpp"
#include "TrailsRenderer.hpp"
#include "PeriodicalTask.hpp"
#include "ShapesRenderer.hpp"
//#include "QuadShape.hpp"
#include "CircleShape.hpp"
//#include "CompositeShape.hpp"
#include "SceneJSShapesParser.hpp"
#include "G3MWidget.hpp"

@implementation ViewController

@synthesize G3MWidget;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
  [super viewDidLoad];

  [[self G3MWidget] initSingletons];

  [self initWidgetDemo];

  [[self G3MWidget] startAnimation];
}

- (void) initWidgetDemo
{
  LayerSet* layerSet = new LayerSet();

  if (false) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?", false),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        new LevelTileCondition(0, 6));
    layerSet->addLayer(blueMarble);

    WMSLayer* i3Landsat = new WMSLayer("esat",
                                       URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                                       WMS_1_1_0,
                                       Sector::fullSphere(),
                                       "image/jpeg",
                                       "EPSG:4326",
                                       "",
                                       false,
                                       new LevelTileCondition(7, 100));
    layerSet->addLayer(i3Landsat);
  }

  //  WMSLayer* political = new WMSLayer("topp:cia",
  //                                     URL("http://worldwind22.arc.nasa.gov/geoserver/wms?"),
  //                                     WMS_1_1_0,
  //                                     Sector::fullSphere(),
  //                                     "image/png",
  //                                     "EPSG:4326",
  //                                     "countryboundaries",
  //                                     true,
  //                                     NULL);
  //  layerSet->addLayer(political);

  bool useBing = true;
  if (useBing) {
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  NULL);
    layerSet->addLayer(bing);
  }

  bool useOSM = false;
  if (useOSM) {
    //    WMSLayer *osm = new WMSLayer("osm",
    //                                 URL("http://wms.latlon.org/"),
    //                                 WMS_1_1_0,
    //                                 Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
    //                                 "image/jpeg",
    //                                 "EPSG:4326",
    //                                 "",
    //                                 false,
    //                                 NULL);
    //    layerSet->addLayer(osm);
    WMSLayer *osm = new WMSLayer("osm_auto:all",
                                 URL("http://129.206.228.72/cached/osm", false),
                                 WMS_1_1_0,
                                 // Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
                                 Sector::fullSphere(),
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL);
    layerSet->addLayer(osm);

  }

  const bool usePnoaLayer = false;
  if (usePnoaLayer) {
    WMSLayer *pnoa = new WMSLayer("PNOA",
                                  URL("http://www.idee.es/wms/PNOA/PNOA", false),
                                  WMS_1_1_0,
                                  Sector::fromDegrees(21, -18, 45, 6),
                                  "image/png",
                                  "EPSG:4326",
                                  "",
                                  true,
                                  NULL);
    layerSet->addLayer(pnoa);
  }

  const bool testURLescape = false;
  if (testURLescape) {
    WMSLayer *ayto = new WMSLayer(URL::escape("Ejes de via"),
                                  URL("http://sig.caceres.es/wms_callejero.mapdef?", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/png",
                                  "EPSG:4326",
                                  "",
                                  true,
                                  NULL);
    layerSet->addLayer(ayto);

  }

  //  WMSLayer *vias = new WMSLayer("VIAS",
  //                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
  //                                WMS_1_1_0,
  //                                "image/gif",
  //                                Sector::fromDegrees(22.5,-22.5, 33.75, -11.25),
  //                                "EPSG:4326",
  //                                "",
  //                                true,
  //                                Angle::nan(),
  //                                Angle::nan());
  //  layerSet->addLayer(vias);

  //  WMSLayer *osm = new WMSLayer("bing",
  //                               "bing",
  //                               "http://wms.latlon.org/",
  //                               WMS_1_1_0,
  //                               "image/jpeg",
  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
  //                               "EPSG:4326",
  //                               "",
  //                               false,
  //                               Angle::nan(),
  //                               Angle::nan());
  //  layerSet->addLayer(osm);

  std::vector<Renderer*> renderers;

  //  if (false) {
  //    // dummy renderer with a simple box
  //    DummyRenderer* dum = new DummyRenderer();
  //    comp->addRenderer(dum);
  //  }

  //  if (false) {
  //    // simple planet renderer, with a basic world image
  //    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
  //    comp->addRenderer(spr);
  //  }


  if (true) {

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


    // marks renderer
    const bool readyWhenMarksReady = false;
    MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
    renderers.push_back(marksRenderer);

    marksRenderer->setMarkTouchListener(new TestMarkTouchListener(), true);

    Mark* m1 = new Mark("Fuerteventura",
                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
    marksRenderer->addMark(m1);


    Mark* m2 = new Mark("Las Palmas",
                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
    marksRenderer->addMark(m2);

    if (false) {
      for (int i = 0; i < 2000; i++) {
        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );

        marksRenderer->addMark(new Mark("Random",
                                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                                        Geodetic3D(latitude, longitude, 0)));
      }
    }
  }

//  if (true) {
    ShapesRenderer* shapesRenderer = new ShapesRenderer();

//    //  std::string textureFileName = "g3m-marker.png";
//    //  IImage* textureImage = IFactory::instance()->createImageFromFileName(textureFileName);
//    //
//    //  Shape* shape = new QuadShape(Geodetic3D(Angle::fromDegrees(37.78333333),
//    //                                          Angle::fromDegrees(-122.41666666666667),
//    //                                          8000),
//    //                               textureImage, true, textureFileName,
//    //                               50000, 50000);
//
//    Shape* shape = new CircleShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
//                                                  Angle::fromDegrees(-122.41666666666667),
//                                                  8000),
//                                   50000,
//                                   Color::newFromRGBA(1, 1, 0, 1));
//    //  shape->setHeading( Angle::fromDegrees(45) );
//    //  shape->setPitch( Angle::fromDegrees(45) );
//    //  shape->setScale(2.0, 0.5, 1);
//
//    shapesRenderer->addShape(shape);

    // CompositeShape* group = new CompositeShape();
    // group->addShape(shape);
    // shapesRenderer->addShape(group);


    renderers.push_back(shapesRenderer);
//  }


  TrailsRenderer* trailsRenderer = new TrailsRenderer();
  renderers.push_back(trailsRenderer);

  Trail* trail = new Trail(50, Color::fromRGBA(1, 0, 0, 1), 2);

  Geodetic3D position(Angle::fromDegrees(37.78333333),
                      Angle::fromDegrees(-122.41666666666667),
                      7500);
  trail->addPosition(position);
  trailsRenderer->addTrail(trail);


  //  if (false) {
  //    LatLonMeshRenderer *renderer = new LatLonMeshRenderer();
  //    renderers.push_back(renderer);
  //  }


  //  renderers.push_back(new GLErrorRenderer());

  class TestTrailTask : public GTask {
  private:
    Trail* _trail;

    double _lastLatitudeDegrees;
    double _lastLongitudeDegrees;
    double _lastHeight;

  public:
    TestTrailTask(Trail* trail,
                  Geodetic3D lastPosition) :
    _trail(trail),
    _lastLatitudeDegrees(lastPosition.latitude()._degrees),
    _lastLongitudeDegrees(lastPosition.longitude()._degrees),
    _lastHeight(lastPosition.height())
    {

    }

    void run() {
      _lastLatitudeDegrees += 0.025;
      _lastLongitudeDegrees += 0.025;
      _lastHeight += 200;

      _trail->addPosition(Geodetic3D(Angle::fromDegrees(_lastLatitudeDegrees),
                                     Angle::fromDegrees(_lastLongitudeDegrees),
                                     _lastHeight));
    }
  };

  std::vector<PeriodicalTask*> periodicalTasks;
  periodicalTasks.push_back( new PeriodicalTask(TimeInterval::fromSeconds(1),
                                                new TestTrailTask(trail, position)));


  std::vector <ICameraConstrainer*> cameraConstraints;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints.push_back(scc);


  class SampleInitializationTask : public GTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;

  public:
    SampleInitializationTask(G3MWidget_iOS* iosWidget,
                             ShapesRenderer* shapesRenderer) :
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer)
    {

    }

    void run() {
      printf("Running initialization Task\n");

      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D(Angle::fromDegreesMinutes(37, 47),
                                                                Angle::fromDegreesMinutes(-122, 25),
                                                                1000000),
                                                     TimeInterval::fromSeconds(10));

      NSString *filePath = [[NSBundle mainBundle] pathForResource:@"seymour-plane" ofType:@"json"];
      if (filePath) {
        NSString *nsString = [NSString stringWithContentsOfFile: filePath
                                                       encoding: NSUTF8StringEncoding
                                                          error: nil];
        if (nsString) {
          std::string str = [nsString UTF8String];
          Shape* tank = SceneJSShapesParser::parse(str);

          tank->setPosition( new Geodetic3D(Angle::fromDegrees(37.78333333),
                                            Angle::fromDegrees(-122.41666666666667),
                                            100) );
          tank->setScale(10, 10, 10);
          _shapesRenderer->addShape(tank);
        }
      }
      

    }
  };

  UserData* userData = NULL;
  const bool incrementalTileQuality = true;
  [[self G3MWidget] initWidgetWithCameraConstraints: cameraConstraints
                                           layerSet: layerSet
                             incrementalTileQuality: incrementalTileQuality
                                          renderers: renderers
                                           userData: userData
                                 initializationTask: new SampleInitializationTask([self G3MWidget], shapesRenderer)
                                    periodicalTasks: periodicalTasks];
}

- (void)viewDidUnload
{
  [super viewDidUnload];
  // Release any retained subviews of the main view.
  // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
  [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
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

@end
