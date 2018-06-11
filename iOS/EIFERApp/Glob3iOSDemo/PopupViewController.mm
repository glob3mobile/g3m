//
//  PopupViewController.m
//  EIFER App
//
//  Created by Chano on 15/11/17.
//
//

#import "PopupViewController.h"

#import "ViewController.h"

#import <G3MiOSSDK/Geodetic3D.hpp>

@interface PopupViewController ()
@property (weak, nonatomic) IBOutlet UIScrollView *theScrollView;
@property (weak, nonatomic) IBOutlet UIView *theScrollContent;

@end

@implementation PopupViewController{
    NSArray *colorArray, *methodArray;
    int colorRow, prevColor, methodRow, prevMethod, modeRow, alphaRow, distanceRow;
    bool areBuildings, arePipes, isCorrection;
}

- (void) setCurrentStateWithMode:(int) mode
                           Color:(int) color
                           Alpha:(int) alpha
                          Method:(int) method
                       Buildings:(bool) buildings
                           Pipes:(bool) pipes
                      Correction:(bool) correction
                        Distance:(int) distance{
    modeRow = mode;
    prevColor = color;
    methodRow = method;
    prevMethod = method;
    alphaRow = alpha;
    colorRow = color;
    distanceRow = distance;
    areBuildings = buildings;
    arePipes = pipes;
    isCorrection = correction;
}

- (IBAction)setCorrectionsAction:(id)sender {
    
    __weak const ViewController *vC = (ViewController *) [self presentingViewController];
    
    if ([vC getMapMode] > 0){
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                        message:@"Correction settings only enabled for Map mode"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }
    else {
        [self dismissViewControllerAnimated:NO completion:^{
           //Do stuff here
            [vC setWidgetAnimation:true];
            [vC activePositionFixer];
        }];
    }
    /*
     public void openPositionFixer(View view){
     
     FragmentManager fmanager = this.getSupportFragmentManager();
     GlobeFragment fragment = (GlobeFragment) fmanager.findFragmentById(R.id.theFragment);
     
     if (fragment.getMapMode() > 0){
     Toast.makeText(this,getString(R.string.fixer_unable),Toast.LENGTH_SHORT).show();
     }
     else {
     dialog.dismiss();
     dialog = null;
     if (dialogItem != null)
     dialogItem.setEnabled(false);
     setPositionFixerBar(true);
     fragment.activePositionFixer();
     }
     }
     */
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    colorArray = @[@"Random Colors", @"Heat Demand", @"Building Volume", @"GHG Emissions", @"Demographic Clusters (SOM)", @"Demographic Clusters (k-means)"];
    methodArray = @[@"Fixed", @"Linear", @"Smoothstep", @"Smootherstep", @"Smootheststep", @"Sigmoid", @"Tanh", @"Arctan", @"Softsign"];
    
    [self.methodSpinner selectRow:methodRow inComponent:0 animated:NO];
    [self.colorSpinner selectRow:colorRow inComponent:0 animated:NO];
    self.modeBar.selectedSegmentIndex = modeRow;
    self.alphaBar.selectedSegmentIndex = alphaRow;
    [self.buildingsSwitch setOn:areBuildings];
    [self.pipesSwitch setOn:arePipes];
    [self.correctionSwitch setOn:isCorrection];
    [self.positionText setText:[self generateMessage]];
    [self.distancePicker setText:[NSString stringWithFormat:@"%d",distanceRow]];
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    self.theScrollView.delegate = self;
    self.distancePicker.delegate = self;
    //self.theScrollView.translatesAutoresizingMaskIntoConstraints = NO;
    [self.theScrollView setScrollEnabled:YES];
//    self.theScrollView.contentSize = self.theScrollContent.frame.size;
    //[scrollview setShowsHorizontalScrollIndicator:YES];
//    [self.theScrollView setShowsVerticalScrollIndicator:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)applySettings:(id)sender {
    __weak const ViewController *vC = (ViewController *) [self presentingViewController];
    
    if ([self.buildingsSwitch isOn] != areBuildings) {
        [vC setBuildingsActive:[self.buildingsSwitch isOn]];
    }
    if ([self.pipesSwitch isOn] != arePipes) {
        [vC setPipesActive:[self.pipesSwitch isOn]];
    }
    if ([self.correctionSwitch isOn] != isCorrection){
        [vC setCorrectionActive:[self.correctionSwitch isOn]];
    }
    
    
    if (prevColor != colorRow){
        [vC setActiveColor:colorRow];
    }
    
    if (modeRow != (int)self.modeBar.selectedSegmentIndex) {
        [vC setMode:(int)self.modeBar.selectedSegmentIndex];
    }
    
    int dt = [[self.distancePicker text] intValue];
    if (dt != distanceRow){
        [vC setDistance:dt];
    }
    
    if (alphaRow != (int)self.alphaBar.selectedSegmentIndex) {
        //bool isHole = ((int)self.alphaBar.selectedSegmentIndex == 1);
        //[vC setHole:isHole];
        bool isDitch = ((int)self.alphaBar.selectedSegmentIndex == 1);
        [vC setDitch:isDitch];
    }
    
    if (methodRow != prevMethod){
        [vC setAlphaMethod:methodRow];
    }
    
    [self dismissViewControllerAnimated:YES completion:^{
        [vC setWidgetAnimation:true];
    }];
}
- (NSString *) generateMessage
{
    __weak const ViewController *vC = (ViewController *) [self presentingViewController];
    
    Geodetic3D mg = [vC getMarkPosition];
    Angle heading = [vC getMarkHeading];
    NSString *message;
    if (!heading.isNan()) {
   
        message = [NSString stringWithFormat:
                         @"Lat: %.8f, lon: %.8f \nHgt: %.2f, heading: %.2f",
                         mg._latitude._degrees,
                         mg._longitude._degrees,
                         mg._height,
                         heading._degrees];
    }
    else{
        message = @"Position undefined";
    }
    
    return message;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return TRUE;
}

////PICKER VIEW

// The number of columns of data
- (int)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}


// The number of rows of data
- (int)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (pickerView == self.colorSpinner)
        return (int) colorArray.count;
    else if (pickerView == self.methodSpinner)
        return (int) methodArray.count;
    else
        return 0;
}

// The data to return for the row and component (column) that's being passed in
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (pickerView == self.colorSpinner)
        return colorArray[row];
    else if (pickerView == self.methodSpinner)
        return methodArray[row];
    else
        return nil;
}

// Catpure the picker view selection
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if (pickerView == self.colorSpinner){
        colorRow = (int) row;
    }
    else if (pickerView == self.methodSpinner){
        methodRow = (int) row;
    }
}


@end
