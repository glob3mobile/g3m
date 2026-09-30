//
//  G3MSceneDelegate.m
//  G3MApp
//

#import "G3MSceneDelegate.h"

@implementation G3MSceneDelegate

- (void)scene:(UIScene *)scene
willConnectToSession:(UISceneSession *)session
      options:(UISceneConnectionOptions *)connectionOptions
{
  if (![scene isKindOfClass:[UIWindowScene class]]) {
    return;
  }
  UIWindowScene* windowScene = (UIWindowScene*) scene;

  // Two storyboards predate UIScene; the manifest can't pick one per idiom, so choose it here
  const BOOL isPad = (windowScene.traitCollection.userInterfaceIdiom == UIUserInterfaceIdiomPad);
  NSString* storyboardName = isPad ? @"MainStoryboard-iPad" : @"MainStoryboard-iPhone";
  UIStoryboard* storyboard = [UIStoryboard storyboardWithName: storyboardName
                                                       bundle: nil];

  self.window = [[UIWindow alloc] initWithWindowScene: windowScene];
  self.window.rootViewController = [storyboard instantiateInitialViewController];
  [self.window makeKeyAndVisible];
}

@end
