//
//  ViewController.m
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"



#include "G3MBuilder_iOS.hpp"

#include "VisibleSectorListener.hpp"
#include "MarksRenderer.hpp"
#include "ShapesRenderer.hpp"
#include "GEORenderer.hpp"
#include "BusyMeshRenderer.hpp"
#include "MeshRenderer.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"
#include "WMSLayer.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "LevelTileCondition.hpp"
#include "LayerBuilder.hpp"
#include "TileRendererBuilder.hpp"
#include "MarkTouchListener.hpp"
#include "TrailsRenderer.hpp"
#include "Mark.hpp"
#include "CircleShape.hpp"
#include "BoxShape.hpp"
#include "SceneJSShapesParser.hpp"

#include "IJSONParser.hpp"
#include "JSONGenerator.hpp"
#include "BSONParser.hpp"
#include "BSONGenerator.hpp"

#include "MeshShape.hpp"
#include "IShortBuffer.hpp"
#include "SimpleCameraConstrainer.hpp"

#include "G3MWidget.hpp"

class TestVisibleSectorListener : public VisibleSectorListener {
public:
  void onVisibleSectorChange(const Sector& visibleSector,
                             const Geodetic3D& cameraPosition) {
    ILogger::instance()->logInfo("VisibleSector=%s, CameraPosition=%s",
                                 visibleSector.description().c_str(),
                                 cameraPosition.description().c_str());
  }
};


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

  // initialize a customized widget without using a builder
  //[[self G3MWidget] initSingletons];
  // [self initWithoutBuilder];

  // initizalize a default widget by using a builder
//  [self initDefaultWithBuilder];

  // initialize a customized widget by using a buider
  [self initCustomizedWithBuilder];

  [[self G3MWidget] startAnimation];
}

//- (void) initWithoutBuilder
//{
//  IStorage* storage = new SQLiteStorage_iOS("g3m.cache");
//
//  const bool saveInBackground = true;
//  IDownloader* downloader = new CachedDownloader(new Downloader_iOS(8),
//                                                 storage,
//                                                 saveInBackground);
//
//  IThreadUtils* threadUtils = new ThreadUtils_iOS();
//
//  const Planet* planet = Planet::createEarth();
//
//  CompositeRenderer* mainRenderer = new CompositeRenderer();
//
//  TileRenderer* tileRenderer = [self createTileRenderer: [self createTileRenderParameters]
//                                               layerSet: [self createLayerSet]];
//  mainRenderer->addRenderer(tileRenderer);
//
//  MarksRenderer* marksRenderer = [self createMarksRenderer];
//  mainRenderer->addRenderer(marksRenderer);
//
//  ShapesRenderer* shapesRenderer = [self createShapesRenderer];
//  mainRenderer->addRenderer(shapesRenderer);
//
//  GEORenderer* geoRenderer = [self createGEORenderer];
//  mainRenderer->addRenderer(geoRenderer);
//
//  Renderer* busyRenderer = new BusyMeshRenderer();
//
//  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
//                                                                     geoRenderer: geoRenderer];
//
//  std::vector<PeriodicalTask*> periodicalTasks;
//
//  // initialization
//  [[self G3MWidget] initWidget: storage
//                    downloader: downloader
//                   threadUtils: threadUtils
//                        planet: planet
//             cameraConstraints: [self createCameraConstraints]
//                cameraRenderer: [self createCameraRenderer]
//                  mainRenderer: mainRenderer
//                  busyRenderer: busyRenderer
//               backgroundColor: Color::fromRGBA((float)0, (float)0.1, (float)0.2, (float)1)
//                        logFPS: true
//       logDownloaderStatistics: false
//            initializationTask: initializationTask
//  autoDeleteInitializationTask: true
//               periodicalTasks: periodicalTasks
//                      userData: NULL];
//
//}

- (void) initDefaultWithBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);

  // initialization
  builder.initializeWidget();
}

- (void) initCustomizedWithBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);

  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  builder.addCameraConstraint(scc);

  builder.setCameraRenderer([self createCameraRenderer]);

  builder.setPlanet(Planet::createEarth());

  Color* bgColor = Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
  builder.setBackgroundColor(bgColor);

  LayerSet* layerSet = [self createLayerSet];

