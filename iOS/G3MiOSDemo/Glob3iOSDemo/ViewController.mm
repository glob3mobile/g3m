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
#import <G3MiOSSDK/CameraRotationHandler.hpp>
#import <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#import <G3MiOSSDK/LevelTileCondition.hpp>
#import <G3MiOSSDK/LayerBuilder.hpp>
#import <G3MiOSSDK/TileRendererBuilder.hpp>
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

- (void)  initializeElevationDataProvider: (G3MBuilder_iOS&) builder
{
  float verticalExaggeration = 5.0f;
  builder.getTileRendererBuilder()->setVerticalExaggeration(verticalExaggeration);
  
  int _DGD_working_on_terrain;
  
  ElevationDataProvider* elevationDataProvider = NULL;
  
  //  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
  
  //  ElevationDataProvider* elevationDataProvider;
  
  /*
   elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
   Sector::fullSphere(),
   Vector2I(2048, 1024),
   0);
   */
  
  ElevationDataProvider* elevationDataProvider1;
//  elevationDataProvider1 = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
//                                                               Sector::fromDegrees(35, -6, 38, -2),
//                                                               Vector2I(4096, 2048),
//                                                               0);
  
  ElevationDataProvider* elevationDataProvider2;
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
  
  ElevationDataProvider* elevationDataProvider4;
  //  elevationDataProvider4 = new SingleBillElevationDataProvider(URL("file:///small-caceres.bil", false),
  //                                                              Sector::fromDegrees(
  //                                                                                  39.4642994358225678,
  //                                                                                  -6.3829980000000042,
  //                                                                                  39.4829889999999608,
  //                                                                                  -6.3645291787065954
  //                                                                                  ),
  //                                                              Vector2I(251, 254),
  //                                                              0);
  
  //  ElevationDataProvider* elevationDataProvider5;
  //  elevationDataProvider5 = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
  //                                                               Sector::fromDegrees(35, -6, 38, -2),
  //                                                               Vector2I(4096, 2048),0);
  
  ElevationDataProvider* elevationDataProvider6;
  elevationDataProvider6 = new SingleBillElevationDataProvider(URL("file:///full-earth-512x512.bil", false),
                                                               Sector::fullSphere(),
                                                               Vector2I(512, 512),
                                                               0);
  
  ElevationDataProvider* elevationDataProvider7;
  elevationDataProvider7 = new SingleBillElevationDataProvider(URL("file:///full-earth-256x256.bil", false),
                                                               Sector::fullSphere(),
                                                               Vector2I(256, 256),
                                                               0);
  
  
  
  CompositeElevationDataProvider* compElevationDataProvider = new CompositeElevationDataProvider();
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider1);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider1);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider2);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider3);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider4);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider5);
  compElevationDataProvider->addElevationDataProvider(elevationDataProvider6);
  //compElevationDataProvider->addElevationDataProvider(elevationDataProvider7);
  elevationDataProvider = compElevationDataProvider;
  
  builder.getTileRendererBuilder()->setElevationDataProvider(elevationDataProvider);
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
  
  [self initializeElevationDataProvider: builder];
  
  builder.getTileRendererBuilder()->setLayerSet(layerSet);
  builder.getTileRendererBuilder()->setTileRendererParameters([self createTileRenderParameters]);
  builder.getTileRendererBuilder()->addVisibleSectorListener(new TestVisibleSectorListener(),
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
                                                   marksRenderer: marksRenderer];
  builder.addRenderer(geoRenderer);
  
  
  //  [self createInterpolationTest: meshRenderer];
  
  //meshRenderer->addMesh([self createPointsMesh: builder.getPlanet() ]);
  
  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
                                                                     geoRenderer: geoRenderer
                                                                    meshRenderer: meshRenderer];
  builder.setInitializationTask(initializationTask, true);
  
  PeriodicalTask* periodicalTask = [self createSamplePeriodicalTask: &builder];
  builder.addPeriodicalTask(periodicalTask);
  
  const bool logFPS = false;
  builder.setLogFPS(logFPS);
  
  const bool logDownloaderStatistics = false;
  builder.setLogDownloaderStatistics(logDownloaderStatistics);
  
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
  
  for (double lat = sector.lower().latitude().degrees();
       lat <= sector.upper().latitude().degrees();
       lat += 0.025) {
    const Angle latitude(Angle::fromDegrees(lat));
    for (double lon = sector.lower().longitude().degrees();
         lon <= sector.upper().longitude().degrees();
         lon += 0.025) {
      
      const Angle longitude(Angle::fromDegrees(lon));
      //      const Geodetic2D position(latitude,
      //                                longitude);
      
      const double height = interpolator->interpolation(sector.lower(),
                                                        sector.upper(),
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
  
  const bool useOSM = false;
  if (useOSM) {
    layerSet->addLayer( new OSMLayer(TimeInterval::fromDays(30)) );
  }
  
  //TODO: Check merkator with elevations
  const bool useMapQuestOSM = false;
  if (useMapQuestOSM) {
    layerSet->addLayer( MapQuestLayer::newOSM(TimeInterval::fromDays(30)) );
  }
  
  const bool useMapQuestOpenAerial = false;
  if (useMapQuestOpenAerial) {
    layerSet->addLayer( MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30)) );
  }
  
  const bool useMapBox = false;
  if (useMapBox) {
    layerSet->addLayer( new MapBoxLayer("dgd.map-v93trj8v",
                                        TimeInterval::fromDays(30)) );
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
                                          //BingMapType::AerialWithLabels(),
                                          BingMapType::Aerial(),
                                          "ArtXu2Z-XSlDVCRVtxtYqtIPVR_0qqLcrfsRyZK_ishjUKvTheYBUH9rDDmAPcnj",
                                          TimeInterval::fromDays(30)) );
  }
  
  const bool blueMarble = true;
  if (blueMarble) {
    WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                        URL("http://www.nasa.network.com/wms?", false),
                                        WMS_1_1_0,
                                        Sector::fullSphere(),
                                        "image/jpeg",
                                        "EPSG:4326",
                                        "",
                                        false,
                                        //new LevelTileCondition(0, 6),
                                        NULL,
                                        TimeInterval::fromDays(30),
                                        new LayerTilesRenderParameters(Sector::fullSphere(),
                                                                       2,4,0,6,
                                                                       Vector2I(256,256),
                                                                       LayerTilesRenderParameters::defaultTileMeshResolution(),
                                                                       false));
    layerSet->addLayer(blueMarble);
    
    //    WMSLayer* i3Landsat = new WMSLayer("esat",
    //                                       URL("http://data.worldwind.arc.nasa.gov/wms?", false),
    //                                       WMS_1_1_0,
    //                                       Sector::fullSphere(),
    //                                       "image/jpeg",
    //                                       "EPSG:4326",
    //                                       "",
    //                                       false,
    //                                       new LevelTileCondition(7, 100),
    //                                       TimeInterval::fromDays(30));
    //    layerSet->addLayer(i3Landsat);
  }
  
  const bool useOrtoAyto = true;
  if (useOrtoAyto){
    WMSLayer* ortoAyto = new WMSLayer("orto_refundida",
                                      URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false),
                                      WMS_1_1_0,
                                      Sector(Geodetic2D(Angle::fromDegrees(39.350228), Angle::fromDegrees(-6.508713)),
                                             Geodetic2D(Angle::fromDegrees(39.536351), Angle::fromDegrees(-6.25946))),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      new LevelTileCondition(4, 19),
                                      TimeInterval::fromDays(30),
                                      new LayerTilesRenderParameters(Sector::fullSphere(),
                                                                     2,4,0,19,
                                                                     Vector2I(256,256),
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
                                 TimeInterval::fromDays(30));
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
                                      TimeInterval::fromDays(30));
    
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
  
  return layerSet;
}

