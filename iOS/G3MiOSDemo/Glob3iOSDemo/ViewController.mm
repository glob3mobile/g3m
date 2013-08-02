//
//  ViewController.m
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"


#import <G3MiOSSDK/G3MBuilder_iOS.hpp>

#import <G3MiOSSDK/VisibleSectorListener.hpp>
#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/ShapesRenderer.hpp>
#import <G3MiOSSDK/GEORenderer.hpp>
#import <G3MiOSSDK/BusyMeshRenderer.hpp>
#import <G3MiOSSDK/MeshRenderer.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromGeodetic.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromColor.hpp>
#import <G3MiOSSDK/DirectMesh.hpp>
#import <G3MiOSSDK/WMSLayer.hpp>
#import <G3MiOSSDK/CameraSingleDragHandler.hpp>
#import <G3MiOSSDK/CameraDoubleDragHandler.hpp>
#import <G3MiOSSDK/CameraZoomAndRotateHandler.hpp>
#import <G3MiOSSDK/CameraRotationHandler.hpp>
#import <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#import <G3MiOSSDK/LevelTileCondition.hpp>
#import <G3MiOSSDK/LayerBuilder.hpp>
#import <G3MiOSSDK/PlanetRendererBuilder.hpp>
#import <G3MiOSSDK/MarkTouchListener.hpp>
#import <G3MiOSSDK/TrailsRenderer.hpp>
#import <G3MiOSSDK/Mark.hpp>
#import <G3MiOSSDK/CircleShape.hpp>
#import <G3MiOSSDK/QuadShape.hpp>
#import <G3MiOSSDK/BoxShape.hpp>
#import <G3MiOSSDK/EllipsoidShape.hpp>
#import <G3MiOSSDK/SceneJSShapesParser.hpp>
#import <G3MiOSSDK/LayoutUtils.hpp>

#import <G3MiOSSDK/IJSONParser.hpp>
#import <G3MiOSSDK/JSONGenerator.hpp>
#import <G3MiOSSDK/BSONParser.hpp>
#import <G3MiOSSDK/BSONGenerator.hpp>

#import <G3MiOSSDK/MeshShape.hpp>
#import <G3MiOSSDK/IShortBuffer.hpp>
#import <G3MiOSSDK/SimpleCameraConstrainer.hpp>
#import <G3MiOSSDK/WMSBillElevationDataProvider.hpp>
#import <G3MiOSSDK/ElevationData.hpp>
#import <G3MiOSSDK/IBufferDownloadListener.hpp>
#import <G3MiOSSDK/BilParser.hpp>
#import <G3MiOSSDK/ShortBufferBuilder.hpp>
#import <G3MiOSSDK/BilinearInterpolator.hpp>
#import <G3MiOSSDK/SubviewElevationData.hpp>
#import <G3MiOSSDK/GInitializationTask.hpp>
#import <G3MiOSSDK/PeriodicalTask.hpp>
#import <G3MiOSSDK/IDownloader.hpp>
#import <G3MiOSSDK/OSMLayer.hpp>
#import <G3MiOSSDK/CartoDBLayer.hpp>
#import <G3MiOSSDK/HereLayer.hpp>
#import <G3MiOSSDK/MapQuestLayer.hpp>
#import <G3MiOSSDK/MapBoxLayer.hpp>
#import <G3MiOSSDK/GoogleMapsLayer.hpp>
#import <G3MiOSSDK/BingMapsLayer.hpp>

#import <G3MiOSSDK/BusyQuadRenderer.hpp>
#import <G3MiOSSDK/Factory_iOS.hpp>

#import <G3MiOSSDK/G3MWidget.hpp>
#import <G3MiOSSDK/GEOJSONParser.hpp>

//import <G3MiOSSDK/WMSBillElevationDataProvider.hpp>
#import <G3MiOSSDK/SingleBillElevationDataProvider.hpp>
#import <G3MiOSSDK/FloatBufferElevationData.hpp>

#import <G3MiOSSDK/GEOSymbolizer.hpp>
#import <G3MiOSSDK/GEO2DMultiLineStringGeometry.hpp>
#import <G3MiOSSDK/GEO2DLineStringGeometry.hpp>
#import <G3MiOSSDK/GEOFeature.hpp>
#import <G3MiOSSDK/JSONObject.hpp>
#import <G3MiOSSDK/GEOLine2DMeshSymbol.hpp>
#import <G3MiOSSDK/GEOMultiLine2DMeshSymbol.hpp>
#import <G3MiOSSDK/GEOLine2DStyle.hpp>
#import <G3MiOSSDK/GEO2DPointGeometry.hpp>
#import <G3MiOSSDK/GEOShapeSymbol.hpp>
#import <G3MiOSSDK/GEOMarkSymbol.hpp>
#import <G3MiOSSDK/GFont.hpp>

#import <G3MiOSSDK/CompositeElevationDataProvider.hpp>
#import <G3MiOSSDK/LayerTilesRenderParameters.hpp>
#import <G3MiOSSDK/RectangleI.hpp>
#import <G3MiOSSDK/LayerTilesRenderParameters.hpp>
#import <G3MiOSSDK/QuadShape.hpp>
#import <G3MiOSSDK/IImageUtils.hpp>
#import <G3MiOSSDK/RectangleF.hpp>
#import <G3MiOSSDK/ShortBufferElevationData.hpp>
#import <G3MiOSSDK/SGShape.hpp>
#import <G3MiOSSDK/SGNode.hpp>
#import <G3MiOSSDK/SGMaterialNode.hpp>

#import <G3MiOSSDK/MapBooBuilder_iOS.hpp>
#import <G3MiOSSDK/MapBooSceneDescription.hpp>
#import <G3MiOSSDK/IWebSocketListener.hpp>

#import <G3MiOSSDK/TileRasterizer.hpp>
#import <G3MiOSSDK/DebugTileRasterizer.hpp>
#import <G3MiOSSDK/GEOTileRasterizer.hpp>

#import <G3MiOSSDK/GEORasterLineSymbol.hpp>
#import <G3MiOSSDK/GEOMultiLineRasterSymbol.hpp>
#import <G3MiOSSDK/GEO2DLineRasterStyle.hpp>

#import <G3MiOSSDK/GEO2DPolygonGeometry.hpp>
#import <G3MiOSSDK/GEORasterPolygonSymbol.hpp>
#import <G3MiOSSDK/GEO2DSurfaceRasterStyle.hpp>

#import <G3MiOSSDK/GEO2DMultiPolygonGeometry.hpp>
#import <G3MiOSSDK/GPUProgramFactory.hpp>


class TestVisibleSectorListener : public VisibleSectorListener {
public:
  void onVisibleSectorChange(const Sector& visibleSector,
                             const Geodetic3D& cameraPosition) {
    ILogger::instance()->logInfo("VisibleSector=%s, CameraPosition=%s",
                                 visibleSector.description().c_str(),
                                 cameraPosition.description().c_str());
  }
};


Mesh* createSectorMesh(const Planet* planet,
                       const int resolution,
                       const Sector& sector,
                       const Color& color,
                       const int lineWidth) {
  // create vectors
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                          planet,
                                          sector._center);

  // create indices
  ShortBufferBuilder indices;

  const int resolutionMinus1 = resolution - 1;
  int indicesCounter = 0;

  const double offset = 0;

  // west side
  for (int j = 0; j < resolutionMinus1; j++) {
    const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                       offset);
    vertices.add(g);

    indices.add(indicesCounter++);
  }

  // south side
  for (int i = 0; i < resolutionMinus1; i++) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                       offset);
    vertices.add(g);

    indices.add(indicesCounter++);
  }

  // east side
  for (int j = resolutionMinus1; j > 0; j--) {
    const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                       offset);
    vertices.add(g);

    indices.add(indicesCounter++);
  }

  // north side
  for (int i = resolutionMinus1; i > 0; i--) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0),
                       offset);
    vertices.add(g);

    indices.add(indicesCounter++);
  }

  return new IndexedMesh(GLPrimitive::lineLoop(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         lineWidth,
                         1,
                         new Color(color),
                         NULL, //colors
                         0,    // colorsIntensity
                         false //depthTest
                         );
}


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


