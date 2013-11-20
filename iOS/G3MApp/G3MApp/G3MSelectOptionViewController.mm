//
//  G3MSelectOptionViewController.m
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/17/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MSelectOptionViewController.h"

#include "G3MDemoScene.hpp"
#import <G3MiOSSDK/NSString_CppAdditions.h>

@implementation G3MSelectOptionViewController

- (BOOL)prefersStatusBarHidden {
  return YES;
}

-(void)setScene:(G3MDemoScene*) scene
{
  _scene = scene;
}

@synthesize popoverController = _myPopoverController;

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
  return 1;
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
  return _scene->getOptionsCount();
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
  static NSString *CellIdentifier = @"Cell";
  UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: CellIdentifier
                                                          forIndexPath: indexPath];

  const std::string option = _scene->getOption( indexPath.row );

  UIButton* button = (UIButton*) [cell viewWithTag: 100];
  [button setTitle: [NSString stringWithCppString: option]
          forState: UIControlStateNormal];

  button.backgroundColor = _scene->isSelectedOption(option) ? [UIColor lightGrayColor] : [UIColor clearColor];

  button.clipsToBounds = YES;
  button.layer.cornerRadius = 8;

  return cell;
}

- (IBAction)changeOption:(UIButton *)sender {
  NSString* option = [[sender titleLabel] text];
  //NSLog(@"Touched on option \"%@\"", option);

  _scene->selectOption( [option toCppString] );

  if (self.popoverController) {
    [self.popoverController dismissPopoverAnimated:YES];
  }
  else {
    [self dismissViewControllerAnimated:YES
                             completion:nil];
  }
}

@end