- (TilesRenderParameters*) createTileRenderParameters
{
  const bool renderDebug = true;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
  
  return new TilesRenderParameters(renderDebug,
                                   useTilesSplitBudget,
                                   forceFirstLevelTilesRenderOnStart,
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
  
  
  Image_iOS* im = new Image_iOS([[UIImage alloc] initWithContentsOfFile:
                                 [[NSBundle mainBundle] pathForResource: @"g3m-marker" ofType: @"png"]], NULL);
  
  Image_iOS* im2 = new Image_iOS([[UIImage alloc] initWithContentsOfFile:
                                 [[NSBundle mainBundle] pathForResource: @"sand-clock" ofType: @"png"]], NULL);
  std::vector<const IImage*> ims; ims.push_back(im2);
  RectangleI * rectIM = new RectangleI(0,0, im->getWidth(), im->getHeight() / 2);
  std::vector<RectangleI*> sr; sr.push_back(new RectangleI(0,0, im2->getWidth(), im2->getHeight()));
  std::vector<RectangleI*> dr; dr.push_back(new RectangleI(0,0, 256, 256));
  
  
  class MyIImageListener: public IImageListener{
  public:
    MyIImageListener(ShapesRenderer* render):_render(render){
      
    }
    ShapesRenderer* _render;
    void imageCreated(IImage* image){
      
      Shape* quadX = new QuadShape(new Geodetic3D(Angle::fromDegrees(30.136637),
                                                  Angle::fromDegrees(-15.447636),
                                                  8000),
                                   image,
                                   350000, 750000);
      _render->addShape(quadX);
    }
  };
  
  im->combineWith(*rectIM, ims, sr, dr, Vector2I(256,256), new MyIImageListener(shapesRenderer), true);
  

  
  return shapesRenderer;
}


class SampleSymbolizer : public GEOSymbolizer {
private:
  
  GEOLine2DStyle createLineStyle(const GEOGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();
    
    const std::string type = properties->getAsString("type", "");
    
    if (type.compare("Water Indicator") == 0) {
      return GEOLine2DStyle(Color::fromRGBA(1, 1, 0, 1), 4);
    }
    
    return GEOLine2DStyle(Color::fromRGBA(1, 0, 1, 1), 2);
  }
  
  CircleShape* createCircleShape(const GEO2DPointGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();
    
    const double population = properties->getAsNumber("population", 0);
    
    const IMathUtils* mu = IMathUtils::instance();
    
    const double area = population * 1200;
    const float radius = (float) mu->sqrt( area / mu->pi() );
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
  
  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    
    symbols->push_back( new GEOLine2DMeshSymbol(geometry->getCoordinates(),
                                                createLineStyle(geometry),
                                                30000) );
    
    return symbols;
  }
  
  
  std::vector<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();
    
    symbols->push_back( new GEOMultiLine2DMeshSymbol(geometry->getCoordinatesArray(),
                                                     createLineStyle(geometry)) );
    
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
{
  GEOSymbolizer* symbolizer = new SampleSymbolizer();
  
  
  GEORenderer* geoRenderer = new GEORenderer(symbolizer,
                                             meshRenderer,
                                             shapesRenderer,
                                             marksRenderer);
  
  return geoRenderer;
}


//class TestElevationDataListener : public IElevationDataListener {
//public:
//  void onData(const Sector& sector,
//              const Vector2I& resolution,
//              ElevationData* elevationData) {
//    if (elevationData != NULL) {
//      ILogger::instance()->logInfo("Elevation data for sector=%s", sector.description().c_str());
//      ILogger::instance()->logInfo("%s", elevationData->description().c_str());
//    }
//
//  }
//
//  void onError(const Sector& sector,
//               const Vector2I& resolution) {
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
                  IByteBuffer* buffer) {
    
    const ElevationData* elevationData = BilParser::parseBil16(_sector,
                                                               _extent,
                                                               0,
                                                               100,
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
    
    const float verticalExaggeration = 3.0f;
    const float pointSize = 2.0f;
    
    const Sector subSector = _sector.shrinkedByPercent(0.2f);
    //    const Sector subSector = _sector.shrinkedByPercent(0.9f);
    //    const Sector subSector = _sector;
    //    const Vector2I subResolution(512, 512);
    //    const Vector2I subResolution(251*2, 254*2);
    const Vector2I subResolution(251*2, 254*2);
    
    int _DGD_working_on_terrain;
    
    const ElevationData* subElevationDataDecimated = new SubviewElevationData(elevationData,
                                                                              false,
                                                                              subSector,
                                                                              subResolution,
                                                                              true);
    
    _meshRenderer->addMesh( subElevationDataDecimated->createMesh(planet,
                                                                  verticalExaggeration,
                                                                  Geodetic3D::fromDegrees(0.02, 0.02, 0),
                                                                  pointSize) );
    
    
    const ElevationData* subElevationDataNotDecimated = new SubviewElevationData(elevationData,
                                                                                 false,
                                                                                 subSector,
                                                                                 subResolution,
                                                                                 false);
    
    _meshRenderer->addMesh( subElevationDataNotDecimated->createMesh(planet,
                                                                     verticalExaggeration,
                                                                     Geodetic3D::fromDegrees(0.02,
                                                                                             0.02 + (subSector.getDeltaLongitude()._degrees * 1.05),
                                                                                             0),
                                                                     pointSize) );
    
    
    IFloatBuffer* deltaBuffer = IFactory::instance()->createFloatBuffer( subResolution._x * subResolution._y );
    
    IMathUtils *mu = IMathUtils::instance();
    int unusedType = -1;
    for (int x = 0; x < subResolution._x; x++) {
      for (int y = 0; y < subResolution._y; y++) {
        const double height1 = subElevationDataDecimated->getElevationAt(x, y, &unusedType);
        const double height2 = subElevationDataNotDecimated->getElevationAt(x, y, &unusedType);
        
        const int index = ((subResolution._y-1-y) * subResolution._x) + x;
        
        if (mu->isNan(height1) || mu->isNan(height2)){
          deltaBuffer->rawPut(index,  mu->NanF());
        } else{
          deltaBuffer->rawPut(index,  (float) (height1 - height2));
        }
        
      }
    }
    
    ElevationData* deltaElevation = new FloatBufferElevationData(subSector,
                                                                 subResolution,
                                                                 0,
                                                                 deltaBuffer);
    
    _meshRenderer->addMesh( deltaElevation->createMesh(planet,
                                                       verticalExaggeration,
                                                       Geodetic3D::fromDegrees(0.02,
                                                                               0.02 + (subSector.getDeltaLongitude()._degrees * 2.1),
                                                                               100),
                                                       pointSize) );
    
    
    delete deltaElevation;
    
    delete planet;
    delete elevationData;
    
    delete subElevationDataDecimated;
    delete subElevationDataNotDecimated;
    
  }
  
  void onError(const URL& url) {
    
  }
  
  void onCancel(const URL& url) {
    
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* data) {
    
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
    
    Mesh* createSectorMesh(const Planet* planet,
                           const int resolution,
                           const Sector& sector,
                           const Color& color,
                           const int lineWidth) {
      // create vectors
      FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
                                              planet,
                                              sector.getCenter());
      
      // create indices
      ShortBufferBuilder indices;
      
      const int resolutionMinus1 = resolution - 1;
      int indicesCounter = 0;
      
      // compute offset for vertices
      //    const Vector3D sw = planet->toVector3D(sector->getSW());
      //    const Vector3D nw = planet->toVector3D(sector->getNW());
      //    const double offset = nw.sub(sw).length(); // * 1e-3;
      //      const double offset = 5000;
      const double offset = 100;
      
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
                             new Color(color));
      
    }
    
    Mesh* createCameraPathMesh(const G3MContext* context,
                               const Geodetic2D& fromPosition,
                               double fromHeight,
                               const Geodetic2D& toPosition,
                               double toHeight,
                               Color* color) {
      
      IMathUtils* mu = IMathUtils::instance();
      
      const double deltaLatInDegrees = fromPosition.latitude()._degrees  - toPosition.latitude()._degrees;
      const double deltaLonInDegrees = fromPosition.longitude()._degrees - toPosition.longitude()._degrees;
      
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


      canvas->setStrokeColor( Color::fromRGBA(1, 0, 1, 0.9) );
      canvas->setStrokeWidth(2.5f);
      const float margin = 1.25f;
      canvas->strokeRoundedRectangle(0 + margin, 0 + margin,
                                     256 - (margin * 2), 256 - (margin * 2),
                                     32);

      canvas->setFillColor( Color::fromRGBA(1, 1, 0, 0.9) );
      canvas->setStrokeWidth(1.1f);
      canvas->setStrokeColor( Color::fromRGBA(0, 0, 0, 0.9) );
      canvas->fillAndStrokeRoundedRectangle(128, 16, 64, 64, 8);

      int __DGD_working_at_Canvas;

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

    void run(const G3MContext* context) {
      printf("Running initialization Task\n");

      testCanvas(context->getFactory());

//      const Sector targetSector(Sector::fromDegrees(35, -6, 38, -2));

      _meshRenderer->addMesh( createSectorMesh(context->getPlanet(),
                                               20,
                                               Sector::fromDegrees(35, -6, 38, -2),
                                               Color::white(),
                                               2) );
      
      _meshRenderer->addMesh( createSectorMesh(context->getPlanet(),
                                               20,
                                               Sector::fromDegrees(
                                                                   39.4642996294239623,
                                                                   -6.3829977122432933,
                                                                   39.4829891936013553,
                                                                   -6.3645288909498845
                                                                   ),
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
      
      
      //      FloatBufferBuilderFromGeodetic vertices(CenterStrategy::noCenter(),
      //                                              context->getPlanet(),
      //                                              Vector3D::zero());
      //
      //      for (double t = 0; t <= 1; t += 0.1) {
      //        Geodetic2D position( Geodetic2D::linearInterpolation(posFrom, posTo, t) );
      //
      //        const double height = IMathUtils::instance()->quadraticBezierInterpolation(fromHeight, middleHeight, toHeight, t);
      //        vertices.add(position, height);
      //      }
      //
      //      Mesh* mesh = new DirectMesh(GLPrimitive::lineStrip(),
      //                                  true,
      //                                  vertices.getCenter(),
      //                                  vertices.create(),
      //                                  2,
      //                                  1,
      //                                  Color::newFromRGBA(1, 1, 0, 1));
      //
      //      _meshRenderer->addMesh( mesh );
      
      
      
      
      
      
      //      Geodetic3D
      
      //      targetSector.c
      
      
      //       context->getDownloader()->requestBuffer(URL("file:///full-earth-2048x1024.bil", false),
      //       1000000,
      //       TimeInterval::fromDays(30),
      //       new Bil16Parser_IBufferDownloadListener(_shapesRenderer,
      //       _meshRenderer,
      //       Vector2I(2048, 1024),
      //       Sector::fullSphere()),
      //       true);
      
      
      /*
       context->getDownloader()->requestBuffer(//URL("file:///sample_bil16_150x150.bil", false),
       //URL("file:///409_554.bil", false),
       //URL("file:///full-earth-512x512.bil", false),
       URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
       1000000,
       TimeInterval::fromDays(30),
       new Bil16Parser_IBufferDownloadListener(_shapesRenderer,
       _meshRenderer,
       Vector2I(4096, 2048),
       Sector::fromDegrees(35, -6, 38, -2)),
       true);
       */
      
      
      /*
       context->getDownloader()->requestBuffer(URL("file:///caceres-2008x2032.bil", false),
       1000000,
       TimeInterval::fromDays(30),
       new Bil16Parser_IBufferDownloadListener(_shapesRenderer,
       _meshRenderer,
       Vector2I(2008, 2032),
       Sector::fromDegrees(
       39.4642996294239623,
       -6.3829977122432933,
       39.4829891936013553,
       -6.3645288909498845
       )),
       true);
       */
      
      /*
       context->getDownloader()->requestBuffer(URL("file:///small-caceres.bil", false),
       1000000,
       TimeInterval::fromDays(30),
       new Bil16Parser_IBufferDownloadListener(_shapesRenderer,
       _meshRenderer,
       Vector2I(251, 254),
       Sector::fromDegrees(
       39.4642994358225678,
       -6.3829980000000042,
       39.4829889999999608,
       -6.3645291787065954
       )
       ),
       true);
       */
      
      //      [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
      //                                                     Geodetic3D(Angle::fromDegrees(37.78333333),
      //                                                                Angle::fromDegrees(-122.41666666666667),
      //                                                                1000000)
      //                                                     //Angle::fromDegrees(45),
      //                                                     //Angle::fromDegrees(30)
      //                                                     );
      
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
            Shape* cc3d = SceneJSShapesParser::parseFromJSON(cc3dJSON, "file:///");
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
      
      /**/
      if (false) {
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
            Shape* plane = SceneJSShapesParser::parseFromBSON(buffer, URL::FILE_PROTOCOL + "textures-A320/");
            
            
            //      NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
            //                                                                ofType: @"json"];
            /*
             NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
             ofType: @"json"];
             if (planeFilePath) {
             NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
             encoding: NSUTF8StringEncoding
             error: nil];
             if (nsPlaneJSON) {
             std::string planeJSON = [nsPlaneJSON UTF8String];
             Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, "file:///textures-A320/");
             //Shape* plane = SceneJSShapesParser::parse(planeJSON, "file:///textures-citation/");
             */
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
              /* */
            }
          }
        }
      }
      /**/
      
      
      
      
      /**/
      /**/
      //      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/coastline"
      //                                                                  ofType: @"geojson"];
      
      //      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/boundary_lines_land"
      //                                                                  ofType: @"geojson"];
      NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/cities"
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
      /**/
      
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
  
  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget],
                                                                         shapesRenderer,
                                                                         geoRenderer,
                                                                         meshRenderer);
  
  return initializationTask;
}