class TestMapBooBuilderScenesDescriptionsListener  : public MapBooBuilderScenesDescriptionsListener {
public:
  void onDownload(std::vector<MapBooSceneDescription*>* scenesDescriptions) {
    const int scenesCount = scenesDescriptions->size();
    for (int i = 0; i < scenesCount; i++) {
      MapBooSceneDescription* sceneDescription = scenesDescriptions->at(i);
      ILogger::instance()->logInfo("%s", sceneDescription->description().c_str());
    }

    for (int i = 0; i < scenesCount; i++) {
      delete scenesDescriptions->at(i);
    }

    delete scenesDescriptions;
  }

  void onError() {
    ILogger::instance()->logError("Error downloading ScenesDescriptions");
  }

};


- (void) initWithMapBooBuilder
{
  MapBooSceneChangeListener* sceneListener = NULL;
  const bool useWebSockets = true;

  _g3mcBuilder =  new MapBooBuilder_iOS([self G3MWidget],
                                      URL("http://192.168.0.103:8080/g3mc-server", false),
                                      URL("ws://192.168.0.103:8888/tube", false),
                                      useWebSockets,
                                      "2g59wh610g6c1kmkt0l",
                                      sceneListener);

  //_g3mcBuilder->requestScenesDescriptions(new TestMapBooBuilderScenesDescriptionsListener(), true);

  _g3mcBuilder->initializeWidget();
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
//  PlanetRenderer* planetRenderer = [self createPlanetRenderer: [self createPlanetRendererParameters]
//                                               layerSet: [self createLayerSet]];
//  mainRenderer->addRenderer(planetRenderer);
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

- (void) initWithDefaultBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);
  builder.initializeWidget();
}



- (void)  initializeElevationDataProvider: (G3MBuilder_iOS&) builder
{
  float verticalExaggeration = 6.0f;
  builder.getPlanetRendererBuilder()->setVerticalExaggeration(verticalExaggeration);

  //ElevationDataProvider* elevationDataProvider = NULL;
  //builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);


  //  ElevationDataProvider* elevationDataProviderACorunia;
  //  elevationDataProviderACorunia = new SingleBillElevationDataProvider(URL("file:///MDT200-A_CORUNIA.bil", false),
  //                                                                      Sector::fromDegrees(42.4785417976084858, -9.3819593635107914,
  //                                                                                          43.8317114006282011, -7.6284544428640784),
  //                                                                      Vector2I(968, 747));
  //
  //  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProviderACorunia);

  ElevationDataProvider* elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
                                                                                     Sector::fullSphere(),
                                                                                     Vector2I(2048, 1024));
  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);


  //  elevationDataProvider = new WMSBillElevationDataProvider(URL("http://data.worldwind.arc.nasa.gov/elev", false),
  //                                                           Sector::fullSphere());
  //  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);

  //  elevationDataProvider = new WMSBillElevationDataProvider(URL("http://igosoftware.dyndns.org:8080/geoserver/wms", false),
  //                                                           "igo:corunia",
  //                                                           Sector::fromDegrees(42.4785417976085213, -9.3819593635107914,
  //                                                                               43.8317114006282011, -7.6284544428641370));
  //  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);

  /*
   //  ElevationDataProvider* elevationDataProvider;
   elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
   Sector::fullSphere(),
   Vector2I(2048, 1024));

   //  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-4096x2048.bil", false),
   //                                                              Sector::fullSphere(),
   //                                                              Vector2I(4096, 2048));

   //  ElevationDataProvider* elevationDataProvider1;
   //  elevationDataProvider1 = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
   //                                                               Sector::fromDegrees(35, -6, 38, -2),
   //                                                               Vector2I(4096, 2048),
   //                                                               0);

   //  ElevationDataProvider* elevationDataProvider2;
   //  elevationDataProvider2 = new SingleBillElevationDataProvider(URL("file:///full-earth-4096x2048.bil", false),
   //                                                              Sector::fullSphere(),
   //                                                              Vector2I(4096, 2048),
   //                                                              0);

   //  ElevationDataProvider* elevationDataProvider3;
   //  elevationDataProvider3 = new SingleBillElevationDataProvider(URL("file:///caceres-2008x2032.bil", false),
   //                                                               Sector::fromDegrees(
   //                                                                                   39.4642996294239623,
   //                                                                                   -6.3829977122432933,
   //                                                                                   39.4829891936013553,
   //                                                                                   -6.3645288909498845
   //                                                                                   ),
   //                                                               Vector2I(2008, 2032),
   //                                                               0);

   //  ElevationDataProvider* elevationDataProvider4;
   //  elevationDataProvider4 = new SingleBillElevationDataProvider(URL("file:///small-caceres.bil", false),
   //                                                               Sector::fromDegrees(
   //                                                                                   39.4642994358225678,
   //                                                                                   -6.3829980000000042,
   //                                                                                   39.4829889999999608,
   //                                                                                   -6.3645291787065954
   //                                                                                   ),
   //                                                               Vector2I(251, 254));

   //  ElevationDataProvider* elevationDataProvider5;
   //  elevationDataProvider5 = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
   //                                                               Sector::fromDegrees(35, -6, 38, -2),
   //                                                               Vector2I(4096, 2048));

   //  ElevationDataProvider* elevationDataProvider6;
   //  elevationDataProvider6 = new SingleBillElevationDataProvider(URL("file:///full-earth-512x512.bil", false),
   //                                                               Sector::fullSphere(),
   //                                                               Vector2I(512, 512),
   //                                                               0);

   //  ElevationDataProvider* elevationDataProvider7;
   //  elevationDataProvider7 = new SingleBillElevationDataProvider(URL("file:///full-earth-256x256.bil", false),
   //                                                               Sector::fullSphere(),
   //                                                               Vector2I(256, 256),
   //                                                               0);

   //    ElevationDataProvider* elevationDataProvider8;
   //    elevationDataProvider8 = new WMSBillElevationDataProvider(URL("http://data.worldwind.arc.nasa.gov/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm30&STYLES=&FORMAT=image/bil&CRS=EPSG:4326&BBOX=-180.0,-90.0,180.0,90.0&WIDTH=10&HEIGHT=10", false),
   //                                                              Sector::fullSphere());




   CompositeElevationDataProvider* compElevationDataProvider = new CompositeElevationDataProvider();
   compElevationDataProvider->addElevationDataProvider(elevationDataProvider);
   //CompositeElevationDataProvider* compElevationDataProvider = new CompositeElevationDataProvider();
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider1);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider1);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider2);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider3);
   //  compElevationDataProvider->addElevationDataProvider(elevationDataProvider4);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider5);
   //elevationDataProvider = compElevationDataProvider;
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider6);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider7);
   //compElevationDataProvider->addElevationDataProvider(elevationDataProvider8);

   compElevationDataProvider->addElevationDataProvider(elevationDataProviderACorunia);

   //  builder.getPlanetRendererBuilder()->setElevationDataProvider(compElevationDataProvider);
   */
}

- (GPUProgramSources) loadDefaultGPUProgramSourcesFromDisk{
  //GPU Program Sources
  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
                                                                 ofType: @"vsh"];
  if (!vertShaderPathname) {
    NSLog(@"Can't load Shader.vsh");
  }
  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
                                                             encoding: NSUTF8StringEncoding
                                                                error: nil] UTF8String]);

  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
                                                                 ofType: @"fsh"];
  if (!fragShaderPathname) {
    NSLog(@"Can't load Shader.fsh");
  }

  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
                                                               encoding: NSUTF8StringEncoding
                                                                  error: nil] UTF8String]);

  return GPUProgramSources("DefaultProgram", vertexSource, fragmentSource);
}

- (GPUProgramSources) loadDefaultGPUProgramSourcesWithName: (NSString*) name{
  //GPU Program Sources
  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: name
                                                                 ofType: @"vsh"];
  if (!vertShaderPathname) {
    NSLog(@"Can't load Shader.vsh");
  }
  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
                                                             encoding: NSUTF8StringEncoding
                                                                error: nil] UTF8String]);

  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: name
                                                                 ofType: @"fsh"];
  if (!fragShaderPathname) {
    NSLog(@"Can't load Shader.fsh");
  }

  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
                                                               encoding: NSUTF8StringEncoding
                                                                  error: nil] UTF8String]);

  return GPUProgramSources([name UTF8String], vertexSource, fragmentSource);
}

