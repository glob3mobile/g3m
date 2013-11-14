//
//  G3MToolbar.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 06/02/13.
//
//

#import <UIKit/UIKit.h>

@interface G3MToolbar : UIView {
  UIDeviceOrientation _currentOrientation;
}

@property (nonatomic) BOOL visible;

- (void) removeAllSubviews;

@end
