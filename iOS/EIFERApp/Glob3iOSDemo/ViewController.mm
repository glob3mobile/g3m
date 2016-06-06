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
#import <G3MiOSSDK/JSONString.hpp>
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
#import <G3MiOSSDK/MapBooOLDBuilder_iOS.hpp>
#import <G3MiOSSDK/IWebSocketListener.hpp>
#import <G3MiOSSDK/GPUProgramFactory.hpp>
#import <G3MiOSSDK/FloatBufferBuilderFromCartesian3D.hpp>
#import <G3MiOSSDK/Color.hpp>
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
#import <G3MiOSSDK/LayerTouchEventListener.hpp>
#import <G3MiOSSDK/TiledVectorLayer.hpp>
#import <G3MiOSSDK/GEORasterSymbolizer.hpp>
#import <G3MiOSSDK/GEO2DPolygonData.hpp>
#import <G3MiOSSDK/ChessboardLayer.hpp>
#import <G3MiOSSDK/GEORectangleRasterSymbol.hpp>

#import <G3MiOSSDK/DefaultInfoDisplay.hpp>
#import <G3MiOSSDK/DebugTileImageProvider.hpp>
#import <G3MiOSSDK/GEOVectorLayer.hpp>
#import <G3MiOSSDK/Info.hpp>

#import <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#import <G3MiOSSDK/LayerSet.hpp>
#import <G3MiOSSDK/PlanetRenderer.hpp>
#import <G3MiOSSDK/ILocationModifier.hpp>
#import <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#import <G3MiOSSDK/CityGMLBuilding.hpp>


#include <G3MiOSSDK/IFactory.hpp>
#include <G3MiOSSDK/IXMLNode.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/G3MContext.hpp>
#include <G3MiOSSDK/DownloadPriority.hpp>
#include <G3MiOSSDK/TimeInterval.hpp>
#include <G3MiOSSDK/IStringUtils.hpp>
#include <G3MiOSSDK/CityGMLParser.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>

#include <G3MiOSSDK/EllipsoidalPlanet.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>

#include <G3MiOSSDK/TerrainTouchListener.hpp>
#include <G3MiOSSDK/ColorLegend.hpp>
#include <G3MiOSSDK/BuildingDataParser.hpp>

#include <G3MiOSSDK/CameraSingleDragHandler.hpp>
#include <G3MiOSSDK/CameraDoubleDragHandler.hpp>
#include <G3MiOSSDK/CameraRotationHandler.hpp>
#include <G3MiOSSDK/CameraDoubleTapHandler.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>
#include <G3MiOSSDK/OSMLayer.hpp>
#include <G3MiOSSDK/AbstractMesh.hpp>
#include <G3MiOSSDK/CityGMLBuildingTessellator.hpp>
#include <G3MiOSSDK/URL.hpp>

#include <G3MiOSSDK/HUDQuadWidget.hpp>
#include <G3MiOSSDK/HUDRenderer.hpp>
#include <G3MiOSSDK/HUDRelativePosition.hpp>
#include <G3MiOSSDK/HUDRelativeSize.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/HUDAbsolutePosition.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>
#include <G3MiOSSDK/FloatBuffer_iOS.hpp>
#include <G3MiOSSDK/GInitializationTask.hpp>

#include <G3MiOSSDK/CityGMLRenderer.hpp>
#include <G3MiOSSDK/SphericalPlanet.hpp>
#include <G3MiOSSDK/ElevationData.hpp>
#include <G3MiOSSDK/PointCloudMesh.hpp>
#include <G3MiOSSDK/Surface.hpp>


#import <QuartzCore/QuartzCore.h>

#import "AppDelegate.h"

#include <typeinfo>

class PointCloudEvolutionTask: public GTask{
  
  PointCloudMesh* _pcMesh;
  
  float _delta;
  int _step;
  ViewController* _vc;
  
  ColorLegend* _colorLegend;
  bool _using0Color;
public:
  
