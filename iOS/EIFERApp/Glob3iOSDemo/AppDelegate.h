//
//  AppDelegate.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property void* cameraVC;

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application;

- (void) enableCameraBackground:(BOOL) active;

@end
