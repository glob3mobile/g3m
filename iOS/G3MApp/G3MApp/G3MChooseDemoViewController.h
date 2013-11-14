//
//  G3MChooseDemoViewController.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface G3MChooseDemoViewController : UITableViewController

@property (weak, nonatomic) UIPopoverController* popoverController;

- (IBAction)changeDemo:(UIButton *)sender;

@end
