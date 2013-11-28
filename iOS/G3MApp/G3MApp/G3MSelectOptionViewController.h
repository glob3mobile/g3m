//
//  G3MSelectOptionViewController.h
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/17/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <UIKit/UIKit.h>

class G3MDemoScene;

@interface G3MSelectOptionViewController : UITableViewController {
  G3MDemoScene* _scene;
}

-(void)setScene:(G3MDemoScene*) scene;

@property (weak, nonatomic) UIPopoverController* popoverController;

- (IBAction)changeOption:(UIButton *)sender;

@end
