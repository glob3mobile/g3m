//
//  G3MNightSkyDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#include "G3MNightSkyDemoScene.hpp"

#include <G3M/Angle.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/Layer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/G3MWidget.hpp>

#include "G3MDemoModel.hpp"


// The stars are fixed on the planet's axes (right ascension 0 over longitude 0), so a constellation
// is straight above latitude = declination, longitude = right ascension (in degrees)
struct Constellation {
  const char* _name;
  double      _declinationDegrees;
  double      _rightAscensionDegrees;
};

static const Constellation CONSTELLATIONS[] = {
  { "Orion",                           -1.0,  84.0 },
  { "Big Dipper (Ursa Major)",         55.5, 186.0 },
  { "Southern Cross and the Pointers", -60.0, 203.0 }
};

static const int CONSTELLATIONS_COUNT = sizeof(CONSTELLATIONS) / sizeof(CONSTELLATIONS[0]);

// seen from a plane at cruise altitude
static const double CAMERA_HEIGHT = 11000;


G3MNightSkyDemoScene::G3MNightSkyDemoScene(G3MDemoModel* model) :
G3MDemoScene(model, "Night Sky", "<select constellation>", 0)
{
  for (int i = 0; i < CONSTELLATIONS_COUNT; i++) {
    _options.push_back(CONSTELLATIONS[i]._name);
  }
}

void G3MNightSkyDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model = getModel();

  model->getLayerSet()->addLayer(model->createRasterLayer());

  model->setAtmosphereEnable(false);
}

void G3MNightSkyDemoScene::rawSelectOption(const std::string& option,
                                           int optionIndex) {
  const Constellation& constellation = CONSTELLATIONS[optionIndex];

  G3MWidget* g3mWidget = getModel()->getG3MWidget();
  g3mWidget->setAnimatedCameraPosition(Geodetic3D::fromDegrees(constellation._declinationDegrees,
                                                               constellation._rightAscensionDegrees,
                                                               CAMERA_HEIGHT),
                                       // looking up, heading north leaves the south at the top of the screen
                                       Angle::fromDegrees(180),
                                       Angle::fromDegrees(90));
}
