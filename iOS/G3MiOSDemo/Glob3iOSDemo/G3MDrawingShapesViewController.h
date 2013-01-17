//
//  G3MDrawingShapesViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 19/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"
#import "BoxShape.hpp"
#import "CircleShape.hpp"

@interface G3MDrawingShapesViewController : UIViewController {
  bool animationRunning;
  Geodetic2D* SAN_FRANCISCO_POSITION;
  Geodetic2D* LOS_ANGELES_POSITION;
  BoxShape* box;
  CircleShape* circle;
  WidgetUserData* data;
}

@property (weak, nonatomic) IBOutlet G3MWidget_iOS *glob3;

@property (weak, nonatomic) IBOutlet UIButton *playButton;

- (IBAction)toggleShapesAnimation:(id)sender;

@end
