//
//  DemosViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import <UIKit/UIKit.h>

@class G3MWidget_iOS;
class LayerSet;
class MarksRenderer;
class ShapesRenderer;
class MeshRenderer;

@interface DemosViewController : UIViewController <UIAlertViewDelegate> {
  NSString* urlMarkString;
  IBOutlet G3MWidget_iOS* G3MWidget;
}

- (IBAction)switchDemo:(UISegmentedControl*)sender;

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property (nonatomic) LayerSet* layerSet;

@property (nonatomic) bool satelliteLayerEnabled;

@property (nonatomic) MarksRenderer* markerRenderer;

@property (nonatomic) ShapesRenderer* shapeRenderer;

@property (nonatomic) MeshRenderer* meshRenderer;

@end
