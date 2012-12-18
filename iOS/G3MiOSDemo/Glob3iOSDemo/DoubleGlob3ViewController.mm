//
//  DoubleGlob3ViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 14/12/12.
//
//

#import "DoubleGlob3ViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"

@interface DoubleGlob3ViewController ()

@end

@implementation DoubleGlob3ViewController

@synthesize _upperGlob3;
@synthesize _lowerGlob3;

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
  
  [self initUpperWidget];
  [self initLowerWidget];
    
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
  [[self _upperGlob3] startAnimation];
  [[self _lowerGlob3] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  [[self _upperGlob3] stopAnimation];
  [[self _lowerGlob3] stopAnimation];
  
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  _upperGlob3 = nil;
  [self set_upperGlob3:nil];
  _lowerGlob3 = nil;
  [self set_lowerGlob3:nil];

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

- (void) initUpperWidget
{
  G3MBuilder_iOS builder(_upperGlob3);
  
  LayerSet* layerSet = new LayerSet();
  layerSet->addLayer([self createLayer: "bing"
                               enabled: true]);
  builder.setLayerSet(layerSet);
    
  // initialization
  builder.initializeWidget();
}

- (void) initLowerWidget
{
  G3MBuilder_iOS builder(_lowerGlob3);
  
  LayerSet* layerSet = new LayerSet();
  layerSet->addLayer([self createLayer: "bing"
                               enabled: true]);
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

  if (name == "pnoa") {
      WMSLayer *pnoa = new WMSLayer("PNOA",
                                    URL("http://www.idee.es/wms/PNOA/PNOA", false),
                                    WMS_1_1_0,
                                    Sector::fromDegrees(21, -18, 45, 6),
                                    "image/png",
                                    "EPSG:4326",
                                    "",
                                    true,
                                    NULL);
    pnoa->setEnable(enabled);
        
    return pnoa;
  }
  
  return NULL;
}

@end