- (void) initCustomizedWithBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);

  GEOTileRasterizer* geoTileRasterizer = new GEOTileRasterizer();

  //builder.getPlanetRendererBuilder()->setTileRasterizer(new DebugTileRasterizer());
  builder.getPlanetRendererBuilder()->setTileRasterizer(geoTileRasterizer);

  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  builder.addCameraConstraint(scc);

  builder.setCameraRenderer([self createCameraRenderer]);

  builder.setPlanet(Planet::createEarth());
  //  builder.setPlanet(Planet::createSphericalEarth());

  Color* bgColor = Color::newFromRGBA(0.0f, 0.1f, 0.2f, 1.0f);

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

  [self initializeElevationDataProvider: builder];

  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  builder.getPlanetRendererBuilder()->setPlanetRendererParameters([self createPlanetRendererParameters]);
  builder.getPlanetRendererBuilder()->addVisibleSectorListener(new TestVisibleSectorListener(),
                                                             TimeInterval::fromSeconds(3));

  Renderer* busyRenderer = new BusyMeshRenderer(Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1));

  //  // Busy quad renderer
  //  NSString* fn = [NSString stringWithCString: "horizontal-gears.png"
  //                                    encoding: [NSString defaultCStringEncoding]];
  //  UIImage* image = [UIImage imageNamed:fn];
  //  IImage* busyImg =  new Image_iOS(image, NULL);
  //  Renderer* busyRenderer = new BusyQuadRenderer(busyImg,
  //                                                Color::newFromRGBA(0.0, 0.0, 0.0, 1.0),
  //                                                Vector2D(250,194),
  //                                                false);

  builder.setBusyRenderer(busyRenderer);

  //    DummyRenderer* dum = new DummyRenderer();
  //    builder->addRenderer(dum);
  //    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
  //    builder->addRenderer(spr);


  ShapesRenderer* shapesRenderer = [self createShapesRenderer: builder.getPlanet()];
  builder.addRenderer(shapesRenderer);

  MeshRenderer* meshRenderer = new MeshRenderer();
  builder.addRenderer( meshRenderer );

  MarksRenderer* marksRenderer = [self createMarksRenderer];
  builder.addRenderer(marksRenderer);

  GEORenderer* geoRenderer = [self createGEORendererMeshRenderer: meshRenderer
                                                  shapesRenderer: shapesRenderer
                                                   marksRenderer: marksRenderer
                                               geoTileRasterizer: geoTileRasterizer];
  builder.addRenderer(geoRenderer);


  //  [self createInterpolationTest: meshRenderer];

  meshRenderer->addMesh([self createPointsMesh: builder.getPlanet() ]);

  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
                                                                     geoRenderer: geoRenderer
                                                                    meshRenderer: meshRenderer];
  builder.setInitializationTask(initializationTask, true);

  PeriodicalTask* periodicalTask = [self createSamplePeriodicalTask: &builder];
  builder.addPeriodicalTask(periodicalTask);

  const bool logFPS = true;
  builder.setLogFPS(logFPS);

  const bool logDownloaderStatistics = false;
  builder.setLogDownloaderStatistics(logDownloaderStatistics);

  GPUProgramSources sources = [self loadDefaultGPUProgramSourcesFromDisk];
  builder.addGPUProgramSources(sources);

  GPUProgramSources sourcesDefault = [self loadDefaultGPUProgramSourcesWithName:@"Default"];
  builder.addGPUProgramSources(sourcesDefault);

  GPUProgramSources sourcesBillboard = [self loadDefaultGPUProgramSourcesWithName:@"Billboard"];
  builder.addGPUProgramSources(sourcesBillboard);

  GPUProgramSources sourcesFlatColorMesh = [self loadDefaultGPUProgramSourcesWithName:@"FlatColorMesh"];
  builder.addGPUProgramSources(sourcesFlatColorMesh);

  GPUProgramSources sourcesTexturedMesh = [self loadDefaultGPUProgramSourcesWithName:@"TexturedMesh"];
  builder.addGPUProgramSources(sourcesTexturedMesh);

  GPUProgramSources sourcesColorMesh = [self loadDefaultGPUProgramSourcesWithName:@"ColorMesh"];
  builder.addGPUProgramSources(sourcesColorMesh);

  GPUProgramSources sourcesTCTexturedMesh = [self loadDefaultGPUProgramSourcesWithName:@"TransformedTexCoorTexturedMesh"];
  builder.addGPUProgramSources(sourcesTCTexturedMesh);

  GPUProgramSources sourcesTexturedMeshPointLight = [self loadDefaultGPUProgramSourcesWithName:@"TexturedMesh+PointLight"];
  builder.addGPUProgramSources(sourcesTexturedMeshPointLight);

  //  WidgetUserData* userData = NULL;
  //  builder.setUserData(userData);

  // initialization
  builder.initializeWidget();
}

