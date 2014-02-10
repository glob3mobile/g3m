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
#import <G3MiOSSDK/SectorTileCondition.hpp>
#import <G3MiOSSDK/AndTileCondition.hpp>
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
#import <G3MiOSSDK/WMSBilElevationDataProvider.hpp>
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
//import <G3MiOSSDK/WMSBilElevationDataProvider.hpp>
#import <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
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
#import <G3MiOSSDK/IWebSocketListener.hpp>
#import <G3MiOSSDK/GPUProgramFactory.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromCartesian3D.hpp>
#import <G3MiOSSDK/Color.hpp>
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
#import <G3MiOSSDK/GenericQuadTree.hpp>
#import <G3MiOSSDK/GEOFeatureCollection.hpp>
#import <G3MiOSSDK/Angle.hpp>
#import <G3MiOSSDK/SectorAndHeightCameraConstrainer.hpp>
#import <G3MiOSSDK/HUDImageRenderer.hpp>
#import <G3MiOSSDK/CartoCSSParser.hpp>
#import <G3MiOSSDK/ColumnCanvasElement.hpp>
#import <G3MiOSSDK/TextCanvasElement.hpp>
#import <G3MiOSSDK/URLTemplateLayer.hpp>
#import <G3MiOSSDK/JSONArray.hpp>
#import <G3MiOSSDK/SceneLighting.hpp>

#import <G3MiOSSDK/HUDRenderer.hpp>
#import <G3MiOSSDK/HUDQuadWidget.hpp>
#import <G3MiOSSDK/HUDAbsolutePosition.hpp>
#import <G3MiOSSDK/HUDRelativePosition.hpp>
#import <G3MiOSSDK/MultiTexturedHUDQuadWidget.hpp>
#import <G3MiOSSDK/HUDAbsoluteSize.hpp>
#import <G3MiOSSDK/HUDRelativeSize.hpp>
#import <G3MiOSSDK/DownloaderImageBuilder.hpp>
#import <G3MiOSSDK/LabelImageBuilder.hpp>
#import <G3MiOSSDK/CanvasImageBuilder.hpp>

#import <G3MiOSSDK/TerrainTouchListener.hpp>
#import <G3MiOSSDK/PlanetRenderer.hpp>
#import <G3MiOSSDK/G3MMeshParser.hpp>

#import <G3MiOSSDK/CoordinateSystem.hpp>
#import <G3MiOSSDK/TaitBryanAngles.hpp>


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
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(),
  //                                          planet,
  //                                          sector._center);
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(planet, sector._center);


  // create indices
  ShortBufferBuilder indices;

  const int resolutionMinus1 = resolution - 1;
  int indicesCounter = 0;

  const double offset = 0;

  // west side
  for (int j = 0; j < resolutionMinus1; j++) {
    const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                       offset);
    vertices->add(g);

    indices.add(indicesCounter++);
  }

  // south side
  for (int i = 0; i < resolutionMinus1; i++) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                       offset);
    vertices->add(g);

    indices.add(indicesCounter++);
  }

  // east side
  for (int j = resolutionMinus1; j > 0; j--) {
    const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                       offset);
    vertices->add(g);

    indices.add(indicesCounter++);
  }

  // north side
  for (int i = resolutionMinus1; i > 0; i--) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0),
                       offset);
    vertices->add(g);

    indices.add(indicesCounter++);
  }

  Mesh* result = new IndexedMesh(GLPrimitive::lineLoop(),
                                 true,
                                 vertices->getCenter(),
                                 vertices->create(),
                                 indices.create(),
                                 lineWidth,
                                 1,
                                 new Color(color),
                                 NULL, //colors
                                 0,    // colorsIntensity
                                 false //depthTest
                                 );

  delete vertices;

  return result;
}


@implementation ViewController

@synthesize G3MWidget;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

//class CameraRollChangerTask : public GTask {
//  G3MWidget* _widget;
//  double _rollInDegrees;
//  double _step;
//
//public:
//  CameraRollChangerTask(G3MWidget* widget) :
//  _widget(widget),
//  _rollInDegrees(0),
//  _step(2)
//  {
//  }
//
//  void run(const G3MContext* context) {
//    if ((_rollInDegrees < -180) ||
//        (_rollInDegrees > 180)) {
//      _step *= -1;
//    }
//    _rollInDegrees += _step;
//
//#warning JM please take a look to setRoll!
//    _widget->setCameraRoll(Angle::fromDegrees(_rollInDegrees));
//  }
//};


- (void)viewDidLoad
{
  [super viewDidLoad];

  // initialize a customized widget without using a builder
  //[[self G3MWidget] initSingletons];
  // [self initWithoutBuilder];

  [self initCustomizedWithBuilder];

  //  [self initWithMapBooBuilder];

  //  [self initWithBuilderAndSegmentedWorld];

  [[self G3MWidget] startAnimation];

  /*
   [[self G3MWidget] widget]->addPeriodicalTask(TimeInterval::fromMilliseconds(100),
   new CameraRollChangerTask([[self G3MWidget] widget]));
   */

}


class MoveCameraInitializationTask : public GInitializationTask {
private:
  G3MWidget_iOS* _iosWidget;
  const Sector   _sector;

public:

  MoveCameraInitializationTask(G3MWidget_iOS* iosWidget,
                               const Sector   sector) :
  _iosWidget(iosWidget),
  _sector(sector)
  {
  }

  void run(const G3MContext* context) {
    //    const std::string cartoCSS = "/* coment */ // comment\n @water: #C0E0F8; [zoom > 1] { line-color:@waterline; line-width:1.6; ::newSymbolizer { line-width:2; } } #world .class [level == 5] { background-color: black; } ";

    //    const std::string cartoCSS = "@water: #ddeeff;\n#lakes[ScaleRank<3][zoom=3],\n#lakes[ScaleRank<4][zoom=4],\n#lakes[ScaleRank<5][zoom=5],\n#lakes[ScaleRank<6][zoom>=6] {\n    polygon-fill:@water;\n    line-color:darken(@water, 20%);\n    line-width:0.3;\n  }\n";

    //    const std::string cartoCSS = "/* coment */ // comment\n @water: #C0E0F8; [zoom > 1] { line-color:@waterline; line-width:1.6; ::newSymbolizer { line-width:2; } } #world .class [level == 5] { background-color: black; } \n@water: #ddeeff;\n#lakes[ScaleRank<3][zoom=3],\n#lakes[ScaleRank<4][zoom=4],\n#lakes[ScaleRank<5][zoom=5],\n#lakes[ScaleRank<6][zoom>=6] {\n    polygon-fill:@water;\n    line-color:darken(@water, 20%);\n    line-width:0.3;\n  }\n.class1.class2{} ::anotherSymbolizer {background-color: black;} * {line-color:white;} ";
    const std::string cartoCSS = "@water: #C0E0F8; #id { a:1; b:2; .class {a:2;} [level > 2] {b:3; [COUNTRY=US][COUNTRY=AR] { d:33;} } }";

    CartoCSSResult* result = CartoCSSParser::parse(cartoCSS);

    if (result->hasError()) {
      std::vector<CartoCSSError> errors = result->getErrors();
      const int errorsSize = errors.size();
      for (int i = 0; i < errorsSize; i++) {
        const CartoCSSError error = errors[i];
        ILogger::instance()->logError("\"%s\" at %d",
                                      error.getDescription().c_str(),
                                      error.getPosition());
      }
    }

    const CartoCSSSymbolizer* symbolizer = result->getSymbolizer();
    if (symbolizer != NULL) {
      ILogger::instance()->logInfo("%s", symbolizer->description(true).c_str());
      delete symbolizer;
    }

    delete result;

    //    Geodetic3D position(Geodetic3D(_sector.getCenter(), 5000));
    //    [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5), position);
    //[_iosWidget widget]->setCameraPosition(position);
  }

  bool isDone(const G3MContext* context) {
    return true;
  }
};


//class ToggleGEORendererTask: public GTask {
//private:
//  GEORenderer* _geoRenderer;
//
//public:
//  ToggleGEORendererTask(GEORenderer* geoRenderer) :
//  _geoRenderer(geoRenderer)
//  {
//
//  }
//
//  void run(const G3MContext* context) {
//    _geoRenderer->setEnable( !_geoRenderer->isEnable() );
//  }
//
//};


