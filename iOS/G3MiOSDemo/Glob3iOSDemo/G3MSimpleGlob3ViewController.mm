//
//  G3MSimpleGlob3ViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 18/12/12.
//
//

#import "G3MSimpleGlob3ViewController.h"

#include "G3MBuilder_iOS.hpp"

@interface G3MSimpleGlob3ViewController ()

@end

@implementation G3MSimpleGlob3ViewController

@synthesize G3MWidget;

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
  G3MBuilder_iOS builder([self G3MWidget]);
  
  // Initialize widget
  builder.initializeWidget();
  
  // Let's get the show on the road!
  [[self G3MWidget] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  // Stop the glob3 render
  [[self G3MWidget] stopAnimation];

	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  self.G3MWidget = nil;
  
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
