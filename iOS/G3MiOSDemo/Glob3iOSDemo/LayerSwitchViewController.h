//
//  LayerSwitchViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 17/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"
#import "WMSLayer.hpp"

@interface LayerSwitchViewController : UIViewController {
  IBOutlet G3MWidget_iOS *_glob3;

  IBOutlet UIButton *_layerSwitcher;
  bool satelliteLayer;
  
  WMSLayer* _bingLayer;
  WMSLayer* _osmLayer;
}
- (IBAction)switchLayer:(id)sender;

@end
