//
//  G3MWebViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 08/01/13.
//
//

#import <UIKit/UIKit.h>

@interface G3MWebViewController : UIViewController

@property (strong, nonatomic) IBOutlet UIWebView *webView;

- (IBAction)pushBackButton:(id)sender;

- (void) loadUrl: (NSURL *) url;

@end
