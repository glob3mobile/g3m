//
//  MenuViewController.h
//  G3MiOSDemo
//
//  Created by Jose Miguel SN on 30/10/15.
//
//

#import <UIKit/UIKit.h>

@interface MenuViewController : UIViewController{

  IBOutlet UILabel* textField;
}

@property (retain, nonatomic) UILabel* textField;

-(IBAction)backToMenu:(id)sender;

-(IBAction)showText:(id)sender;



@end
