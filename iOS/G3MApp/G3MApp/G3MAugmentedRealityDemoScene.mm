//
//  G3MAugmentedRealityDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//

#include "G3MAugmentedRealityDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include "G3MDemoModel.hpp"

#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>

#include <G3MiOSSDK/CameraRenderer.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>

void G3MAugmentedRealityDemoScene::deactivate(const G3MContext* context) {
  if (_dac != NULL){
    getModel()->getG3MWidget()->getCameraRenderer()->removeHandler(_dac);
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
  g3mWidget->setViewMode(MONO);

  _dac = new DeviceAttitudeCameraHandler(true);
  g3mWidget->getCameraRenderer()->addHandler(_dac);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));

//  MapQuestLayer* layer = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));

//  URLTemplateLayer* layer = URLTemplateLayer::newMercator("http://api.mapbox.com/v4/mapbox.streets-satellite/{z}/{x}/{y}.jpg70?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6IlhHVkZmaW8ifQ.hAMX5hSW-QnTeRCMAy9A8Q",
//                                                          Sector::FULL_SPHERE,
//                                                          false, // isTransparent
//                                                          2,     // firstLevel
//                                                          22,    // maxLevel
//                                                          TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);
  
  Camera* camera = g3mWidget->getNextCamera();
  camera->setGeodeticPosition( Geodetic3D::fromDegrees(28.1001809,-15.4147574, 500) );
}