  PointCloudEvolutionTask(ViewController* vc):
  _pcMesh(NULL),
  _delta(0.0),
  _step(0),
  _vc(vc),
  _colorLegend(NULL),
  _using0Color(false)
  {
    
    std::vector<ColorLegend::ColorAndValue*> legend;
    legend.push_back(new ColorLegend::ColorAndValue(Color::black(), 0)); //Min
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(51, 31, 0, 255), 6)); //Percentile 10 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(128, 77, 0, 255), 21)); //Percentile 25 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(255, 153, 0, 255), 75)); //Mean (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(255, 204, 128, 255), 100)); //Percentile 75 (without 0's)
    legend.push_back(new ColorLegend::ColorAndValue(Color::white(), 806.0)); //Max
    _colorLegend = new ColorLegend(legend);
  }
  
  ~PointCloudEvolutionTask(){
    delete _colorLegend;
    
    [_vc removePointCloudMesh];
  }
  
  void run(const G3MContext* context){
    
    _step++;
    
    //#warning REMOVE DEMOSNTRATION FOR SCREENSHOTS
    //    _step = 59;
    
    NSString* folder = @"EIFER Resources/Solar Radiation/buildings_imp_table_0";
    
    if (_pcMesh == NULL){
      
      NSString *filePath = [[NSBundle mainBundle] pathForResource:@"vertices" ofType:@"csv" inDirectory:folder];
      if (filePath) {
        NSString *myText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
        
        _pcMesh = (PointCloudMesh*) BuildingDataParser::createSolarRadiationMeshFromCSV([myText UTF8String],
                                                                                        [[_vc G3MWidget] widget]->getG3MContext()->getPlanet(),
                                                                                        [_vc elevationData],
                                                                                        *_colorLegend);
        [_vc addPointCloudMesh:_pcMesh];
      }
      
    }
    
    if (_pcMesh != NULL){
      NSString* fileColors = [NSString stringWithFormat:@"values_t%d", _step, nil];
      NSString *filePath = [[NSBundle mainBundle] pathForResource:fileColors ofType:@"csv" inDirectory:folder];
      if (filePath) {
        NSString *myText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
        
        IFloatBuffer* colors = BuildingDataParser::createColorsForSolarRadiationMeshFromCSV([myText UTF8String], *_colorLegend);
        
        
        _pcMesh->changeToColors(colors);
        _using0Color = false;
      } else{
        if (!_using0Color){
          IFloatBuffer* colors = BuildingDataParser::create0ColorsForSolarRadiationMeshFromCSV(*_colorLegend, (int) _pcMesh->getVertexCount());
          _pcMesh->changeToColors(colors);
          _using0Color = true;
        }
      }
      
      int hour = _step;
      
      //Label
      std::string s = "Day " + context->getStringUtils()->toString(hour / 24) + " " +
      context->getStringUtils()->toString(hour % 24) + ":00";
      
      [_vc.timeLabel setHidden:FALSE];
      
      [[_vc timeLabel] setText:[NSString stringWithUTF8String:s.c_str()]];
    }
    
  }
  
};

class ColorChangingMeshTask: public GTask{
  
  AbstractMesh* _abstractMesh;
  int _step;
  std::vector<IFloatBuffer*> _colors;
public:
  
  ColorChangingMeshTask(AbstractMesh* abstractMesh, std::vector<IFloatBuffer*> colors):
  _abstractMesh(abstractMesh),
  _step(0),
  _colors(colors)
  {
    IFloatBuffer* meshColors = _abstractMesh->getColorsFloatBuffer();
    
    for (size_t i = 0; i < _colors.size(); i++) {
      if (colors[i]->size() != meshColors->size()){
        THROW_EXCEPTION("WRONG NUMBER OF COLORS");
      }
    }
  }
  
  void run(const G3MContext* context){
    IFloatBuffer* colors = _abstractMesh->getColorsFloatBuffer();
    IFloatBuffer* newColors = _colors[_step];
    
    colors->put(0, newColors);
    
    _step++; //Advance
  }
  
};

class MyEDCamConstrainer: public ICameraConstrainer {
  
  ElevationData* _ed;
public:
  
  MyEDCamConstrainer(ElevationData* ed):_ed(ed){}
  
  void setED(ElevationData* ed){
    _ed = ed;
  }
  
  //Returns false if it could not create a valid nextCamera
  virtual bool onCameraChange(const Planet* planet,
                              const Camera* previousCamera,
                              Camera* nextCamera) const{
    
    if (previousCamera->computeZNear() < 5){
      //We are using VR
      return true;
    }
    if (_ed != NULL){
      Geodetic3D g = nextCamera->getGeodeticPosition();
      Geodetic2D g2D = g.asGeodetic2D();
      if (_ed->getSector().contains(g2D)){
        double d = _ed->getElevationAt(g2D);
        const double limit = d + 1.1 * nextCamera->computeZNear();
        
        if (g._height < limit){
          nextCamera->copyFrom(*previousCamera, false);
        }
      }
    }
    return true;
  }
};

