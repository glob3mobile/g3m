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
#include "LayerBuilder.hpp"

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
  upperLayerSet->addLayer(LayerBuilder::createBingLayer(true));
  LayerSet* lowerLayerSet = new LayerSet();
  lowerLayerSet->addLayer(LayerBuilder::createOSMLayer(true));

  // Set the layer sets to be used by the widgets
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

@end
