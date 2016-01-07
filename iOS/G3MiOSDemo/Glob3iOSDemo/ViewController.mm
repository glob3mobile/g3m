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
#import <G3MiOSSDK/TileRenderingListener.hpp>
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
#import <G3MiOSSDK/ITimer.hpp>

#import <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>

#include <typeinfo>

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
  
  //[self initCustomizedWithBuilder];
  
  //[self initTestingTileImageProvider];
  
  [self initWithNonOverlappingMarks];
  
  [[self G3MWidget] widget]->setCameraPosition(Geodetic3D::fromDegrees(0,0,3.2e7));
  
  
  //  [self initWithMapBooBuilder];
  
  //  [self initWithBuilderAndSegmentedWorld];
  
  //  [[self G3MWidget] widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
  //                                                       Geodetic3D::fromDegrees(25.743467472995700263,
  //                                                                               -5.3656762990500403987,
  //                                                                               1664155.1381164737977),
  //                                                       Angle::fromDegrees(-0.145718),
  //                                                       Angle::fromDegrees(-52.117699));
  
  
  //#warning Buggy mark
  //  [[self G3MWidget] widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
  //                                                       Geodetic3D::fromDegrees(47.3665119223405,
  //                                                                               19.251949160207758,
  //                                                                               1076.892613024946),
  //                                                       Angle::fromDegrees(-5.714247),
  //                                                       Angle::fromDegrees(-5.297620));
  
  //    [[self G3MWidget] widget]->setAnimatedCameraPosition(TimeInterval::fromSeconds(5),
  //                                                         Geodetic3D::fromDegrees(40.3323148480663158,
  //                                                                                 -5.5216079822178570,
  //                                                                                 1000076.892613024946));
  //
  [[self G3MWidget] startAnimation];
  
  /*
   [[self G3MWidget] widget]->addPeriodicalTask(TimeInterval::fromMilliseconds(100),
   new CameraRollChangerTask([[self G3MWidget] widget]));
   */
  
}
//
//- (void) initWithNonOverlappingMarks
//{
//  G3MBuilder_iOS builder([self G3MWidget]);
//
//  Vector2D::intersectionOfTwoLines(Vector2D(0,0), Vector2D(10,10),
//                                   Vector2D(10,0), Vector2D(-10, 10));
//
//  LayerSet* layerSet = new LayerSet();
//  layerSet->addLayer(MapQuestLayer::newOSM(TimeInterval::fromDays(30)));
//  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
//
//  NonOverlappingMarksRenderer* nomr = new NonOverlappingMarksRenderer(30);
//  builder.addRenderer(nomr);
//
//  class MyMarkWidgetTouchListener: public NonOverlappingMarkTouchListener{
//  public:
//    MyMarkWidgetTouchListener(){
//
//    }
//
//    bool touchedMark(const NonOverlappingMark* mark,
//                     const Vector2F& touchedPixel){
//      NSString* message = [NSString stringWithFormat: @"Canarias!"];
//      UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Island Selected"
//                                                      message:message
//                                                     delegate:nil
//                                            cancelButtonTitle:@"OK"
//                                            otherButtonTitles:nil];
//      [alert show];
//      return true;
//    }
//  };
//
//  Geodetic3D canarias[] = { Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
//  Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
//  Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
//  Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
//  Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
//  Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
//  Geodetic3D::fromDegrees(27.810709, -17.917639, 0)
//  };
//
//  NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                    new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                    Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
//                                                    new MyMarkWidgetTouchListener(),
//                                                    10.0);
//  nomr->addMark(mark);
//
//  NonOverlappingMark* mark2 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     10.0);
//  nomr->addMark(mark2);
//
//  NonOverlappingMark* mark3 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     10.0);
//  nomr->addMark(mark3);
//
//  NonOverlappingMark* mark4 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     10.0);
//  nomr->addMark(mark4);
//
//  NonOverlappingMark* mark5 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     10.0);
//  nomr->addMark(mark5);
//
//  NonOverlappingMark* mark6 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     10.0);
//  nomr->addMark(mark6);
//
//  NonOverlappingMark* mark7 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                     new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                     Geodetic3D::fromDegrees(27.810709, -17.917639, 0),
//                                                     new MyMarkWidgetTouchListener(),
//                                                     100.0);
//  nomr->addMark(mark7);
//
//  for(int i = 0; i < 50; i++){
//
//    double lat = ((rand() % 18000) - 9000) / 100.0;
//    double lon = ((rand() % 36000) - 18000) / 100.0;
//
//    NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
//                                                      new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
//                                                      Geodetic3D::fromDegrees(lat, lon, 0),
//                                                      NULL,
//                                                      100.0);
//    nomr->addMark(mark);
//  }
//
//  //nomr->setEnable(false);
//
//  builder.initializeWidget();
//}


class AnalyzerNOMSL: public NonOverlappingMarksStoppedListener{
  NonOverlappingMarksRenderer* _nomr;
  long long _lastTime;
  int _nAttempt = 0;
public:
  
  AnalyzerNOMSL(NonOverlappingMarksRenderer* nomr):_nomr(nomr), _lastTime(0), _nAttempt(0){
    
  }
  
