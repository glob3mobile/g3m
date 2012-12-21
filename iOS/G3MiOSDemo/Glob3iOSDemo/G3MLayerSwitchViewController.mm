//
//  G3MLayerSwitchViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 17/12/12.
//
//

#import "G3MLayerSwitchViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"
#include "LayerBuilder.hpp"

@interface G3MLayerSwitchViewController ()

@end

@implementation G3MLayerSwitchViewController

@synthesize glob3;
@synthesize layerSwitcher;
@synthesize bingLayer;
@synthesize osmLayer;
@synthesize satelliteLayer;


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
  G3MBuilder_iOS builder(self.glob3);

  // Initialize layer flag
  self.satelliteLayer = true;

  // Initialize layers
  self.bingLayer = LayerBuilder::createBingLayer(true);
  self.osmLayer = LayerBuilder::createOSMLayer(false);
  
  // Create and populate a layer set
  LayerSet* layerSet = new LayerSet();
  layerSet->addLayer(self.bingLayer);
  layerSet->addLayer(self.osmLayer);
  
  // Set the layer set to be used by the glob3
  builder.setLayerSet(layerSet);
  
  // Initialize the glob3
  builder.initializeWidget();

  // Let's get the show on the road!
  [[self glob3] startAnimation];
}

- (void)viewDidUnload
{
  [self setGlob3:nil];
  [self setLayerSwitcher:nil];
  
  self.bingLayer = nil;
  self.osmLayer = nil;
  
  [super viewDidUnload];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [[self glob3] stopAnimation];
  
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

- (IBAction)switchLayer:(id)sender {
  self.satelliteLayer = !self.satelliteLayer;
  
  self.bingLayer->setEnable(self.satelliteLayer);
  self.osmLayer->setEnable(!self.satelliteLayer);

  if (self.satelliteLayer) {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"satellite_on_96x48.png"] forState:UIControlStateNormal];
  }
  else {
    [[self layerSwitcher] setImage:[UIImage imageNamed:@"map_on_96x48.png"] forState:UIControlStateNormal];
  }
}
@end
