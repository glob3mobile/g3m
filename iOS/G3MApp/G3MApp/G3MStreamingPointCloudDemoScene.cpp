//
//  G3MStreamingPointCloudDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 8/19/14.
//  Copyright (c) 2014 Igo Software SL. All rights reserved.
//

#include "G3MStreamingPointCloudDemoScene.hpp"
#include <G3MiOSSDK/MapBoxLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>

#include "G3MDemoModel.hpp"

void G3MStreamingPointCloudDemoScene::rawSelectOption(const std::string& option,
                                                      int optionIndex) {

}

void G3MStreamingPointCloudDemoScene::rawActivate(const G3MContext *context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  MapBoxLayer* layer = new MapBoxLayer("examples.map-m0t0lrpu",
                                       TimeInterval::fromDays(30),
                                       true,
                                       2);
  model->getLayerSet()->addLayer(layer);

}
