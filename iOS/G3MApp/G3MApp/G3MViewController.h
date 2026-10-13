//
//  G3MViewController.h
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//

#import <UIKit/UIKit.h>

#import <string>

@class G3MWidget_iOS;
class G3MDemoModel;
class G3MDemoScene;
class G3MColorGradingDemoScene;
@class G3MColorGradingPanel;

@interface G3MViewController : UIViewController {
  G3MDemoModel* _demoModel;
  UIButton*     _demoSelector;
  UIStackView*  _optionSelectors;
  G3MColorGradingPanel* _colorGradingPanel;
}

@property (retain, nonatomic) IBOutlet G3MWidget_iOS* g3mWidget;

-(void) onChangedScene:(const G3MDemoScene*) scene;

-(void) onChangedOptionInGroup:(size_t) groupIndex
                       inScene:(const G3MDemoScene*) scene;

-(void) showColorGradingPanel:(G3MColorGradingDemoScene*) scene;

-(void) hideColorGradingPanel;

@end
