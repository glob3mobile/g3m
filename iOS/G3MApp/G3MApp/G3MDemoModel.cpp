//
//  G3MDemoModel.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/ILogger.hpp>
#include <G3MiOSSDK/GEORenderer.hpp>
#include <G3MiOSSDK/GEOTileRasterizer.hpp>
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/ErrorHandling.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>

#include "G3MDemoScene.hpp"
#include "G3MDemoListener.hpp"
#include "G3MRasterLayersDemoScene.hpp"
#include "G3MVectorialDemoScene.hpp"
#include "G3MMarkersDemoScene.hpp"
#include "G3M3DSymbologyDemoScene.hpp"
#include "G3MPointCloudDemoScene.hpp"
#include "G3M3DModelDemoScene.hpp"
#include "G3MCameraDemoScene.hpp"
#include "G3MIsosurfaceDemoScene.hpp"

G3MDemoModel::G3MDemoModel(G3MDemoListener* listener,
                           LayerSet* layerSet,
                           GEORenderer* geoRenderer) :
_listener(listener),
_g3mWidget(NULL),
_layerSet(layerSet),
_geoRenderer(geoRenderer),
_selectedScene(NULL),
_context(NULL)
{
  _scenes.push_back( new G3MRasterLayersDemoScene(this) );
  //  _scenes.push_back( new G3MDemoScene("Scenario+DEM") );
  _scenes.push_back( new G3MVectorialDemoScene(this) );
  _scenes.push_back( new G3MMarkersDemoScene(this) );
  _scenes.push_back( new G3M3DSymbologyDemoScene(this) );
  _scenes.push_back( new G3MPointCloudDemoScene(this) );
  _scenes.push_back( new G3M3DModelDemoScene(this) );
  _scenes.push_back( new G3MCameraDemoScene(this) );
  _scenes.push_back( new G3MIsosurfaceDemoScene(this) );
}

void G3MDemoModel::initializeG3MContext(const G3MContext* context) {
  if (_context != NULL) {
    ERROR("G3MContext already initialized");
  }
  _context = context;

  selectScene(_scenes[0]);
}

void G3MDemoModel::initializeG3MWidget(G3MWidget* g3mWidget) {
  if (_g3mWidget != NULL) {
    ERROR("G3MWidget already initialized");
  }
  _g3mWidget = g3mWidget;
}

void G3MDemoModel::reset() {
  //_g3mWidget->cancelCameraAnimation();
  _g3mWidget->cancelAllEffects();

  _g3mWidget->setBackgroundColor( Color::fromRGBA(0.0f, 0.1f, 0.2f, 1.0f) );

  _g3mWidget->setShownSector( Sector::fullSphere() );

  getMarksRenderer()->removeAllMarks();
  getMeshRenderer()->clearMeshes();
  getShapesRenderer()->removeAllShapes(true);
  getGEOTileRasterizer()->clear();

  _layerSet->removeAllLayers(true);

}

GEOTileRasterizer* G3MDemoModel::getGEOTileRasterizer() const {
  return _geoRenderer->getGEOTileRasterizer();
}

MarksRenderer* G3MDemoModel::getMarksRenderer() const {
  return _geoRenderer->getMarksRenderer();
}

MeshRenderer* G3MDemoModel::getMeshRenderer() const {
  return _geoRenderer->getMeshRenderer();
}

ShapesRenderer* G3MDemoModel::getShapesRenderer() const {
  return _geoRenderer->getShapesRenderer();
}


G3MDemoScene* G3MDemoModel::getSceneByName(const std::string& sceneName) const {
  const int scenesSize = _scenes.size();
  for (int i = 0; i < scenesSize; i++) {
    G3MDemoScene* scene = _scenes[i];
    if (scene->getName() == sceneName) {
      return scene;
    }
  }
  return NULL;
}

void G3MDemoModel::selectScene(const std::string& sceneName) {
  G3MDemoScene* scene = getSceneByName(sceneName);
  if (scene == NULL) {
    ILogger::instance()->logError("Invalid sceneName \"%s\"", sceneName.c_str());
  }
  else {
    selectScene(scene);
  }
}

void G3MDemoModel::selectScene(G3MDemoScene* scene) {
  if (_context == NULL) {
    ERROR("G3MContext not initialized");
  }

  if ((scene != NULL) &&
      (scene != _selectedScene)) {

    if (_selectedScene != NULL) {
      _selectedScene->deactivate(_context);
    }

    ILogger::instance()->logInfo("Selected scene \"%s\"", scene->getName().c_str());

    _selectedScene = scene;

    _selectedScene->activate(_context);
    if (_listener != NULL) {
      _listener->onChangedScene(_selectedScene);
    }
    _selectedScene->activateOptions(_context);
  }

}

void G3MDemoModel::onChangeSceneOption(G3MDemoScene* scene,
                                       const std::string& option,
                                       int optionIndex) {
  ILogger::instance()->logInfo("Selected option \"%s\" in scene \"%s\"", option.c_str(), scene->getName().c_str());

  if (_listener != NULL) {
    _listener->onChangeSceneOption(scene, option, optionIndex);
  }
  
}
