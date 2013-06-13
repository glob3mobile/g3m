//
//  ViewController.h
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <G3MiOSSDK/G3MWidget_iOS.h>
class G3MCBuilder_iOS;

class Sector;
class Mesh;

Mesh* createSectorMesh(const Planet* planet,
                       const int resolution,
                       const Sector& sector,
                       const Color& color,
                       const int lineWidth);

@interface ViewController : UIViewController {
  IBOutlet G3MWidget_iOS* G3MWidget;

  G3MCBuilder_iOS* _g3mcBuilder;
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@end
