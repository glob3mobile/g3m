//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <G3MiOSSDK/G3MWidget_iOS.h>

#import "DeviceOrientation.h"
class MapBooBuilder_iOS;

class Sector;
class Mesh;

Mesh* createSectorMesh(const Planet* planet,
                       const int resolution,
                       const Sector& sector,
                       const Color& color,
                       const int lineWidth);

@interface ViewController : UIViewController {
  IBOutlet G3MWidget_iOS* G3MWidget;

  MapBooBuilder_iOS* _g3mcBuilder;
  
  DeviceOrientation* _dO;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

-(void) tick;

@end
