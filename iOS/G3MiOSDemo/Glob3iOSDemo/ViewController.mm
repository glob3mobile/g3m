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
#import <G3MiOSSDK/GEOLineRasterSymbol.hpp>
#import <G3MiOSSDK/GEOMultiLineRasterSymbol.hpp>
#import <G3MiOSSDK/GEO2DLineRasterStyle.hpp>
#import <G3MiOSSDK/GEO2DPolygonGeometry.hpp>
#import <G3MiOSSDK/GEOPolygonRasterSymbol.hpp>
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
#import <G3MiOSSDK/GEOLabelRasterSymbol.hpp>
#import <G3MiOSSDK/TileRenderingListener.hpp>
#import <G3MiOSSDK/LayerTouchEventListener.hpp>
#import <G3MiOSSDK/TiledVectorLayer.hpp>
#import <G3MiOSSDK/GEORasterSymbolizer.hpp>
#import <G3MiOSSDK/GEO2DPolygonData.hpp>
#import <G3MiOSSDK/ChessboardLayer.hpp>
#import <G3MiOSSDK/GEO2DPointRasterStyle.hpp>
#import <G3MiOSSDK/GEOPointRasterSymbol.hpp>
#import <G3MiOSSDK/GEORectangleRasterSymbol.hpp>



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


- (void)viewDidLoad
{
  [super viewDidLoad];

  [self initCustomizedWithBuilder];

  [[self G3MWidget] startAnimation];
    
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
   
    Geodetic3D position(Geodetic3D(_sector.getCenter(), 5000));
    [_iosWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5), position);
    [_iosWidget widget]->setCameraPosition(position);
  }

  bool isDone(const G3MContext* context) {
    return true;
  }
};



- (void) initWithDefaultBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);
  builder.initializeWidget();
}


- (void) initializeElevationDataProvider: (G3MBuilder_iOS&) builder
{
  float verticalExaggeration = 1.0f;
  builder.getPlanetRendererBuilder()->setVerticalExaggeration(verticalExaggeration);

  ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
                                                                                    Sector::fullSphere(),
                                                                                    Vector2I(2048, 1024));

  builder.getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);
}


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


//class SampleTileRenderingListener : public TileRenderingListener {
//public:
//  void startRendering(const Tile* tile) {
//    ILogger::instance()->logInfo("** Start rendering tile %d/%d/%d", tile->_level, tile->_column, tile->_row);
//  }
//
//  void stopRendering(const Tile* tile) {
//    ILogger::instance()->logInfo("** Stop rendering tile %d/%d/%d", tile->_level, tile->_column, tile->_row);
//  }
//};


- (void) initCustomizedWithBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);

  GEOTileRasterizer* geoTileRasterizer = new GEOTileRasterizer();

  builder.getPlanetRendererBuilder()->addTileRasterizer(geoTileRasterizer);

  builder.setCameraRenderer([self createCameraRenderer]);

  const Planet* planet = Planet::createEarth();
  builder.setPlanet(planet);

  Color* bgColor = Color::newFromRGBA(0.0f, 0.1f, 0.2f, 1.0f);