- (void) initWithBuilderAndSegmentedWorld
{
  G3MBuilder_iOS builder([self G3MWidget]);

  LayerSet* layerSet = new LayerSet();
  //  layerSet->addLayer(MapQuestLayer::newOSM(TimeInterval::fromDays(30), true, 10));
  layerSet->addLayer(MapQuestLayer::newOSM(TimeInterval::fromDays(30)));
  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);


  //  GEORenderer* geoRenderer = builder.createGEORenderer( new SampleSymbolizer(builder.getPlanet()) );
  //
  //  geoRenderer->loadJSON(URL("file:///geojson/countries-50m.geojson", false),
  //                        new SampleSymbolizer(builder.getPlanet()));

  //  builder.addPeriodicalTask(new PeriodicalTask(TimeInterval::fromSeconds(5),
  //                                               new ToggleGEORendererTask(geoRenderer)));

  //  builder.getPlanetRendererBuilder()->addTileRasterizer(new DebugTileRasterizer());

  //  builder.getPlanetRendererBuilder()->addTileRasterizer(new DebugTileRasterizer(GFont::monospaced(),
  //                                                                                Color::red(),
  //                                                                                false,
  //                                                                                true));
  //  builder.getPlanetRendererBuilder()->addTileRasterizer(new DebugTileRasterizer(GFont::monospaced(),
  //                                                                                Color::yellow(),
  //                                                                                true,
  //                                                                                false));


  //  const Sector sector = Sector::fromDegrees(-17.2605373678851670, 145.4760907919427950,
  //                                            -17.2423142646939311, 145.4950606689779420);
  const Sector sector = Sector::fromDegrees(40.1540143280790858, -5.8664874640814313,
                                            40.3423148480663158, -5.5116079822178570);

  //  final Geodetic2D lower = new Geodetic2D( //
  //                                          Angle.fromDegrees(40.1540143280790858), //
  //                                          Angle.fromDegrees(-5.8664874640814313));
  //  final Geodetic2D upper = new Geodetic2D( //
  //                                          Angle.fromDegrees(40.3423148480663158), //
  //                                          Angle.fromDegrees(-5.5116079822178570));
  //
  //  final Sector demSector = new Sector(lower, upper);

  builder.setShownSector(sector);

  //  builder.setPlanet(Planet::createSphericalEarth());
  //  builder.setPlanet(Planet::createFlatEarth());

  //  int _DIEGO_AT_WORK;
  //  builder.getPlanetRendererBuilder()->setShowStatistics(true);

  //  MeshRenderer* meshRenderer = new MeshRenderer();
  //  builder.addRenderer(meshRenderer);
  //  meshRenderer->addMesh( createSectorMesh(builder.getPlanet(), 32, sector, Color::red(), 2) );

  builder.setInitializationTask(new MoveCameraInitializationTask([self G3MWidget], sector),
                                true);

  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///aus4326.bil", false),
                                                                                    sector,
                                                                                    Vector2I(2083, 2001),
                                                                                    -758.905);

  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);
  builder.getPlanetRendererBuilder()->setVerticalExaggeration(3);

  builder.initializeWidget();
}


//class SampleMapBooApplicationChangeListener : public MapBooApplicationChangeListener {
//public:
//  void onNameChanged(const G3MContext* context,
//                     const std::string& name) {
//    ILogger::instance()->logInfo("MapBoo application name=\"%s\"",
//                                 name.c_str());
//  }
//
//  void onIconChanged(const G3MContext* context,
//                     const std::string& icon) {
//    ILogger::instance()->logInfo("MapBoo application icon=\"%s\"",
//                                 icon.c_str());
//  }
//
//  void onScenesChanged(const G3MContext* context,
//                       const std::vector<MapBoo_Scene*>& scenes) {
//    const int scenesSize = scenes.size();
//    for (int i = 0; i < scenesSize; i++) {
//      ILogger::instance()->logInfo("MapBoo application scene #%d %s",
//                                   i,
//                                   scenes[i]->description().c_str());
//    }
//  }
//
//  void onSceneChanged(const G3MContext* context,
//                      int sceneIndex,
//                      const MapBoo_Scene* scene) {
//    ILogger::instance()->logInfo("MapBoo application current scene=%l",
//                                 sceneIndex);
//  }
//
//  void onWebsiteChanged(const G3MContext* context,
//                        const std::string& website) {}
//
//  void onEMailChanged(const G3MContext* context,
//                      const std::string& eMail) {}
//
//  void onAboutChanged(const G3MContext* context,
//                      const std::string& about) {}
//
//  void onWebSocketOpen(const G3MContext* context) {}
//
//  void onWebSocketClose(const G3MContext* context) {}
//
//  void onTerrainTouch(MapBooBuilder*         builder,
//                      const G3MEventContext* ec,
//                      const Vector2I&        pixel,
//                      const Camera*          camera,
//                      const Geodetic3D&      position,
//                      const Tile*            tile) { }
//};


//- (void) initWithMapBooBuilder
//{
//  MapBooApplicationChangeListener* applicationListener = new SampleMapBooApplicationChangeListener();
//
//  const std::string applicationId = "2glgs5j2mq5i9nxx68q";
//
//  bool enableNotifications = false;
//
//  _g3mcBuilder = new MapBooBuilder_iOS([self G3MWidget],
//                                       URL("http://192.168.1.2:8080/web", false),
//                                       URL("ws://192.168.1.2:8888/tube", false),
//                                       "",
//                                       VIEW_RUNTIME,
//                                       applicationListener,
//                                       enableNotifications);
//
//  _g3mcBuilder->initializeWidget();
//}

- (void) initWithDefaultBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);
  builder.initializeWidget();
}


- (void)  initializeElevationDataProvider: (G3MBuilder_iOS&) builder
{
  float verticalExaggeration = 1.0f;
  builder.getPlanetRendererBuilder()->setVerticalExaggeration(verticalExaggeration);

  //ElevationDataProvider* elevationDataProvider = NULL;
  //builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);


  //  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
  //                                                                                     Sector::fullSphere(),
  //                                                                                     Vector2I(2048, 1024));

  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///caceres-2008x2032.bil", false),
                                                                                    Sector::fromDegrees(                                                                                 39.4642996294239623,                                                                                -6.3829977122432933,                                                                                  39.4829891936013553,-6.3645288909498845),                                                              Vector2I(2008, 2032),0);

  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);
}

//- (GPUProgramSources) loadDefaultGPUProgramSourcesFromDisk{
//  //GPU Program Sources
//  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
//                                                                 ofType: @"vsh"];
//  if (!vertShaderPathname) {
//    NSLog(@"Can't load Shader.vsh");
//  }
//  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
//                                                             encoding: NSUTF8StringEncoding
//                                                                error: nil] UTF8String]);
//
//  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: @"Shader"
//                                                                 ofType: @"fsh"];
//  if (!fragShaderPathname) {
//    NSLog(@"Can't load Shader.fsh");
//  }
//
//  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
//                                                               encoding: NSUTF8StringEncoding
//                                                                  error: nil] UTF8String]);
//
//  return GPUProgramSources("DefaultProgram", vertexSource, fragmentSource);
//}

- (GPUProgramSources) loadDefaultGPUProgramSourcesWithName: (NSString*) name{
  //GPU Program Sources
  NSString* vertShaderPathname = [[NSBundle mainBundle] pathForResource: name
                                                                 ofType: @"vsh"];
  if (!vertShaderPathname) {
    NSLog(@"Can't load %@.vsh", name);
  }
  const std::string vertexSource ([[NSString stringWithContentsOfFile: vertShaderPathname
                                                             encoding: NSUTF8StringEncoding
                                                                error: nil] UTF8String]);

  NSString* fragShaderPathname = [[NSBundle mainBundle] pathForResource: name
                                                                 ofType: @"fsh"];
  if (!fragShaderPathname) {
    NSLog(@"Can't load %@.fsh", name);
  }

  const std::string fragmentSource ([[NSString stringWithContentsOfFile: fragShaderPathname
                                                               encoding: NSUTF8StringEncoding
                                                                  error: nil] UTF8String]);

  return GPUProgramSources([name UTF8String], vertexSource, fragmentSource);
}

class TestMeshLoadListener : public MeshLoadListener {
public:
  void onBeforeAddMesh(Mesh* mesh) {
  }

  void onAfterAddMesh(Mesh* mesh) {
  }

};