class MyEDListener: public IElevationDataListener{
  
  
private:
  ViewController* _demo;
  const IThreadUtils* _threadUtils;
  
public:
  
  MyEDListener(ViewController* demo, const IThreadUtils* threadUtils):_demo(demo), _threadUtils(threadUtils){}
  
  virtual void onData(const Sector& sector,
                      const Vector2I& extent,
                      ElevationData* elevationData){
    
    _demo.cityGMLRenderer->setElevationData(elevationData);
    
    [_demo camConstrainer]->setED(elevationData);
    
    [_demo setElevationData:elevationData];
    
    [_demo loadCityModel];
  }
  
  virtual void onError(const Sector& sector,
                       const Vector2I& extent){
    
  }
  
  virtual void onCancel(const Sector& sector,
                        const Vector2I& extent){
    
  }
};

class MyCityGMLBuildingTouchedListener : public CityGMLBuildingTouchedListener{
  ViewController* _vc;
public:
  
  MyCityGMLBuildingTouchedListener(ViewController* vc):_vc(vc){}
  
  virtual ~MyCityGMLBuildingTouchedListener(){}
  virtual void onBuildingTouched(CityGMLBuilding* building){
    std::string name = "ID: " + building->_name + "\n" + building->getPropertiesDescription();
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Building selected"
                                  message:[NSString stringWithUTF8String:name.c_str()]
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* yesButton = [UIAlertAction
                                actionWithTitle:@"Ok"
                                style:UIAlertActionStyleDefault
                                handler:^(UIAlertAction * action)
                                {
                                  //Handel your yes please button action here
                                  
                                }];
    
    [alert addAction:yesButton];
    
    
    
    UIAlertAction* SRButton = [UIAlertAction
                               actionWithTitle:@"Show Solar Radiation Data"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                 [_vc loadSolarRadiationPointCloudForBuilding:building];
                               }];
    
    [alert addAction:SRButton];
    
    [_vc presentViewController:alert animated:YES completion:nil];
  }
  
};



class MyInitTask: public GInitializationTask{
  bool _useDEM;
  ViewController* _vc;
public:
  
  MyInitTask(ViewController* vc, bool useDEM):_useDEM(useDEM), _vc(vc){
    
  }
  
  void run(const G3MContext* context){
    if (_useDEM){
      Sector karlsruheSector = Sector::fromDegrees(48.9397891179, 8.27643508429, 49.0930546874, 8.5431344933);
      SingleBilElevationDataProvider* edp = new SingleBilElevationDataProvider(URL("file:///ka_31467.bil"),
                                                                               karlsruheSector,
                                                                               Vector2I(308, 177));
      [_vc.G3MWidget widget]->getPlanetRenderer()->setElevationDataProvider(edp, true);
      
      edp->requestElevationData(karlsruheSector, Vector2I(308, 177), new MyEDListener(_vc, context->getThreadUtils()), true);
    } else{
      [_vc loadCityModel];
    }
  }
  
  bool isDone(const G3MContext* context){
    return true;
  }
  
};

class MyCityGMLRendererListener: public CityGMLRendererListener{
  ViewController* _vc;
public:
  MyCityGMLRendererListener(ViewController* vc):_vc(vc){}
  
  void onBuildingsLoaded(const std::vector<CityGMLBuilding*>& buildings){
    [_vc onCityModelLoaded];
    
#pragma mark UNCOMMENT TO SAVE MEMORY
    //Decreasing consumed memory
    for (size_t i = 0; i < buildings.size(); i++) {
      buildings[i]->removeSurfaceData();
    }
  }
  
};

class KarlsruheVirtualWalkLM: public ILocationModifier{
  const ElevationData* _ed;
  const Geodetic2D* _initialPosition;
public:
  KarlsruheVirtualWalkLM(const ElevationData* ed):
  _ed(ed),
  _initialPosition(NULL)
  {}
  
  ~KarlsruheVirtualWalkLM() {
    if (_initialPosition != NULL)
      delete _initialPosition;
  }
  