//  Color* bgColor = Color::newFromRGBA(1, 0, 0, 1);

  builder.setBackgroundColor(bgColor);

  LayerSet* layerSet = [self createLayerSet];

  bool useElevations = false;
  if (useElevations) {
    [self initializeElevationDataProvider: builder];
  }

  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  builder.getPlanetRendererBuilder()->setPlanetRendererParameters([self createPlanetRendererParameters]);

  ProtoRenderer* busyRenderer = new BusyMeshRenderer(Color::newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1));
  builder.setBusyRenderer(busyRenderer);

  ShapesRenderer* shapesRenderer = [self createShapesRenderer: builder.getPlanet()];
  builder.addRenderer(shapesRenderer);

  MeshRenderer* meshRenderer = new MeshRenderer();
  builder.addRenderer( meshRenderer );

  MarksRenderer* marksRenderer = [self createMarksRenderer];
  builder.addRenderer(marksRenderer);

  GEORenderer* geoRenderer = [self createGEORendererMeshRenderer: meshRenderer
                                                  shapesRenderer: shapesRenderer
                                                   marksRenderer: marksRenderer
                                               geoTileRasterizer: geoTileRasterizer
                                                          planet: builder.getPlanet()];
  builder.addRenderer(geoRenderer);


  GInitializationTask* initializationTask = [self createSampleInitializationTask: shapesRenderer
                                                                     geoRenderer: geoRenderer
                                                                    meshRenderer: meshRenderer
                                                                   marksRenderer: marksRenderer
                                                                          planet: planet];
  builder.setInitializationTask(initializationTask, true);

  const bool logFPS = false;
  builder.setLogFPS(logFPS);

  const bool logDownloaderStatistics = false;
  builder.setLogDownloaderStatistics(logDownloaderStatistics);

  // initialization
  builder.initializeWidget();

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


class SampleRasterSymbolizer : public GEORasterSymbolizer {
private:
    
  static GEO2DLineRasterStyle createPointLineRasterStyle(const GEOGeometry* geometry) {
      
      const JSONObject* properties = geometry->getFeature()->getProperties();
      const std::string geoType = properties->getAsString("lodType", "");
      
      if(geoType != ""){
          if (geoType=="LINESTRING" || geoType=="MULTILINESTRING"){
              //const Color color = Color::fromRGBA(0.1, 0.1, 0.95, 0.85).muchDarker();
              const Color color = Color::fromRGBA(0.1, 1.0, 0.1, 0.95);
              float dashLengths[] = {};
              int dashCount = 0;
              
              return GEO2DLineRasterStyle(color,
                                          1,
                                          CAP_ROUND,
                                          JOIN_ROUND,
                                          1,
                                          dashLengths,
                                          dashCount,
                                          0);
          }
          else if(geoType=="POLYGON" || geoType=="MULTIPOLYGON"){
              //return createPolygonLineRasterStyle(geometry);
              const Color color = Color::fromRGBA(0.1, 1.0, 0.1, 0.95);
              float dashLengths[] = {};
              int dashCount = 0;
              
              return GEO2DLineRasterStyle(color,
                                          1,
                                          CAP_ROUND,
                                          JOIN_ROUND,
                                          1,
                                          dashLengths,
                                          dashCount,
                                          0);
          }
      }
      
      // for POINTS and MULTIPOINTS
      float dashLengths[] = {};
      int dashCount = 0;
      
      return GEO2DLineRasterStyle(Color::white(),
                                  1,
                                  CAP_ROUND,
                                  JOIN_ROUND,
                                  1,
                                  dashLengths,
                                  dashCount,
                                  0);
  }
    
