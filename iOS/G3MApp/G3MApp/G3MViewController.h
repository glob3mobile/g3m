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

@interface G3MViewController : UIViewController {
  G3MDemoModel* _demoModel;
}

@property (retain, nonatomic) IBOutlet G3MWidget_iOS* g3mWidget;
@property (weak, nonatomic)   IBOutlet UIButton*      demoSelector;
@property (weak, nonatomic)   IBOutlet UIButton*      optionSelector;

-(void) onChangedScene:(const G3MDemoScene*) scene;

-(void) onChangedOption:(const std::string&) option
                inScene:(const G3MDemoScene*) scene;

@end
