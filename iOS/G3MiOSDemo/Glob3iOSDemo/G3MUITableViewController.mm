//
//  G3MUITableViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 26/12/12.
//
//

#import "G3MUITableViewController.h"

@implementation G3MUITableViewController

- (void) viewDidLoad
{
  [self setBackground];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  }
  else {
    return YES;
  }
}

- (void) didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation
{
  [self setBackground];
}

- (void) setBackground
{
  NSString* imageName;
  if ([[UIScreen mainScreen] bounds].size.width == 320) { // iPhone
    imageName = (self.interfaceOrientation == UIInterfaceOrientationPortrait
                 || self.interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) ? @"Bg-Phone-Portrait.png" : @"Bg-Phone-Landscape.png";
  }
  else if ([[UIScreen mainScreen] bounds].size.width == 640) { // iPhone Retina
    imageName = (self.interfaceOrientation == UIInterfaceOrientationPortrait
                 || self.interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) ? @"Bg-Phone-Portrait@2x.png" : @"Bg-Phone-Landscape@2x.png";
  }
  else if ([[UIScreen mainScreen] bounds].size.width == 768) { // iPad
    imageName = (self.interfaceOrientation == UIInterfaceOrientationPortrait
                 || self.interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) ? @"Bg-Pad-Portrait.png" : @"Bg-Pad-Landscape.png";
  }
  else if ([[UIScreen mainScreen] bounds].size.width == 1536) { // iPad Retina
    imageName = (self.interfaceOrientation == UIInterfaceOrientationPortrait
                 || self.interfaceOrientation == UIInterfaceOrientationPortraitUpsideDown) ? @"Bg-Pad-Portrait@2x.png" : @"Bg-Pad-Landscape@2x.png";
  }
  
  UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:imageName]];
  
  self.tableView.backgroundView = imageView;
}

@end
