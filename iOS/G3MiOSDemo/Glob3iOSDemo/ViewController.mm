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
#include "SingleImageTileTexturizer.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "TileRenderer.hpp"
#include "TilesRenderParameters.hpp"
#include "MarksRenderer.hpp"
#include "CameraConstraints.hpp"
#include "GLErrorRenderer.hpp"

#include "LevelTileCondition.hpp"

#include "BingLayer.hpp"
//#include "OSMLayer.hpp"

#include "IJSONParser.hpp"
#include "JSONParser_iOS.hpp"


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
	// Do any additional setup after loading the view, typically from a nib.
  //  [[self G3MWidget] initWidgetDemo];
  [self initWidgetDemo];
  
  [[self G3MWidget] startAnimation];
}

- (void) initWidgetDemo
{
  LayerSet* layerSet = new LayerSet();
  
  
  
  
  if (false) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?"),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        new LevelTileCondition(0, 6));
    layerSet->addLayer(blueMarble);
    
    WMSLayer* i3Landsat = new WMSLayer("esat",
                                       URL("http://data.worldwind.arc.nasa.gov/wms?"),
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
  
  
  //IJSONParser* parser = new JSONParser_iOS();
  //JSONParser.setInstance(parser);
  
//  //Map styles: Road, Aerial, Hybrid
//  //Languages: English, Spanish, German, Italian, French, Dutch
//  BingLayer* realBing = new BingLayer(URL("http://dev.virtualearth.net/REST/v1/Imagery/Metadata"), NULL, Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0), Hybrid, Dutch,"AgOLISvN2b3012i-odPJjVxhB1dyU6avZ2vG9Ub6Z9-mEpgZHre-1rE8o-DUinUH");
//  layerSet->addLayer(realBing);
//  
  
  /*OSMLayer* osm = new OSMLayer(URL("http://a.tile.openstreetmap.org"), NULL, Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0));
  
  layerSet->addLayer(osm);*/
  
  
  bool useBing = false;
  if (useBing) {
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?"),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  NULL);
    layerSet->addLayer(bing);
  }
  
  bool useOSMLatLon = true;
  if (useOSMLatLon) {
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
                                 URL("http://129.206.228.72/cached/osm"),
                                 WMS_1_1_0,
                                 Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
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
                                  URL("http://www.idee.es/wms/PNOA/PNOA"),
                                  WMS_1_1_0,
                                  Sector::fromDegrees(21, -18, 45, 6),
                                  "image/png",
                                  "EPSG:4326",
                                  "",
                                  true,
                                  NULL);
    layerSet->addLayer(pnoa);
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
    // marks renderer
    MarksRenderer* marks = new MarksRenderer();
    renderers.push_back(marks);
    
    Mark* m1 = new Mark("Fuerteventura",
                        "g3m-marker.png",
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
    //m1->addTouchListener(listener);
    marks->addMark(m1);
    
    
    Mark* m2 = new Mark("Las Palmas",
                        "g3m-marker.png",
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
    //m2->addTouchListener(listener);
    marks->addMark(m2);
    
    if (false) {
      for (int i = 0; i < 500; i++) {
        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
        
        marks->addMark(new Mark("Random",
                                "g3m-marker.png",
                                Geodetic3D(latitude, longitude, 0)));
      }
    }
    
    //Tests for JSON
    /*if (true){
      IJSONParser* parser = new JSONParser_iOS();
      JSONParser.setInstance(parser);
      std::string testString = "{\"a\": [1, false, \"hola\"]}";
    
      JSONBaseObject* testObj = IJSONParser::instance()->parse(testString);
      JSONObject* object = testObj->getObject();
    
      JSONArray* array = object->getObjectForKey("a")->getArray();
      int element0 = array->getElement(0)->getNumber()->getIntValue();
      bool element1 = array->getElement(1)->getBoolean()->getValue();
      std::string element2 = array->getElement(2)->getString()->getValue();
    }*/
  }
  
  //  if (false) {
  //    LatLonMeshRenderer *renderer = new LatLonMeshRenderer();
  //    renderers.push_back(renderer);
  //  }
  
  //  if (false) {
  //    SceneGraphRenderer* sgr = new SceneGraphRenderer();
  //    SGCubeNode* cube = new SGCubeNode();
  //    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
  //    sgr->getRootNode()->addChild(cube);
  //    renderers.push_back(sgr);
  //  }
  
//  renderers.push_back(new GLErrorRenderer());
  
  std::vector <ICameraConstrainer*> cameraConstraints;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints.push_back(scc);
  
  UserData* userData = NULL;
  [[self G3MWidget] initWidgetWithCameraConstraints: cameraConstraints
                                           layerSet: layerSet
                                          renderers: renderers
                                           userData: userData];
  
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
