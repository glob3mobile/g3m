//
//  G3MDemoModel.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#include "G3MDemoModel.hpp"

#include <G3MiOSSDK/ILogger.hpp>

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

void G3MDemoModel::selectScene(const std::string& sceneName) {
#warning Diego at work!

  ILogger::instance()->logInfo("Selected scene \"%s\"", sceneName.c_str());
}
