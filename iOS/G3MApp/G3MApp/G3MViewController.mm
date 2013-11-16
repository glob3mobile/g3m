//
//  G3MViewController.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MViewController.h"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/G3MWidget.hpp>
#import <G3MiOSSDK/G3MBuilder_iOS.hpp>
#import <G3MiOSSDK/NSString_CppAdditions.h>
//#import <G3MiOSSDK/GInitializationTask.hpp>
//#import <G3MiOSSDK/PlanetRendererBuilder.hpp>
//#import <G3MiOSSDK/LayerBuilder.hpp>
//#import <G3MiOSSDK/MarksRenderer.hpp>
//#import <G3MiOSSDK/ShapesRenderer.hpp>
//#import <G3MiOSSDK/MeshRenderer.hpp>
//#import <G3MiOSSDK/MercatorTiledLayer.hpp>
//#import <G3MiOSSDK/LayerTilesRenderParameters.hpp>
//#import <G3MiOSSDK/MapQuestLayer.hpp>
//#import <G3MiOSSDK/SingleBillElevationDataProvider.hpp>
//#include <G3MiOSSDK/TimeInterval.hpp>

#import "G3MToolbar.h"
#import "G3MWebViewController.h"

#import "G3MSelectDemoSceneViewController.h"

#include "G3MDemoBuilder_iOS.hpp"
#include "G3MDemoModel.hpp"
#include "G3MDemoScene.hpp"
#include "G3MDemoListener.hpp"

@interface G3MViewController ()

@end

@implementation G3MViewController

@synthesize g3mWidget    = _g3mWidget;
@synthesize demoSelector = _demoSelector;
//@synthesize demoMenu      = _demoMenu;
//@synthesize toolbar       = _toolbar;
//@synthesize layerSelector = _layerSelector;
//@synthesize layerMenu     = _layerMenu;

//- (id)initWithNibName:(NSString *)nibNameOrNil
//               bundle:(NSBundle *)nibBundleOrNil
//{
//  self = [super initWithNibName:nibNameOrNil
//                         bundle:nibBundleOrNil];
//  if (self) {
//    _demoModel = NULL;
//  }
//  return self;
//}

- (BOOL)prefersStatusBarHidden {
  return YES;
}

class DemoListener : public G3MDemoListener {
private:
  G3MViewController* _viewController;

public:
  DemoListener(G3MViewController* viewController) :
  _viewController(viewController)
  {
  }

  void onChangedScene(const G3MDemoScene* scene) {
    [_viewController onChangedScene: scene];
  }
};

-(void) onChangedScene:(const G3MDemoScene*) scene
{
  [self.demoSelector setTitle: [NSString stringWithCppString: scene->getName()]
                     forState: UIControlStateNormal];
}

- (void)viewDidLoad
{
  [super viewDidLoad];

  G3MDemoListener* listener = new DemoListener(self);

  G3MDemoBuilder_iOS demoBuilder(new G3MBuilder_iOS(self.g3mWidget),
                                 listener);
  demoBuilder.initializeWidget();

  _demoModel = demoBuilder.getModel();

  //  [self showSimpleGlob3];

//  [self initDropDownMenu];
//  [self initToolbar];
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];

  // Let's get the show on the road!
  [self.g3mWidget startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [self.g3mWidget stopAnimation];

	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  self.g3mWidget     = nil;
//  self.toolbar       = nil;
//  self.layerSelector = nil;
  self.demoSelector  = nil;
//  self.demoMenu      = nil;

  [super viewDidUnload];
}

- (void)dealloc
{
  delete _demoModel;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  }
  else {
    return YES;
  }
}

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

//- (void) resetWidget
//{
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  g3mAppUserData->getMarksRenderer()->setEnable(false);
//  g3mAppUserData->getShapeRenderer()->setEnable(false);
//  g3mAppUserData->getMeshRenderer()->setEnable(false);
//
//  [self.g3mWidget stopCameraAnimation];
//
//  LayerSet* layerSet = g3mAppUserData->getLayerSet();
//  for (int i = 0; i < layerSet->size(); i++) {
//    layerSet->getLayer(i)->setEnable(false);
//  }
//}