- (void) initCustomizedWithBuilder
{


  G3MBuilder_iOS builder([self G3MWidget]);

  GEOTileRasterizer* geoTileRasterizer = new GEOTileRasterizer();

  //builder.getPlanetRendererBuilder()->addTileRasterizer(new DebugTileRasterizer());
  builder.getPlanetRendererBuilder()->addTileRasterizer(geoTileRasterizer);
  builder.getPlanetRendererBuilder()->setShowStatistics(false);

  //  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  //  builder.addCameraConstraint(scc);

  builder.setCameraRenderer([self createCameraRenderer]);

  const Planet* planet = Planet::createEarth();
  //const Planet* planet = Planet::createSphericalEarth();
  //  const Planet* planet = Planet::createFlatEarth();
  builder.setPlanet(planet);

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

  bool useElevations = true;
  if (useElevations) {
    [self initializeElevationDataProvider: builder];
  }

  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  builder.getPlanetRendererBuilder()->setPlanetRendererParameters([self createPlanetRendererParameters]);
  builder.getPlanetRendererBuilder()->addVisibleSectorListener(new TestVisibleSectorListener(),
                                                               TimeInterval::fromSeconds(3));

  // builder.getPlanetRendererBuilder()->addTileRasterizer(new DebugTileRasterizer());

  Renderer* busyRenderer = new BusyMeshRenderer(Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1));
  builder.setBusyRenderer(busyRenderer);

  ShapesRenderer* shapesRenderer = [self createShapesRenderer: builder.getPlanet()];

  builder.addRenderer(shapesRenderer);

  MeshRenderer* meshRenderer = new MeshRenderer();
  builder.addRenderer( meshRenderer );

  if (false) { //Testing Reference System

    //Test Plane
    Plane plane(Vector3D(0.0,1.0,1.0), 0.0);
    Vector3D vectorInPlane(0,0,1);
    Vector3D axis(1.0,0.0,0.0);

    for (int i = 0; i <= 90; i++){
      Vector3D v2 = vectorInPlane.rotateAroundAxis(axis, Angle::fromDegrees(-i));
      Angle angle = plane.vectorRotationForAxis(v2, axis);
    }

    //CoordinateSystem sr = CoordinateSystem::global();
    double lat = 28.96384553643802, lon = -13.60974902228918;
    CoordinateSystem sr = planet->getCoordinateSystemAt(Geodetic3D::fromDegrees(lat, lon, 0));

    //Heading
    CoordinateSystem sr2 = sr.applyTaitBryanAngles(TaitBryanAngles::fromDegrees(170, 80, 10))
    .changeOrigin(planet->toCartesian(Geodetic3D::fromDegrees(lat, lon, 1.5e4)));

    meshRenderer->addMesh( sr.createMesh(1e4, Color::red(), Color::green(), Color::blue()));
    meshRenderer->addMesh( sr2.createMesh(1e4, Color::red(), Color::green(), Color::blue()));

    TaitBryanAngles tba = sr2.getTaitBryanAngles(sr);
    printf("ANGLES: %f, %f, %f\n", tba._heading._degrees, tba._pitch._degrees, tba._roll._degrees);

  }


  //  meshRenderer->loadJSONPointCloud(URL("file:///pointcloud/points.json"),
  //                                   10,
  //                                   new TestMeshLoadListener(),
  //                                   true);
  //  meshRenderer->loadJSONPointCloud(URL("file:///pointcloud/matterhorn.json"),
  //                                   2,
  //                                   0,
  //                                   new TestMeshLoadListener(),
  //                                   true);

  //  void testMeshLoad(const G3MContext* context) {
  //    context->getDownloader()->requestBuffer(URL("file:///isosurface-mesh.json"),
  //                                            100000, //  priority,
  //                                            TimeInterval::fromDays(30),
  //                                            true,
  //                                            new ParseMeshBufferDownloadListener(_meshRenderer, _planet),
  //                                            true);
  //  }
  //  meshRenderer->loadJSONMesh(URL("file:///isosurface-mesh.json"),
  //                             Color::newFromRGBA(1, 1, 0, 1));

  meshRenderer->showNormals(true); //SHOWING NORMALS

  MarksRenderer* marksRenderer = [self createMarksRenderer];
  builder.addRenderer(marksRenderer);

  GEORenderer* geoRenderer = [self createGEORendererMeshRenderer: meshRenderer
                                                  shapesRenderer: shapesRenderer
                                                   marksRenderer: marksRenderer
                                               geoTileRasterizer: geoTileRasterizer
                                                          planet: builder.getPlanet()];
  builder.addRenderer(geoRenderer);

  //Showing light directions
  if (false){
    CameraFocusSceneLighting* light = new CameraFocusSceneLighting(Color::fromRGBA(0.3, 0.3, 0.3, 1.0),
                                                                   Color::fromRGBA(1.0, 1.0, 1.0, 1.0));
    light->setLightDirectionsMeshRenderer(meshRenderer);
    builder.setSceneLighting(light);
  }

  if (false) { //HUD

#warning Diego at work!


    HUDRenderer* hudRenderer = new HUDRenderer();
    builder.setHUDRenderer(hudRenderer);


    class AltimeterCanvasImageBuilder : public CanvasImageBuilder {
    private:
      float _altitude = 38500;
      float _step     = 100;

    protected:
      void buildOnCanvas(const G3MContext* context,
                         ICanvas* canvas) {
        const float width  = canvas->getWidth();
        const float height = canvas->getHeight();

        canvas->setFillColor(Color::fromRGBA(0, 0, 0, 0.5));
        canvas->fillRectangle(0, 0, width, height);

        canvas->setFillColor(Color::white());


        const IStringUtils* su = context->getStringUtils();

        int altitude = _altitude;

        canvas->setFont(GFont::monospaced(32));
        for (int y = 0; y <= height; y += 16) {
          if ((y % 80) == 0) {
            canvas->fillRectangle(0, y-1.5f, width/6.0f, 3);

            const std::string label = su->toString(altitude);
            const Vector2F labelExtent = canvas->textExtent(label);
            canvas->fillText(label,
                             width/6.0f * 1.25f,
                             y - labelExtent._y/2);

            altitude -= 100;
          }
          else {
            canvas->fillRectangle(0, y-0.5f, width/8.0f, 1);
          }
        }

        canvas->setLineColor(Color::white());
        canvas->setLineWidth(8);
        canvas->strokeRectangle(0, 0, width, height);
      }

    public:
      AltimeterCanvasImageBuilder() :
      CanvasImageBuilder(256, 256*3)
      {
      }

      bool isMutable() const {
        return true;
      }

      void step() {
        _altitude += _step;
        if (_altitude > 40000) {
          _altitude = 40000;
          _step *= -1;
        }
        if (_altitude < 0) {
          _altitude = 0;
          _step *= -1;
        }

        changed();
      }

    };


    AltimeterCanvasImageBuilder* altimeterCanvasImageBuilder = new AltimeterCanvasImageBuilder();
    HUDQuadWidget* test = new HUDQuadWidget(altimeterCanvasImageBuilder,
                                            new HUDRelativePosition(0,
                                                                    HUDRelativePosition::VIEWPORT_WIDTH,
                                                                    HUDRelativePosition::RIGHT,
                                                                    10),
                                            new HUDRelativePosition(0.5,
                                                                    HUDRelativePosition::VIEWPORT_HEIGTH,
                                                                    HUDRelativePosition::MIDDLE),
                                            new HUDRelativeSize(0.22,
                                                                HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                            new HUDRelativeSize(0.66,
                                                                HUDRelativeSize::VIEWPORT_MIN_AXIS)
                                            );
    hudRenderer->addWidget(test);


    LabelImageBuilder* labelBuilder = new LabelImageBuilder("glob3",               // text
                                                            GFont::monospaced(38), // font
                                                            6,                     // margin
                                                            Color::yellow(),       // color
                                                            Color::black(),        // shadowColor
                                                            3,                     // shadowBlur
                                                            1,                     // shadowOffsetX
                                                            -1,                    // shadowOffsetY
                                                            Color::red(),          // backgroundColor
                                                            4,                     // cornerRadius
                                                            true                   // mutable
                                                            );

    HUDQuadWidget* label = new HUDQuadWidget(labelBuilder,
                                             new HUDAbsolutePosition(10),
                                             new HUDAbsolutePosition(10),
                                             new HUDRelativeSize(1, HUDRelativeSize::BITMAP_WIDTH),
                                             new HUDRelativeSize(1, HUDRelativeSize::BITMAP_HEIGTH) );
    hudRenderer->addWidget(label);

    HUDQuadWidget* compass2 = new HUDQuadWidget(//new DownloaderImageBuilder(URL("file:///Compass_rose_browns_00_transparent.png")),
                                                new DownloaderImageBuilder(URL("file:///CompassHeadings.png")),
                                                new HUDRelativePosition(0.5,
                                                                        HUDRelativePosition::VIEWPORT_WIDTH,
                                                                        HUDRelativePosition::CENTER),
                                                new HUDRelativePosition(0.5,
                                                                        HUDRelativePosition::VIEWPORT_HEIGTH,
                                                                        HUDRelativePosition::MIDDLE),
                                                new HUDRelativeSize(0.25,  // 0.5,
                                                                    HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                                new HUDRelativeSize(0.125, // 0.25,
                                                                    HUDRelativeSize::VIEWPORT_MIN_AXIS));
    compass2->setTexCoordsRotation(Angle::fromDegrees(30),
                                   0.5f, 0.5f);
    compass2->setTexCoordsScale(1, 0.5f);
    hudRenderer->addWidget(compass2);

    float visibleFactor = 3;
    HUDQuadWidget* ruler = new HUDQuadWidget(new DownloaderImageBuilder(URL("file:///altimeter-ruler-1536x113.png")),
                                             new HUDRelativePosition(1,
                                                                     HUDRelativePosition::VIEWPORT_WIDTH,
                                                                     HUDRelativePosition::LEFT,
                                                                     10),
                                             new HUDRelativePosition(0.5,
                                                                     HUDRelativePosition::VIEWPORT_HEIGTH,
                                                                     HUDRelativePosition::MIDDLE),
                                             new HUDRelativeSize(2 * (113.0 / 1536.0),
                                                                 HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                             new HUDRelativeSize(2 / visibleFactor,
                                                                 HUDRelativeSize::VIEWPORT_MIN_AXIS),
                                             new DownloaderImageBuilder(URL("file:///widget-background.png")));
    ruler->setTexCoordsScale(1 , 1.0f / visibleFactor);
    hudRenderer->addWidget(ruler);

    //    float visibleFactor = 10; // 85x5100
    //    HUDQuadWidget* ruler = new HUDQuadWidget(new DownloaderImageBuilder(URL("file:///altitude_ladder.png")),
    //                                             new HUDRelativePosition(1,
    //                                                                     HUDRelativePosition::VIEWPORT_WIDTH,
    //                                                                     HUDRelativePosition::LEFT,
    //                                                                     10),
    //                                             new HUDRelativePosition(0.5,
    //                                                                     HUDRelativePosition::VIEWPORT_HEIGTH,
    //                                                                     HUDRelativePosition::MIDDLE),
    //                                             new HUDRelativeSize(8.0f * (85.0f / 5100.0f),
    //                                                                 HUDRelativeSize::VIEWPORT_MIN_AXIS),
    //                                             new HUDRelativeSize(8.0f / visibleFactor,
    //                                                                 HUDRelativeSize::VIEWPORT_MIN_AXIS),
    //                                             new DownloaderImageBuilder(URL("file:///widget-background.png")));
    //    ruler->setTexCoordsScale(1 , 1.0f / visibleFactor);
    //    hudRenderer->addWidget(ruler);


    class AnimateHUDWidgetsTask : public GTask {
    private:
      HUDQuadWidget*     _compass1;
      HUDQuadWidget*     _compass2;
      HUDQuadWidget*     _ruler;
      LabelImageBuilder* _labelBuilder;
      AltimeterCanvasImageBuilder* _altimeterCanvasImageBuilder;

      double _angleInRadians;

      float _translationV;
      float _translationStep;

    public:
      AnimateHUDWidgetsTask(HUDQuadWidget* compass1,
                            HUDQuadWidget* compass2,
                            HUDQuadWidget* ruler,
                            LabelImageBuilder* labelBuilder,
                            AltimeterCanvasImageBuilder* altimeterCanvasImageBuilder) :
      _compass1(compass1),
      _compass2(compass2),
      _ruler(ruler),
      _labelBuilder(labelBuilder),
      _altimeterCanvasImageBuilder(altimeterCanvasImageBuilder),
      _angleInRadians(0),
      _translationV(0),
      _translationStep(0.002)
      {
      }

      void run(const G3MContext* context) {
        _angleInRadians += Angle::fromDegrees(2)._radians;
        //        _labelBuilder->setText( Angle::fromRadians(_angleInRadians).description() );
        double degrees = Angle::fromRadians(_angleInRadians)._degrees;
        while (degrees > 360) {
          degrees -= 360;
        }
        const std::string degreesText = IStringUtils::instance()->toString( IMathUtils::instance()->round( degrees )  );
        _labelBuilder->setText( degreesText );

        //        _compass1->setTexCoordsRotation(_angleInRadians,
        //                                        0.5f, 0.5f);
        _compass2->setTexCoordsRotation(-_angleInRadians,
                                        0.5f, 0.5f);

        //        _compass3->setTexCoordsRotation(Angle::fromRadians(_angle),
        //                                        0.5f, 0.5f);

        if (_translationV > 0.5 || _translationV < 0) {
          _translationStep *= -1;
        }
        _translationV += _translationStep;
        _ruler->setTexCoordsTranslation(0, _translationV);

        _altimeterCanvasImageBuilder->step();
      }
    };

    builder.addPeriodicalTask(new PeriodicalTask(TimeInterval::fromMilliseconds(50),
                                                 new AnimateHUDWidgetsTask(label,
                                                                           compass2,
                                                                           ruler,
                                                                           labelBuilder,
                                                                           altimeterCanvasImageBuilder)));

    if (false){ //Changing FOV


      class AnimatedFOVCameraConstrainer: public ICameraConstrainer {
      private:
        mutable double _angle;
        mutable double _step;
      public:

        AnimatedFOVCameraConstrainer() : _angle(70), _step(1)
        {
        }

        bool onCameraChange(const Planet* planet,
                            const Camera* previousCamera,
                            Camera* nextCamera) const {

          if (_angle > 170) {
            _step *= -1;
          }
          else if (_angle < 5) {
            _step *= -1;
          }

          _angle += _step;

          nextCamera->setFOV(Angle::nan(), Angle::fromDegrees(_angle));

          return true;


        }
      };

      builder.addCameraConstraint(new AnimatedFOVCameraConstrainer());
    }

    if (false){ //Changing ROLL

      class AnimatedRollCameraConstrainer: public ICameraConstrainer {
      private:
        mutable double _angle;
        mutable double _step;
      public:

        AnimatedRollCameraConstrainer() : _angle(0), _step(2)
        {
        }

        bool onCameraChange(const Planet* planet,
                            const Camera* previousCamera,
                            Camera* nextCamera) const {
          _angle += _step;

          nextCamera->setRoll(Angle::fromDegrees(_angle));

          return true;


        }
      };

      builder.addCameraConstraint(new AnimatedRollCameraConstrainer());

    }




  }


  //  [self createInterpolationTest: meshRenderer];

  //  meshRenderer->addMesh([self createPointsMesh: builder.getPlanet() ]);

  //Draw light direction
  if (false) {

    Vector3D lightDir = Vector3D(100000, 0,0);
    //    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero);
    FloatBufferBuilderFromCartesian3D* vertex = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();

    Vector3D v = planet->toCartesian(Geodetic3D(Angle::fromDegrees(28.127222),
                                                Angle::fromDegrees(-15.431389),
                                                10000));

    vertex->add(v);
    vertex->add(v.add(lightDir));
    //lightDir.normalized().times(planet->getRadii().maxAxis() *1.5));

    meshRenderer->addMesh( new DirectMesh(GLPrimitive::lines(),
                                          true,
                                          vertex->getCenter(),
                                          vertex->create(),
                                          3.0,
                                          1.0,
                                          Color::newFromRGBA(1.0, 0.0, 0.0, 1.0)));

    delete vertex;

  }

  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
                                                                     geoRenderer: geoRenderer
                                                                    meshRenderer: meshRenderer
                                                                   marksRenderer: marksRenderer
                                                                          planet: planet];
  builder.setInitializationTask(initializationTask, true);

  PeriodicalTask* periodicalTask = [self createSamplePeriodicalTask: &builder];
  builder.addPeriodicalTask(periodicalTask);

  const bool logFPS = false;
  builder.setLogFPS(logFPS);

  const bool logDownloaderStatistics = false;
  builder.setLogDownloaderStatistics(logDownloaderStatistics);

  //builder.getPlanetRendererBuilder()->setRenderDebug(true);

  //  WidgetUserData* userData = NULL;
  //  builder.setUserData(userData);

  // initialization
  builder.initializeWidget();
  //  [self testGenericQuadTree:geoTileRasterizer];

}

- (void) testGenericQuadTree: (GEOTileRasterizer*) geoTileRasterizer{


  NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/populated_places"
                                                              ofType: @"geojson"];

  if (geoJSONFilePath) {
    NSString *nsGEOJSON = [NSString stringWithContentsOfFile: geoJSONFilePath
                                                    encoding: NSUTF8StringEncoding
                                                       error: nil];

    if (nsGEOJSON) {
      std::string geoJSON = [nsGEOJSON UTF8String];

      GEOObject* geoObject = GEOJSONParser::parseJSON(geoJSON);

      GEOFeatureCollection* fc = (GEOFeatureCollection*) geoObject;

      for (double areaProportion = 0.1; areaProportion < 0.9; areaProportion += 0.1) {

        GenericQuadTree tree(1,12,areaProportion);

        std::string* x = new std::string("OK");

        for (int i = 0; i < fc->size(); i++) {
          GEO2DPointGeometry* p = (GEO2DPointGeometry*) fc->get(i)->getGeometry();
          p->getPosition();

          Geodetic2D geo = p->getPosition();
          //        printf("POINT: %s\n", geo.description().c_str());

          tree.add(p->getPosition(), x);
        }

        //      double areaProportion = 0.5;
        printf("TREE WITH CHILD_ARE_PROPORTION %f\n--------------------\n", areaProportion);
        GenericQuadTree_TESTER::run(tree, geoTileRasterizer);

        delete x;
      }

      delete fc;

    }

  } else{
    GenericQuadTree_TESTER::run(10000, geoTileRasterizer);
  }

  ////////////////////////////////////////////////////
  /*
   {

   NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: @"geojson/countries-50m"
   ofType: @"geojson"];

   if (geoJSONFilePath) {
   NSString *nsGEOJSON = [NSString stringWithContentsOfFile: geoJSONFilePath
   encoding: NSUTF8StringEncoding
   error: nil];

   if (nsGEOJSON) {
   std::string geoJSON = [nsGEOJSON UTF8String];

   GEOObject* geoObject = GEOJSONParser::parse(geoJSON);

   GEOFeatureCollection* fc = (GEOFeatureCollection*) geoObject;

   for (double areaProportion = 0.1; areaProportion < 0.9; areaProportion += 0.1) {

   GenericQuadTree tree(1,12,areaProportion);

   std::string* x = new std::string("OK");

   for (int i = 0; i < fc->size(); i++) {
   GEO2DPolygonGeometry* p = (GEO2DPolygonGeometry*) fc->get(i)->getGeometry();

   const std::vector<Geodetic2D*>* ps = p->getCoordinates();
   Sector *s = new Sector(*ps->at(0), *ps->at(0));
   for (unsigned int j = 0; j < ps->size(); j++) {
   if (ps->at(j) != NULL) {
   Geodetic2D g = *ps->at(j);
   Sector x(g,g);
   Sector *s2 = new Sector( s->mergedWith(x));
   delete s;
   s = s2;
   }
   }

   //            Geodetic2D geo = p->getPosition();
   printf("SEC: %s\n", s->description().c_str());

   tree.add(*s, x);
   delete s;
   }

   //      double areaProportion = 0.5;
   printf("TREE WITH CHILD_ARE_PROPORTION %f\n--------------------\n", areaProportion);
   GenericQuadTree_TESTER::run(tree, geoTileRasterizer);

   delete x;
   }
   }
   } else{
   GenericQuadTree_TESTER::run(10000, geoTileRasterizer);
   }

   }
   */
}

