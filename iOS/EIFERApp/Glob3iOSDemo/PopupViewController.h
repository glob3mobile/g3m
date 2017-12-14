//
//  PopupViewController.h
//  EIFER App
//
//  Created by Chano on 15/11/17.
//
//

#import <UIKit/UIKit.h>

@interface PopupViewController : UIViewController<UIPickerViewDelegate,UIScrollViewDelegate>
@property (weak, nonatomic) IBOutlet UISegmentedControl *modeBar;
@property (weak, nonatomic) IBOutlet UIPickerView *colorSpinner;
@property (weak, nonatomic) IBOutlet UISwitch *buildingsSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *pipesSwitch;
@property (weak, nonatomic) IBOutlet UISegmentedControl *alphaBar;
@property (weak, nonatomic) IBOutlet UIPickerView *methodSpinner;
@property (weak, nonatomic) IBOutlet UILabel *positionText;
@property (weak, nonatomic) IBOutlet UIButton *correctionsButton;
@property (weak, nonatomic) IBOutlet UISwitch *correctionSwitch;

- (void) setCurrentStateWithMode:(int) mode
                           Color:(int) color
                           Alpha:(int) alpha
                          Method:(int) method
                       Buildings:(bool) buildings
                           Pipes:(bool) pipes
                      Correction:(bool) correction;

- (IBAction)setCorrectionsAction:(id)sender;

@end
