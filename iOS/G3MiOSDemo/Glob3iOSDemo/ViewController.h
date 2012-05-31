//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "Glob3.h"

@interface ViewController : UIViewController{

IBOutlet Glob3 *G3MWidget;
    
}

@property (retain, nonatomic) Glob3 *G3MWidget;

@end