- (void)createInterpolationTest: (MeshRenderer*) meshRenderer
{

  const Planet* planet = Planet::createEarth();

  Interpolator* interpolator = new BilinearInterpolator();

  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
  //                                          planet,
  //                                          Geodetic2D::zero());
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);

  FloatBufferBuilderFromColor colors;


  //  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::firstVertex(),
  //                                             Vector3D::zero);
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


  vertices->add(sector.getSW(), heightSW);  colors.add(1, 0, 0, 1);
  vertices->add(sector.getSE(), heightSE);  colors.add(1, 0, 0, 1);
  vertices->add(sector.getNE(), heightNE);  colors.add(1, 0, 0, 1);
  vertices->add(sector.getNW(), heightNW);  colors.add(1, 0, 0, 1);

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

      vertices->add(latitude, longitude, height);

      colors.add(alpha, alpha, alpha, 1);
    }
  }


  const float lineWidth = 2;
  const float pointSize = 3;
  Color* flatColor = NULL;
  Mesh* mesh = new DirectMesh(GLPrimitive::points(),
                              //GLPrimitive::lineStrip(),
                              true,
                              vertices->getCenter(),
                              vertices->create(),
                              lineWidth,
                              pointSize,
                              flatColor,
                              colors.create());
  delete vertices;

  meshRenderer->addMesh( mesh );

  delete planet;
}