  Geodetic3D modify(const Geodetic3D& location){
    // code to virtually walk in Karlsruhe
    
    // the first time save GPS position
    if (_initialPosition == NULL){
      _initialPosition = new Geodetic2D(location._latitude, location._longitude);
    }
    
    // compute what I have walked from initial position
    const Geodetic2D Karlsruhe(Angle::fromDegrees(49.010), Angle::fromDegrees(8.394));
    const Geodetic2D incGeo = location.asGeodetic2D().sub(*_initialPosition);
    return Geodetic3D(Karlsruhe.add(incGeo.times(100)), 160);
  }
};


class AltitudeFixerLM: public ILocationModifier{
  const ElevationData* _ed;
  const Geodetic2D* _initialPosition;
public:
  AltitudeFixerLM(const ElevationData* ed):
  _ed(ed),
  _initialPosition(NULL)
  {}
  
  ~AltitudeFixerLM() {
    if (_initialPosition != NULL)
      delete _initialPosition;
  }
  
  Geodetic3D modify(const Geodetic3D& location){
    const double heightDEM = _ed->getElevationAt(location._latitude, location._longitude);
    if (location._height < heightDEM + 1.6){
      return Geodetic3D::fromDegrees(location._latitude._degrees,
                                     location._longitude._degrees,
                                     heightDEM + 1.6);
    }
    
    if (location._height > heightDEM + 25){
      return Geodetic3D::fromDegrees(location._latitude._degrees,
                                     location._longitude._degrees,
                                     heightDEM + 25);
    }
    
    return location;
  }
};

///////////////////

@implementation ViewController

@synthesize G3MWidget;
@synthesize meshRenderer;
@synthesize marksRenderer;
@synthesize meshRendererPC;
@synthesize cityGMLRenderer;
@synthesize elevationData;
@synthesize timeLabel;
@synthesize camConstrainer;
@synthesize vectorLayer;
@synthesize shapesRenderer;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

-(void) addCityGMLFile:(const std::string&) fileName needsToBeFixOnGround:(BOOL) fix{
  CityGMLModelFile m;
  m._fileName = fileName;
  m._needsToBeFixedOnGround = fix;
  _cityGMLFiles.push_back(m);
}

- (void)viewDidLoad
{
  [super viewDidLoad];
  
  elevationData = NULL;
  meshRenderer = NULL;
  marksRenderer = NULL;
  
  //VR;
  _prevPos = NULL;
  _prevHeading = NULL;
  _prevRoll = NULL;
  _prevPitch = NULL;
  
  _isMenuAvailable = false;
  
  _waitingMessageView.layer.cornerRadius = 5;
  _waitingMessageView.layer.masksToBounds = TRUE;
  
  G3MWidget.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleBottomMargin |
  UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin |
  UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
  
  [timeLabel setHidden:TRUE];
  
  _pickerArray = @[@"Random Colors", @"Heat Demand", @"Building Volume", @"GHG Emissions", @"Demographic Clusters (SOM)", @"Demographic Clusters (k-means)"];
  
  [self addCityGMLFile:"file:///innenstadt_ost_4326_lod2.gml" needsToBeFixOnGround:false];
  [self addCityGMLFile:"file:///innenstadt_west_4326_lod2.gml" needsToBeFixOnGround:false];
  [self addCityGMLFile:"file:///technologiepark_WGS84.gml" needsToBeFixOnGround:true];
  [self addCityGMLFile:"file:///hagsfeld_4326_lod2.gml" needsToBeFixOnGround:false];
  [self addCityGMLFile:"file:///durlach_4326_lod2_PART_1.gml" needsToBeFixOnGround:false];
  [self addCityGMLFile:"file:///durlach_4326_lod2_PART_2.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///hohenwettersbach_4326_lod2.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///bulach_4326_lod2.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///daxlanden_4326_lod2.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_1.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_2.gml" needsToBeFixOnGround:false];
  //      [self addCityGMLFile:"file:///knielingen_4326_lod2_PART_3.gml" needsToBeFixOnGround:false];
  
  _modelsLoadedCounter = 0;
  
  [_progressBar setProgress:0.0f];
  
  _useDem = true;
  [self initEIFERG3m:_useDem];
  
  //HIDING MENU
  [self showMenuButtonPressed:_showMenuButton];
  
  
  [[self G3MWidget] startAnimation];
  
  //Las Palmas de G.C.
  [G3MWidget widget]->setCameraPosition(Geodetic3D::fromDegrees(27.995258816253532075, -15.431324237687769951, 19995.736280026820168));
  [G3MWidget widget]->setCameraPitch(Angle::fromDegrees(-53.461659));
  
  
}

