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
#import <G3MiOSSDK/GInitializationTask.hpp>
#import <G3MiOSSDK/TileRendererBuilder.hpp>
#import <G3MiOSSDK/LayerBuilder.hpp>
#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/ShapesRenderer.hpp>
#import <G3MiOSSDK/MeshRenderer.hpp>
#import "G3MToolbar.h"
#import "G3MAppUserData.hpp"
#import "G3MMarkerUserData.hpp"
#import "G3MMeshRenderer.hpp"
#import "G3MMarkerRenderer.hpp"
#import "G3MPlaneParseTask.hpp"
#import "G3MAppInitializationTask.hpp"
#import "G3MWebViewController.h"

@interface G3MViewController ()

@end

@implementation G3MViewController

@synthesize g3mWidget, demoSelector, demoMenu, toolbar, layerSwitcher;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
  self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
  if (self) {
    // Custom initialization
  }
  return self;
}

- (void)viewDidLoad
{
  [super viewDidLoad];
  
  // Create a builder
  G3MBuilder_iOS builder([self g3mWidget]);
  
  G3MAppUserData* userData = new G3MAppUserData();
  userData->setSatelliteLayerEnabled(true);
  userData->setLayerSet([self createLayerSet: userData->getSatelliteLayerEnabled()]);
  MarksRenderer* markerRenderer = G3MMarkerRenderer::createMarkerRenderer(self);
  markerRenderer->setEnable(false);
  userData->setMarkerRenderer(markerRenderer);
  ShapesRenderer* shapeRenderer = new ShapesRenderer();
  shapeRenderer->setEnable(false);
  userData->setShapeRenderer(shapeRenderer);
  MeshRenderer* meshRenderer = G3MMeshRenderer::createMeshRenderer(builder.getPlanet());
  meshRenderer->setEnable(false);
  userData->setMeshRenderer(meshRenderer);
  
  // Setup the builder
  builder.getTileRendererBuilder()->setLayerSet(userData->getLayerSet());
  // store satellite layers names
  satelliteLayersNames = builder.getTileRendererBuilder()->getDefaultLayersNames();
  //  builder.getTileRendererBuilder()->setShowStatistics(true);
  builder.addRenderer(markerRenderer);
  builder.addRenderer(shapeRenderer);
  builder.addRenderer(meshRenderer);
  builder.setInitializationTask(new G3MAppInitializationTask([self g3mWidget]), true);
  builder.setUserData(userData);
  
  // Initialize widget
  builder.initializeWidget();
  [self resetWidget];
  
  [self initDropDownMenu];
  [self initToolbar];
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
  // Let's get the show on the road!
  [[self g3mWidget] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [[self g3mWidget] stopAnimation];
  
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  [self setG3mWidget: nil];
  [self setToolbar: nil];
  [self setLayerSwitcher: nil];
  [self setDemoSelector:nil];
  [self setDemoMenu: nil];
  
  [super viewDidUnload];
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

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

- (LayerSet*) createLayerSet: (bool) satelliteLayerEnabled
{
  LayerSet* layers = LayerBuilder::createDefaultSatelliteImagery();
  layers->addLayer(LayerBuilder::createOSMLayer(!satelliteLayerEnabled));
  
  return layers;
}


- (void) resetWidget
{
  ((G3MAppUserData*) [[self g3mWidget] userData])->getMarkerRenderer()->setEnable(false);
  ((G3MAppUserData*) [[self g3mWidget] userData])->getShapeRenderer()->setEnable(false);
  ((G3MAppUserData*) [[self g3mWidget] userData])->getMeshRenderer()->setEnable(false);
  
  [[self g3mWidget] stopCameraAnimation];
}

- (void) showSimpleGlob3
{
  const bool satelliteLayerEnabled = ((G3MAppUserData*) [[self g3mWidget] userData])->getSatelliteLayerEnabled();
  if (!satelliteLayerEnabled) {
    [self switchLayer];
  }
  
  [[self g3mWidget] setAnimatedCameraPosition: Geodetic3D(Angle::fromDegrees(0),
                                                          Angle::fromDegrees(0),
                                                          25000000)
                                 timeInterval: TimeInterval::fromSeconds(5)];
}

- (void) switchLayer
{
  ((G3MAppUserData*) [[self g3mWidget] userData])->toggleSatelliteLayerEnabled();
  const bool satelliteLayerEnabled = ((G3MAppUserData*) [[self g3mWidget] userData])->getSatelliteLayerEnabled();
  
  if (satelliteLayerEnabled) {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"satellite-on-96x48.png"] forState:UIControlStateNormal];
  }
  else {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"map-on-96x48.png"] forState:UIControlStateNormal];
  }
  
  LayerSet* layerSet = ((G3MAppUserData*) [[self g3mWidget] userData])->getLayerSet();
  // satellite layers
  for (int i = 0; i < satelliteLayersNames.size(); i++) {
    layerSet->getLayer(satelliteLayersNames[i])->setEnable(satelliteLayerEnabled);
  }
  // osm
  layerSet->getLayer("osm_auto:all")->setEnable(!satelliteLayerEnabled);
}

