//
//  G3MStereoDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MStereoDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include "G3MDemoModel.hpp"

#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>


void G3MStereoDemoScene::deactivate(const G3MContext* context) {
  [_locationManager stopUpdatingLocation];
  [_locationManager stopUpdatingHeading];
  _locationManager = nil;

  [_motionManager stopDeviceMotionUpdates];
  _motionManager = nil;

  G3MDemoScene::deactivate(context);
}


void G3MStereoDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


//  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
//                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
//                                           TimeInterval::fromDays(30));

//  MapQuestLayer* layer = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));

  URLTemplateLayer* layer = URLTemplateLayer::newMercator("http://api.mapbox.com/v4/mapbox.streets-satellite/{z}/{x}/{y}.jpg70?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IlhHVkZmaW8ifQ.hAMX5hSW-QnTeRCMAy9A8Q",
                                                          Sector::FULL_SPHERE,
                                                          false, // isTransparent
                                                          2,     // firstLevel
                                                          22,    // maxLevel
                                                          TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);
  
}
