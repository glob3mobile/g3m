//
//  G3MDoubleGlob3ViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 14/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"

@interface G3MDoubleGlob3ViewController : UIViewController{

  IBOutlet G3MWidget_iOS* upperGlob3;
  IBOutlet G3MWidget_iOS* lowerGlob3;
  
}

@property (retain, nonatomic) G3MWidget_iOS *upperGlob3;
@property (retain, nonatomic) G3MWidget_iOS *lowerGlob3;

@end