- (Mesh*) createPointsMesh: (const Planet*)planet
{
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
  //                                          planet,
  //                                          Geodetic2D::zero());
  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
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

      vertices->add( lat, lon, 100000 );

      const float red   = (float) (i + halfSteps + 1) / steps;
      const float green = (float) (j + halfSteps + 1) / steps;
      colors.add(Color::fromRGBA(red, green, 0, 1));
    }
  }

  const float lineWidth = 1;
  const float pointSize = 2;
  Color* flatColor = NULL;

  Mesh* result = new DirectMesh(GLPrimitive::points(),
                                true,
                                vertices->getCenter(),
                                vertices->create(),
                                lineWidth,
                                pointSize,
                                flatColor,
                                colors.create());

  delete vertices;

  return result;
}

- (CameraRenderer*) createCameraRenderer
{
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  //cameraRenderer->addHandler(new CameraZoomAndRotateHandler());

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
    //    layerSet->addLayer( MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30)) );
  }

  //  const std::string& mapKey,
  //  const TimeInterval& timeToCache,
  //  bool readExpired = true,
  //  int initialLevel = 1,
  //  int maxLevel = 19,
  //  LayerCondition* condition = NULL
  if (true) {
    layerSet->addLayer(new MapBoxLayer("examples.map-9ijuk24y",
                                       TimeInterval::fromDays(30)));
  }

  bool testingTransparencies = false;
  if (testingTransparencies){

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
                                  true,
                                  NULL,
                                  0.5);
    layerSet->addLayer(pnoa);
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
                                          //BingMapType::AerialWithLabels(),
                                          BingMapType::Aerial(),
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
    //    layerSet->addLayer(URLTemplateLayer::newWGS84("http://192.168.1.2/1-TrueMarble_2km_21600x10800_tif.tiles/{level}/{x}/{y}.png",
    //                                                  Sector::fullSphere(),
    //                                                  false,
    //                                                  0,
    //                                                  4,
    //                                                  TimeInterval::zero(),
    //                                                  false));

    //    WMSLayer* blueMarble = new WMSLayer("bmng200405",
    //                                        URL("http://www.nasa.network.com/wms?", false),
    //                                        WMS_1_1_0,
    //                                        Sector::fullSphere(),
    //                                        "image/jpeg",
    //                                        "EPSG:4326",
    //                                        "",
    //                                        false,
    //                                        //new LevelTileCondition(0, 8),
    //                                        NULL,
    //                                        TimeInterval::fromDays(30),
    //                                        true);
    //    layerSet->addLayer(blueMarble);


    // [lower=[lat=39.99854166666677, lon=-72.00145833333336], upper=[lat=42.50145833333343, lon=-68.9985416666667]]
    // [lower=[lat=48.302366666666664, lon=11.65903888888889], upper=[lat=48.40372222222222, lon=11.788533333333335]]

    //    AndTileCondition* condition = new AndTileCondition(new LevelTileCondition(0, 500),
    //                                                       new SectorTileCondition(Sector::fromDegrees(39.99854166666677, -72.00145833333336,
    //                                                                                                   42.50145833333343, -68.9985416666667)));
    //
    //    layerSet->addLayer(URLTemplateLayer::newWGS84("http://192.168.1.2/2-N40-W072_ll_tif.tiles/{level}/{x}/{y}.png",
    //                                                  Sector::fullSphere(),
    //                                                  true,
    //                                                  0,
    //                                                  8,
    //                                                  TimeInterval::zero(),
    //                                                  false,
    //                                                  //new LevelTileCondition(3, 500)
    //                                                  //new SectorTileCondition(Sector::fromDegrees(39.99833333333333, -0.0016666666666663962,
    //                                                  //                                            42.50166666666667, 3.0016666666666665))
    //                                                  condition
    //                                                  ));

    layerSet->addLayer(URLTemplateLayer::newWGS84("http://192.168.1.2/_merged/{level}/{x}/{y}.jpg",
                                                  Sector::fullSphere(),
                                                  true,
                                                  0,
                                                  15,
                                                  TimeInterval::zero(),
                                                  false,
                                                  NULL
                                                  ));

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

    class PNOATerrainTouchEventListener : public LayerTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const LayerTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        printf ("PNOA touched. Feature info = %s\n", url.getPath().c_str());

        return true;
      }
    };

    pnoa->addLayerTouchEventListener(new PNOATerrainTouchEventListener());
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

    class CatastroTerrainTouchEventListener : public LayerTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const LayerTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        ILogger::instance()->logInfo("%s", url.getPath().c_str());

        return true;
      }
    };

    catastro->addLayerTouchEventListener(new CatastroTerrainTouchEventListener());

    layerSet->addLayer(catastro);
  }

  if (false) {
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

    class TempTerrainTouchEventListener : public LayerTouchEventListener {
    public:
      bool onTerrainTouch(const G3MEventContext* context,
                          const LayerTouchEvent& event) {
        const URL url = event.getLayer()->getFeatureInfoURL(event.getPosition().asGeodetic2D(),
                                                            event.getSector());

        printf ("touched Temperature. Feature info = %s\n", url.getPath().c_str());

        return true;
      }
    };

    temp->addLayerTouchEventListener(new TempTerrainTouchEventListener());
  }

  //Worng TEMP Layer
  if (false) {
    WMSLayer* temp = new WMSLayer("temp",
                                  URL("http://wms.openweathermap.org/service", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/png2",  //WRONG
                                  "EPSG:4326",
                                  "",
                                  true,
                                  NULL,
                                  TimeInterval::zero(),
                                  true);
    layerSet->addLayer(temp);
  }

  //  if (true) {
  //    layerSet->addLayer( new URLTemplateLayer() );
  //  }

  return layerSet;
}