-(void) loadSolarRadiationPointCloudForBuilding:(CityGMLBuilding*) building{
  
  if (_buildingShowingPC != NULL){
    CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color::blue());
  }
  
  _buildingShowingPC = building;
  CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(_buildingShowingPC, Color::transparent());
  
  if (_pointCloudTask != NULL){
    [G3MWidget widget]->removeAllPeriodicalTasks();
  }
  _pointCloudTask = new PointCloudEvolutionTask(self);
  
  [G3MWidget widget]->addPeriodicalTask(new PeriodicalTask(TimeInterval::fromSeconds(0.1),
                                                           new PointCloudEvolutionTask(self)));
}

-(void) addPointCloudMesh:(Mesh*) pc{
  meshRendererPC->addMesh(pc);
}

-(void) removePointCloudMesh{
  meshRendererPC->clearMeshes();
  [timeLabel setHidden:TRUE];
}

-(void) useOSM:(BOOL) v{
  layerSet->removeAllLayers(true);
  if (v){
    OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
    layerSet->addLayer(layer);
  } else{
    BingMapsLayer* layer = new BingMapsLayer(BingMapType::Aerial(),
                                             "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                             TimeInterval::fromDays(30));
    layerSet->addLayer(layer);
  }
  
}


- (void)initEIFERG3m:(BOOL) useDEM
{
  
  G3MBuilder_iOS builder([self G3MWidget]);
  layerSet = new LayerSet();
  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  
  layerSet->addLayer(layer);
  
  _planet = SphericalPlanet::createEarth();
  builder.setPlanet(_planet);
  
  _hudRenderer = new HUDRenderer();
  
  builder.addRenderer(_hudRenderer);
  
  meshRenderer = new MeshRenderer();
  meshRendererPC = new MeshRenderer();
  builder.addRenderer(meshRendererPC);
  marksRenderer = new MarksRenderer(false);
  
  //Karlsruhe Schloss model
  shapesRenderer = new ShapesRenderer();
  builder.addRenderer(shapesRenderer);
  
  //Showing Footprints
  vectorLayer = new GEOVectorLayer(2,18,
                                   0,18,
                                   1.0f,
                                   new LevelTileCondition(17, 18));
  layerSet->addLayer(vectorLayer);
  
  cityGMLRenderer = new CityGMLRenderer(meshRenderer,
                                        NULL /* marksRenderer */,
                                        vectorLayer);
  
  cityGMLRenderer->setTouchListener(new MyCityGMLBuildingTouchedListener(self));
  
  builder.addRenderer(cityGMLRenderer);
  
  builder.setInitializationTask(new MyInitTask(self, useDEM));
  
  camConstrainer = new MyEDCamConstrainer(NULL); //Wait for ED to arrive
  builder.addCameraConstraint(camConstrainer);
  
  builder.setBackgroundColor(new Color(Color::fromRGBA255(0, 0, 0, 0)));
  
  builder.initializeWidget();
}

-(void) onCityModelLoaded{
  _modelsLoadedCounter++;
  if (_modelsLoadedCounter == _cityGMLFiles.size()){
    cityGMLRenderer->addBuildingDataFromURL(URL("file:///karlsruhe_data.geojson"));
  }
  [self onProgress];
}

-(void) loadCityModel{
  
  for (size_t i = 0; i < _cityGMLFiles.size(); i++) {
    cityGMLRenderer->addBuildingsFromURL(URL(_cityGMLFiles[i]._fileName),
                                         _cityGMLFiles[i]._needsToBeFixedOnGround,
                                         new MyCityGMLRendererListener(self),
                                         true);
  }
}

-(void) onProgress {
  //N MODELS + 1 POINT CLOUD
  float p = (float)(_modelsLoadedCounter) / ((float)_cityGMLFiles.size());
  [_progressBar setProgress: p animated:TRUE];
  
  if (p == 1){
    [self onAllDataLoaded];
  }
}

-(void) onAllDataLoaded{
  ILogger::instance()->logInfo("City Model Loaded");
  
  const bool includeCastleModel = true;
  if (includeCastleModel){
    class SchlossListener : public ShapeLoadListener {
    public:
      SchlossListener()
      {
      }
      
      void onBeforeAddShape(SGShape* shape) {
        shape->setPitch(Angle::fromDegrees(90));
      }
      
      void onAfterAddShape(SGShape* shape) {
        shape->setScale(250);
        //      shape->setHeading(Angle::fromDegrees(-4));
      }
    };
    
    shapesRenderer->loadJSONSceneJS(URL("file:///k_s/k_schloss.json"),
                                    "file:///k_s/",
                                    false, // isTransparent
                                    new Geodetic3D(Geodetic3D::fromDegrees(49.013500, 8.404249, 117.82)), //
                                    ABSOLUTE,
                                    new SchlossListener());
    
    std::string v[] = {"91214493", "23638639", "15526553", "15526562", "15526550", "13956101", "156061723", "15526578"};
    
    for (int i = 0; i < 8; i++){
      CityGMLBuilding* b = cityGMLRenderer->getBuildingWithName(v[i]);
      if (b != NULL){
        CityGMLBuildingTessellator::changeColorOfBuildingInBoundedMesh(b, Color::transparent());
      }
    }
    
    
  }
  
  //Whole city!
  [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
                                                Geodetic3D::fromDegrees(49.07139214735035182, 8.134019638291379195, 22423.46165080198989),
                                                Angle::fromDegrees(-109.452892),
                                                Angle::fromDegrees(-44.938813)
                                                );
  
  //NO WAITING ANYMORE
  _waitingMessageView.hidden = TRUE;
  _isMenuAvailable = true;
  
  printf("N OF WALLS: %d\n", numberOfWalls);
  printf("N OF TESSELLATED WALLS: %d\n", numberOfP3D);
  printf("N OF TESSELLATED WALLS_4: %d\n", numberOfP3D_4);
}

- (IBAction)switchCityGML:(id)sender {
  bool viewBuildings = [((UISwitch*) sender) isOn];
  
  if (viewBuildings){
    cityGMLRenderer->setEnable(true);
    [_dataPicker setUserInteractionEnabled:TRUE];
    
  } else{
    cityGMLRenderer->setEnable(false);
    [_dataPicker setUserInteractionEnabled:FALSE];
  }
}

- (IBAction)switchSolarRadiationPC:(id)sender {
  bool viewPC = [((UISwitch*) sender) isOn];
  
  meshRendererPC->setEnable(viewPC);
}

-(void) activateMapMode{
  
  [self useOSM:TRUE];
  
  _headerView.hidden = FALSE;
  
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [G3MWidget widget]->setViewMode(MONO);
  
  CameraRenderer* cameraRenderer = [G3MWidget widget]->getCameraRenderer();
  cameraRenderer->clearHandlers();
  
  //Restoring prev cam
  const Camera* cam = [G3MWidget widget]->getCurrentCamera();
  [G3MWidget widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(2),
                                                cam->getGeodeticPosition(), *_prevPos,
                                                cam->getHeading(), *_prevHeading,
                                                cam->getPitch(), *_prevPitch);
  delete _prevPitch;
  _prevPitch = NULL;
  delete _prevHeading;
  _prevHeading = NULL;
  delete _prevRoll;
  _prevRoll = NULL;
  delete _prevPos;
  _prevPos = NULL;
  
  
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
  [G3MWidget widget]->getNextCamera()->forceZNear(NAND);
}

-(void) activateMonoVRMode{
  
  [self useOSM:FALSE];
  
  _headerView.hidden = FALSE;
  
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [G3MWidget widget]->setViewMode(MONO);
  [_camVC enableVideo:FALSE];
  [self activateDeviceAttitudeTracking];
  
  
  [G3MWidget widget]->getNextCamera()->setFOV(Angle::nan(), Angle::nan());
}

-(void) activateDeviceAttitudeTracking{
  
  CameraRenderer* cameraRenderer = [G3MWidget widget]->getCameraRenderer();
  cameraRenderer->clearHandlers();
  
  //Storing prev cam
  if (_prevPos == NULL){
    const Camera* cam = [G3MWidget widget]->getCurrentCamera();
    _prevPos = new Geodetic3D(cam->getGeodeticPosition());
    _prevRoll = new Angle(cam->getRoll());
    _prevPitch = new Angle(cam->getPitch());
    _prevHeading = new Angle(cam->getHeading());
  }
  
  ILocationModifier * lm = new AltitudeFixerLM([self elevationData]);
  
  DeviceAttitudeCameraHandler* dac = new DeviceAttitudeCameraHandler(true, lm);
  cameraRenderer->addHandler(dac);
  
  [G3MWidget widget]->getNextCamera()->forceZNear(0.1);
  
  //Theoretical horizontal FOV
  float hFOVDegrees = [_camVC fieldOfViewInDegrees];
  [G3MWidget widget]->getNextCamera()->setFOV(Angle::nan(), Angle::fromDegrees(hFOVDegrees));
}

-(void) activateStereoVRMode{
  
  [self useOSM:FALSE];
  
  _headerView.hidden = TRUE;
  [G3MWidget widget]->getPlanetRenderer()->setEnable(true);
  
  [_camVC enableVideo:FALSE];
  
  [G3MWidget widget]->setViewMode(STEREO);
  [G3MWidget widget]->setInterocularDistanceForStereoView(0.03); //VR distance between eyes
  
  //Forcing orientation
  NSNumber *value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
  [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
  
  [self activateDeviceAttitudeTracking];
}

-(void) activateARMode{
  
  _headerView.hidden = FALSE;
  
  [_camVC enableVideo:TRUE];
  [G3MWidget widget]->getPlanetRenderer()->setEnable(false);
  [G3MWidget widget]->setViewMode(MONO);
  [self activateDeviceAttitudeTracking];
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
  
  const bool usingStereo = [G3MWidget widget]->getViewMode() == STEREO;
  
  //FORCE ORTIENTATION FOR STEREO
  if (usingStereo && interfaceOrientation != UIInterfaceOrientationLandscapeLeft){
    return FALSE;
  }
  
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  } else {
    return YES;
  }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
  if ([segue.identifier isEqualToString:@"cameraViewSegue"])
    _camVC = (CameraViewController *)segue.destinationViewController;
  
}

//// MENU
- (IBAction)modeChanged:(UISegmentedControl *)sender {
  
  switch (sender.selectedSegmentIndex){
    case 0:
      [self activateMapMode];
      break;
    case 1:
      [self activateMonoVRMode];
      break;
    case 2:
      [self activateStereoVRMode];
      break;
    case 3:
      [self activateARMode];
      break;
  }
}


- (IBAction)showMenuButtonPressed:(id)sender {
  
  if (_menuHeightConstraint.constant == 0){
    
    UIImage* image = [UIImage imageNamed:@"down"];
    [_showMenuButton setImage:image forState:UIControlStateNormal];
    
    _menuHeightConstraint.constant = - _menuView.bounds.size.height + _showMenuButton.bounds.size.height;
    
    //Gradient background
    CAGradientLayer *gradient = [CAGradientLayer layer];
    gradient.frame = CGRectMake(_menuView.bounds.origin.x, _menuView.bounds.origin.y,
                                _menuView.bounds.size.width*3, _menuView.bounds.size.height);    gradient.colors = [NSArray arrayWithObjects:(id)[[UIColor clearColor] CGColor], (id)[[UIColor whiteColor] CGColor], nil];
    [_menuView.layer insertSublayer:gradient atIndex:0];
    
  } else{
    if (!_isMenuAvailable){
      return;
    }
    
    UIImage* image = [UIImage imageNamed:@"up"];
    [_showMenuButton setImage:image forState:UIControlStateNormal];
    _menuHeightConstraint.constant = 0;
  }
  
  [UIView animateWithDuration:1 animations:^{
    [self.view layoutIfNeeded];
  }];
}



/////PICKER VIEW

// The number of columns of data
- (int)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
  return 1;
}

