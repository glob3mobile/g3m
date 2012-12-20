//
//  G3MSimpleGlob3ViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 18/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"

@interface G3MSimpleGlob3ViewController : UIViewController {

  IBOutlet G3MWidget_iOS* G3MWidget;
  
}

@property (retain, nonatomic) G3MWidget_iOS* G3MWidget;

@end
