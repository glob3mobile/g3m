//
//  G3MViewController.h
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <string>
#import "GAITrackedViewController.h"

@class G3MWidget_iOS;
class G3MDemoModel;
class G3MDemoScene;

//@interface G3MViewController : UIViewController <UIAlertViewDelegate> {
@interface G3MViewController : GAITrackedViewController <UIAlertViewDelegate> {
  G3MDemoModel* _demoModel;
}

@property (retain, nonatomic) IBOutlet G3MWidget_iOS* g3mWidget;
@property (weak, nonatomic)   IBOutlet UIButton*      demoSelector;
@property (weak, nonatomic)   IBOutlet UIView*        secondaryToolbar;
@property (weak, nonatomic)   IBOutlet UIButton*      optionSelector;

-(void) onChangedScene:(const G3MDemoScene*) scene;

-(void) onChangedOption:(const std::string&) option
                inScene:(const G3MDemoScene*) scene;

@end