// The number of rows of data
- (int)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
  return (int)_pickerArray.count;
}

// The data to return for the row and component (column) that's being passed in
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
  return _pickerArray[row];
}

// Catpure the picker view selection
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
  if (row == 0){
    RandomBuildingColorPicker* rcp = new RandomBuildingColorPicker();
    cityGMLRenderer->colorBuildings(rcp);
    delete rcp;
  }
  
  
  //  if (row == 1){
  //
  //    std::vector<ColorLegend::ColorAndValue*> legend;
  //    legend.push_back(new ColorLegend::ColorAndValue(Color::blue(), 6336.0));
  //    legend.push_back(new ColorLegend::ColorAndValue(Color::red(), 70000.0));
  //    ColorLegend* cl = new ColorLegend(legend);
  //    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Heat_Dem_1", cl);
  //    cityGMLRenderer->colorBuildings(colorProvider);
  //    delete colorProvider;
  //
  //  }
  
  if (row == 1){
    
    cityGMLRenderer->colorBuildingsWithColorBrewer("Heat Demand", "Pastel1", 8);
    
    //    std::vector<double> vs = cityGMLRenderer->getAllValuesOfProperty("Bui_Volu_1");
    //    ColorLegend* cl = ColorLegendHelper::createColorBrewLegendWithNaturalBreaks(vs, "BuGn", 4);
    //    BuildingDataColorProvider* colorProvider = new BuildingDataColorProvider("Bui_Volu_1", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
    
    //    std::vector<ColorLegend::ColorAndValue*> legend;
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), 2196.0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 6816.0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(254,224,139, 255), 17388.0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(230,245,152, 255), 33165.0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(153,213,148, 255), 62472.0));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(50,136,189, 255), 122553.0));
    //    ColorLegend* cl = new ColorLegend(legend);
    //
    //    BuildingDataColorProvider* colorProvider = new BuildingDataColorProvider("Bui_Volu_1", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
  }
  
  if (row == 2){
    
    //    int nClasses = 18;
    //    int colors[] = {103,0,31,
    //      178,24,43,
    //      214,96,77,
    //      244,165,130,
    //      253,219,199,
    //      247,247,247,
    //      209,229,240,
    //      146,197,222,
    //      67,147,195,
    //      33,102,172,
    //      5,48,97,
    //      171,221,164,
    //      102,194,165,
    //      50,136,189,
    //      94,79,162,
    //      227,26,28,
    //      253,191,111,
    //      255,127,0};
    //
    //    std::vector<ColorLegend::ColorAndValue*> legend;
    //    for (int i = 0; i < nClasses;i++) {
    //      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
    //                                                                         colors[i*3+1],
    //                                                                         colors[i*3+2],
    //                                                                         255),
    //                                                      (double)(i+1)));
    //    }
    //
    //    ColorLegend* cl = new ColorLegend(legend);
    //    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("QCL_1", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
    
    cityGMLRenderer->colorBuildingsWithColorBrewer("Building Volume", "Pastel1", 8);
    
  }
  
  if (row == 3){
    
    cityGMLRenderer->colorBuildingsWithColorBrewer("GHG Emissions", "Pastel1", 8);
    //
    //    std::vector<ColorLegend::ColorAndValue*> legend;
    //
    //    int nClasses = 15;
    //    int colors[] = {103,0,31,
    //      178,24,43,
    //      214,96,77,
    //      244,165,130,
    //      253,219,199,
    //      247,247,247,
    //      209,229,240,
    //      146,197,222,
    //      67,147,195,
    //      33,102,172,
    //      5,48,97,
    //      166,219,160,
    //      90,174,97,
    //      27,120,55,
    //      0,68,27};
    //
    //    for (int i = 0; i < nClasses;i++) {
    //      legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(colors[i*3],
    //                                                                         colors[i*3+1],
    //                                                                         colors[i*3+2],
    //                                                                         255),
    //                                                      (double)(i+1)));
    //    }
    //
    //    ColorLegend* cl = new ColorLegend(legend);
    //    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("SOMcluster", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
    
  }
  
  if (row == 4){
    
    cityGMLRenderer->colorBuildingsWithColorBrewer("Demographic Clusters (SOM)", "Pastel1", 8);
    
    //    std::vector<ColorLegend::ColorAndValue*> legend;
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), -0.05));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 0.05));
    //
    //    ColorLegend* cl = new ColorLegend(legend);
    //    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Field2_12", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
    
  }
  
  if (row == 5){
    
    cityGMLRenderer->colorBuildingsWithColorBrewer("Demographic Clusters (k-Means)", "Pastel1", 8);
    
    //    std::vector<ColorLegend::ColorAndValue*> legend;
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(213,62,79, 255), -0.05));
    //    legend.push_back(new ColorLegend::ColorAndValue(Color::fromRGBA255(252,141,89, 255), 0.05));
    //
    //    ColorLegend* cl = new ColorLegend(legend);
    //    CityGMLBuildingColorProvider* colorProvider = new BuildingDataColorProvider("Field2_12", cl);
    //    cityGMLRenderer->colorBuildings(colorProvider);
    //    delete colorProvider;
    
  }
}

@end
