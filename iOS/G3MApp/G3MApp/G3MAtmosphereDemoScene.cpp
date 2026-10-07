//
//  G3MAtmosphereDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#include "G3MAtmosphereDemoScene.hpp"

#include <G3M/Angle.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Layer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/LayerBuilder.hpp>
#include <G3M/G3MWidget.hpp>

#include "G3MDemoModel.hpp"


struct AtmosphereCameraPose {
  const char* _name;
  double      _latitudeDegrees;
  double      _longitudeDegrees;
  double      _height;
  // 0 is the horizon, -90 is nadir, positive looks up
  double      _pitchDegrees;
  bool        _openStreetMap;
};

static const AtmosphereCameraPose POSES[] = {
  { "Space, 20000 km, nadir",                40.4719,  -3.5626, 20000000, -90 },
  { "Space, 20000 km, nadir, Open Street Map", 40.4719, -3.5626, 20000000, -90, true },
  { "Limb, 3000 km, pitch -60",              40.4719,  -3.5626,  3000000, -60 },
  { "Horizon, 500 km, pitch -20",            40.4719,  -3.5626,   500000, -20 },
  { "Sky, 40 km, pitch +20",                 40.4719,  -3.5626,    40000,  20 },
  { "Cruise, 11 km, horizon (pitch -5)",     40.4719,  -3.5626,    11000,  -5 },
  { "Cruise, 11 km, sky (pitch +20)",        40.4719,  -3.5626,    11000,  20 },
  { "Above the 8 km cut-off, 8.5 km, pitch +10", 40.4719, -3.5626,  8500,  10 },
  { "Below the 8 km cut-off, 7.5 km, pitch +10", 40.4719, -3.5626,  7500,  10 },
  { "Low, 2 km, pitch +5",                   40.4719,  -3.5626,     2000,   5 },
  { "Equator, 20 km, pitch +10",              0.0,     -3.5626,    20000,  10 },
  { "Near the North Pole, 20 km, pitch +10", 89.0,     -3.5626,    20000,  10 }
};

static const int POSES_COUNT = sizeof(POSES) / sizeof(POSES[0]);


G3MAtmosphereDemoScene::G3MAtmosphereDemoScene(G3MDemoModel* model) :
G3MDemoScene(model, "Atmosphere", "<select camera pose>", 0),
_satelliteLayer(NULL),
_openStreetMapLayer(NULL)
{
  for (int i = 0; i < POSES_COUNT; i++) {
    addOption(POSES[i]._name);
  }
}

void G3MAtmosphereDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model = getModel();

  _satelliteLayer = model->createRasterLayer();
  model->getLayerSet()->addLayer(_satelliteLayer);

  _openStreetMapLayer = LayerBuilder::createOSMLayer();
  _openStreetMapLayer->setEnable(false);
  model->getLayerSet()->addLayer(_openStreetMapLayer);
}

void G3MAtmosphereDemoScene::rawSelectOption(const std::string& option,
                                             int optionIndex) {
  const AtmosphereCameraPose& pose = POSES[optionIndex];

  _satelliteLayer->setEnable(!pose._openStreetMap);
  _openStreetMapLayer->setEnable(pose._openStreetMap);

  G3MWidget* g3mWidget = getModel()->getG3MWidget();
  g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(pose._latitudeDegrees,
                                                               pose._longitudeDegrees,
                                                               pose._height),
                                       Angle::zero(),
                                       Angle::fromDegrees(pose._pitchDegrees));
}