  void reset(int n){
    _nomr->removeAllMarks();
    for(int i = 0; i < n; i++){
      
      double lat = ((rand() % 18000) - 9000) / 100.0;
      double lon = ((rand() % 36000) - 18000) / 100.0;
      
      NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
                                                        new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
                                                        Geodetic3D::fromDegrees(lat, lon, 0),
                                                        NULL,
                                                        100.0);
      _nomr->addMark(mark);
    }
  }
  
  void onMarksStopped(const G3MRenderContext& rc, const std::vector<NonOverlappingMark*>& visible){
    
    const long long t = rc.getFrameStartTimer()->nowInMilliseconds();
    
    double minDis = 9999999999999;
    for(size_t i = 0; i < visible.size(); i++){
      NonOverlappingMark* m1 = visible[i];
      for(size_t j = i+1; j < visible.size(); j++){
        NonOverlappingMark* m2 = visible[j];
        
        double d = m1->getScreenPos().sub(m2->getScreenPos()).length();
        if (d < minDis){
          minDis = d;
        }
      }
    }
    
    //printf("N MARKS \t %d \t MIN DIS \t %f \t T: \t %lld\n", (int)visible.size(), minDis, t - _lastTime);
    
    _lastTime = t;
    
    _nAttempt++;
    if (_nAttempt > 0){
        printf("N MARKS \t %d TM: \t %f TR: \t %f\n", (int)visible.size(),
               ((double)_nomr->_timeSpentRepositioningInMS / _nomr->_frames),
               ((double)_nomr->_timeSpentRenderingInMS / _nomr->_frames));
      _nomr->_timeSpentRepositioningInMS = 0;
      _nomr->_timeSpentRenderingInMS = 0;
      _nomr->_frames = 0;
      
      
      reset((int)visible.size() + 1);
      _nAttempt=0;
      

      
    } else{
      reset((int)visible.size());
    }
  }
  
};


- (void) initWithNonOverlappingMarks
{
  G3MBuilder_iOS builder([self G3MWidget]);
  
  LayerSet* layerSet = new LayerSet();
  layerSet->addLayer(MapQuestLayer::newOSM(TimeInterval::fromDays(30)));
  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  
  NonOverlappingMarksRenderer* nomr = new NonOverlappingMarksRenderer(100);
  
  nomr->addStoppedListener(new AnalyzerNOMSL(nomr));
  
  builder.addRenderer(nomr);
  
  builder.setPlanet(Planet::createFlatEarth());
  
  for(int i = 0; i < 1; i++){
    
    double lat = ((rand() % 18000) - 9000) / 100.0;
    double lon = ((rand() % 36000) - 18000) / 100.0;
    
    NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
                                                      new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
                                                      Geodetic3D::fromDegrees(lat, lon, 0),
                                                      NULL,
                                                      100.0);
    nomr->addMark(mark);
  }
  /*
   class MyMarkWidgetTouchListener: public NonOverlappingMarkTouchListener{
   public:
   MyMarkWidgetTouchListener(){
   
   }
   
   bool touchedMark(const NonOverlappingMark* mark,
   const Vector2F& touchedPixel){
   NSString* message = [NSString stringWithFormat: @"Canarias!"];
   UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Island Selected"
   message:message
   delegate:nil
   cancelButtonTitle:@"OK"
   otherButtonTitles:nil];
   [alert show];
   return true;
   }
   };
   
   Geodetic3D canarias[] = { Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
   Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
   Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
   Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
   Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
   Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
   Geodetic3D::fromDegrees(27.810709, -17.917639, 0)
   };
   
   NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.131817, -15.440219, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark);
   
   NonOverlappingMark* mark2 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.947345, -13.523105, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark2);
   
   NonOverlappingMark* mark3 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.473802, -13.859360, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark3);
   
   NonOverlappingMark* mark4 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.467706, -16.251426, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark4);
   
   NonOverlappingMark* mark5 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.701819, -17.762003, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark5);
   
   NonOverlappingMark* mark6 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(28.086595, -17.105796, 0),
   new MyMarkWidgetTouchListener(),
   10.0);
   nomr->addMark(mark6);
   
   NonOverlappingMark* mark7 = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(27.810709, -17.917639, 0),
   new MyMarkWidgetTouchListener(),
   100.0);
   nomr->addMark(mark7);
   
   for(int i = 0; i < 50; i++){
   
   double lat = ((rand() % 18000) - 9000) / 100.0;
   double lon = ((rand() % 36000) - 18000) / 100.0;
   
   NonOverlappingMark* mark = new NonOverlappingMark(new DownloaderImageBuilder(URL("file:///g3m-marker.png")),
   new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
   Geodetic3D::fromDegrees(lat, lon, 0),
   NULL,
   100.0);
   nomr->addMark(mark);
   }
   
   //nomr->setEnable(false);
   */
  builder.initializeWidget();
}



- (void) initCustomizedWithBuilder
{
  G3MBuilder_iOS builder([self G3MWidget]);
  
  builder.initializeWidget();
  
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
