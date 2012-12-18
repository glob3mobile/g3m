//
//  SimpleGlob3ViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 18/12/12.
//
//

#import "SimpleGlob3ViewController.h"

#include "G3MBuilder_iOS.hpp"

@interface SimpleGlob3ViewController ()

@end

@implementation SimpleGlob3ViewController

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
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
  [[self G3MWidget] startAnimation];
}

- (void)viewDidDisappear:(BOOL)animated
{
  [[self G3MWidget] stopAnimation];

	[super viewDidDisappear:animated];
}

- (void)viewDidUnload
{
  G3MWidget = nil;
  [self setG3MWidget:nil];
  
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
