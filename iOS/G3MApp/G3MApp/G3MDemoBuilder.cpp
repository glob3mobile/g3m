//
//  G3MDemoBuilder.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoBuilder.hpp"

#include <G3MiOSSDK/PlanetRendererBuilder.hpp>
#include <G3MiOSSDK/SingleBillElevationDataProvider.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/IG3MBuilder.hpp>
#include <G3MiOSSDK/ErrorHandling.hpp>

#include "G3MDemoModel.hpp"


G3MDemoBuilder::G3MDemoBuilder(G3MDemoListener* listener) :
_listener(listener),
_initialized(false),
_model(NULL)
{
}

G3MDemoBuilder::~G3MDemoBuilder() {
}

void G3MDemoBuilder::build() {
  if (_initialized) {
    ERROR("G3MWidget already initialized.");
  }

  IG3MBuilder* builder = getG3MBuilder();

  const float verticalExaggeration = 1.0f;
  builder->getPlanetRendererBuilder()->setVerticalExaggeration(verticalExaggeration);

  ElevationDataProvider* elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
                                                                                     Sector::fullSphere(),
                                                                                     Vector2I(2048, 1024));
  builder->getPlanetRendererBuilder()->setElevationDataProvider(elevationDataProvider);


  LayerSet* layerSet = new LayerSet();
  builder->getPlanetRendererBuilder()->setLayerSet(layerSet);

  GEORenderer* geoRenderer = builder->createGEORenderer(NULL);

  //  ShapesRenderer* shapeRenderer = new ShapesRenderer();
  //  shapeRenderer->setEnable(false);
  //  builder.addRenderer(shapeRenderer);
  //
  //  MarksRenderer* marksRenderer = G3MMarksRenderer::createMarksRenderer(self);
  //  marksRenderer->setEnable(false);
  //  builder.addRenderer(marksRenderer);
  //
  //  MeshRenderer* meshRenderer = G3MMeshRenderer::createMeshRenderer(builder.getPlanet());
  //  meshRenderer->setEnable(false);
  //  builder.addRenderer(meshRenderer);
  //
  //  G3MAppUserData* userData = new G3MAppUserData(layerSet,
  //                                                shapeRenderer,
  //                                                marksRenderer,
  //                                                meshRenderer);
  //  userData->setSatelliteLayerEnabled(true);
  //  userData->setLayerSet([self createLayerSet: userData->getSatelliteLayerEnabled()]);
  //  userData->setMarksRenderer(marksRenderer);
  //  userData->setShapeRenderer(shapeRenderer);
  //  userData->setMeshRenderer(meshRenderer);

  // Setup the builder
  //  builder.getPlanetRendererBuilder()->setShowStatistics(true);
  //  builder.setInitializationTask(new G3MAppInitializationTask(self.g3mWidget), true);
  //  builder.setUserData(userData);

  _initialized = true;
  _model = new G3MDemoModel(_listener, layerSet, geoRenderer);
}

G3MDemoModel* G3MDemoBuilder::getModel() {
  if (!_initialized || _model == NULL) {
    ERROR("Model not yet created. Have to initialize the widget before getting the model.");
  }
  return _model;
}
