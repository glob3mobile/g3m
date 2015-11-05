//
//  MenuViewController.h
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/10/15.
//
//

#import <UIKit/UIKit.h>

#import "ViewController.h"


@interface MenuViewController : UIViewController{

  IBOutlet UILabel* textField;
  
  __weak IBOutlet UISwitch *galaxiesSwitch;
  ViewController* _theVC;
}

@property (retain, nonatomic) UILabel* textField;

@property (strong, nonatomic) ViewController* _theVC;

-(IBAction)backToMenu:(id)sender;

-(IBAction)showText:(id)sender;



@end
