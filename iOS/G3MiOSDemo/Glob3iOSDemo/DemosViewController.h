//
//  DemosViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import <UIKit/UIKit.h>
#import <vector>
#import "UIDropDownMenu.h"

@class G3MWidget_iOS;
@class G3MToolbar;
class LayerSet;
class MarksRenderer;
class ShapesRenderer;
class MeshRenderer;
class Shape;

@interface DemosViewController : UIViewController <UIAlertViewDelegate, UIDropDownMenuDelegate> {
  NSString* urlMarkString;
  std::vector<std::string> satelliteLayersNames;
}

@property (strong, nonatomic) IBOutlet UIButton *demoSelector;
@property (strong, nonatomic) UIDropDownMenu* demoMenu;

@property (strong, nonatomic) G3MToolbar* toolbar;
@property (strong, nonatomic) UIButton* layerSwitcher;
@property (strong, nonatomic) UIButton* playButton;

@property (retain, nonatomic) IBOutlet G3MWidget_iOS* G3MWidget;

@property (nonatomic) LayerSet* layerSet;
@property (nonatomic) bool satelliteLayerEnabled;
@property (nonatomic) MarksRenderer* markerRenderer;
@property (nonatomic) ShapesRenderer* shapeRenderer;
@property (nonatomic) MeshRenderer* meshRenderer;

@end
