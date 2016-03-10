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
//#import <G3MiOSSDK/MapBooBuilder_iOS.hpp>
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
//#import <G3MiOSSDK/CartoCSSParser.hpp>
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
#import <G3MiOSSDK/CompositeRenderer.hpp>

#import <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>

#import <G3MiOSSDK/Quaternion.hpp>
#import <G3MiOSSDK/FlatPlanet.hpp>

#import "StarDomeRenderer.hpp"
#import "StarsParser.hpp"

#include <typeinfo>

#import "AppDelegate.h"

#import "MenuViewController.h"

#import <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#import <G3MiOSSDK/LayerSet.hpp>

@implementation ViewController

@synthesize G3MWidget;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

Geodetic3D* cameraPositionForStars = NULL;

MeshRenderer* mr;
const Planet* planet;

- (void)viewDidLoad
{
  [super viewDidLoad];
  
  cameraPositionForStars = new Geodetic3D(Geodetic3D::fromDegrees(27.973105, -15.597545, 1000));
  
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
  
  /*
   class RotateStarsTask: public GTask{
   ViewController* _vc;
   public:
   RotateStarsTask(ViewController* vc):_vc(vc){}
   
   void run(const G3MContext* context){
   [_vc rotateStars];
   }
   
   };
   */
  
  G3MBuilder_iOS builder([self G3MWidget]);
  
  LayerSet* layerSet = new LayerSet();
  
  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  layerSet->addLayer(layer);
  builder.getPlanetRendererBuilder()->setLayerSet(layerSet);
  
  
  DeviceAttitudeCameraHandler* dach = new DeviceAttitudeCameraHandler(false);
  CameraRenderer* cr = new CameraRenderer();
  cr->addHandler(dach);
  builder.setCameraRenderer(cr);
  
  //builder.addPeriodicalTask(new PeriodicalTask(TimeInterval::fromSeconds(0.1), new RotateStarsTask(self)));
  
  planet = FlatPlanet::createEarth();
  
  
  mr = new MeshRenderer();
  builder.addRenderer(mr);
  
  AppDelegate* delegate = [UIApplication sharedApplication].delegate;
  for (int i = 0; i < 5; i++) {
    if ([delegate areStarsActive:i]){
      
      NSString* sfn = [NSString stringWithFormat:@"stars%d", i];
      NSString* slfn = [NSString stringWithFormat:@"stars_links%d", i];
      
      Renderer* stars1 = [self readStars: &builder
                       withStarsFileName: sfn
                       withLinksFileName: slfn];
      builder.addRenderer(stars1);
    }
  }

  
  [self createHorizonLine:&builder];
  
  if (delegate.showingGalaxies){
    [self createGalaxies:&builder];
  }
  
  builder.setPlanet(planet);
  builder.initializeWidget();
  [[self G3MWidget] startAnimation];
  
  [G3MWidget widget]->getPlanetRenderer()->setEnable(false);
  
  [G3MWidget widget]->setCameraPosition(*cameraPositionForStars);
//  [G3MWidget widget]->setCameraPitch(Angle::fromDegrees(0));
//  [G3MWidget widget]->setCameraHeading(Angle::fromDegrees(30));
  
}

std::vector<StarDomeRenderer*> _sdrs;

/*
 - (void) rotateStars{
 
 for (int i = 0; i < _sdrs.size(); i++){
 _sdrs[i]->setClockTimeInDegrees(_sdrs[i]->getCurrentClockTimeInDegrees() + 0.6);
 _sdrs[i]->clear();
 
 }
 
 }
 */

- (void) createGalaxies: (IG3MBuilder*) builder{
  
  MarksRenderer* galaxies = new MarksRenderer(true);
  builder->addRenderer(galaxies);
  
  Vector3D center = planet->toCartesian(*cameraPositionForStars);
  double domeHeight = 1e5;
  //  Vector3D vx = Vector3D::upX().times(domeHeight).add(Vector3D::upZ().times(5e3));
  
  Vector3D vx = Star::getStarDisplacementInDome(domeHeight, Angle::fromDegrees(0), Angle::fromDegrees(0));
  
  Geodetic3D g = planet->toGeodetic3D(vx);
  
  Mark* n = new   Mark(URL("file:///galaxy.png"),
                       g,
                       ABSOLUTE);
  
  galaxies->addMark(n);
}


//-(void) showGalaxies: (BOOL) v{
//  galaxies->setEnable(v);
//}

