//
//  G3MUITableViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 26/12/12.
//
//

#import "G3MUITableViewController.h"

@implementation G3MUITableViewController

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