- (void) showMarkersDemo
{
  ((G3MAppUserData*) [[self g3mWidget] userData])->getMarkerRenderer()->setEnable(true);
  [self gotoPosition: Geodetic3D(Angle::fromDegrees(37.7658),
                                 Angle::fromDegrees(-122.4185),
                                 12000)];
}

- (void) showModelDemo
{
  ((G3MAppUserData*) [[self g3mWidget] userData])->getShapeRenderer()->setEnable(true);
  
  Shape* plane = ((G3MAppUserData*) [[self g3mWidget] userData])->getPlane();
  plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                    Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                    10000));
  
  plane->setAnimatedPosition(TimeInterval::fromSeconds(26),
                             Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                        Angle::fromDegreesMinutesSeconds(-78, 2, 10.92),
                                        10000),
                             true);
  
  const double fromDistance = 50000 * 1.5;
  const double toDistance   = 25000 * 1.5 / 2;
  
  const Angle fromAzimuth = Angle::fromDegrees(-90);
  const Angle toAzimuth   = Angle::fromDegrees(-90 + 360);
  
  const Angle fromAltitude = Angle::fromDegrees(90);
  const Angle toAltitude   = Angle::fromDegrees(15);
  
  plane->orbitCamera(TimeInterval::fromSeconds(20),
                     fromDistance, toDistance,
                     fromAzimuth,  toAzimuth,
                     fromAltitude, toAltitude);
  
  [self gotoPosition: Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                 6000)];
}

- (void) showMeshDemo
{
  ((G3MAppUserData*) [[self g3mWidget] userData])->getMeshRenderer()->setEnable(true);
  [self gotoPosition: Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                 Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                 6700000)];
}

- (void) gotoPosition: (Geodetic3D) position
{
  [[self g3mWidget] setAnimatedCameraPosition: position
                                 timeInterval: TimeInterval::fromSeconds(5)];
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
  NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
  
  if([title isEqualToString:@"Learn more..."]) {
    G3MWebViewController *webView = [self.storyboard instantiateViewControllerWithIdentifier:@"G3MWebViewController"];
    [self presentModalViewController: webView
                            animated: YES];
    [webView loadUrl: [NSURL URLWithString: urlMarkString]];
  }
}

