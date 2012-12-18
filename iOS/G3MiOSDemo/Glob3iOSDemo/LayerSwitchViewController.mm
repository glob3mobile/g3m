//
//  LayerSwitchViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 17/12/12.
//
//

#import "LayerSwitchViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"

@implementation LayerSwitchViewController

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
	// Do any additional setup after loading the view.
  
  satelliteLayer = true;
  
  [self initWidget];
}

- (void)viewDidUnload
{
  _glob3 = nil;
  _layerSwitcher = nil;
  [super viewDidUnload];
  // Release any retained subviews of the main view.
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
  [_glob3 startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  [super viewDidDisappear:animated];
  
  [_glob3 stopAnimation];
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

- (void) initWidget
{
  G3MBuilder_iOS builder(_glob3);
  
  LayerSet* layerSet = new LayerSet();
  _bingLayer = [self createLayer: "bing"
                         enabled: true];
  layerSet->addLayer(_bingLayer);
  _osmLayer = [self createLayer: "osm"
                        enabled: false];
  layerSet->addLayer(_osmLayer);
  builder.setLayerSet(layerSet);
    
  // initialization
  builder.initializeWidget();
}

- (WMSLayer*) createLayer: (const std::string) name
                  enabled: (bool) enabled
{
  if (name == "bing") {
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                  WMS_1_1_0,
                                  Sector::fullSphere(),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  NULL);
    bing->setEnable(enabled);
        
    return bing;
  }
    
  if (name == "osm") {
    WMSLayer* osm = new WMSLayer("osm_auto:all",
                                 URL("http://129.206.228.72/cached/osm", false),
                                 WMS_1_1_0,
                                 // Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
                                 Sector::fullSphere(),
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL);
    osm->setEnable(enabled);
        
    return osm;
  }
  
  return NULL;
}


- (IBAction)switchLayer:(id)sender {
  satelliteLayer = !satelliteLayer;
  
  _bingLayer->setEnable(satelliteLayer);
  _osmLayer->setEnable(!satelliteLayer);

  if (satelliteLayer) {
    [_layerSwitcher setImage:[UIImage imageNamed:@"satellite_on_96x48.png"] forState:UIControlStateNormal];
  }
  else {
    [_layerSwitcher setImage:[UIImage imageNamed:@"map_on_96x48.png"] forState:UIControlStateNormal];
  }
}
@end
