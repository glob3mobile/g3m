//
//  G3MMarkersViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 19/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"

@interface G3MMarkersViewController : UIViewController <UIAlertViewDelegate> {
  NSString* urlMarkString;
}


@property (strong, nonatomic) IBOutlet G3MWidget_iOS *glob3;

@end

