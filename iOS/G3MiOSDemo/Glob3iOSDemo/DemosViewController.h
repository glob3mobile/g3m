//
//  DemosViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"
#import "MarksRenderer.hpp"

@interface DemosViewController : UIViewController <UIAlertViewDelegate> {
  NSString* urlMarkString;
  IBOutlet G3MWidget_iOS* G3MWidget;
}
@property (weak, nonatomic) IBOutlet UISegmentedControl *demoSwitcher;

- (IBAction)switchDemo:(UISegmentedControl*)sender;

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property (nonatomic) LayerSet* layerSet;

@property (nonatomic) bool satelliteLayerEnabled;

@property (nonatomic) MarksRenderer* markerRenderer;

@end
