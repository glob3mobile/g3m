//
//  G3MAugmentedRealityDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//

#include "G3MAugmentedRealityDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/DeviceAttitudeCameraHandler.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
//#include <G3MiOSSDK/PlanetRenderer.hpp>
//#include <G3MiOSSDK/SingleBilElevationDataProvider.hpp>

#include "G3MDemoModel.hpp"


void G3MAugmentedRealityDemoScene::deactivate(const G3MContext* context) {
  if (_dac != NULL) {
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

  // PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  // planetRenderer->setVerticalExaggeration(1);
  //
  // ElevationDataProvider* elevationDataProvider = new SingleBilElevationDataProvider(URL("file:///full-earth-2048x1024.bil"),
  //                                                                                   Sector::fullSphere(),
  //                                                                                   Vector2I(2048, 1024));
  // planetRenderer->setElevationDataProvider(elevationDataProvider, true);


  _dac = new DeviceAttitudeCameraHandler(true);
  g3mWidget->getCameraRenderer()->addHandler(_dac);

  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);

}
