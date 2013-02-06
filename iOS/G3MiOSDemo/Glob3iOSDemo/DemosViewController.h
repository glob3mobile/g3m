//
//  DemosViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 29/01/13.
//
//

#import <UIKit/UIKit.h>
#import "UIDropDownMenu.h"

@class G3MWidget_iOS;
@class G3MToolbar;
class LayerSet;
class MarksRenderer;
class ShapesRenderer;
class MeshRenderer;

@interface DemosViewController : UIViewController <UIAlertViewDelegate, UIDropDownMenuDelegate> {
  NSString* urlMarkString;
  IBOutlet G3MWidget_iOS* G3MWidget;
}

@property (strong, nonatomic) IBOutlet UIButton *demoSelector;
@property (strong, nonatomic) UIDropDownMenu* demoMenu;

@property (strong, nonatomic) G3MToolbar* toolbar;
@property (strong, nonatomic) UIButton* layerSwitcher;
@property (strong, nonatomic) UIButton* playMarkersDemo;
@property (strong, nonatomic) UIButton* playModelDemo;
@property (strong, nonatomic) UIButton* playMeshDemo;

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@property (nonatomic) LayerSet* layerSet;
@property (nonatomic) bool satelliteLayerEnabled;
@property (nonatomic) MarksRenderer* markerRenderer;
@property (nonatomic) ShapesRenderer* shapeRenderer;
@property (nonatomic) MeshRenderer* meshRenderer;

@end