- (TilesRenderParameters*) createPlanetRendererParameters
{
  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
  //const Quality quality = QUALITY_MEDIUM;
  const Quality quality = QUALITY_LOW;

  return new TilesRenderParameters(renderDebug,
                                   useTilesSplitBudget,
                                   forceFirstLevelTilesRenderOnStart,
                                   incrementalTileQuality,
                                   quality);
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
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0),
                      RELATIVE_TO_GROUND);
  marksRenderer->addMark(m1);


  if (true) {
    for (int i = 0; i < 10; i+=2) {
      for (int j = 0; j < 10; j+=2) {
        Geodetic3D g(Angle::fromDegrees(28.05 + i), Angle::fromDegrees(-14.36 + j - 10), (i+j)*10000);

        Mark* m1 = new Mark("M", g, RELATIVE_TO_GROUND);
        marksRenderer->addMark(m1);

      }
    }
  }


  Mark* m2 = new Mark(URL("file:///plane.png", false),
                      Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0),
                      RELATIVE_TO_GROUND);
  marksRenderer->addMark(m2);

  //  Mark* m3 = new Mark("Washington, DC",
  //                      Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
  //                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
  //                                 0),
  //                      0);
  //  marksRenderer->addMark(m3);

  if (false) {
    for (int i = 0; i < 2000; i++) {
      const Angle latitude  = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
      const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );

      marksRenderer->addMark(new Mark("Random",
                                      URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false),
                                      Geodetic3D(latitude, longitude, 0), RELATIVE_TO_GROUND));
    }
  }

  return marksRenderer;

}

- (ShapesRenderer*) createShapesRenderer: (const Planet*) planet
{
  ShapesRenderer* shapesRenderer = new ShapesRenderer();
  Shape* quad1 = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                              Angle::fromDegrees(-122 - 2),
                                              824000 / 2),
                               RELATIVE_TO_GROUND,
                               URL("file:///hud.png", false),
                               //50000, 50000,
                               663000, 824000,
                               false);
  //  Shape* quad1 = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
  //                                              Angle::fromDegrees(-122 - 2),
  //                                              50000 / 2),
  //                               RELATIVE_TO_GROUND,
  //                               URL("file:///hud2.png", false),
  //                               //50000, 50000,
  //                               50000, 50000,
  //                               false);
  quad1->setPitch(Angle::fromDegrees(90));
  shapesRenderer->addShape(quad1);

  Shape* quad2 = new QuadShape(new Geodetic3D(Angle::fromDegrees(37.78333333),
                                              Angle::fromDegrees(-123),
                                              8000),
                               RELATIVE_TO_GROUND,
                               35000, 75000,
                               Color::newFromRGBA(1, 0, 1, 0.5),
                               true);
  shapesRenderer->addShape(quad2);

  Shape* circle = new CircleShape(new Geodetic3D(Angle::fromDegrees(38.78333333),
                                                 Angle::fromDegrees(-123),
                                                 8000),
                                  ABSOLUTE,
                                  50000,
                                  Color::fromRGBA(1, 1, 0, 0.5));
  shapesRenderer->addShape(circle);

  Shape* sphere = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(40),
                                                    Angle::fromDegrees(-123.5),
                                                    8000),
                                     ABSOLUTE,
                                     Vector3D(50000, 50000, 50000),
                                     16,
                                     0,
                                     false,
                                     false,
                                     Color::fromRGBA(0, 1, 1, 1));
  shapesRenderer->addShape(sphere);


  Shape* box = new BoxShape(new Geodetic3D(Angle::fromDegrees(39.78333333),
                                           Angle::fromDegrees(-122),
                                           45000),
                            ABSOLUTE,
                            Vector3D(20000, 30000, 50000),
                            2,
                            Color::fromRGBA(0,    1, 0, 0.5),
                            Color::newFromRGBA(0, 0.75, 0, 0.75));
  box->setAnimatedScale(1, 1, 20);
  shapesRenderer->addShape(box);

  //    const URL textureURL("file:///world.jpg", false);
  //
  //  const double factor = 2e5;
  //  const Vector3D radius1(factor, factor, factor);
  //  const Vector3D radius2(factor*1.5, factor*1.5, factor*1.5);
  //  const Vector3D radiusBox(factor, factor*1.5, factor*2);


  //  Shape* box1 = new BoxShape(new Geodetic3D(Angle::fromDegrees(0),
  //                                           Angle::fromDegrees(10),
  //                                           radiusBox.z()/2),
  //                            ABSOLUTE,
  //                            radiusBox,
  //                            2,
  //                            Color::fromRGBA(0,    1, 0, 1),
  //                            Color::newFromRGBA(0, 0.75, 0, 1));
  //  //box->setAnimatedScale(1, 1, 20);
  //  shapesRenderer->addShape(box1);
  //
  //
  //    Shape* ellipsoid1 = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
  //                                                          Angle::fromDegrees(0),
  //                                                          radius1._x),
  //                                           ABSOLUTE,
  //                                           planet,
  //                                           URL("file:///world.jpg", false),
  //                                           radius1,
  //                                           32,
  //                                           0,
  //                                           false,
  //                                           false
  //                                           //Color::newFromRGBA(0,    0.5, 0.8, 0.5),
  //                                           //Color::newFromRGBA(0, 0.75, 0, 0.75)
  //                                           );
  //  //ellipsoid1->setScale(2);
  //    shapesRenderer->addShape(ellipsoid1);
  //
  //  Shape* mercator1 = new EllipsoidShape(new Geodetic3D(Angle::fromDegrees(0),
  //                                                       Angle::fromDegrees(5),
  //                                                       radius2._x),
  //                                          ABSOLUTE,
  //                                          planet,
  //                                          URL("file:///mercator_debug.png", false),
  //                                          radius2,
  //                                          32,
  //                                          0,
  //                                          false,
  //                                          true
  //                                          //Color::newFromRGBA(0.5,    0.0, 0.8, 0.5),
  //                                          //Color::newFromRGBA(0, 0.75, 0, 0.75)
  //                                          );
  //    shapesRenderer->addShape(mercator1);

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

  std::vector<float> transparencies;
  transparencies.push_back(1.0);
  transparencies.push_back(0.5);

  class QuadListener: public IImageListener {
    ShapesRenderer* _sr;
  public:

    QuadListener(ShapesRenderer* sr):_sr(sr) {

    }

    void imageCreated(const IImage* image) {

      Shape* quadImages = new QuadShape(new Geodetic3D(Angle::fromDegrees(28.410728),
                                                       Angle::fromDegrees(-16.339417),
                                                       8000),
                                        RELATIVE_TO_GROUND,
                                        image,
                                        49000, 38000,
                                        false);

      _sr->addShape(quadImages);
    }
  };


  IImageUtils::combine(Vector2I(256,256),
                       images,
                       srcRs,
                       destRs,
                       transparencies,
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
  mutable int _colorIndex = 0;

  const Planet* _planet;

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

    //    std::string name = properties->getAsString("name", "");
    //    if (name.compare("Russia") == 0) {
    //      int _XXX;
    //      printf("break point on me\n");
    //    }
    //    if (name.compare("Antarctica") == 0) {
    //      int _XXX;
    //      printf("break point on me\n");
    //    }

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
    Color color = Color::fromRGBA(1, 1, 0, 1);

    return new CircleShape(new Geodetic3D(geometry->getPosition(), 200),
                           RELATIVE_TO_GROUND,
                           radius,
                           color);
  }

  EllipsoidShape* createEllipsoidShape(const GEO2DPointGeometry* geometry,
                                       const Planet* planet) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double population = properties->getAsNumber("population", 0);

    double radius = population / 1e2;

    const int wheelSize = 7;
    _colorIndex = (_colorIndex + 1) % wheelSize;

    return new EllipsoidShape(new Geodetic3D(geometry->getPosition(), 0),
                              RELATIVE_TO_GROUND,
                              Vector3D(radius, radius, radius),
                              10,
                              0.0,
                              false,
                              false,
                              Color( Color::fromRGBA(1, 1, 0, 1).wheelStep(wheelSize, _colorIndex) ),
                              Color::newFromRGBA(0.2, 0.2, 0, 1),
                              true);
  }

  BoxShape* createBoxShape(const GEO2DPointGeometry* geometry,
                           const Planet* planet) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double population = properties->getAsNumber("population", 0);

    const double boxExtent = 50000;
    const double baseArea = boxExtent*boxExtent;
    const double volume = population * boxExtent * 3500;
    const double height = volume / baseArea;

    const int wheelSize = 7;
    _colorIndex = (_colorIndex + 1) % wheelSize;


    BoxShape* box = new BoxShape(new Geodetic3D(geometry->getPosition(), 0),
                                 RELATIVE_TO_GROUND,
                                 Vector3D(boxExtent, boxExtent, height),
                                 1,
                                 //Color::newFromRGBA(1, 1, 0, 0.6),
                                 Color( Color::fromRGBA(1, 0.5, 0, 1).wheelStep(wheelSize, _colorIndex) ),
                                 Color::newFromRGBA(0.2, 0.2, 0, 1));

    //box->setPitch(Angle::fromDegrees(45));
    return box;

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
      //      const double height = (volume / baseArea) / 2 * 1.1;

      const double height = 1000;

      return new Mark(label,
                      Geodetic3D(geometry->getPosition(), height),
                      RELATIVE_TO_GROUND,
                      0,
                      //25 + (scalerank * -3)
                      22 + (scalerank * -3)
                      );
    }

    return NULL;
  }


