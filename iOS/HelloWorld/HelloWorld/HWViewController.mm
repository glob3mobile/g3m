//
//  HWViewController.m
//  HelloWorld
//
//  Created by Mari Luz Mateo on 04/09/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#import "HWViewController.h"
#import "G3MWidget_iOS.h"
#import "G3MBuilder_iOS.hpp"

@interface HWViewController ()

@end

@implementation HWViewController

@synthesize g3mWidget        = _g3mWidget;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
  
  G3MBuilder_iOS builder([self g3mWidget]);
  builder.initializeWidget();
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  
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
  self.g3mWidget        = nil;
}

@end