- (void)createInterpolationTest: (MeshRenderer*) meshRenderer
{

  const Planet* planet = Planet::createEarth();

  Interpolator* interpolator = new BilinearInterpolator();

  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          planet,
                                          Geodetic2D::zero());
  FloatBufferBuilderFromColor colors;


  //  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::firstVertex(),
  //                                             Vector3D::zero());
  //  FloatBufferBuilderFromColor colors;

  const Sector sector = Sector::fromDegrees(-34, -58,
                                            -32, -57);

  const double a = 2;
  //  const double valueSW = 45000 * a;
  //  const double valueSE = 45000 * a;
  //  const double valueNE = 45000 * a;
  //  const double valueNW = 45000 * a;
  const double heightSW = 10000 * a;
  const double heightSE = 20000 * a;
  const double heightNE = 5000 * a;
  const double heightNW = 45000 * a;
  const double minHeight = heightNE;
  const double maxHeight = heightNW;
  const double deltaHeight = maxHeight - minHeight;


  vertices.add(sector.getSW(), heightSW);  colors.add(1, 0, 0, 1);
  vertices.add(sector.getSE(), heightSE);  colors.add(1, 0, 0, 1);
  vertices.add(sector.getNE(), heightNE);  colors.add(1, 0, 0, 1);
  vertices.add(sector.getNW(), heightNW);  colors.add(1, 0, 0, 1);

  for (double lat = sector._lower._latitude._degrees;
       lat <= sector._upper._latitude._degrees;
       lat += 0.025) {
    const Angle latitude(Angle::fromDegrees(lat));
    for (double lon = sector._lower._longitude._degrees;
         lon <= sector._upper._longitude._degrees;
         lon += 0.025) {

      const Angle longitude(Angle::fromDegrees(lon));
      //      const Geodetic2D position(latitude,
      //                                longitude);

      const double height = interpolator->interpolation(sector._lower,
                                                        sector._upper,
                                                        heightSW,
                                                        heightSE,
                                                        heightNE,
                                                        heightNW,
                                                        latitude,
                                                        longitude);

      const float alpha = (deltaHeight == 0) ? 1 : (float) ((height - minHeight) / deltaHeight);

      vertices.add(latitude, longitude, height);

      colors.add(alpha, alpha, alpha, 1);
    }
  }


  const float lineWidth = 2;
  const float pointSize = 3;
  Color* flatColor = NULL;
  Mesh* mesh = new DirectMesh(GLPrimitive::points(),
                              //GLPrimitive::lineStrip(),
                              true,
                              vertices.getCenter(),
                              vertices.create(),
                              lineWidth,
                              pointSize,
                              flatColor,
                              colors.create());

  meshRenderer->addMesh( mesh );


  delete planet;
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

      vertices.add( lat, lon, 100000 );

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

  cameraRenderer->addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
  //cameraRenderer->addHandler(new CameraZoomAndRotateHandler(processRotation, processZoom));

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

  const bool useOSM = false;
  if (useOSM) {
    layerSet->addLayer( new OSMLayer(TimeInterval::fromDays(30)) );
  }

  //TODO: Check merkator with elevations
  const bool useMapQuestOSM = false;
  if (useMapQuestOSM) {
    layerSet->addLayer( MapQuestLayer::newOSM(TimeInterval::fromDays(30)) );
  }

  const bool useCartoDB = false;
  if (useCartoDB) {
    layerSet->addLayer( new CartoDBLayer("mdelacalle",
                                         "tm_world_borders_simpl_0_3",
                                         TimeInterval::fromDays(30)) );
  }
  const bool useMapQuestOpenAerial = false;
  if (useMapQuestOpenAerial) {
    layerSet->addLayer( MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30)) );
  }

  const bool useMapBox = false;
  if (useMapBox) {
    //const std::string mapKey = "dgd.map-v93trj8v";
    //const std::string mapKey = "examples.map-cnkhv76j";
    const std::string mapKey = "examples.map-qogxobv1";
    layerSet->addLayer( new MapBoxLayer(mapKey, TimeInterval::fromDays(30)) );
  }

  const bool useHere = false;
  if (useHere) {
    layerSet->addLayer( new HereLayer("zrgCx5FrbnlPZWPHuvMO",
                                      "cdJ14wN488Oh5DH6KwQ9GA",
                                      TimeInterval::fromDays(30)) );
  }

  const bool useGoogleMaps = false;
  if (useGoogleMaps) {
    layerSet->addLayer( new GoogleMapsLayer("AIzaSyC9pospBjqsfpb0Y9N3E3uNMD8ELoQVOrc",
                                            TimeInterval::fromDays(30)) );
  }

  const bool useBingMaps = false;
  if (useBingMaps) {
    layerSet->addLayer( new BingMapsLayer(//BingMapType::Road(),
                                          BingMapType::AerialWithLabels(),
                                          //BingMapType::Aerial(),
                                          "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                          TimeInterval::fromDays(30)) );
  }

  const bool useOSMEditMap = false;
  if (useOSMEditMap) {
    // http://d.tiles.mapbox.com/v3/enf.osm-edit-date/4/4/5.png

    std::vector<std::string> subdomains;
    subdomains.push_back("a.");
    subdomains.push_back("b.");
    subdomains.push_back("c.");
    subdomains.push_back("d.");

    MercatorTiledLayer* osmEditMapLayer = new MercatorTiledLayer("osm-edit-map",
                                                                 "http://",
                                                                 "tiles.mapbox.com/v3/enf.osm-edit-date",
                                                                 subdomains,
                                                                 "png",
                                                                 TimeInterval::fromDays(30),
                                                                 true,
                                                                 Sector::fullSphere(),
                                                                 2,
                                                                 11,
                                                                 NULL);
    layerSet->addLayer(osmEditMapLayer);
  }

  const bool blueMarble = false;
  if (blueMarble) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?", false),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        new LevelTileCondition(0, 6),
                                        //NULL,
                                        TimeInterval::fromDays(30),
                                        true,
                                        new LayerTilesRenderParameters(Sector::fullSphere(),
                                                                       2, 4,
                                                                       0, 6,
                                                                       LayerTilesRenderParameters::defaultTileTextureResolution(),
                                                                       LayerTilesRenderParameters::defaultTileMeshResolution(),
                                                                       false)
                                        );
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
                                       TimeInterval::fromDays(30),
                                       true,
                                       new LayerTilesRenderParameters(Sector::fullSphere(),
                                                                      2, 4,
                                                                      0, 12,
                                                                      LayerTilesRenderParameters::defaultTileTextureResolution(),
                                                                      LayerTilesRenderParameters::defaultTileMeshResolution(),
                                                                      false));
    layerSet->addLayer(i3Landsat);
  }

  const bool useOrtoAyto = false;
  if (useOrtoAyto) {
    WMSLayer* ortoAyto = new WMSLayer("orto_refundida,etiquetas_50k,Numeros%20de%20Gobierno,etiquetas%20inicial,etiquetas%2020k,Nombres%20de%20Via,etiquetas%2015k,etiquetas%202k,etiquetas%2010k",
                                      URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false),
                                      WMS_1_1_0,
                                      Sector(Geodetic2D(Angle::fromDegrees(39.350228), Angle::fromDegrees(-6.508713)),
                                             Geodetic2D(Angle::fromDegrees(39.536351), Angle::fromDegrees(-6.25946))),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(3, 20),
                                      //NULL,
                                      TimeInterval::fromDays(30),
                                      false,
                                      new LayerTilesRenderParameters(Sector::fullSphere(),
                                                                     2, 4,
                                                                     0, 20,
                                                                     LayerTilesRenderParameters::defaultTileTextureResolution(),
                                                                     LayerTilesRenderParameters::defaultTileMeshResolution(),
                                                                     false));
    layerSet->addLayer(ortoAyto);
  }

  bool useWMSBing = false;
  if (useWMSBing) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?", false),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        new LevelTileCondition(0, 5),
                                        TimeInterval::fromDays(30),
                                        true);
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
                                  TimeInterval::fromDays(30),
                                  true);
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
                                       TimeInterval::fromDays(30),
                                       true);
    layerSet->addLayer(political);
  }

  bool useOSM_WMS = false;
  if (useOSM_WMS) {
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
                                 TimeInterval::fromDays(30),
                                 true);
    // osm->setEnable(false);

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
                                  TimeInterval::fromDays(30),
                                  true);
    layerSet->addLayer(pnoa);

    class PNOATerrainTouchEventListener : public TerrainTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const TerrainTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        printf ("PNOA touched. Feature info = %s\n", url.getPath().c_str());

        return true;
      }
    };

    pnoa->addTerrainTouchEventListener(new PNOATerrainTouchEventListener());
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
                                  TimeInterval::fromDays(30),
                                  true);
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

  if (false) {
    WMSLayer* catastro = new WMSLayer("catastro", //
                                      URL("http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx", false), //
                                      WMS_1_1_0, //
                                      Sector::fromDegrees(26.275479, -18.409639, 44.85536, 5.225974),
                                      "image/png", //
                                      "EPSG:4326", //
                                      "", //
                                      true, //
                                      NULL, //
                                      TimeInterval::fromDays(30),
                                      true);

    class CatastroTerrainTouchEventListener : public TerrainTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const TerrainTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        ILogger::instance()->logInfo("%s", url.getPath().c_str());

        return true;
      }
    };

    catastro->addTerrainTouchEventListener(new CatastroTerrainTouchEventListener());

    layerSet->addLayer(catastro);
  }

  if (true) {
    WMSLayer* bing = LayerBuilder::createBingLayer(true);
    layerSet->addLayer(bing);
  }

  if (false) {
    WMSLayer* temp = new WMSLayer("temp",
                                  URL("http://wms.openweathermap.org/service", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/png",
                                  "EPSG:4326",
                                  "",
                                  true,
                                  NULL,
                                  TimeInterval::zero(),
                                  true);
    layerSet->addLayer(temp);

    class TempTerrainTouchEventListener : public TerrainTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const TerrainTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        printf ("touched Temperature. Feature info = %s\n", url.getPath().c_str());

        return true;
      }
    };

    temp->addTerrainTouchEventListener(new TempTerrainTouchEventListener());
  }


  return layerSet;
}

- (TilesRenderParameters*) createPlanetRendererParameters
{
  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;

  return new TilesRenderParameters(renderDebug,
                                   useTilesSplitBudget,
                                   forceFirstLevelTilesRenderOnStart,
                                   incrementalTileQuality);
}