//  layerSet->addLayer(new WMSLayer("precipitation", //
//                                  URL("http://wms.openweathermap.org/service", false), //
//                                  WMS_1_1_0, //
//                                  Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0), //
//                                  "image/png", //
//                                  "EPSG:4326", //
//                                  "", //
//                                  true, //
//                                  NULL)
//                     );

  builder.getTileRendererBuilder()->setLayerSet(layerSet);
  builder.getTileRendererBuilder()->setTileRendererParameters([self createTileRenderParameters]);
  builder.getTileRendererBuilder()->addVisibleSectorListener(new TestVisibleSectorListener(),
                                                            TimeInterval::fromSeconds(3));
  
  Renderer* busyRenderer = new BusyMeshRenderer();
  builder.setBusyRenderer(busyRenderer);

  //    DummyRenderer* dum = new DummyRenderer();
  //    builder->addRenderer(dum);
  //    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
  //    builder->addRenderer(spr);

  MarksRenderer* marksRenderer = [self createMarksRenderer];
  builder.addRenderer(marksRenderer);

  ShapesRenderer* shapesRenderer = [self createShapesRenderer];
  builder.addRenderer(shapesRenderer);

  GEORenderer* geoRenderer = [self createGEORenderer];
  builder.addRenderer(geoRenderer);

  MeshRenderer* meshRenderer = new MeshRenderer();
  builder.addRenderer( meshRenderer );

//  meshRenderer->addMesh([self createPointsMesh: builder.getPlanet() ]);

  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
                                                                     geoRenderer: geoRenderer];
  builder.setInitializationTask(initializationTask, true);

  PeriodicalTask* periodicalTask = [self createSamplePeriodicalTask: &builder];
  builder.addPeriodicalTask(periodicalTask);

  const bool logFPS = false;
  builder.setLogFPS(logFPS);

  const bool logDownloaderStatistics = false;
  builder.setLogDownloaderStatistics(logDownloaderStatistics);

  WidgetUserData* userData = NULL;
  builder.setUserData(userData);

  // initialization
  builder.initializeWidget();
}

- (Mesh*) createPointsMesh: (const Planet*)planet
{
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
  return new DirectMesh(GLPrimitive::points(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        pointSize,
                        flatColor,
                        colors.create());
}

- (CameraRenderer*) createCameraRenderer
{
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  const bool processRotation = true;
  const bool processZoom = true;
  cameraRenderer->addHandler(new CameraDoubleDragHandler(processRotation,
                                                         processZoom));
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());

  return cameraRenderer;
}

- (std::vector <ICameraConstrainer*>) createCameraConstraints
{
  std::vector <ICameraConstrainer*> cameraConstraints;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints.push_back(scc);

  return cameraConstraints;
}

- (LayerSet*) createLayerSet
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
                                        new LevelTileCondition(0, 6),
                                        TimeInterval::fromDays(30));
    layerSet->addLayer(blueMarble);

    WMSLayer* i3Landsat = new WMSLayer("esat",
                                       URL("http://data.worldwind.arc.nasa.gov/wms?", false),
                                       WMS_1_1_0,
                                       Sector::fullSphere(),
                                       "image/jpeg",
                                       "EPSG:4326",
                                       "",
                                       false,
                                       new LevelTileCondition(7, 100),
                                       TimeInterval::fromDays(30));
    layerSet->addLayer(i3Landsat);
  }


  bool useBing = true;
  if (useBing) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?", false),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        new LevelTileCondition(0, 5),
                                        TimeInterval::fromDays(30));
    layerSet->addLayer(blueMarble);


//    bool enabled = true;
//    WMSLayer* bing = LayerBuilder::createBingLayer(enabled);
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  new LevelTileCondition(6, 500),
                                  TimeInterval::fromDays(30));
    layerSet->addLayer(bing);
  }

  if (false) {
    WMSLayer* political = new WMSLayer("topp:cia",
                                       URL("http://worldwind22.arc.nasa.gov/geoserver/wms?", false),
                                       WMS_1_1_0,
                                       Sector::fullSphere(),
                                       "image/png",
                                       "EPSG:4326",
                                       "countryboundaries",
                                       true,
                                       NULL,
                                       TimeInterval::fromDays(30));
    layerSet->addLayer(political);
  }

  bool useOSM = true;
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
                                 //Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
                                 Sector::fullSphere(),
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL,
                                 TimeInterval::fromDays(30));
    osm->setEnable(false);
    layerSet->addLayer(osm);
  }

  
//  WMSLayer* pressure = new WMSLayer("pressure_cntr", //
//                                    URL("http://wms.openweathermap.org/service", false), //
//                                    WMS_1_1_0, //
//                                    Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0), //
//                                    "image/png", //
//                                    "EPSG:4326", //
//                                    "", //
//                                    true, //
//                                    NULL,
//                                    TimeInterval::zero());
//  layerSet->addLayer(pressure);

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
                                  NULL,
                                  TimeInterval::fromDays(30));
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
                                  NULL,
                                  TimeInterval::fromDays(30));
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

  return layerSet;
}

