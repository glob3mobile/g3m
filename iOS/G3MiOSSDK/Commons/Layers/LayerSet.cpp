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
#include "LayerTilesRenderParameters.hpp"

LayerSet::~LayerSet() {
  delete _layerTilesRenderParameters;
  for (unsigned int i = 0; i < _layers.size(); i++) {
    delete _layers[i];
  }
}

std::vector<Petition*> LayerSet::createTileMapPetitions(const G3MRenderContext* rc,
                                                        const Tile* tile) const {
  std::vector<Petition*> petitions;

  const int layersSize = _layers.size();
  for (int i = 0; i < layersSize; i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(rc, tile)) {
      
      const Tile* petitionTile = tile;
      while (petitionTile->getLevel() > layer->getLayerTilesRenderParameters()->_maxLevel && petitionTile != NULL) {
        petitionTile = petitionTile->getParent();
      }
      
      if (petitionTile == NULL){
        printf("Error retrieving requests.");
      }
      
      std::vector<Petition*> pet = layer->createTileMapPetitions(rc, petitionTile);

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

bool LayerSet::onTerrainTouchEvent(const G3MEventContext* ec,
                                   const Geodetic3D& position,
                                   const Tile* tile) const {



  for (int i = _layers.size()-1; i >= 0; i--) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(ec, tile)) {
      TerrainTouchEvent tte(position, tile->getSector(), layer);

      if (layer->onTerrainTouchEventListener(ec, tte)) {
        return true;
      }
    }
  }

  return false;
}

void LayerSet::initialize(const G3MContext* context)const{
  for (int i = 0; i<_layers.size(); i++){
    _layers[i]->initialize(context);
  }
}

bool LayerSet::isReady() const {
  const int layersCount = _layers.size();
  if (layersCount < 1) {
    return false;
  }

  for (int i = 0; i < layersCount; i++){
    if (!(_layers[i]->isReady())) {
      return false;
    }
  }
  return true;
}

Layer* LayerSet::get(int index) {
  if (index < _layers.size()) {
    return _layers[index];
  }

  return NULL;
}

Layer* LayerSet::getLayer(const std::string &name) {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    if (_layers[i]->getName() == name) {
      return _layers[i];
    }
  }

  return NULL;
}

void LayerSet::addLayer(Layer* layer) {
  layer->setLayerSet(this);
  _layers.push_back(layer);

  layersChanged();
}

void LayerSet::layerChanged(const Layer* layer) const {
  layersChanged();
}

void LayerSet::layersChanged() const {
  delete _layerTilesRenderParameters;
  _layerTilesRenderParameters = NULL;

  if (_listener != NULL) {
    _listener->changed(this);
  }
}

const LayerTilesRenderParameters* LayerSet::getLayerTilesRenderParameters() const {
  if (_layerTilesRenderParameters == NULL) {
    _layerTilesRenderParameters = createLayerTilesRenderParameters();
  }
  return _layerTilesRenderParameters;
}

LayerTilesRenderParameters* LayerSet::createLayerTilesRenderParameters() const {
  Sector* topSector                  = NULL;
  int     topSectorSplitsByLatitude  = 0;
  int     topSectorSplitsByLongitude = 0;
  int     firstLevel                 = 0;
  int     maxLevel                   = 0;
  int     tileTextureWidth           = 0;
  int     tileTextureHeight          = 0;
  int     tileMeshWidth              = 0;
  int     tileMeshHeight             = 0;
  bool    mercator                   = false;

  bool first = true;
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    Layer* layer = _layers[i];

    if (layer->isEnable() && layer->isReady()) {
      const LayerTilesRenderParameters* layerParam = layer->getLayerTilesRenderParameters();

      if (layerParam == NULL) {
        continue;
      }

      if (first) {
        first = false;

        topSector                  = new Sector( layerParam->_topSector );
        topSectorSplitsByLatitude  = layerParam->_topSectorSplitsByLatitude;
        topSectorSplitsByLongitude = layerParam->_topSectorSplitsByLongitude;
        firstLevel                 = layerParam->_firstLevel;
        maxLevel                   = layerParam->_maxLevel;
        tileTextureWidth           = layerParam->_tileTextureResolution._x;
        tileTextureHeight          = layerParam->_tileTextureResolution._y;
        tileMeshWidth              = layerParam->_tileMeshResolution._x;
        tileMeshHeight             = layerParam->_tileMeshResolution._y;
        mercator                   = layerParam->_mercator;
      }
      else {
        if (!topSector->isEqualsTo(layerParam->_topSector) ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: topSector");
          return NULL;
        }

        if ( topSectorSplitsByLatitude != layerParam->_topSectorSplitsByLatitude ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
          return NULL;
        }

        if ( topSectorSplitsByLongitude != layerParam->_topSectorSplitsByLongitude ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
          return NULL;
        }

        // if ( maxLevel != layerParam->_maxLevel ) {
        //   ILogger::instance()->logError("Inconsistency in Layer's Parameters: maxLevel");
        //   return NULL;
        // }
        if ( maxLevel < layerParam->_maxLevel ) {
          ILogger::instance()->logWarning("Inconsistency in Layer's Parameters: maxLevel (upgrading from %d to %d)",
                                          maxLevel,
                                          layerParam->_maxLevel);
          maxLevel = layerParam->_maxLevel;
        }

        if ( firstLevel < layerParam->_firstLevel ) {
          ILogger::instance()->logWarning("Inconsistency in Layer's Parameters: firstLevel (upgrading from %d to %d)",
                                          firstLevel,
                                          layerParam->_firstLevel);
          firstLevel = layerParam->_firstLevel;
        }

        if (( tileTextureWidth  != layerParam->_tileTextureResolution._x ) ||
            ( tileTextureHeight != layerParam->_tileTextureResolution._y ) ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: tileTextureResolution");
          return NULL;
        }

        if (( tileMeshWidth  != layerParam->_tileMeshResolution._x ) ||
            ( tileMeshHeight != layerParam->_tileMeshResolution._y ) ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: tileMeshResolution");
          return NULL;
        }

        if ( mercator != layerParam->_mercator ) {
          ILogger::instance()->logError("Inconsistency in Layer's Parameters: mercator");
          return NULL;
        }

      }
    }
  }

  if (first) {
    ILogger::instance()->logError("Can't create LayerSet's LayerTilesRenderParameters, not found any enable Layer");
    return NULL;
  }

  LayerTilesRenderParameters* parameters = new LayerTilesRenderParameters(*topSector,
                                                                          topSectorSplitsByLatitude,
                                                                          topSectorSplitsByLongitude,
                                                                          firstLevel,
                                                                          maxLevel,
                                                                          Vector2I(tileTextureWidth, tileTextureHeight),
                                                                          Vector2I(tileMeshWidth,    tileMeshHeight),
                                                                          mercator);

  delete topSector;

  return parameters;
}