- (PlanetRenderer*) createPlanetRenderer: (TilesRenderParameters*) parameters
                            layerSet: (LayerSet*) layerSet
{
  PlanetRendererBuilder* trBuilder = new PlanetRendererBuilder();
  trBuilder->setShowStatistics(false);
  trBuilder->setPlanetRendererParameters(parameters);
  trBuilder->setLayerSet(layerSet);

  PlanetRenderer* planetRenderer = trBuilder->create();

  return planetRenderer;
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

- (ShapesRenderer*) createShapesRenderer: (const Planet*) planet
{
  ShapesRenderer* shapesRenderer = new ShapesRenderer();
  Shape* quad1 = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                              Angle::fromDegrees(-122),
                                              8000),
                               URL("file:///g3m-marker.png", false),
                               50000, 50000);
  shapesRenderer->addShape(quad1);

  Shape* quad2 = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                              Angle::fromDegrees(-123),
                                              8000),
                               35000, 75000,
                               Color::newFromRGBA(1, 0, 1, 0.5));
  shapesRenderer->addShape(quad2);

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

  //  const URL textureURL("file:///world.jpg", false);

  //const Vector3D radius(50000, 50000, 50000);
  //  const double factor = 80;
  //  const Vector3D radius(6378137.0 / factor, 6378137.0 / factor, 6356752.314245 / factor);
  //
  //  Shape* ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(41),
  //                                                        Angle::fromDegrees(-121),
  //                                                        radius._x * 1.1),
  //                                         URL("file:///world.jpg", false),
  //                                         radius,
  //                                         32,
  //                                         0,
  //                                         false,
  //                                         false
  //                                         //Color::newFromRGBA(0,    0.5, 0.8, 0.5),
  //                                         //Color::newFromRGBA(0, 0.75, 0, 0.75)
  //                                         );
  //  shapesRenderer->addShape(ellipsoid1);
  //
  //  Shape* mercator1 = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(41),
  //                                                       Angle::fromDegrees(-119),
  //                                                       radius._x * 1.1),
  //                                        URL("file:///mercator_debug.png", false),
  //                                        radius,
  //                                        32,
  //                                        0,
  //                                        false,
  //                                        true
  //                                        //Color::newFromRGBA(0.5,    0.0, 0.8, 0.5),
  //                                        //Color::newFromRGBA(0, 0.75, 0, 0.75)
  //                                        );
  //  shapesRenderer->addShape(mercator1);
  //
  //  Shape* mercator2 = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(41),
  //                                                       Angle::fromDegrees(-117),
  //                                                       radius._x * 1.1),
  //                                        URL("file:///mercator.jpg", false),
  //                                        radius,
  //                                        32,
  //                                        0,
  //                                        true,
  //                                        true
  //                                        //Color::newFromRGBA(0.5,    0.0, 0.8, 0.5),
  //                                        //Color::newFromRGBA(0, 0.75, 0, 0.75)
  //                                        );
  //  shapesRenderer->addShape(mercator2);

  //  Shape* colored = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(41),
  //                                                     Angle::fromDegrees(-115),
  //                                                     radius._x * 1.1),
  //                                      radius,
  //                                      16,
  //                                      1,
  //                                      true,
  //                                      Color::newFromRGBA(1, 1, 0, 0.75),
  //                                      Color::newFromRGBA(0, 0, 0, 1)
  //                                      );
  //  shapesRenderer->addShape(colored);

  //  // to test layout::splitOverCircle
  //  Geodetic3D* center = new Geodetic3D(Angle::fromDegrees(40.429701),
  //                                      Angle::fromDegrees(-3.703766),
  //                                      0);
  //  double radius = 5e4;
  //  Vector3D radiusVector(radius, radius, radius);
  //  Shape* centralSphere = new EllipsoidShape(center,
  //                                      radiusVector,
  //                                      8,
  //                                      1,
  //                                      false,
  //                                      false,
  //                                      Color::newFromRGBA(0.8, 0, 0, 0.5),
  //                                      Color::newFromRGBA(0, 0, 0, 0.5)
  //                                      );
  //  shapesRenderer->addShape(centralSphere);
  //  int splits = 5;
  //  std::vector<Geodetic3D*> spheres3D = LayoutUtils::splitOverCircle(planet, *center, 1e6, splits);
  //  for (int i=0; i<splits; i++) {
  //    Shape* sphere = new EllipsoidShape(spheres3D[i],
  //                                       radiusVector,
  //                                       8,
  //                                       1,
  //                                       false,
  //                                       false,
  //                                       Color::newFromRGBA(0.0, 0.8, 0, 0.5),
  //                                       Color::newFromRGBA(0, 0, 0, 0.5)
  //                                       );
  //    shapesRenderer->addShape(sphere);
  //  }
  //  std::vector<Geodetic2D*> spheres2D = LayoutUtils::splitOverCircle(planet, center->asGeodetic2D(), 1e6, splits, Angle::fromDegrees(36));
  //  for (int i=0; i<splits; i++) {
  //    Geodetic3D* centerSplit = new Geodetic3D(*spheres2D[i], 0);
  //    delete spheres2D[i];
  //    Shape* sphere = new EllipsoidShape(centerSplit,
  //                                       radiusVector,
  //                                       8,
  //                                       1,
  //                                       false,
  //                                       false,
  //                                       Color::newFromRGBA(0.8, 0.8, 0, 0.5),
  //                                       Color::newFromRGBA(0, 0, 0, 0.5)
  //                                       );
  //    shapesRenderer->addShape(sphere);
  //  }

  Image_iOS *image1 = new Image_iOS([[UIImage alloc] initWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Icon-72" ofType:@"png"]], NULL);

  Image_iOS *image2 = new Image_iOS([[UIImage alloc] initWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Default-Landscape" ofType:@"png"]], NULL);

  std::vector<const IImage*> images;
  images.push_back(image2);
  images.push_back(image1);

  std::vector<RectangleF *> srcRs;
  srcRs.push_back(new RectangleF(0,0,1024,748));
  srcRs.push_back(new RectangleF(0, 0, 72, 72));

  std::vector<RectangleF *> destRs;
  destRs.push_back(new RectangleF(0,0,256,256));
  destRs.push_back(new RectangleF(0, 128, 64, 64));

  class QuadListener: public IImageListener {
    ShapesRenderer* _sr;
  public:

    QuadListener(ShapesRenderer* sr):_sr(sr) {

    }

    void imageCreated(IImage* image) {


      Shape* quadImages = new QuadShape(new Geodetic3D(Angle::fromDegrees(28.410728),
                                                       Angle::fromDegrees(-16.339417),
                                                       8000),
                                        image,
                                        49000, 38000);

      _sr->addShape(quadImages);
    }
  };


  IImageUtils::combine(Vector2I(256,256),
                       images,
                       srcRs,
                       destRs,
                       new QuadListener(shapesRenderer), true);

  for (int i = 0; i < 2; i++) {
    delete images[i];
    delete srcRs[i];
    delete destRs[i];
  }

  return shapesRenderer;
}


class SampleSymbolizer : public GEOSymbolizer {
private:
  
//  GEOLine2DStyle createLineStyle(const GEOGeometry* geometry) const {
//    const JSONObject* properties = geometry->getFeature()->getProperties();
//    
//    const std::string type = properties->getAsString("type", "");
//    
//    if (type.compare("Water Indicator") == 0) {
//      return GEOLine2DStyle(Color::fromRGBA(1, 1, 1, 1), 2);
//    }
//    
//    return GEOLine2DStyle(Color::fromRGBA(1, 1, 0, 1), 2);
//  }

  GEO2DLineRasterStyle createPolygonLineRasterStyle(const GEOGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();


//    const Color color = Color::fromRGBA(0.85, 0.85, 0.85, 0.6);
    const int colorIndex = (int) properties->getAsNumber("mapcolor7", 0);

    const Color color = Color::fromRGBA(0.7, 0, 0, 0.5).wheelStep(7, colorIndex).muchLighter().muchLighter();


    float dashLengths[] = {};
    int dashCount = 0;
//    float dashLengths[] = {3, 6};
//    int dashCount = 2;

    return GEO2DLineRasterStyle(color,
                                2,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

  GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(const GEOGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const int colorIndex = (int) properties->getAsNumber("mapcolor7", 0);

    const Color color = Color::fromRGBA(0.7, 0, 0, 0.5).wheelStep(7, colorIndex);

    return GEO2DSurfaceRasterStyle( color );

//    return GEO2DSurfaceRasterStyle(Color::transparent());
  }

  GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const std::string type = properties->getAsString("type", "");

    float dashLengths[] = {1, 12};
    int dashCount = 2;
//    float dashLengths[] = {};
//    int dashCount = 0;

    if (type.compare("Water Indicator") == 0) {
      return GEO2DLineRasterStyle(Color::fromRGBA(1, 1, 1, 0.9),
                                  8,
                                  CAP_ROUND,
                                  JOIN_ROUND,
                                  1,
                                  dashLengths,
                                  dashCount,
                                  0);
    }

    return GEO2DLineRasterStyle(Color::fromRGBA(1, 1, 0, 0.9),
                                8,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

  CircleShape* createCircleShape(const GEO2DPointGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double population = properties->getAsNumber("population", 0);

    const IMathUtils* mu = IMathUtils::instance();

    const double area = population * 1200;
    const float radius = (float) mu->sqrt( area / PI );
    Color* color = Color::newFromRGBA(1, 1, 0, 1);

    return new CircleShape(new Geodetic3D(geometry->getPosition(), 200),
                           radius,
                           color);
  }

  BoxShape* createBoxShape(const GEO2DPointGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double population = properties->getAsNumber("population", 0);

    const double boxExtent = 50000;
    const double baseArea = boxExtent*boxExtent;
    const double volume = population * boxExtent * 3500;
    const double height = volume / baseArea;

    return new BoxShape(new Geodetic3D(geometry->getPosition(), 0),
                        Vector3D(boxExtent, boxExtent, height),
                        1,
                        Color::newFromRGBA(1, 1, 0, 1),
                        Color::newFromRGBA(0.1, 0.1, 0, 1));
  }

  Mark* createMark(const GEO2DPointGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const std::string label = properties->getAsString("name", "");

    if (label.compare("") != 0) {
      double scalerank = properties->getAsNumber("scalerank", 0);

      //      const double population = properties->getAsNumber("population", 0);
      //
      //      const double boxExtent = 50000;
      //      const double baseArea = boxExtent*boxExtent;
      //      const double volume = population * boxExtent * 3500;
      //      const double height = (volume / baseArea) * 0.7;
      const double height = 1000;

      return new Mark(label,
                      Geodetic3D(geometry->getPosition(), height),
                      0,
                      25 + (scalerank * -3) );
    }

    return NULL;
  }


public:

  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

    const GEO2DLineRasterStyle    lineStyle    = createPolygonLineRasterStyle(geometry);
    const GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

    const std::vector<GEO2DPolygonData*>* polygonsData = geometry->getPolygonsData();
    const int polygonsDataSize = polygonsData->size();

    for (int i = 0; i < polygonsDataSize; i++) {
      GEO2DPolygonData* polygonData = polygonsData->at(i);
      symbols->push_back( new GEORasterPolygonSymbol(polygonData,
                                                     lineStyle,
                                                     surfaceStyle) );

    }

    return symbols;
  }


  std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

    symbols->push_back( new GEORasterPolygonSymbol(geometry->getPolygonData(),
                                                   createPolygonLineRasterStyle(geometry),
                                                   createPolygonSurfaceRasterStyle(geometry)) );

    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    
//    symbols->push_back( new GEOLine2DMeshSymbol(geometry->getCoordinates(),
//                                                createLineStyle(geometry),
//                                                30000) );

    symbols->push_back( new GEORasterLineSymbol(geometry->getCoordinates(),
                                                createLineRasterStyle(geometry)) );

    return symbols;
  }


  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

//    symbols->push_back( new GEOMultiLine2DMeshSymbol(geometry->getCoordinatesArray(),
//                                                     createLineStyle(geometry)) );

    symbols->push_back( new GEOMultiLineRasterSymbol(geometry->getCoordinatesArray(),
                                                     createLineRasterStyle(geometry)) );

    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

    //symbols->push_back( new GEOShapeSymbol( createCircleShape(geometry) ) );

    symbols->push_back( new GEOShapeSymbol( createBoxShape(geometry) ) );

    Mark* mark = createMark(geometry);
    if (mark != NULL) {
      symbols->push_back( new GEOMarkSymbol(mark) );
    }

    return symbols;
  }

};


- (GEORenderer*) createGEORendererMeshRenderer: (MeshRenderer*) meshRenderer
                                shapesRenderer: (ShapesRenderer*) shapesRenderer
                                 marksRenderer: (MarksRenderer*) marksRenderer
                             geoTileRasterizer: (GEOTileRasterizer*) geoTileRasterizer
{
  GEOSymbolizer* symbolizer = new SampleSymbolizer();


  GEORenderer* geoRenderer = new GEORenderer(symbolizer,
                                             meshRenderer,
                                             shapesRenderer,
                                             marksRenderer,
                                             geoTileRasterizer);

  return geoRenderer;
}


//class TestElevationDataListener : public IElevationDataListener {
//public:
//  void onData(const Sector& sector,
//              const Vector2I& extent,
//              ElevationData* elevationData) {
//    if (elevationData != NULL) {
//      ILogger::instance()->logInfo("Elevation data for sector=%s", sector.description().c_str());
//      ILogger::instance()->logInfo("%s", elevationData->description().c_str());
//    }
//
//  }
//
//  void onError(const Sector& sector,
//               const Vector2I& extent) {
//
//  }
//};


class Bil16Parser_IBufferDownloadListener : public IBufferDownloadListener {
private:
  ShapesRenderer* _shapesRenderer;
  MeshRenderer*   _meshRenderer;
  const Vector2I  _extent;
  const Sector    _sector;

public:
  Bil16Parser_IBufferDownloadListener(ShapesRenderer* shapesRenderer,
                                      MeshRenderer*   meshRenderer,
                                      const Vector2I& extent,
                                      const Sector& sector) :
  _shapesRenderer(shapesRenderer),
  _meshRenderer(meshRenderer),
  _extent(extent),
  _sector(sector)
  {

  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    const ShortBufferElevationData* elevationData = BilParser::parseBil16(_sector,
                                                                          _extent,
                                                                          buffer);
    delete buffer;

    if (elevationData == NULL) {
      return;
    }

    ILogger::instance()->logInfo("Elevation data=%s", elevationData->description(false).c_str());

    const Planet* planet = Planet::createEarth();

    //    _meshRenderer->addMesh( elevationData->createMesh(planet,
    //                                                      5,
    //                                                      Geodetic3D::fromDegrees(0.02, 0, 0),
    //                                                      2) );

    const float verticalExaggeration = 20.0f;
    const float pointSize = 2.0f;

    //    const Sector subSector = _sector.shrinkedByPercent(0.2f);
    //    //    const Sector subSector = _sector.shrinkedByPercent(0.9f);
    //    //    const Sector subSector = _sector;
    //    //    const Vector2I subResolution(512, 512);
    //    //    const Vector2I subResolution(251*2, 254*2);
    //    const Vector2I subResolution(251*2, 254*2);

    _meshRenderer->addMesh( createSectorMesh(planet,
                                             32,
                                             Sector::fromDegrees(-22, -73,
                                                                 -16, -61),
                                             Color::yellow(),
                                             2) );

    const Sector meshSector = Sector::fromDegrees(-22, -73,
                                                  -16, -61);

    const Vector2I meshResolution(512, 256);


    _meshRenderer->addMesh( elevationData->createMesh(planet,
                                                      verticalExaggeration,
                                                      Geodetic3D::zero(),
                                                      pointSize,
                                                      meshSector,
                                                      meshResolution) );


//    const ElevationData* subElevationData = new SubviewElevationData(elevationData,
//                                                                     meshSector,
//                                                                     meshResolution,
//                                                                     false);
//
//    _meshRenderer->addMesh( subElevationData->createMesh(planet,
//                                                         verticalExaggeration,
//                                                         Geodetic3D::fromDegrees(meshSector._deltaLatitude._degrees + 0.1,
//                                                                                 0,
//                                                                                 0),
//                                                         pointSize) );
//
//    delete subElevationData;



    delete planet;
    delete elevationData;
  }

  void onError(const URL& url) {

  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* data,
                          bool expired) {
  }

};


class RadarParser_BufferDownloadListener : public IBufferDownloadListener {
private:
  ShapesRenderer* _shapesRenderer;

public:
  RadarParser_BufferDownloadListener(ShapesRenderer* shapesRenderer) :
  _shapesRenderer(shapesRenderer)
  {

  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {

    SGShape* radarModel = (SGShape*) SceneJSShapesParser::parseFromBSON(buffer,
                                                                        "http://radar3d.glob3mobile.com/models/",
                                                                        true);

    if (radarModel != NULL) {
      SGNode* node  = radarModel->getNode();

      const int childrenCount = node->getChildrenCount();
      for (int i = 0; i < childrenCount; i++) {
        SGNode* child = node->getChild(i);
        SGMaterialNode* material = (SGMaterialNode*) child;
        material->setBaseColor( NULL );
      }

      //    radarModel->setPosition(Geodetic3D::fromDegrees(0, 0, 0));
      radarModel->setPosition(new Geodetic3D(Angle::zero(), Angle::zero(), 10000));
      //    radarModel->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(25, 47, 16),
      //                                           Angle::fromDegreesMinutesSeconds(-80, 13, 27),
      //                                           10000));
      //radarModel->setScale(10);

      _shapesRenderer->addShape(radarModel);
    }
    delete buffer;
  }

  void onError(const URL& url) {
    printf("Error downloading %s\n", url.getPath().c_str());
  }

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
  }

};


- (GInitializationTask*) createSampleInitializationTask: (ShapesRenderer*) shapesRenderer
                                            geoRenderer: (GEORenderer*) geoRenderer
                                           meshRenderer: (MeshRenderer*) meshRenderer
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;
    GEORenderer*    _geoRenderer;
    MeshRenderer*   _meshRenderer;

    void testRadarModel(const G3MContext* context) {

      context->getDownloader()->requestBuffer(URL("http://radar3d.glob3mobile.com/models/radar.bson", false),
                                              1000000,
                                              TimeInterval::fromDays(1),
                                              true,
                                              new RadarParser_BufferDownloadListener(_shapesRenderer),
                                              true);
    }


  public:
    SampleInitializationTask(G3MWidget_iOS*  iosWidget,
                             ShapesRenderer* shapesRenderer,
                             GEORenderer*    geoRenderer,
                             MeshRenderer*   meshRenderer) :
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer),
    _geoRenderer(geoRenderer),
    _meshRenderer(meshRenderer)
    {

    }

    Mesh* createCameraPathMesh(const G3MContext* context,
                               const Geodetic2D& fromPosition,
                               double fromHeight,
                               const Geodetic2D& toPosition,
                               double toHeight,
                               Color* color) {

      IMathUtils* mu = IMathUtils::instance();

      const double deltaLatInDegrees = fromPosition._latitude._degrees  - toPosition._latitude._degrees;
      const double deltaLonInDegrees = fromPosition._longitude._degrees - toPosition._longitude._degrees;

      const double distanceInDegrees = mu->sqrt((deltaLatInDegrees * deltaLatInDegrees) +
                                                (deltaLonInDegrees * deltaLonInDegrees)  );

      // const double distanceMaxHeight = mu->sqrt((90.0 * 90) + (180 * 180));
      const double distanceInDegreesMaxHeight = 180;

      const double maxHeight = context->getPlanet()->getRadii().axisAverage();

      double middleHeight;
      if (distanceInDegrees >= distanceInDegreesMaxHeight) {
        middleHeight = maxHeight;
      }
      else {
        middleHeight = (distanceInDegrees / distanceInDegreesMaxHeight) * maxHeight;
        //        const double averageHeight = (fromHeight + toHeight) / 2;
        //        if (middleHeight < averageHeight) {
        //          middleHeight = averageHeight;
        //        }
      }
      // const double middleHeight = ((averageHeight * distanceInDegrees) > maxHeight) ? maxHeight : (averageHeight * distanceInDegrees);

      FloatBufferBuilderFromGeodetic vertices(CenterStrategy::noCenter(),
                                              context->getPlanet(),
                                              Vector3D::zero());

      for (double alpha = 0; alpha <= 1; alpha += 0.025) {
        const double height = mu->quadraticBezierInterpolation(fromHeight, middleHeight, toHeight, alpha);

        vertices.add(Geodetic2D::linearInterpolation(fromPosition, toPosition, alpha),
                     height);
      }


      return new DirectMesh(GLPrimitive::lineStrip(),
                            true,
                            vertices.getCenter(),
                            vertices.create(),
                            2,
                            1,
                            color);
    }

    void testCanvas(const IFactory* factory) {

      class MyImageListener : public IImageListener {
      private:
        ShapesRenderer* _shapesRenderer;

      public:
        MyImageListener(ShapesRenderer* shapesRenderer) :
        _shapesRenderer(shapesRenderer)
        {

        }

        void imageCreated(IImage* image) {
          //printf("Created image=%s\n", image->description().c_str());
          //delete image;

          Shape* quad = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                                     Angle::fromDegrees(-121.5),
                                                     8000),
                                      image,
                                      50000, 50000);
          _shapesRenderer->addShape(quad);
        }
      };


      ICanvas* canvas = factory->createCanvas();


      const std::string text = "Hello World!";
      //const GFont font = GFont::serif();
      //const GFont font = GFont::monospaced();
      const GFont font = GFont::sansSerif();

      canvas->setFont(font);

      const Vector2F textExtent = canvas->textExtent(text);


      canvas->initialize(256, 256);

      canvas->setFillColor( Color::fromRGBA(1, 1, 1, 0.75) );
      canvas->fillRoundedRectangle(0, 0, 256, 256, 32);


      canvas->setShadow(Color::black(), 5, 3.5, -3.5);
      canvas->setFillColor( Color::fromRGBA(1, 0, 0, 0.5) );
      canvas->fillRectangle(32, 64, 64, 128);
      canvas->removeShadow();

      canvas->setLineColor( Color::fromRGBA(1, 0, 1, 0.9) );
      canvas->setLineWidth(2.5f);

      const float margin = 1.25f;
      canvas->strokeRoundedRectangle(0 + margin, 0 + margin,
                                     256 - (margin * 2), 256 - (margin * 2),
                                     32);

      canvas->setFillColor( Color::fromRGBA(1, 1, 0, 0.9) );
      canvas->setLineWidth(1.1f);
      canvas->setLineColor( Color::fromRGBA(0, 0, 0, 0.9) );
      canvas->fillAndStrokeRoundedRectangle(128, 16, 64, 64, 8);

      canvas->setFillColor( Color::white() );
      canvas->setShadow(Color::black(), 5, 1, -1);
      canvas->fillText(text,
                       128 - textExtent._x/2,
                       128 - textExtent._y/2);


      canvas->removeShadow();
      canvas->setFillColor(Color::black());
      canvas->fillRectangle(10, 10, 5, 5);


      canvas->createImage(new MyImageListener(_shapesRenderer),
                          true);

      delete canvas;
    }

    void testWebSocket(const G3MContext* context) {

      class WSListener : public IWebSocketListener {
        void onOpen(IWebSocket* ws) {

        }

        void onError(IWebSocket* ws,
                     const std::string& error) {

        }

        void onMesssage(IWebSocket* ws,
                        const std::string& message) {

        }

        void onClose(IWebSocket* ws) {

        }

      };

      const URL wsURL("ws://127.0.0.1:8888/tube/scene/2g59wh610g6c1kmkt0l", false);
      context->getFactory()->createWebSocket(wsURL,
                                             new WSListener(),
                                             true,
                                             true);

    }

    void run(const G3MContext* context) {
      printf("Running initialization Task\n");

      //testWebSocket(context);

      testCanvas(context->getFactory());


      //      const Sector targetSector(Sector::fromDegrees(35, -6, 38, -2));


      //      const Sector targetSector(Sector::fromDegrees(35, -6, 38, -2));


      //      testRadarModel(context);

      _meshRenderer->addMesh( createSectorMesh(context->getPlanet(),
                                               20,
                                               Sector::fromDegrees(35, -6,
                                                                   38, -2),
                                               Color::white(),
                                               2) );

      _meshRenderer->addMesh( createSectorMesh(context->getPlanet(),
                                               20,
                                               Sector::fromDegrees(39.4642996294239623, -6.3829977122432933,
                                                                   39.4829891936013553, -6.3645288909498845),
                                               Color::magenta(),
                                               2) );

      // mesh1
      Angle latFrom(Angle::fromDegreesMinutesSeconds(38, 53, 42.24));
      Angle lonFrom(Angle::fromDegreesMinutesSeconds(-77, 2, 10.92));

      Geodetic2D posFrom(latFrom,
                         lonFrom);

      Geodetic2D posTo(latFrom.add(Angle::fromDegrees(0.75)),
                       lonFrom.add(Angle::fromDegrees(-0.75)));

      double fromHeight = 30000;
      double toHeight   = 2000;
      //      double middleHeight = 60000;

      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom, fromHeight, posTo, toHeight, Color::newFromRGBA(1, 1, 0, 1)));

      // mesh2
      Geodetic2D posFrom2(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                          Angle::fromDegreesMinutesSeconds(-77, 2, 10.92));
      Geodetic2D posTo2(latFrom.add(Angle::fromDegrees(0.75)), lonFrom.add(Angle::fromDegrees(+0.75)));
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom2, 100000, posTo2, toHeight, Color::newFromRGBA(1, 0, 0, 1)));

      // mesh3
      Geodetic2D posTo3(Angle::fromDegrees(37.7658),
                        Angle::fromDegrees(-122.4185));
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom2, 1000000, posTo3, toHeight, Color::newFromRGBA(0, 1, 0, 1)));

      // mesh3a
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom2, fromHeight, posTo3, toHeight, Color::newFromRGBA(0, 1, 0, 1)));

      // mesh4
      Geodetic2D posFrom4(Angle::fromDegrees(-79.687184),
                          Angle::fromDegrees(-81.914062));
      Geodetic2D posTo4(Angle::fromDegrees(73.124945),
                        Angle::fromDegrees(-47.460937));
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom4, fromHeight, posTo4, toHeight, Color::newFromRGBA(0, 0, 1, 1)));

      // mesh5
      Geodetic2D posFrom5(Angle::fromDegrees(39.909736),
                          Angle::fromDegrees(-3.515625));
      Geodetic2D posTo5(Angle::fromDegrees(39.909736),
                        Angle::fromDegrees(-178.945312));
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom5, fromHeight, posTo5, 1000000, Color::newFromRGBA(0, 1, 1, 1)));

      // mesh5a
      _meshRenderer->addMesh(createCameraPathMesh(context, posFrom5, fromHeight, posTo5, toHeight, Color::newFromRGBA(0, 1, 1, 1)));



      //      [_iosWidget setCameraPosition: Geodetic3D(posFrom, 60000)];
      //      [_iosWidget setCameraPitch: Angle::fromDegrees(95)];

      /*
       context->getDownloader()->requestBuffer(URL("file:///full-earth-2048x1024.bil", false),
       1000000,
       TimeInterval::fromDays(30),
       true,
       new Bil16Parser_IBufferDownloadListener(_shapesRenderer,
       _meshRenderer,
       Vector2I(2048, 1024),
       Sector::fullSphere()),
       true);
       */

      //      [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
      //                                                     Geodetic3D(Angle::fromDegrees(37.78333333),
      //                                                                Angle::fromDegrees(-122.41666666666667),
      //                                                                1000000)
      //                                                     //Angle::fromDegrees(45),
      //                                                     //Angle::fromDegrees(30)
      //                                                     );
      // go to Grand Canyon
      [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                     Geodetic3D(Angle::fromDegreesMinutes(36, 6),
                                                                Angle::fromDegreesMinutes(-112, 6),
                                                                25000),
                                                     Angle::zero(),
                                                     Angle::fromDegrees(75)
                                                     );
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

      if (false) {
        NSString *cc3dFilePath = [[NSBundle mainBundle] pathForResource: @"cc3d4326"
                                                                 ofType: @"json"];
        if (cc3dFilePath) {
          NSString *nsCC3dJSON = [NSString stringWithContentsOfFile: cc3dFilePath
                                                           encoding: NSUTF8StringEncoding
                                                              error: nil];
          if (nsCC3dJSON) {
            std::string cc3dJSON = [nsCC3dJSON UTF8String];
            Shape* cc3d = SceneJSShapesParser::parseFromJSON(cc3dJSON,
                                                             "file:///",
                                                             false);
            if (cc3d) {
              cc3d->setPosition(new Geodetic3D(Angle::fromDegrees(39.473641),
                                               Angle::fromDegrees(-6.370732),
                                               500) );
              cc3d->setPitch(Angle::fromDegrees(-90));

              _shapesRenderer->addShape(cc3d);
            }
          }
        }
      }

      if (true) {
        NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
                                                                  ofType: @"bson"];
        if (planeFilePath) {
          NSData* data = [NSData dataWithContentsOfFile: planeFilePath];
          const int length = [data length];
          unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
          [data getBytes: bytes
                  length: length];
          IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);
          if (buffer) {
            Shape* plane = SceneJSShapesParser::parseFromBSON(buffer,
                                                              URL::FILE_PROTOCOL + "textures-A320/",
                                                              false);

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

              /**/
              const double fromDistance = 75000;
              const double toDistance   = 18750;

              // const Angle fromAzimuth = Angle::fromDegrees(-90);
              // const Angle toAzimuth   = Angle::fromDegrees(-90 + 360 + 180);
              const Angle fromAzimuth = Angle::fromDegrees(-90);
              const Angle toAzimuth   = Angle::fromDegrees(270);

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

              delete buffer;
              /* */
            }
          }
        }
      }
      /**/

      /**/

