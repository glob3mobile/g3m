//
//  PopupViewController.h
//  EIFER App
//
//  Created by Chano on 15/11/17.
//
//

#import <UIKit/UIKit.h>

@interface PopupViewController : UIViewController
@property (weak, nonatomic) IBOutlet UISegmentedControl *modeBar;
@property (weak, nonatomic) IBOutlet UIPickerView *colorSpinner;
@property (weak, nonatomic) IBOutlet UISwitch *buildingsSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *pipesSwitch;
@property (weak, nonatomic) IBOutlet UISegmentedControl *alphaBar;
@property (weak, nonatomic) IBOutlet UIPickerView *methodSpinner;

@end
