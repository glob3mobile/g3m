//
//  LayerSwitchViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 17/12/12.
//
//

#import "LayerSwitchViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"

@implementation LayerSwitchViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    UIWindow* window = [UIApplication sharedApplication].keyWindow;
    
    [window addSubview:_glob3];
    
    CGRect  viewRect = CGRectMake(0, 0, 100, 100);
    _glob3 = [[G3MWidget_iOS alloc] initWithFrame:viewRect];
    [self initWidget];
    
    [window makeKeyAndVisible];
    
    [_glob3 startAnimation];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void) initWidget
{
    G3MBuilder_iOS* builder = new G3MBuilder_iOS(_glob3);
    
    LayerSet* layerSet = new LayerSet();
    layerSet->addLayer([self createLayer: "bing"
                                 enabled: true]);
    builder->setLayerSet(layerSet);
    
    // initialization
    builder->initializeWidget();
}

- (WMSLayer*) createLayer: (const std::string) name
                  enabled: (bool) enabled
{
    if (name == "bing") {
        WMSLayer* bing = new WMSLayer("ve",
                                      URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      NULL);
        bing->setEnable(enabled);
        
        return bing;
    }
    
    if (name == "osm") {
        WMSLayer* osm = new WMSLayer("osm_auto:all",
                                     URL("http://129.206.228.72/cached/osm", false),
                                     WMS_1_1_0,
                                     // Sector::fromDegrees(-85.05, -180.0, 85.05, 180.0),
                                     Sector::fullSphere(),
                                     "image/jpeg",
                                     "EPSG:4326",
                                     "",
                                     false,
                                     NULL);
        osm->setEnable(enabled);
        
        return osm;
    }
    
    if (name == "pnoa") {
        WMSLayer *pnoa = new WMSLayer("PNOA",
                                      URL("http://www.idee.es/wms/PNOA/PNOA", false),
                                      WMS_1_1_0,
                                      Sector::fromDegrees(21, -18, 45, 6),
                                      "image/png",
                                      "EPSG:4326",
                                      "",
                                      true,
                                      NULL);
        pnoa->setEnable(enabled);
        
        return pnoa;
    }
    return nil;
}


@end
