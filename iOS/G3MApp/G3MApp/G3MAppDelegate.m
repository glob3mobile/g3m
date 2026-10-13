//
//  G3MAppDelegate.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//

#import "G3MAppDelegate.h"

@implementation G3MAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  return YES;
}

- (UISceneConfiguration *)application:(UIApplication *)application
configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession
                              options:(UISceneConnectionOptions *)options
{
  // The name must match the entry in UIApplicationSceneManifest (Info.plist)
  return [[UISceneConfiguration alloc] initWithName: @"Default Configuration"
                                        sessionRole: connectingSceneSession.role];
}

@end