- (void) initDropDownMenu
{
  // demoSelector: left align button text and style
  [[self demoSelector] setContentHorizontalAlignment: UIControlContentHorizontalAlignmentLeft];
  [[self demoSelector] setContentEdgeInsets: UIEdgeInsetsMake(0, 10, 0, 0)];
  UIImage *demoSelectorBg = [UIImage imageNamed: @"selector-background.png"];
  [[self demoSelector] setBackgroundImage: demoSelectorBg forState: UIControlStateNormal];
  [[self demoSelector] setBackgroundImage: demoSelectorBg forState: UIControlStateHighlighted];
  
  [self setDemoMenu: [[G3MUIDropDownMenu alloc] initWithIdentifier: @"demoMenu"]];
  
  NSMutableArray *demoNames = [[NSMutableArray alloc] initWithObjects:
                               @"Simple glob3",
                               @"Switch layer",
                               @"Markers",
                               @"3D Model",
                               @"Point Mesh",
                               @"TopRight",
                               @"TopLeft",
                               @"BottomRight",
                               @"BottomLeft",
                               nil];
  
  [[self demoMenu] setDelegate: self];
  [[self demoMenu] setMenuWidth: 200];
  [[self demoMenu] setBackgroundColor: [UIColor darkGrayColor]];
  [[self demoMenu] setBorderColor: [UIColor blackColor]];
  [[self demoMenu] setTextColor: [UIColor lightGrayColor]];
  [[self demoMenu] setTitleArray: demoNames];
  [[self demoMenu] setValueArray: demoNames];
  [[self demoMenu] makeMenu: [self demoSelector]
                 targetView: [self view]];
}

- (UIButton*) createToolbarButton: (NSString*) imageName
                            frame: (CGRect) frame
{
  UIButton* button = [UIButton buttonWithType: UIButtonTypeCustom];
  [button setTitle: @""
          forState: nil];
  [[button layer] setBorderWidth: 0];
  [button setImage: [UIImage imageNamed: imageName]
          forState: UIControlStateNormal];
  [button setFrame: frame];
  
  return button;
}

- (void) initToolbar
{
  [self setToolbar: [[G3MToolbar alloc] init]];
  [[self view] addSubview: toolbar];
  
  // layerSwitcher
  [self setLayerSwitcher: [self createToolbarButton: @"satellite-on-96x48.png"
                                              frame: CGRectMake(10.0, 10.0, 96.0, 48.0)]];
  [[self layerSwitcher] addTarget: self
                           action: @selector(switchLayer)
                 forControlEvents: UIControlEventTouchUpInside];
}

- (void) updateToolbar: (NSString*) option
{
  [[self toolbar] clear];
  if ([option isEqual: @"Switch layer"]) {
    [[self toolbar] addSubview: [self layerSwitcher]];
    [[self toolbar] setVisible: TRUE];
  }
  else {
    [[self toolbar] setVisible: FALSE];
  }
}

- (void) DropDownMenuDidChange: (NSString *) identifier
                              : (NSString *) returnValue
{
  if ([identifier isEqual: @"demoMenu"]) {
    [self resetWidget];
    [self updateToolbar: returnValue];
    [[self demoSelector] setTitle: returnValue
                         forState: nil];
    
    if ([returnValue isEqual: @"Simple glob3"]) {
      [self showSimpleGlob3];
    }
    else if ([returnValue isEqual: @"Switch layer"]) {
      [self switchLayer];
    }
    else if ([returnValue isEqual: @"Markers"]) {
      [self showMarkersDemo];
    }
    else if ([returnValue isEqual: @"3D Model"]) {
      [self showModelDemo];
    }
    else if ([returnValue isEqual: @"Point Mesh"]) {
      [self showMeshDemo];
    }
    else if ([returnValue isEqualToString: @"TopRight"]) {
      [self gotoPosition: Geodetic3D(Angle::fromDegrees(70),
                                     Angle::fromDegrees(-160),
                                     500000)];
    }
    else if ([returnValue isEqualToString: @"TopLeft"]) {
      [self gotoPosition: Geodetic3D(Angle::fromDegrees(70),
                                     Angle::fromDegrees(160),
                                     500000)];
    }
    else if ([returnValue isEqualToString: @"BottomRight"]) {
      [self gotoPosition: Geodetic3D(Angle::fromDegrees(-70),
                                     Angle::fromDegrees(-160),
                                     500000)];
    }
    else if ([returnValue isEqualToString: @"BottomLeft"]) {
      [self gotoPosition: Geodetic3D(Angle::fromDegrees(-70),
                                     Angle::fromDegrees(160),
                                     500000)];
    }
  }
}

@end

