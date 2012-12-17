//
//  DoubleGlob3ViewController.m
//  G3MiOSDemo
//
//  Created by Mari Luz Mateo on 14/12/12.
//
//

#import "DoubleGlob3ViewController.h"

#include "G3MBuilder_iOS.hpp"
#include "LayerSet.hpp"
#include "WMSLayer.hpp"

@implementation DoubleGlob3ViewController
@synthesize _upperGlob3;
@synthesize _lowerGlob3;

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self initUpperWidget];
    [self initLowerWidget];
    
}

- (void)viewDidUnload
{
    [self set_upperGlob3:nil];
    [self set_lowerGlob3:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [_upperGlob3 startAnimation];
    [_lowerGlob3 startAnimation];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
    
    [_upperGlob3 stopAnimation];
    [_lowerGlob3 stopAnimation];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
    } else {
        return YES;
    }
}

- (void) initUpperWidget
{
    G3MBuilder_iOS* builder = new G3MBuilder_iOS(_upperGlob3);
    
    LayerSet* layerSet = new LayerSet();
    layerSet->addLayer([self createLayer: "bing"
                                 enabled: true]);
    builder->setLayerSet(layerSet);
    
    // initialization
    builder->initializeWidget();
}

- (void) initLowerWidget
{
    G3MBuilder_iOS* builder = new G3MBuilder_iOS(_lowerGlob3);
    
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