//- (void) setSatelliteLayerEnabled: (bool) enabled
//{
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  LayerSet* layerSet = g3mAppUserData->getLayerSet();
//  // satellite layers
//  for (int i = 0; i < satelliteLayersNames.size(); i++) {
//    layerSet->getLayerByName(satelliteLayersNames[i])->setEnable(enabled);
//  }
//
//  g3mAppUserData->setSatelliteLayerEnabled(enabled);
//}

//- (void) showSimpleGlob3
//{
//  [self setSatelliteLayerEnabled: true];
//
//  [self.g3mWidget setAnimatedCameraPosition: Geodetic3D(Angle::fromDegrees(0),
//                                                        Angle::fromDegrees(0),
//                                                        25000000)
//                               timeInterval: TimeInterval::fromSeconds(5)];
//}

//- (void) switchLayer
//{
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//
//  const bool satelliteLayerEnabled = g3mAppUserData->getSatelliteLayerEnabled();
//
//  LayerSet* layerSet = g3mAppUserData->getLayerSet();
//  // osm
//  layerSet->getLayerByName("osm_auto:all")->setEnable(satelliteLayerEnabled);
//  // satellite layers
//  [self setSatelliteLayerEnabled: !satelliteLayerEnabled];
//
//  if (satelliteLayerEnabled) {
//    [self.layerSwitcher setImage:[UIImage imageNamed:@"satellite-on-96x48.png"] forState:UIControlStateNormal];
//  }
//  else {
//    [self.layerSwitcher setImage:[UIImage imageNamed:@"map-on-96x48.png"] forState:UIControlStateNormal];
//  }
//}

//- (void) showMarkersDemo
//{
//  [self setSatelliteLayerEnabled: true];
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  g3mAppUserData->getMarksRenderer()->setEnable(true);
//  [self gotoPosition: Geodetic3D(Angle::fromDegrees(37.7658),
//                                 Angle::fromDegrees(-122.4185),
//                                 12000)];
//}

//- (void) showModelDemo
//{
//  [self setSatelliteLayerEnabled: true];
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  g3mAppUserData->getShapeRenderer()->setEnable(true);
//
//  Shape* plane = g3mAppUserData->getPlane();
//  plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                    Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
//                                    10000));
//
//  plane->setAnimatedPosition(TimeInterval::fromSeconds(26),
//                             Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                        Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
//                                        10000),
//                             true);
//
//  const double fromDistance = 50000 * 1.5;
//  const double toDistance   = 25000 * 1.5 / 2;
//
//  const Angle fromAzimuth = Angle::fromDegrees(-90);
//  const Angle toAzimuth   = Angle::fromDegrees(-90 + 360);
//
//  const Angle fromAltitude = Angle::fromDegrees(90);
//  const Angle toAltitude   = Angle::fromDegrees(15);
//
//  plane->orbitCamera(TimeInterval::fromSeconds(20),
//                     fromDistance, toDistance,
//                     fromAzimuth,  toAzimuth,
//                     fromAltitude, toAltitude);
//
//  [self gotoPosition: Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
//                                 6000)];
//}

//- (void) showMeshDemo
//{
//  [self setSatelliteLayerEnabled: true];
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  g3mAppUserData->getMeshRenderer()->setEnable(true);
//  [self gotoPosition: Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
//                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
//                                 6700000)];
//}
//
//- (void) showMeteoriteImpactsLayer
//{
//  G3MAppUserData* g3mAppUserData = (G3MAppUserData*) self.g3mWidget.userData;
//  g3mAppUserData->setSatelliteLayerEnabled(false);
//  LayerSet* layerSet = g3mAppUserData->getLayerSet();
//
//  layerSet->getLayerByName("MapQuest-OSM")->setEnable(true);
//  layerSet->getLayerByName("CartoDB-meteoritessize")->setEnable(true);
//  layerSet->getLayerByName("g3m:mosaic-sst,g3m:mosaic-sla")->setEnable(true);
//
//
//  [self.g3mWidget setAnimatedCameraPosition: Geodetic3D(Angle::fromDegrees(0),
//                                                        Angle::fromDegrees(0),
//                                                        25000000)
//                               timeInterval: TimeInterval::fromSeconds(5)];
//}