- (TilesRenderParameters*) createTileRenderParameters
{
  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceTopLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = true;

  return TilesRenderParameters::createDefault(renderDebug,
                                              useTilesSplitBudget,
                                              forceTopLevelTilesRenderOnStart,
                                              incrementalTileQuality);
}

- (TileRenderer*) createTileRenderer: (TilesRenderParameters*) parameters
                            layerSet: (LayerSet*) layerSet
{
  TileRendererBuilder* trBuilder = new TileRendererBuilder();
  trBuilder->setShowStatistics(false);
  trBuilder->setTileRendererParameters(parameters);
  trBuilder->setLayerSet(layerSet);

  TileRenderer* tileRenderer = trBuilder->create();

  return tileRenderer;
}

- (MarksRenderer*) createMarksRenderer
{

  class TestMarkTouchListener : public MarkTouchListener {
  public:
    bool touchedMark(Mark* mark) {
      NSString* message = [NSString stringWithFormat: @"Touched on mark \"%s\"", mark->getLabel().c_str()];

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

  marksRenderer->setMarkTouchListener(new TestMarkTouchListener(), true);

  Mark* m1 = new Mark("Fuerteventura",
                      URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
  marksRenderer->addMark(m1);


  Mark* m2 = new Mark(URL("file:///plane.png", false),
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
  marksRenderer->addMark(m2);

  Mark* m3 = new Mark("Washington, DC",
                      Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                 0),
                      0);
  marksRenderer->addMark(m3);

  if (false) {
    for (int i = 0; i < 2000; i++) {
      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );

      marksRenderer->addMark(new Mark("Random",
                                      URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                                      Geodetic3D(latitude, longitude, 0)));
    }
  }

  return marksRenderer;

}

- (ShapesRenderer*) createShapesRenderer
{
  ShapesRenderer* shapesRenderer = new ShapesRenderer();

  //  std::string textureFileName = "g3m-marker.png";
  //  IImage* textureImage = IFactory::instance()->createImageFromFileName(textureFileName);
  //
  //  Shape* shape = new QuadShape(Geodetic3D(Angle::fromDegrees(37.78333333),
  //                                          Angle::fromDegrees(-122.41666666666667),
  //                                          8000),
  //                               textureImage, true, textureFileName,
  //                               50000, 50000);

  Shape* circle = new CircleShape(new Geodetic3D(Angle::fromDegrees(38.78333333),
                                                 Angle::fromDegrees(-123),
                                                 8000),
                                  50000,
                                  Color::newFromRGBA(1, 1, 0, 0.5));
  //  circle->setHeading( Angle::fromDegrees(45) );
  //  circle->setPitch( Angle::fromDegrees(45) );
  //  circle->setScale(2.0, 0.5, 1);
  shapesRenderer->addShape(circle);

  Shape* box = new BoxShape(new Geodetic3D(Angle::fromDegrees(39.78333333),
                                           Angle::fromDegrees(-122),
                                           45000),
                            Vector3D(20000, 30000, 50000),
                            2,
                            Color::newFromRGBA(0,    1, 0, 0.5),
                            Color::newFromRGBA(0, 0.75, 0, 0.75));
  box->setAnimatedScale(1, 1, 20);
  shapesRenderer->addShape(box);

  return shapesRenderer;
}

- (GEORenderer*) createGEORenderer
{
  GEORenderer* geoRenderer = new GEORenderer();

  return geoRenderer;
}

- (GInitializationTask*) createSampleInitializationTask: (ShapesRenderer*) shapesRenderer
                                            geoRenderer: (GEORenderer*) geoRenderer
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;
    GEORenderer*    _geoRenderer;

  public:
    SampleInitializationTask(G3MWidget_iOS*  iosWidget,
                             ShapesRenderer* shapesRenderer,
                             GEORenderer*    geoRenderer) :
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer),
    _geoRenderer(geoRenderer)
    {

    }

    void run(const G3MContext* context) {
      printf("Running initialization Task\n");

      
//      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D(//Angle::fromDegreesMinutes(37, 47),
//                                                                //Angle::fromDegreesMinutes(-122, 25),
//                                                                Angle::fromDegrees(37.78333333),
//                                                                Angle::fromDegrees(-122.41666666666667),
//                                                                1000000),
//                                                     TimeInterval::fromSeconds(5));

      /*
      NSString *bsonFilePath = [[NSBundle mainBundle] pathForResource: @"test"
                                                               ofType: @"bson"];
      if (bsonFilePath) {

        NSData* data = [NSData dataWithContentsOfFile: bsonFilePath];

        const int length = [data length];
        unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
        [data getBytes: bytes
                length: length];
        

        IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);

        JSONBaseObject* bson = BSONParser::parse(buffer);

        printf("%s\n", bson->description().c_str());

        delete bson;

        delete buffer;
      }
      */

      /**/
//      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
//                                                                ofType: @"json"];

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
          Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, "file:///textures-A320/");
          //Shape* plane = SceneJSShapesParser::parse(planeJSON, "file:///textures-citation/");
          if (plane) {
            // Washington, DC
            plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                              Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                              10000) );
            const double scale = 200;
            plane->setScale(scale, scale, scale);
            plane->setPitch(Angle::fromDegrees(90));
            _shapesRenderer->addShape(plane);

            plane->setAnimatedPosition(TimeInterval::fromSeconds(26),
                                       Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                  Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
                                                  10000),
                                       true);

            const double fromDistance = 50000 * 1.5;
            const double toDistance   = 25000 * 1.5 / 2;

            // const Angle fromAzimuth = Angle::fromDegrees(-90);
            // const Angle toAzimuth   = Angle::fromDegrees(-90 + 360 + 180);
            const Angle fromAzimuth = Angle::fromDegrees(-90);
            const Angle toAzimuth   = Angle::fromDegrees(-90 + 360);

            // const Angle fromAltitude = Angle::fromDegrees(65);
            // const Angle toAltitude   = Angle::fromDegrees(5);
            // const Angle fromAltitude = Angle::fromDegrees(30);
            // const Angle toAltitude   = Angle::fromDegrees(15);
            const Angle fromAltitude = Angle::fromDegrees(90);
            const Angle toAltitude   = Angle::fromDegrees(15);

            plane->orbitCamera(TimeInterval::fromSeconds(20),
                               fromDistance, toDistance,
                               fromAzimuth,  toAzimuth,
                               fromAltitude, toAltitude);
          }
        }
      }
      /**/


      /*
      IFloatBuffer* vertices = IFactory::instance()->createFloatBuffer(6 * 3);
      int i =0;
      vertices->put(i++, -1);
      vertices->put(i++, -0.5);
      vertices->put(i++, 0);

      vertices->put(i++, 0);
      vertices->put(i++, 0);
      vertices->put(i++, 0);

      vertices->put(i++, 0);
      vertices->put(i++, -0.5);
      vertices->put(i++, 0);

      vertices->put(i++, -1);
      vertices->put(i++, -1);
      vertices->put(i++, 0);

      vertices->put(i++, 1);
      vertices->put(i++, -0.5);
      vertices->put(i++, 0);

      vertices->put(i++, 1);
      vertices->put(i++, -1);
      vertices->put(i++, 0);

      IShortBuffer* indices = IFactory::instance()->createShortBuffer(12);
      i = 0;
      indices->put(i++, 0);
      indices->put(i++, 1);
      indices->put(i++, 2);
      indices->put(i++, 0);
      indices->put(i++, 2);
      indices->put(i++, 3);
      indices->put(i++, 2);
      indices->put(i++, 1);
      indices->put(i++, 4);
      indices->put(i++, 2);
      indices->put(i++, 4);
      indices->put(i++, 5);

      IndexedMesh* travelledMesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                                                   true,
                                                   Vector3D::zero(),
                                                   vertices,
                                                   indices,
                                                   1,
                                                   2,
                                                   Color::newFromRGBA(1, 1, 0, 1));

      IndexedMesh* toTravelMesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                                                   true,
                                                   Vector3D::zero(),
                                                   vertices,
                                                   indices,
                                                   1,
                                                   2,
                                                   Color::newFromRGBA(0.5, 0.5, 0.5, 1));

//      Geodetic3D* buenosAiresPosition = new Geodetic3D(Angle::fromDegreesMinutesSeconds(-34, 36, 13.44),
//                                                       Angle::fromDegreesMinutesSeconds(-58, 22, 53.74),
//                                                       1000);
      
//      Geodetic3D* dcPosition = new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                              Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
//                                              600);
//      Shape* trailShape = new MeshShape(dcPosition, mesh);
//      // Washington, DC
//      trailShape->setHeading(Angle::fromDegrees(270));
//      trailShape->setScale(500, 500, 500);
//      _shapesRenderer->addShape(trailShape);

      for (double lon = -70; lon >= -80; lon -= 0.008) {
        Mesh* mesh = (lon >= -76.96) ? travelledMesh : toTravelMesh;
        Shape* trailShape = new MeshShape(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                         Angle::fromDegrees(lon),
                                                         9400),
                                          mesh);
        trailShape->setHeading(Angle::fromDegrees(270));
        const double scale = 500;
        trailShape->setScale(scale * 2 / 3, scale * 1.8, scale);
        _shapesRenderer->addShape(trailShape);
      }
      */

      /**/
      /*
      //      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/coastline"
      //                                                                  ofType: @"geojson"];

      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/boundary_lines_land"
                                                                  ofType: @"geojson"];

      //      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/extremadura-roads"
      //                                                                  ofType: @"geojson"];

      if (geoJSONFilePath) {
        NSString *nsGEOJSON = [NSString stringWithContentsOfFile: geoJSONFilePath
                                                        encoding: NSUTF8StringEncoding
                                                           error: nil];
        if (nsGEOJSON) {
          std::string geoJSON = [nsGEOJSON UTF8String];

          GEOObject* geoObject = GEOJSONParser::parse(geoJSON);

          _geoRenderer->addGEOObject(geoObject);
        }
      }
      */

      /*
      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
                                                                ofType: @"json"];
      if (planeFilePath) {
        NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
                                                          encoding: NSUTF8StringEncoding
                                                             error: nil];
        if (nsPlaneJSON) {
          std::string planeJSON = [nsPlaneJSON UTF8String];
          JSONBaseObject* jsonObject = IJSONParser::instance()->parse(planeJSON);

          IByteBuffer* bson = BSONGenerator::generate(jsonObject);
          printf("%s\n", bson->description().c_str());

          JSONBaseObject* bsonObject = BSONParser::parse(bson);
          printf("%s\n", bsonObject->description().c_str());

          delete bson;

          delete jsonObject;

          delete bsonObject;
        }
      }
      */

      /*
      // JSONBaseObject* jsonObject = IJSONParser::instance()->parse("{\"key1\":\"string\", \"key2\": 100, \"key3\": false, \"key4\":123.5}");
//      JSONBaseObject* jsonObject = IJSONParser::instance()->parse("{\"hello\":\"world\"}");
      JSONBaseObject* jsonObject = IJSONParser::instance()->parse("{\"BSON\": [\"awesome\", 5.05, 1986, true, false], \"X\": {\"foo\": 20000000000}}");
      printf("%s\n", jsonObject->description().c_str());

      std::string jsonString = JSONGenerator::generate(jsonObject);
      printf("%s (lenght=%lu)\n", jsonString.c_str(), jsonString.size());

      IByteBuffer* bson = BSONGenerator::generate(jsonObject);
      printf("%s\n", bson->description().c_str());

      JSONBaseObject* bsonObject = BSONParser::parse(bson);
      printf("%s\n", bsonObject->description().c_str());

      delete bson;

      delete jsonObject;
      */
    }

    bool isDone(const G3MContext* context) {
      return true;
    }
  };

  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget], shapesRenderer, geoRenderer);

  return initializationTask;
}

- (PeriodicalTask*) createSamplePeriodicalTask: (G3MBuilder_iOS*) builder
{
  TrailsRenderer* trailsRenderer = new TrailsRenderer();

  Trail* trail = new Trail(50, Color::fromRGBA(1, 1, 1, 1), 2);

  Geodetic3D position(Angle::fromDegrees(37.78333333),
                      Angle::fromDegrees(-122.41666666666667),
                      7500);
  trail->addPosition(position);
  trailsRenderer->addTrail(trail);
  builder->addRenderer(trailsRenderer);

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

    void run(const G3MContext* context) {
      _lastLatitudeDegrees += 0.025;
      _lastLongitudeDegrees += 0.025;
      _lastHeight += 200;

      _trail->addPosition(Geodetic3D(Angle::fromDegrees(_lastLatitudeDegrees),
                                     Angle::fromDegrees(_lastLongitudeDegrees),
                                     _lastHeight));
    }
  };

  PeriodicalTask* periodicalTask = new PeriodicalTask(TimeInterval::fromSeconds(1),
                                                      new TestTrailTask(trail, position));
  return periodicalTask;
}

- (void)viewDidUnload
{
  G3MWidget = nil;
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
  [G3MWidget stopAnimation];
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
