//
//  G3MChooseDemoViewController.m
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MChooseDemoViewController.h"

#include "G3MDemoModel.hpp"

#import <G3MiOSSDK/NSString_CppAdditions.h>

@interface G3MChooseDemoViewController ()

@end

@implementation G3MChooseDemoViewController

@synthesize popoverController = _myPopoverController;

-(void)setDemoModel:(G3MDemoModel*) demoModel
{
  _demoModel = demoModel;
}

//- (id)init
//{
//  self = [super init];
//  if (self) {
//    _demoModel = NULL;
//  }
//  return self;
//}
//
//- (id)initWithStyle:(UITableViewStyle)style
//{
//  self = [super initWithStyle:style];
//  if (self) {
//    // Custom initialization
//  }
//  return self;
//}

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

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

 */

@end
