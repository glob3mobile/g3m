//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"
#include "Tile.hpp"
#include "TileKey.hpp"

std::vector<Petition*> LayerSet::createTileMapPetitions(const RenderContext* rc,
                                                        const Tile* tile,
                                                        int width, int height) const {
  std::vector<Petition*> petitions;
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(rc, tile)) {
      std::vector<Petition*> pet = layer->getMapPetitions(rc, tile, width, height);
      
      //Storing petitions
      for (int j = 0; j < pet.size(); j++) {
        petitions.push_back(pet[j]);
      }
    }
  }
  
  if (petitions.empty()) {
    rc->getLogger()->logWarning("Can't create map petitions for tile %s",
                                tile->getKey().description().c_str());
  }
  
  return petitions;
}

void LayerSet::onTerrainTouchEvent(const EventContext* ec,
                                   const Geodetic3D& position,
                                   const Tile* tile) const {
  
  for (int i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(ec, tile)) {
      TerrainTouchEvent tte(position, tile->getSector(), layer);
      
      layer->onTerrainTouchEventListener(ec, tte);
    }
  }
  
}

void LayerSet::initialize(const G3MContext* context)const{
  for (int i = 0; i<_layers.size(); i++){
    _layers[i]->initialize(context);
  }
}

bool LayerSet::isReady()const{
  for (int i = 0; i<_layers.size(); i++){
    if (!(_layers[i]->isReady())) {
      return false;
    }
  }
  return true;
}

void LayerSet::addLayer(Layer* layer) {
  layer->setLayerSet(this);
  _layers.push_back(layer);
  if (_listener == NULL) {
    ILogger::instance()->logError("Can't notify, _listener not set");
  }
  else {
    _listener->changed(this);
  }
}

void LayerSet::layerChanged(const Layer* layer) const {
  if (_listener == NULL) {
    ILogger::instance()->logError("Can't notify, _listener not set");
  }
  else {
    _listener->changed(this);
  }
}