public:
  SampleSymbolizer(const Planet* planet) :
  _planet(planet),
  _colorIndex(0) {

  }

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

    const JSONObject* properties = geometry->getFeature()->getProperties();

    const double population = properties->getAsNumber("population", 0);

    if (population > 2000000) {
      //    if (rand()%2 == 0) {
      //      symbols->push_back( new GEOShapeSymbol( createEllipsoidShape(geometry) ) );
      //    }
      //    else {
      symbols->push_back( new GEOShapeSymbol( createBoxShape(geometry, _planet) ) );
      //    }

      Mark* mark = createMark(geometry);
      if (mark != NULL) {
        symbols->push_back( new GEOMarkSymbol(mark) );
      }
    }

    return symbols;
  }

};


- (GEORenderer*) createGEORendererMeshRenderer: (MeshRenderer*) meshRenderer
                                shapesRenderer: (ShapesRenderer*) shapesRenderer
                                 marksRenderer: (MarksRenderer*) marksRenderer
                             geoTileRasterizer: (GEOTileRasterizer*) geoTileRasterizer
                                        planet: (const Planet*) planet
{
  GEOSymbolizer* symbolizer = new SampleSymbolizer(planet);


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
                                                                        true,
                                                                        new Geodetic3D(Angle::zero(), Angle::zero(), 10000),
                                                                        ABSOLUTE);

    if (radarModel != NULL) {
      SGNode* node  = radarModel->getNode();

      const int childrenCount = node->getChildrenCount();
      for (int i = 0; i < childrenCount; i++) {
        SGNode* child = node->getChild(i);
        SGMaterialNode* material = (SGMaterialNode*) child;
        material->setBaseColor( NULL );
      }

      //    radarModel->setPosition(Geodetic3D::fromDegrees(0, 0, 0));
      //      radarModel->setPosition(new Geodetic3D(Angle::zero(), Angle::zero(), 10000));
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

//class ParseMeshBufferDownloadListener : public IBufferDownloadListener {
//  MeshRenderer* _meshRenderer;
//  const Planet* _planet;
//
//public:
//  ParseMeshBufferDownloadListener(MeshRenderer* meshRenderer,
//                                  const Planet* planet) :
//  _meshRenderer(meshRenderer),
//  _planet(planet)
//  {
//  }
//
//  void onDownload(const URL& url,
//                  IByteBuffer* buffer,
//                  bool expired) {
//    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);
//
//    const JSONObject* jsonObject = jsonBaseObject->asObject();
//    if (jsonObject == NULL) {
//      ILogger::instance()->logError("Invalid format for \"%s\"", url.getPath().c_str());
//    }
//    else {
//      const JSONArray* jsonCoordinates = jsonObject->getAsArray("coordinates");
//
//      int __DGD_At_Work;
//
//      FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_planet);
//
//      const int coordinatesSize = jsonCoordinates->size();
//      for (int i = 0; i < coordinatesSize; i += 3) {
//        const double latInDegrees = jsonCoordinates->getAsNumber(i    , 0);
//        const double lonInDegrees = jsonCoordinates->getAsNumber(i + 1, 0);
//        const double height       = jsonCoordinates->getAsNumber(i + 2, 0);
//
//        vertices.add(Angle::fromDegrees(latInDegrees),
//                     Angle::fromDegrees(lonInDegrees),
//                     height);
//      }
//
//      const JSONArray* jsonNormals = jsonObject->getAsArray("normals");
//      const int normalsSize = jsonNormals->size();
//      IFloatBuffer* normals = IFactory::instance()->createFloatBuffer(normalsSize);
//      for (int i = 0; i < normalsSize; i++) {
//        normals->put(i, (float) jsonNormals->getAsNumber(i, 0) );
//      }
//
//      const JSONArray* jsonIndices = jsonObject->getAsArray("indices");
//      const int indicesSize = jsonIndices->size();
//      IShortBuffer* indices = IFactory::instance()->createShortBuffer(indicesSize);
//      for (int i = 0; i < indicesSize; i++) {
//        indices->put(i, (short) jsonIndices->getAsNumber(i, 0) );
//      }
//
//      Mesh* mesh = new IndexedMesh(GLPrimitive::triangles(),
//                                   true,
//                                   vertices.getCenter(),
//                                   vertices.create(),
//                                   indices,
//                                   1, // lineWidth
//                                   1, // pointSize
//                                   Color::newFromRGBA(1,0,0,1), // flatColor
//                                   NULL, // colors,
//                                   1, //  colorsIntensity,
//                                   true, // depthTest,
//                                   normals
//                                   );
//      _meshRenderer->addMesh(mesh);
//    }
//
//    delete jsonBaseObject;
//
//    delete buffer;
//  }
//
//  void onError(const URL& url) {
//    ILogger::instance()->logError("Can't download %s", url.getPath().c_str());
//  }
//
//  void onCancel(const URL& url) {
//  }
//
//  void onCanceledDownload(const URL& url,
//                          IByteBuffer* buffer,
//                          bool expired) {
//  }
//
//};

- (GInitializationTask*) createSampleInitializationTask: (ShapesRenderer*) shapesRenderer
                                            geoRenderer: (GEORenderer*) geoRenderer
                                           meshRenderer: (MeshRenderer*) meshRenderer
                                          marksRenderer: (MarksRenderer*) marksRenderer
                                                 planet: (const Planet*) planet
{
  class SampleInitializationTask : public GInitializationTask {
  private:
    G3MWidget_iOS*  _iosWidget;
    ShapesRenderer* _shapesRenderer;
    GEORenderer*    _geoRenderer;
    MeshRenderer*   _meshRenderer;
    MarksRenderer*  _marksRenderer;
    const Planet* _planet;



  public:
    SampleInitializationTask(G3MWidget_iOS*  iosWidget,
                             ShapesRenderer* shapesRenderer,
                             GEORenderer*    geoRenderer,
                             MeshRenderer*   meshRenderer,
                             MarksRenderer*  marksRenderer,
                             const Planet* planet) :
    _iosWidget(iosWidget),
    _shapesRenderer(shapesRenderer),
    _geoRenderer(geoRenderer),
    _meshRenderer(meshRenderer),
    _marksRenderer(marksRenderer),
    _planet(planet)
    {

    }

    void run(const G3MContext* context) {
      printf("Running initialization Task\n");

      [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(10.0),
                                                     Geodetic3D::fromDegrees(28.624949838863251728, -13.898810737833036555, 18290),
                                                     Angle::fromDegrees(180),
                                                     Angle::fromDegrees(-45),
                                                     false,
                                                     false);
      
      //      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D::fromDegrees(36.518803097704875427,
      //                                                                             -6.2814697225724938079,
      //                                                                             30.098082578364309114),
      //                                                     Angle::fromDegrees(-17.488762),
      //                                                     Angle::fromDegrees(82.525557));

      //      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D::fromDegrees(36.51826434744587857, 6.2798347736047421819, 102.37859667537750852),
      //                                                     Angle::fromDegrees(-32.066195 ),
      //                                                     Angle::fromDegrees(78.523121));

      //      [_iosWidget widget]->setAnimatedCameraPosition(Geodetic3D::fromDegrees(36.51826434744587857, 6.2798347736047421819, 102.37859667537750852),
      //                                                     Angle::fromDegrees(-32.066195 ),
      //                                                     Angle::fromDegrees(78.523121));

      class G3MeshBufferDownloadListener : public IBufferDownloadListener {
        const Planet* _planet;
        MeshRenderer* _meshRenderer;
      public:
        G3MeshBufferDownloadListener(const Planet* planet,
                                     MeshRenderer* meshRenderer) :
        _planet(planet),
        _meshRenderer(meshRenderer)
        {
        }

        void onDownload(const URL& url,
                        IByteBuffer* buffer,
                        bool expired) {
          const JSONBaseObject* jsonObject = IJSONParser::instance()->parse(buffer);
          std::vector<Mesh*> meshes = G3MMeshParser::parse(jsonObject->asObject(), _planet);
          const int meshesSize = meshes.size();
          for (int i = 0; i < meshesSize; i++) {
            _meshRenderer->addMesh( meshes[i] );
          }

          delete jsonObject;
          delete buffer;
        }

        void onError(const URL& url) {
          ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
        }

        void onCancel(const URL& url) {
          // do nothing
        }

        void onCanceledDownload(const URL& url,
                                IByteBuffer* buffer,
                                bool expired) {
          // do nothing
        }

      };


#warning Diego at work!
      context->getDownloader()->requestBuffer(URL("file:///3d_.json"),
                                              1000000,
                                              TimeInterval::zero(),
                                              false,
                                              new G3MeshBufferDownloadListener(context->getPlanet(),
                                                                               _meshRenderer),
                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_1.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_1-1.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_1-2.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_1-3.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_2-0.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_2-1.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);
      //      context->getDownloader()->requestBuffer(URL("file:///3d_2-2.json"),
      //                                              1000000,
      //                                              TimeInterval::zero(),
      //                                              false,
      //                                              new G3MeshBufferDownloadListener(context->getPlanet(),
      //                                                                               _meshRenderer),
      //                                              true);

      if (true){

        class PlaneShapeLoadListener : public ShapeLoadListener {
        public:
          void onBeforeAddShape(SGShape* shape) {
            const double scale = 200;
            shape->setScale(scale, scale, scale);
            shape->setPitch(Angle::fromDegrees(90));
            //shape->setRoll(Angle::fromDegrees(45));
          }

          void onAfterAddShape(SGShape* shape) {
            shape->setAnimatedPosition(TimeInterval::fromSeconds(26),
                                       Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                  Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
                                                  10000),
                                       true);
            /*
             const double fromDistance = 75000;
             const double toDistance   = 18750;

             const Angle fromAzimuth = Angle::fromDegrees(-90);
             const Angle toAzimuth   = Angle::fromDegrees(270);

             const Angle fromAltitude = Angle::fromDegrees(90);
             const Angle toAltitude   = Angle::fromDegrees(15);

             shape->orbitCamera(TimeInterval::fromSeconds(20),
             fromDistance, toDistance,
             fromAzimuth,  toAzimuth,
             fromAltitude, toAltitude);
             */
          }
        };

        _shapesRenderer->loadBSONSceneJS(URL("file:///A320.bson"),
                                         URL::FILE_PROTOCOL + "textures-A320/",
                                         false,
                                         new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                        Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                                        10000),
                                         ABSOLUTE,
                                         new PlaneShapeLoadListener(),
                                         true);

      }

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
            Shape* plane = SceneJSShapesParser::parseFromBSON(buffer,
                                                              URL::FILE_PROTOCOL + "textures-A320/",
                                                              false,
                                                              new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                                                             Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                                                             10000),
                                                              ABSOLUTE);

            if (plane) {
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
              /**/

              delete buffer;
            }
          }
        }
      }

      if (true) {
        //      NSString* geojsonName = @"geojson/countries";
        //        NSString* geojsonName = @"geojson/countries-50m";
        //      NSString* geojsonName = @"geojson/boundary_lines_land";
        NSString* geojsonName = @"geojson/cities";
        //      NSString* geojsonName = @"geojson/test";

        NSString *geoJSONFilePath = [[NSBundle mainBundle] pathForResource: geojsonName
                                                                    ofType: @"geojson"];

        if (geoJSONFilePath) {
          NSString *nsGEOJSON = [NSString stringWithContentsOfFile: geoJSONFilePath
                                                          encoding: NSUTF8StringEncoding
                                                             error: nil];

          if (nsGEOJSON) {
            std::string geoJSON = [nsGEOJSON UTF8String];

            GEOObject* geoObject = GEOJSONParser::parseJSON(geoJSON);

            _geoRenderer->addGEOObject(geoObject);
          }
        }
      }

      //  Touched on (Tile level=18, row=161854, column=74976, sector=(Sector (lat=38.888895015761768548d, lon=-77.036132812499985789d) - (lat=38.889963929167578272d, lon=-77.034759521484360789d)))
      //  Camera position=(lat=38.889495390450342427d, lon=-77.035258992009289614d, height=666.01783933913191049) heading=1.074786 pitch=0.180631

      const bool loadWashingtonModel = false;
      if (loadWashingtonModel) {
        NSString* washingtonFilePath = [[NSBundle mainBundle] pathForResource: @"washington-memorial"
                                                                       ofType: @"json"];
        if (washingtonFilePath) {
          NSString *nsWashingtonJSON = [NSString stringWithContentsOfFile: washingtonFilePath
                                                                 encoding: NSUTF8StringEncoding
                                                                    error: nil];
          if (nsWashingtonJSON) {
            std::string washingtonJSON = [nsWashingtonJSON UTF8String];

            Shape* washington = SceneJSShapesParser::parseFromJSON(washingtonJSON,
                                                                   URL::FILE_PROTOCOL + "/images/" ,
                                                                   false,
                                                                   new Geodetic3D(Angle::fromDegrees(38.888895015761768548),
                                                                                  Angle::fromDegrees(-77.036132812499985789),
                                                                                  10000),
                                                                   ABSOLUTE //RELATIVE_TO_GROUND
                                                                   );

            const double scale = 100;
            washington->setScale(scale, scale, scale);
            washington->setPitch(Angle::fromDegrees(90));
            //            washington->setHeading(Angle::fromDegrees(0));
            _shapesRenderer->addShape(washington);
          }
        }
      }

      if (true) {
        NSString *planeFilePath = [[NSBundle mainBundle] pathForResource: @"seymour-plane"
                                                                  ofType: @"json"];
        if (planeFilePath) {
          NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: planeFilePath
                                                            encoding: NSUTF8StringEncoding
                                                               error: nil];
          if (nsPlaneJSON) {
            std::string planeJSON = [nsPlaneJSON UTF8String];

            Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON,
                                                              URL::FILE_PROTOCOL + "/" ,
                                                              false,
                                                              new Geodetic3D(Angle::fromDegrees(28.127222),
                                                                             Angle::fromDegrees(-15.431389),
                                                                             10000),
                                                              ABSOLUTE);

            // Washington, DC
            const double scale = 1000;
            plane->setScale(scale, scale, scale);
            plane->setPitch(Angle::fromDegrees(90));
            plane->setHeading(Angle::fromDegrees(0));
            _shapesRenderer->addShape(plane);


            plane->setAnimatedPosition(TimeInterval::fromSeconds(60),
                                       Geodetic3D(Angle::fromDegrees(28.127222),
                                                  Angle::fromDegrees(-15.431389),
                                                  10000),
                                       Angle::fromDegrees(90),
                                       Angle::fromDegrees(720),
                                       Angle::zero());

          }
        }
      }

      if (false){ //CHANGE CAMERA WITH TOUCH
        class CameraAnglesTerrainListener: public TerrainTouchListener{
        private:
          G3MWidget_iOS* _iosWidget;
          MeshRenderer* _meshRenderer;
        public:

          CameraAnglesTerrainListener(G3MWidget_iOS* widget, MeshRenderer* mr): _iosWidget(widget), _meshRenderer(mr){}


          virtual bool onTerrainTouch(const G3MEventContext* ec,
                                      const Vector2I&        pixel,
                                      const Camera*          camera,
                                      const Geodetic3D&      position,
                                      const Tile*            tile){

            //            [_iosWidget widget]->getNextCamera()->setRoll(Angle::fromDegrees(45));
            Camera* cam = [_iosWidget widget]->getNextCamera();


            TaitBryanAngles angles = cam->getHeadingPitchRoll();
            printf("A1: %s\n", angles.description().c_str() );

            Angle step = Angle::fromDegrees(10);

            switch ((pixel._x * 4) / cam->getWidth()) {
              case 0:
                [_iosWidget widget]->getNextCamera()->setHeading(angles._heading.add(step));
                break;

              case 1:
                [_iosWidget widget]->getNextCamera()->setPitch(angles._pitch.add(step));
                break;

              case 2:
                [_iosWidget widget]->getNextCamera()->setRoll(angles._roll.add(step));
                break;

              default:
                break;
            }

            TaitBryanAngles angles2 = cam->getHeadingPitchRoll();
            printf("A2: %s\n", angles2.description().c_str() );

            Geodetic2D g(cam->getGeodeticPosition()._latitude, cam->getGeodeticPosition()._longitude);
            Vector3D posInGround = ec->getPlanet()->toCartesian(cam->getGeodeticPosition()._latitude, cam->getGeodeticPosition()._longitude, 0);


            _meshRenderer->addMesh(cam->getLocalCoordinateSystem().changeOrigin(posInGround).createMesh(1e3, Color::red(), Color::green(), Color::blue())  );
            _meshRenderer->addMesh(cam->getCameraCoordinateSystem().createMesh(1e3, Color::red(), Color::green(), Color::blue())  );


            return true;
          }

        };


        [_iosWidget widget]->getPlanetRenderer()->addTerrainTouchListener(new CameraAnglesTerrainListener(_iosWidget, _meshRenderer));

      }

    }

    bool isDone(const G3MContext* context) {
      return true;
    }
  };

  GInitializationTask* initializationTask = new SampleInitializationTask([self G3MWidget],
                                                                         shapesRenderer,
                                                                         geoRenderer,
                                                                         meshRenderer,
                                                                         marksRenderer,
                                                                         planet);
  
  return initializationTask;
}

- (PeriodicalTask*) createSamplePeriodicalTask: (G3MBuilder_iOS*) builder
{
  TrailsRenderer* trailsRenderer = new TrailsRenderer();
  
  Trail* trail = new Trail(Color::fromRGBA(0, 1, 1, 0.6f),
                           5000,
                           0);
  
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
