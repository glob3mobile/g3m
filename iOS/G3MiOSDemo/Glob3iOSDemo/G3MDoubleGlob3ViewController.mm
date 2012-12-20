//
//  G3MDoubleGlob3ViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 14/12/12.
//
//

#import "G3MDoubleGlob3ViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"

@interface G3MDoubleGlob3ViewController ()

@end

@implementation G3MDoubleGlob3ViewController

@synthesize upperGlob3;
@synthesize lowerGlob3;

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
  
  // Create two builders
  G3MBuilder_iOS upperBuilder(self.upperGlob3);
  G3MBuilder_iOS lowerBuilder(self.lowerGlob3);
  
  // Create and populate two layer sets
  LayerSet* upperLayerSet = new LayerSet();
  upperLayerSet->addLayer([self createLayer: "bing"
                                    enabled: true]);
  LayerSet* lowerLayerSet = new LayerSet();
  lowerLayerSet->addLayer([self createLayer: "osm"
                                    enabled: true]);

  // Set the layer sets to be used in the widgets
  upperBuilder.setLayerSet(upperLayerSet);
  lowerBuilder.setLayerSet(lowerLayerSet);
  
  // Initialize the glob3 widgets
  upperBuilder.initializeWidget();
  lowerBuilder.initializeWidget();
  
  // Let's get the show on the road!
  [[self upperGlob3] startAnimation];
  [[self lowerGlob3] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the widgets render
  [[self upperGlob3] stopAnimation];
  [[self lowerGlob3] stopAnimation];
  
	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  self.upperGlob3 = nil;
  self.lowerGlob3 = nil;

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
