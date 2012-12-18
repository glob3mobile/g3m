//
//  DoubleGlob3ViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 14/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"

@interface DoubleGlob3ViewController : UIViewController{

  IBOutlet G3MWidget_iOS* _upperGlob3;
  IBOutlet G3MWidget_iOS* _lowerGlob3;
  
}

@property (retain, nonatomic) G3MWidget_iOS *_upperGlob3;
@property (retain, nonatomic) G3MWidget_iOS *_lowerGlob3;

@end