//      NSString* geojsonName = @"geojson/countries";
      NSString* geojsonName = @"geojson/countries-50m";
//      NSString* geojsonName = @"geojson/boundary_lines_land";
//      NSString* geojsonName = @"geojson/cities";
//      NSString* geojsonName = @"geojson/test";

      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: geojsonName
                                                                  ofType: @"geojson"];

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
      /**/


      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
                                                                ofType: @"json"];
      if (planeFilePath) {
        NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
                                                          encoding: NSUTF8StringEncoding
                                                             error: nil];
        if (nsPlaneJSON) {
          std::string planeJSON = [nsPlaneJSON UTF8String];

          Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, URL::FILE_PROTOCOL + "/" , false);

          // Washington, DC
          plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                            Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                            10000) );
          const double scale = 200;
          plane->setScale(scale, scale, scale);
          plane->setPitch(Angle::fromDegrees(90));
          _shapesRenderer->addShape(plane);
          

//          JSONBaseObject* jsonObject = IJSONParser::instance()->parse(planeJSON);
//
//          IByteBuffer* bson = BSONGenerator::generate(jsonObject);
//          printf("%s\n", bson->description().c_str());
//
//          JSONBaseObject* bsonObject = BSONParser::parse(bson);
//          printf("%s\n", bsonObject->description().c_str());
//
//          delete bson;
//
//          delete jsonObject;
//
//          delete bsonObject;
        }
      }


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

  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget],
                                                                         shapesRenderer,
                                                                         geoRenderer,
                                                                         meshRenderer);

  return initializationTask;
}

- (PeriodicalTask*) createSamplePeriodicalTask: (G3MBuilder_iOS*) builder
{
  TrailsRenderer* trailsRenderer = new TrailsRenderer();

  Trail* trail = new Trail(Color::fromRGBA(0, 1, 1, 0.6f),
                           5000);

  Geodetic3D position(Angle::fromDegrees(37.78333333),
                      Angle::fromDegrees(-122.41666666666667),
                      25000);
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
    _lastLatitudeDegrees(lastPosition._latitude._degrees),
    _lastLongitudeDegrees(lastPosition._longitude._degrees),
    _lastHeight(lastPosition._height)
    {
    }

    void run(const G3MContext* context) {
      const double latStep = 2.0 / ((arc4random() % 100) + 50);
      const double lonStep = 2.0 / ((arc4random() % 100) + 50);

      _lastLatitudeDegrees  -= latStep;
      _lastLongitudeDegrees += lonStep;

      _trail->addPosition(Geodetic3D(Angle::fromDegrees(_lastLatitudeDegrees),
                                     Angle::fromDegrees(_lastLongitudeDegrees),
                                     _lastHeight));
    }
  };

  PeriodicalTask* periodicalTask = new PeriodicalTask(TimeInterval::fromSeconds(0.25),
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
