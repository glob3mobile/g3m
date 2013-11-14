//
//  G3MViewController.h
//  G3MApp
//
//  Created by Mari Luz Mateo on 18/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <vector>

#import "G3MUIDropDownMenu.h"

@class G3MWidget_iOS;
@class G3MToolbar;

@interface G3MViewController : UIViewController <UIAlertViewDelegate, G3MUIDropDownMenuDelegate> {
  NSString* urlMarkString;
  std::vector<std::string> satelliteLayersNames;
}

@property (retain, nonatomic) IBOutlet G3MWidget_iOS* g3mWidget;
@property (strong, nonatomic) IBOutlet UIButton*      demoSelector;
@property (strong, nonatomic) G3MUIDropDownMenu*      demoMenu;
@property (strong, nonatomic) G3MToolbar*             toolbar;
@property (strong, nonatomic) UIButton*               layerSwitcher;

@end