- (PeriodicalTask*) createSamplePeriodicalTask: (G3MBuilder_iOS*) builder
{
  TrailsRenderer* trailsRenderer = new TrailsRenderer();
  
  Trail* trail = new Trail(50,
                           //Color::yellow(),
                           Color::fromRGBA(0, 1, 1, 0.6f),
                           1000);
  
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
    double _odd;
    
  public:
    TestTrailTask(Trail* trail,
                  Geodetic3D lastPosition) :
    _trail(trail),
    _lastLatitudeDegrees(lastPosition.latitude()._degrees),
    _lastLongitudeDegrees(lastPosition.longitude()._degrees),
    _lastHeight(lastPosition.height()),
    _odd(true)
    {
      
    }
    
    void run(const G3MContext* context) {
      // _lastLatitudeDegrees += 0.025;
      // _lastLongitudeDegrees += 0.025;
      // _lastHeight += 200;
      
      const double latStep = 1.0 / ((arc4random() % 100) + 50);
      const double lonStep = 1.0 / ((arc4random() % 100) + 50);
      
      //      if (_odd) {
      _lastLatitudeDegrees  += latStep;
      _lastLongitudeDegrees += lonStep;
      //      }
      //      else {
      //        _lastLatitudeDegrees  -= latStep;
      //        _lastLongitudeDegrees -= lonStep;
      //      }
      _odd = !_odd;
      
      //      _lastHeight += (arc4random() % 200) - 100;
      
      //      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
      //      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
      
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
