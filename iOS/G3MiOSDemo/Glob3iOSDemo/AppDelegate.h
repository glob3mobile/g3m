//
//  AppDelegate.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>{
  BOOL _starsActive[5];
}

@property (strong, nonatomic) UIWindow *window;


@property (nonatomic) BOOL showingGalaxies;

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application;

-(BOOL) areStarsActive: (int) i;
-(void) setStarsActive: (int) i value: (BOOL) v;



@end
