//
//  G3MChooseDemoViewController.m
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MChooseDemoViewController.h"

#include "G3MDemoModel.hpp"
#include "G3MDemoScene.hpp"

#import <G3MiOSSDK/NSString_CppAdditions.h>

@interface G3MChooseDemoViewController ()

@end

@implementation G3MChooseDemoViewController

@synthesize popoverController = _myPopoverController;

-(void)setDemoModel:(G3MDemoModel*) demoModel
{
  _demoModel = demoModel;
}

- (void)viewDidLoad
{
  [super viewDidLoad];

  // Uncomment the following line to preserve selection between presentations.
  // self.clearsSelectionOnViewWillAppear = NO;

  // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
  // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
  return 1;
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
  return _demoModel->getScenesCount();
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
  static NSString *CellIdentifier = @"Cell";
  UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier
                                                          forIndexPath: indexPath];

  const std::string sceneName = _demoModel->getScene( indexPath.row )->getName();

  UIButton* button = (UIButton*) [cell viewWithTag: 100];
  [button setTitle: [NSString stringWithCppString: sceneName]
          forState: UIControlStateNormal];

  return cell;
}

- (IBAction)changeDemo:(UIButton *)sender {
  NSString* sceneName = [[sender titleLabel] text];
  //NSLog(@"Touched on \"%@\"", sceneName);

  _demoModel->selectScene( [sceneName toCppString] );

  [self.popoverController dismissPopoverAnimated:YES];
}

@end
