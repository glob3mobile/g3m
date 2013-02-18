//
//  G3MToolbar.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 06/02/13.
//
//

#import <UIKit/UIKit.h>

@interface G3MToolbar : UIView {
  UIDeviceOrientation currentOrientation;
}

@property (nonatomic) BOOL visible;

- (void) clear;
- (void) addTool:(UIView *)view;

@end
