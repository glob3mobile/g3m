//
//  G3MDemoModel.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/ILogger.hpp>

#include "G3MDemoScene.hpp"


G3MDemoModel::G3MDemoModel(G3MDemoListener* listener) :
_listener(listener)
{
  _scenes.push_back( new G3MDemoScene("Raster Layers") );
  _scenes.push_back( new G3MDemoScene("Scenario+DEM") );
  _scenes.push_back( new G3MDemoScene("Vectorial") );
  _scenes.push_back( new G3MDemoScene("Markers") );
  _scenes.push_back( new G3MDemoScene("3D Symbology") );
  _scenes.push_back( new G3MDemoScene("Point clouds") );
  _scenes.push_back( new G3MDemoScene("3D Model") );
  _scenes.push_back( new G3MDemoScene("Camera") );
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
#warning Diego at work!

  if (scene != NULL) {
    ILogger::instance()->logInfo("Selected scene \"%s\"", scene->getName().c_str());
  }
}
