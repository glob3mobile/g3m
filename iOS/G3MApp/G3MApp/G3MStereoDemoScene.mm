//
//  G3MStereoDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MStereoDemoScene.hpp"

#include <G3MSharedSDK/G3MWidget.hpp>
#include <G3MSharedSDK/LayerSet.hpp>
#include <G3MSharedSDK/BingMapsLayer.hpp>
#include <G3MSharedSDK/Geodetic3D.hpp>

#include "G3MDemoModel.hpp"


void G3MStereoDemoScene::deactivate(const G3MContext* context) {
  getModel()->getG3MWidget()->setViewMode(MONO);

  G3MDemoScene::deactivate(context);
}

void G3MStereoDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model = getModel();

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

  G3MWidget* g3mWidget = model->getG3MWidget();
  g3mWidget->setViewMode(STEREO);
  g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(30),
                                       Geodetic3D::fromDegrees(0, 0, 5e7),
                                       Geodetic3D::fromDegrees(27.974652332849732517,-15.583470715075184998,175649.0264215345378),
                                       Angle::zero(),
                                       Angle::fromDegrees(360),
                                       Angle::fromDegrees(-45),
                                       Angle::fromDegrees(-90));

}