//- (void) gotoPosition: (Geodetic3D) position
//{
//  [self.g3mWidget setAnimatedCameraPosition: position
//                               timeInterval: TimeInterval::fromSeconds(5)];
//}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
  NSString *title = [alertView buttonTitleAtIndex:buttonIndex];

  if([title isEqualToString:@"Learn more..."]) {
    G3MWebViewController *webView = [self.storyboard instantiateViewControllerWithIdentifier:@"G3MWebViewController"];
    [self presentViewController:webView
                       animated:YES
                     completion:nil];
    [webView loadUrl: [NSURL URLWithString: urlMarkString]];
  }
}

//- (void) initDropDownMenu
//{
//  // demoSelector: left align button text and style
//  [self.demoSelector setContentHorizontalAlignment: UIControlContentHorizontalAlignmentLeft];
//  [self.demoSelector setContentEdgeInsets: UIEdgeInsetsMake(0, 10, 0, 0)];
//  UIImage *demoSelectorBg = [UIImage imageNamed: @"selector-background.png"];
//  [self.demoSelector setBackgroundImage: demoSelectorBg forState: UIControlStateNormal];
//
//  self.demoMenu = [[G3MUIDropDownMenu alloc] initWithIdentifier: @"demoMenu"];
//
//  NSMutableArray *demoNames = [NSMutableArray arrayWithObjects:
//                               @"Simple glob3",
//                               @"Switch Layer",
//                               @"Markers",
//                               @"3D Model",
//                               @"Point Mesh",
//                               @"Meteorite Impacts",
//                               nil];
//
//  [self.demoMenu setDelegate: self];
//  [self.demoMenu setMenuWidth: 200];
//  [self.demoMenu setBackgroundColor: [UIColor darkGrayColor]];
//  [self.demoMenu setBorderColor: [UIColor blackColor]];
//  [self.demoMenu setTextColor: [UIColor lightGrayColor]];
//  [self.demoMenu setTitleArray: demoNames];
//  [self.demoMenu setValueArray: demoNames];
//  [self.demoMenu makeMenu: self.demoSelector
//               targetView: self.view];
//}

//- (UIButton*) createToolbarButton: (NSString*) imageName
//                            frame: (CGRect) frame
//{
//  UIButton* button = [UIButton buttonWithType: UIButtonTypeCustom];
//  button.layer.borderWidth = 0;
//  button.frame = frame;
//  [button setTitle: @""
//          forState: nil];
//  [button setImage: [UIImage imageNamed: imageName]
//          forState: UIControlStateNormal];
//
//  return button;
//}

