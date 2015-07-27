//
//  G3MAugmentedRealityDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MAugmentedRealityDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include "G3MDemoModel.hpp"

#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>

#include <G3MiOSSDK/DeviceAttitudeCameraConstrainer.hpp>

void G3MAugmentedRealityDemoScene::deactivate(const G3MContext* context) {
  if (_dac != NULL){
    getModel()->getG3MWidget()->removeCameraConstrainer(_dac);
    delete _dac;
    _dac = NULL;
  }
  
  Camera* camera = getModel()->getG3MWidget()->getNextCamera();
  camera->setHeadingPitchRoll(Angle::zero(), Angle::fromDegrees(-90), Angle::zero());
  
  G3MDemoScene::deactivate(context);
}

void G3MAugmentedRealityDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();
  
  _dac = new DeviceAttitudeCameraConstrainer();
  g3mWidget->addCameraConstrainer(_dac);


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
//  MapQuestLayer* layer = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);
  
  Camera* camera = g3mWidget->getNextCamera();
  camera->setGeodeticPosition( Geodetic3D::fromDegrees(28.1001809,-15.4147574, 500) );
}
