//
//  G3MLayerSwitchViewController.h
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 17/12/12.
//
//

#import <UIKit/UIKit.h>

#import "G3MWidget_iOS.h"
#import "WMSLayer.hpp"

@interface G3MLayerSwitchViewController : UIViewController

@property (weak, nonatomic) IBOutlet G3MWidget_iOS* glob3;

@property (weak, nonatomic) IBOutlet UIButton* layerSwitcher;

@property (nonatomic) WMSLayer* bingLayer;

@property (nonatomic) WMSLayer* osmLayer;

@property (nonatomic) bool satelliteLayer;

- (IBAction)switchLayer:(id)sender;

@end