//- (void) initToolbar
//{
//  self.toolbar = [[G3MToolbar alloc] init];
//  [self.view addSubview: self.toolbar];
//
////  // layerSwitcher
////  self.layerSwitcher = [self createToolbarButton: @"satellite-on-96x48.png"
////                                           frame: CGRectMake(10.0, 10.0, 96.0, 48.0)];
////  [self.layerSwitcher addTarget: self
////                         action: @selector(switchLayer)
////               forControlEvents: UIControlEventTouchUpInside];
//
//  UIButton* layerSelector = [UIButton buttonWithType:UIButtonTypeCustom];
//  layerSelector.layer.borderWidth = 2;
//  layerSelector.layer.frame = CGRectMake(5, 5, 200, 48);
//
////  layerSelector.titleLabel.textAlignment = NSTextAlignmentLeft;
//
//
//  [layerSelector setTitle:@"Layer..."
//                 forState:UIControlStateNormal];
////  [layerSelector setBackgroundImage:[UIImage imageNamed: @"selector-background.png"]
////                           forState:UIControlStateNormal];
//  [layerSelector setContentHorizontalAlignment: UIControlContentHorizontalAlignmentLeft];
//  [layerSelector setContentEdgeInsets: UIEdgeInsetsMake(0, 10, 0, 0)];
//  UIImage *demoSelectorBg = [UIImage imageNamed: @"selector-background.png"];
//  [layerSelector setBackgroundImage: demoSelectorBg forState: UIControlStateNormal];
//
////  G3MUIDropDownMenu* layerMenu = [[G3MUIDropDownMenu alloc] initWithIdentifier: @"layerMenu"];
////
////  NSMutableArray *layerNames = [NSMutableArray arrayWithObjects:
////                                @"OSM",
////                                @"Bla",
////                                nil];
////
////  layerMenu.delegate        = self;
////  layerMenu.menuWidth       = 200;
////  layerMenu.backgroundColor = [UIColor darkGrayColor];
////  layerMenu.borderColor     = [UIColor blackColor];
////  layerMenu.textColor       = [UIColor lightGrayColor];
////  layerMenu.titleArray      = layerNames;
////  layerMenu.valueArray      = layerNames;
////  [layerMenu makeMenu: layerSelector
////           targetView: self.view];
////
////  self.layerMenu = layerMenu;
////
////  self.layerSelector = layerSelector;
//}

//- (void) updateToolbar: (NSString*) option
//{
//  [self.toolbar removeAllSubviews];
//  if ([option isEqual: @"Switch Layer"]) {
//    //[self.toolbar addSubview: self.layerSelector];
//    self.toolbar.visible = YES;
//  }
//  else {
//    self.toolbar.visible = NO;
//  }
//}

//- (void) DropDownMenuDidChange: (NSString *) identifier
//                              : (NSString *) returnValue
//{
//  if ([identifier isEqual: @"demoMenu"]) {
//    //    [self resetWidget];
//    [self updateToolbar: returnValue];
//    [self.demoSelector setTitle: returnValue
//                       forState: nil];
//
//    if ([returnValue isEqual: @"Simple glob3"]) {
//      //      [self showSimpleGlob3];
//    }
//    else if ([returnValue isEqual: @"Switch Layer"]) {
//      //      [self switchLayer];
//    }
//    else if ([returnValue isEqual: @"Markers"]) {
//      //      [self showMarkersDemo];
//    }
//    else if ([returnValue isEqual: @"3D Model"]) {
//      //      [self showModelDemo];
//    }
//    else if ([returnValue isEqual: @"Point Mesh"]) {
//      //      [self showMeshDemo];
//    }
//    else if ([returnValue isEqualToString: @"Meteorite Impacts"]) {
//      //      [self showMeteoriteImpactsLayer];
//    }
//  }
//}

- (void)prepareForSegue:(UIStoryboardSegue *)segue
                 sender:(id)sender {

  //NSLog(@"Prepare for segue: %@", [segue identifier]);

  if ([segue.destinationViewController isKindOfClass:[G3MSelectDemoSceneViewController class]]) {
    G3MSelectDemoSceneViewController* viewController = (G3MSelectDemoSceneViewController*) segue.destinationViewController;
    UIStoryboardPopoverSegue* popoverSegue = (UIStoryboardPopoverSegue*)segue;
    viewController.popoverController = popoverSegue.popoverController;
    viewController.demoModel = _demoModel;
  }

//  if ( [[segue identifier] isEqualToString:@"showZones"] ) {
//    ZonesTableViewController *vc = [segue destinationViewController];
//    [vc setGlobe: _globe];
//  }
//  if ( [[segue identifier] isEqualToString:@"showDates"] ) {
//    DateViewController *vc = [segue destinationViewController];
//    [vc setGlobe: _globe];
//  }
}

@end