-(void) createHorizonLine: (IG3MBuilder*) builder{
  
  MeshRenderer* mr = new MeshRenderer();
  builder->addRenderer(mr);
  
  double domeHeight = 1e5;
  Vector3D center = planet->toCartesian(*cameraPositionForStars);
  
  FloatBufferBuilderFromCartesian3D* fbb = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  
  FloatBufferBuilderFromColor cfbb;
  
  MarksRenderer* marks = new MarksRenderer(false);
  builder->addRenderer(marks);
  
  Vector3D up = Vector3D::upZ().times(3e3);
  
  for(int i = 0; i < 360; i+=5){
    
    Vector3D pos = Star::getStarDisplacementInDome(domeHeight, Angle::fromDegrees(i), Angle::fromDegrees(10));
    
    fbb->add(pos);
    fbb->add(pos);
    
    if (i % 10 == 0){
      cfbb.add(1.0, 0.0, 0.0, 1.0);
      cfbb.add(1.0, 0.0, 0.0, 0.0);
    } else{
      cfbb.add(1.0, 0.0, 0.0, 0.0);
      cfbb.add(1.0, 0.0, 0.0, 1.0);
    }
    
    if (i == 0){
      Mark* n = new Mark("N", planet->toGeodetic3D(pos.add(center).add(up)), ABSOLUTE, 4.5e+06, 40);
      marks->addMark(n);
    }
    if (i == 270){
      Mark* n = new Mark("W", planet->toGeodetic3D(pos.add(center).add(up)), ABSOLUTE, 4.5e+06, 40);
      marks->addMark(n);
    }
    if (i == 180){
      Mark* n = new Mark("S", planet->toGeodetic3D(pos.add(center).add(up)), ABSOLUTE, 4.5e+06, 40);
      marks->addMark(n);
    }
    if (i == 90){
      Mark* n = new Mark("E", planet->toGeodetic3D(pos.add(center).add(up)), ABSOLUTE, 4.5e+06, 40);
      marks->addMark(n);
    }
    
  }
  
  Mesh* m = new DirectMesh(GLPrimitive::lineLoop(), true,
                           center, fbb->create(),
                           6.0, 10.0,
                           NULL,
                           cfbb.create());
  
  mr->addMesh(m);
  
  
  delete fbb;
  
}

-(Renderer*) readStars: (IG3MBuilder*) builder withStarsFileName: (NSString*) starsFile withLinksFileName: (NSString*) linksFile {
  
  //CALENDAR STUFF
  
  NSDate *today = [NSDate date];
  NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
  NSDateComponents *weekdayComponents =
  [gregorian components:(NSHourCalendarUnit | NSMinuteCalendarUnit | NSSecondCalendarUnit | NSDayCalendarUnit) fromDate:today];
  
  NSInteger h = [weekdayComponents hour];
  NSInteger m = [weekdayComponents minute];
  NSInteger s = [weekdayComponents second];
  
  NSUInteger dayOfYear = [gregorian ordinalityOfUnit:NSDayCalendarUnit
                                              inUnit:NSYearCalendarUnit forDate:[NSDate date]];
  
  /*
   //FOR TESTING
   h = 23;
   m = 21;
   s = 15;
   dayOfYear = 141;
   */
  
#warning VALID ONLY FOR CANARIAN TIMEZONE
  double hoursOffset = [[NSTimeZone localTimeZone] daylightSavingTimeOffset] / 36000;
  h -= hoursOffset;
  h--; //Por estar en canarias
  
  double clockTimeInDegrees =  Angle::fromClockHoursMinutesSeconds(h, m, s)._degrees;
  
  //////
  
  
  NSString *csvPath = [[NSBundle mainBundle] pathForResource: starsFile ofType: @"csv"];
  
  NSString *csvLinksPath = [[NSBundle mainBundle] pathForResource: linksFile ofType: @"csv"];
  
  //Geodetic3D gcPosition = Geodetic3D::fromDegrees(28.1, -15.43, 500);
  
  //  double clockTimeInDegrees = [self getClockTimeInDegrees];
  
  CompositeRenderer* cr = new CompositeRenderer();
  
  if (csvPath) {
    NSString *csv = [NSString stringWithContentsOfFile: csvPath
                                              encoding: NSMacOSRomanStringEncoding
                                                 error: nil];
    
    NSString *csv2 = [NSString stringWithContentsOfFile: csvLinksPath
                                               encoding: NSMacOSRomanStringEncoding
                                                  error: nil];
    
    std::vector<Constellation> cs = [StarsParser parse:csv withLinks:csv2];
    
    
    for(unsigned int i = 0; i < cs.size(); i++){
      Constellation c = cs[i];
      
      
      MarksRenderer* mr = new MarksRenderer(false);
      builder->addRenderer(mr);
      
      StarDomeRenderer* sdr = new StarDomeRenderer(c._name, c._stars, c._lines, *cameraPositionForStars, clockTimeInDegrees, dayOfYear, *c._color, mr);
//      builder->addRenderer(sdr);
      
//      _sdrs.push_back(sdr);
      
      cr->addRenderer(sdr);
    }
    
  }
  
  return cr;
  
}

-(NSUInteger)supportedInterfaceOrientations {
  return UIInterfaceOrientationMaskAll;
}


-(BOOL)shouldAutorotate {
  return YES;
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
  //  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
  //    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  //  } else {
  //    return YES;
  //  }
  return YES;
}

-(IBAction)showMenu:(id)sender{
  [self performSegueWithIdentifier:@"show_menu" sender:self];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
  if ([[segue identifier] isEqualToString:@"show_menu"])
  {
    MenuViewController* destination = [segue destinationViewController];
    destination._theVC = self;
  }
}


@end