    static GEO2DSurfaceRasterStyle createPointSurfaceRasterStyle(const GEOGeometry* geometry) {
        
        const JSONObject* properties = geometry->getFeature()->getProperties();
        const std::string geoType = properties->getAsString("lodType", "");
        
        if(geoType != ""){
            if (geoType=="LINESTRING" || geoType=="MULTILINESTRING"){
                //return createLineRasterStyle(geometry);
                const Color color = Color::fromRGBA(0.1, 1.0, 0.1, 0.95);
                
                return GEO2DSurfaceRasterStyle(color);
            }
            else if(geoType=="POLYGON" || geoType=="MULTIPOLYGON"){
                //return createPolygonSurfaceRasterStyle(geometry);
                const Color color = Color::fromRGBA(0.1, 1.0, 0.1, 0.95);
                
                return GEO2DSurfaceRasterStyle(color);
            }
        }
        
        // for POINTS and MULTIPOINTS
        return GEO2DSurfaceRasterStyle(Color::white());
    }
    
    
  static GEO2DLineRasterStyle createPolygonLineRasterStyle(const GEOGeometry* geometry) {

    const Color color = Color::fromRGBA(0.95, 0.1, 0.1, 0.85).muchDarker();

    float dashLengths[] = {};
    int dashCount = 0;

    return GEO2DLineRasterStyle(color,
                                1,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

  static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(const GEOGeometry* geometry) {

    const Color color = Color::fromRGBA(0.95, 0.1, 0.1, 0.85);

    return GEO2DSurfaceRasterStyle( color );
  }

  static GEO2DLineRasterStyle createLineRasterStyle(const GEOGeometry* geometry) {
    const Color color = Color::fromRGBA(0.1, 0.1, 0.95, 0.85).muchDarker();

    float dashLengths[] = {};
    int dashCount = 0;

    return GEO2DLineRasterStyle(color,
                                1,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
  }

public:
  GEORasterSymbolizer* copy() const {
    return new SampleRasterSymbolizer();
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const {
      
      std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
      std::vector<Geodetic2D*>* coordinates = new std::vector<Geodetic2D*>();
      coordinates->push_back(new Geodetic2D(geometry->getPosition()));
      
      GEO2DPolygonData* rectangleData = new GEO2DPolygonData(coordinates, NULL);
      
      symbols->push_back( new GEORectangleRasterSymbol(rectangleData,
                                                       Vector2F(3.0f,3.0f),
                                                       createPointLineRasterStyle(geometry),
                                                       createPointSurfaceRasterStyle(geometry)));
      
      return symbols;
      
  }
    

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {

      std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

      symbols->push_back( new GEOLineRasterSymbol(geometry->getCoordinates(),
                                                  createLineRasterStyle(geometry)) );
      
      return symbols;

  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    symbols->push_back( new GEOMultiLineRasterSymbol(geometry->getCoordinatesArray(),
                                                     createLineRasterStyle(geometry)) );

    return symbols;
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();

    symbols->push_back( new GEOPolygonRasterSymbol(geometry->getPolygonData(),
                                                   createPolygonLineRasterStyle(geometry),
                                                   createPolygonSurfaceRasterStyle(geometry)) );

    return symbols;
  }

  std::vector<GEORasterSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const {
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();


    const GEO2DLineRasterStyle    lineStyle    = createPolygonLineRasterStyle(geometry);
    const GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

    const std::vector<GEO2DPolygonData*>* polygonsData = geometry->getPolygonsData();
    const int polygonsDataSize = polygonsData->size();

    for (int i = 0; i < polygonsDataSize; i++) {
      GEO2DPolygonData* polygonData = polygonsData->at(i);
      symbols->push_back( new GEOPolygonRasterSymbol(polygonData,
                                                     lineStyle,
                                                     surfaceStyle) );

    }

    return symbols;
  }

};


- (LayerSet*) createLayerSet
{
  LayerSet* layerSet = new LayerSet();

  const bool useOSM = true;
  if (useOSM) {
    layerSet->addLayer( new OSMLayer(TimeInterval::fromDays(30)) );
  }
    
  const bool useBingMaps = false;
  if (useBingMaps) {
      layerSet->addLayer( new BingMapsLayer(BingMapType::AerialWithLabels(),
                                            "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                            TimeInterval::fromDays(30)) );
  }
  
  if (true) {
      
    const std::string italyUrl = "http://192.168.1.12:8000/vectorial/italy_MERGED_LEVELS_12-16_MERCATOR/GEOJSON/{level}/{x}/{y}.geojson";
      
    const std::string scotlandUrl = "http://192.168.1.12:8000/vectorial/scotland_buildings/GEOJSON/{level}/{x}/{y}.geojson";
      
    const int firstLevel = 2;
    const int maxLevel = 17;
    
    const Sector italySector = Sector::fromDegrees(35.4934238, 6.5991058,
                                                     47.108009, 18.5198145);
    
 
    const Sector scotlandSector = Sector::fromDegrees(54.7226296, -7.6536084,
                                                       60.855646, -0.7279944);

    const GEORasterSymbolizer* symbolizer = new SampleRasterSymbolizer();
      
    TiledVectorLayer* scotlandLayer = TiledVectorLayer::newMercator(symbolizer,
                                                                    scotlandUrl,
                                                                    scotlandSector,
                                                                    firstLevel,
                                                                    maxLevel,
                                                                    TimeInterval::fromDays(30), // timeToCache
                                                                    true,                       // readExpired
                                                                    1,                          // transparency
                                                                    new LevelTileCondition(1, 17), // condition
                                                                    ""                          // disclaimerInfo
                                                                      );

    layerSet->addLayer(scotlandLayer);
  }

  
  return layerSet;
}

- (TilesRenderParameters*) createPlanetRendererParameters
{
  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
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
  // marks renderer
  const bool readyWhenMarksReady = false;
  MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);

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
//    const JSONObject* properties = geometry->getFeature()->getProperties();
//
//    const std::string label = properties->getAsString("name", "");
//
//    if (label.compare("") != 0) {
//      double scalerank = properties->getAsNumber("scalerank", 0);
//
//      //      const double population = properties->getAsNumber("population", 0);
//      //
//      //      const double boxExtent = 50000;
//      //      const double baseArea = boxExtent*boxExtent;
//      //      const double volume = population * boxExtent * 3500;
//      //      const double height = (volume / baseArea) / 2 * 1.1;
//
//      const double height = 1000;
//
//      return new Mark(label,
//                      Geodetic3D(geometry->getPosition(), height),
//                      RELATIVE_TO_GROUND,
//                      0,
//                      //25 + (scalerank * -3)
//                      22 + (scalerank * -3)
//                      );
//    }

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
      symbols->push_back( new GEOPolygonRasterSymbol(polygonData,
                                                     lineStyle,
                                                     surfaceStyle) );

    }

    return symbols;
  }


  std::vector<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

    symbols->push_back( new GEOPolygonRasterSymbol(geometry->getPolygonData(),
                                                   createPolygonLineRasterStyle(geometry),
                                                   createPolygonSurfaceRasterStyle(geometry)) );

    return symbols;
  }

  std::vector<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const {
    std::vector<GEOSymbol*>* symbols = new std::vector<GEOSymbol*>();

    //    symbols->push_back( new GEOLine2DMeshSymbol(geometry->getCoordinates(),
    //                                                createLineStyle(geometry),
    //                                                30000) );

    symbols->push_back( new GEOLineRasterSymbol(geometry->getCoordinates(),
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
      symbols->push_back( new GEOShapeSymbol( createBoxShape(geometry, _planet) ) );

      Mark* mark = createMark(geometry);
      if (mark != NULL) {
        symbols->push_back( new GEOMarkSymbol(mark) );
      }
    }

    //    const std::string label = properties->getAsString("name", "");
    //
    //    if (label.compare("") != 0) {
    //      symbols->push_back( new GEOLabelRasterSymbol(label,
    //                                                   geometry->getPosition(),
    //                                                   GFont::monospaced(),
    //                                                   Color::yellow()) );
    //    }

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
    printf("Error downloading %s\n", url._path.c_str());
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
          ILogger::instance()->logError("Error downloading \"%s\"", url._path.c_str());
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


      context->getDownloader()->requestBuffer(URL("file:///3d_.json"),
                                              1000000,
                                              TimeInterval::zero(),
                                              false,
                                              new G3MeshBufferDownloadListener(context->getPlanet(),
                                                                               _meshRenderer),
                                              true);

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

      if (false) {
//#warning Diego at work!
        //      NSString* geojsonName = @"geojson/countries";
        NSString* geojsonName = @"geojson/countries-50m";
        //      NSString* geojsonName = @"geojson/boundary_lines_land";
        // NSString* geojsonName = @"geojson/cities";
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
            //            Camera* cam = [_iosWidget widget]->getNextCamera();
            /*

             <<<<<<< HEAD
             //TaitBryanAngles angles = cam->getTaitBryanAngles();
             =======

             TaitBryanAngles angles = cam->getHeadingPitchRoll();
             >>>>>>> 10100b4c5f73c124779494d0ba45d11b9ed1ebc2
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

             */
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
