//
//  G3MWebViewController.mm
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 08/01/13.
//
//

#import "G3MWebViewController.h"

@implementation G3MWebViewController
@synthesize webView;

- (void)viewDidUnload {
  [self setWebView:nil];
  [super viewDidUnload];
}

- (IBAction)pushBackButton:(id)sender {
  [self dismissModalViewControllerAnimated: YES];
}

- (void) loadUrl: (NSURL *) url {
  [webView loadRequest:[NSURLRequest requestWithURL:url]];
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
