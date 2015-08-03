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
#include <G3MiOSSDK/MarksRenderer.hpp>
#include <G3MiOSSDK/MeshRenderer.hpp>
#include <G3MiOSSDK/ShapesRenderer.hpp>
#include <G3MiOSSDK/ErrorHandling.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/PlanetRenderer.hpp>
#include <G3MiOSSDK/GEOVectorLayer.hpp>
#include <G3MiOSSDK/PointCloudsRenderer.hpp>
#include <G3MiOSSDK/HUDRenderer.hpp>
#include <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#include <G3MiOSSDK/VectorStreamingRenderer.hpp>

#include "G3MDemoScene.hpp"
#include "G3MDemoListener.hpp"
#include "G3MRasterLayersDemoScene.hpp"
#include "G3MVectorialDemoScene.hpp"
#include "G3MMarksDemoScene.hpp"
#include "G3M3DSymbologyDemoScene.hpp"
#include "G3MPointCloudDemoScene.hpp"
#include "G3M3DModelDemoScene.hpp"
#include "G3MCameraDemoScene.hpp"
#include "G3MIsosurfaceDemoScene.hpp"
#include "G3MScenarioDEMDemoScene.hpp"
#include "G3MTiledVectorDemoScene.hpp"
#include "G3MStreamingPointCloud1DemoScene.hpp"
#include "G3MStreamingPointCloud2DemoScene.hpp"
#include "G3MHUDDemoScene.hpp"
#include "G3MNonOverlappingMarksDemoScene.hpp"
#include "G3MCanvas2DDemoScene.hpp"
#include "G3MAugmentedRealityDemoScene.hpp"
#include "G3MAnimatedMarksDemoScene.hpp"
#include "G3MVectorStreamingDemoScene.hpp"

G3MDemoModel::G3MDemoModel(G3MDemoListener*             listener,
                           LayerSet*                    layerSet,
                           MeshRenderer*                meshRenderer,
                           ShapesRenderer*              shapesRenderer,
                           MarksRenderer*               marksRenderer,
                           GEORenderer*                 geoRenderer,
                           PointCloudsRenderer*         pointCloudsRenderer,
                           HUDRenderer*                 hudRenderer,
                           NonOverlappingMarksRenderer* nonOverlappingMarksRenderer,
                           VectorStreamingRenderer*     vectorStreamingRenderer) :
_listener(listener),
_g3mWidget(NULL),
_layerSet(layerSet),
_meshRenderer(meshRenderer),
_shapesRenderer(shapesRenderer),
_marksRenderer(marksRenderer),
_geoRenderer(geoRenderer),
_pointCloudsRenderer(pointCloudsRenderer),
_hudRenderer(hudRenderer),
_nonOverlappingMarksRenderer(nonOverlappingMarksRenderer),
_vectorStreamingRenderer(vectorStreamingRenderer),
_selectedScene(NULL),
_context(NULL)
{
  _scenes.push_back( new G3MRasterLayersDemoScene(this) );
  _scenes.push_back( new G3MScenarioDEMDemoScene(this) );
  _scenes.push_back( new G3MVectorialDemoScene(this) );
  _scenes.push_back( new G3MMarksDemoScene(this) );
  _scenes.push_back( new G3M3DSymbologyDemoScene(this) );
  _scenes.push_back( new G3MPointCloudDemoScene(this) );
  _scenes.push_back( new G3M3DModelDemoScene(this) );
  _scenes.push_back( new G3MCameraDemoScene(this) );
  _scenes.push_back( new G3MIsosurfaceDemoScene(this) );
  _scenes.push_back( new G3MTiledVectorDemoScene(this) );
  _scenes.push_back( new G3MStreamingPointCloud1DemoScene(this) );
  _scenes.push_back( new G3MStreamingPointCloud2DemoScene(this) );
  _scenes.push_back( new G3MHUDDemoScene(this) );
  _scenes.push_back( new G3MNonOverlappingMarksDemoScene(this) );
  _scenes.push_back( new G3MAugmentedRealityDemoScene(this) );
  _scenes.push_back( new G3MAnimatedMarksDemoScene(this) );
  // _scenes.push_back( new G3MCanvas2DDemoScene(this) );
  _scenes.push_back( new G3MVectorStreamingDemoScene(this) );
}

void G3MDemoModel::initializeG3MContext(const G3MContext* context) {
  if (_context != NULL) {
    THROW_EXCEPTION("G3MContext already initialized");
  }
  _context = context;

  selectScene(_scenes[0]);
}

void G3MDemoModel::initializeG3MWidget(G3MWidget* g3mWidget) {
  if (_g3mWidget != NULL) {
    THROW_EXCEPTION("G3MWidget already initialized");
  }
  _g3mWidget = g3mWidget;
}

void G3MDemoModel::reset() {
  _g3mWidget->cancelAllEffects();

  PlanetRenderer* planetRenderer = getPlanetRenderer();
  planetRenderer->setVerticalExaggeration(1);

  ElevationDataProvider* elevationDataProvider = NULL;
  planetRenderer->setElevationDataProvider(elevationDataProvider, true);

  _g3mWidget->setBackgroundColor( Color::fromRGBA(0.0f, 0.1f, 0.2f, 1.0f) );

  _g3mWidget->setRenderedSector( Sector::fullSphere() );

  getG3MWidget()->removeAllPeriodicalTasks();

  getMarksRenderer()->removeAllMarks();
  getMarksRenderer()->setRenderInReverse(false);

  getMeshRenderer()->clearMeshes();
  getShapesRenderer()->removeAllShapes(true);
  getPointCloudsRenderer()->removeAllPointClouds();
  getHUDRenderer()->removeAllWidgets();

  getNonOverlappingMarksRenderer()->removeAllMarks();
  getNonOverlappingMarksRenderer()->removeAllListeners();

  getVectorStreamingRenderer()->removeAllVectorSets();

  _layerSet->removeAllLayers(true);
}

PlanetRenderer* G3MDemoModel::getPlanetRenderer() const {
  return _g3mWidget->getPlanetRenderer();
}

G3MDemoScene* G3MDemoModel::getSceneByName(const std::string& sceneName) const {
  const size_t scenesSize = _scenes.size();
  for (size_t i = 0; i < scenesSize; i++) {
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
    THROW_EXCEPTION("G3MContext not initialized");
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
  ILogger::instance()->logInfo("Selected option \"%s\" in scene \"%s\"",
                               option.c_str(),
                               scene->getName().c_str());

  if (_listener != NULL) {
    _listener->onChangeSceneOption(scene, option, optionIndex);
  }
}

void G3MDemoModel::showDialog(const std::string& title,
                              const std::string& message) const {
  ILogger::instance()->logInfo("Show dialog title=\"%s\", message=\"%s\"",
                               title.c_str(),
                               message.c_str());

  if (_listener != NULL) {
    _listener->showDialog(title, message);
  }
}
