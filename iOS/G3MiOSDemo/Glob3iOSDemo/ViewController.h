//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"

@interface ViewController : UIViewController{

IBOutlet G3MWidget_iOS* G3MWidget;
    
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@end
