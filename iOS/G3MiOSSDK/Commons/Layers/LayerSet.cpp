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
#include "ChangedListener.hpp"

LayerSet::~LayerSet() {
//  delete _layerTilesRenderParameters;
  for (unsigned int i = 0; i < _layers.size(); i++) {
    delete _layers[i];
  }
}

std::vector<Petition*> LayerSet::createTileMapPetitions(const G3MRenderContext* rc,
                                                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                        const Tile* tile) const {
  std::vector<Petition*> petitions;

  const int layersSize = _layers.size();
  for (int i = 0; i < layersSize; i++) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(rc, tile)) {

#ifdef C_CODE
      const Tile* petitionTile = tile;
#else
      Tile* petitionTile = tile;
#endif
      const int maxLevel = layer->getLayerTilesRenderParameters()->_maxLevel;
      while ((petitionTile->_level > maxLevel) && (petitionTile != NULL)) {
        petitionTile = petitionTile->getParent();
      }

      if (petitionTile == NULL) {
        ILogger::instance()->logError("Can't find a valid tile for petitions");
      }

      std::vector<Petition*> tilePetitions = layer->createTileMapPetitions(rc,
                                                                           layerTilesRenderParameters,
                                                                           petitionTile);

      const int tilePetitionsSize = tilePetitions.size();
      for (int j = 0; j < tilePetitionsSize; j++) {
        petitions.push_back( tilePetitions[j] );
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
      LayerTouchEvent tte(position, tile->_sector, layer);

      if (layer->onLayerTouchEventListener(ec, tte)) {
        return true;
      }
    }
  }

  return false;
}

void LayerSet::initialize(const G3MContext* context) const {
  _context = context;

  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    _layers[i]->initialize(context);
  }
}

RenderState LayerSet::getRenderState() {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;
  const int layersCount = _layers.size();
  
  for (int i = 0; i < layersCount; i++) {
    Layer* child = _layers[i];
    if (child->isEnable()) {
      const RenderState childRenderState = child->getRenderState();
      
      const RenderState_Type childRenderStateType = childRenderState._type;
      
      if (childRenderStateType == RENDER_ERROR) {
        errorFlag = true;
        
        const std::vector<std::string> childErrors = childRenderState.getErrors();
#ifdef C_CODE
        _errors.insert(_errors.end(),
        childErrors.begin(),
        childErrors.end());
#endif
#ifdef JAVA_CODE
        _errors.addAll(childErrors);
#endif
      }
      else if (childRenderStateType == RENDER_BUSY) {
        busyFlag = true;
      }
    }
  }
  
  if (errorFlag) {
    return RenderState::error(_errors);
  }
  else if (busyFlag) {
    return RenderState::busy();
  }
  return RenderState::ready();
}

Layer* LayerSet::getLayer(int index) const {
  if (index < _layers.size()) {
    return _layers[index];
  }

  return NULL;
}

void LayerSet::disableAllLayers() {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    _layers[i]->setEnable(false);
  }
}

Layer* LayerSet::getLayerByName(const std::string& name) const {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    if (_layers[i]->getName() == name) {
      return _layers[i];
    }
  }
  return NULL;
}

Layer* LayerSet::getLayerByTitle(const std::string& title) const {
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    if (_layers[i]->getTitle() == title) {
      return _layers[i];
    }
  }
  return NULL;
}

void LayerSet::addLayer(Layer* layer) {
  layer->setLayerSet(this);
  _layers.push_back(layer);

  if (_context != NULL) {
    layer->initialize(_context);
  }

  layersChanged();
}

void LayerSet::removeAllLayers(const bool deleteLayers) {
  const int layersSize = _layers.size();
  if (layersSize > 0) {
    for (int i = 0; i < layersSize; i++) {
      Layer* layer = _layers[i];
      layer->removeLayerSet(this);
      if (deleteLayers) {
        delete layer;
      }
    }
    _layers.clear();

    layersChanged();
  }
}

void LayerSet::layerChanged(const Layer* layer) const {
  layersChanged();
}

void LayerSet::layersChanged() const {
//  delete _layerTilesRenderParameters;
//  _layerTilesRenderParameters = NULL;

  if (_listener != NULL) {
    _listener->changed();
  }
}

//const LayerTilesRenderParameters* LayerSet::getLayerTilesRenderParameters(std::vector<std::string>& errors) const {
//  if (_layerTilesRenderParameters == NULL) {
//    _layerTilesRenderParameters = createLayerTilesRenderParameters(errors);
//  }
//  return _layerTilesRenderParameters;
//}

bool LayerSet::isEquals(const LayerSet* that) const {
  if (that == NULL) {
    return false;
  }

  const int thisSize = size();
  const int thatSize = that->size();

  if (thisSize != thatSize) {
    return false;
  }

  for (int i = 0; i < thisSize; i++) {
    Layer* thisLayer = getLayer(i);
    Layer* thatLayer = that->getLayer(i);

    if (!thisLayer->isEquals(thatLayer)) {
      return false;
    }
  }

  return true;
}

LayerTilesRenderParameters* LayerSet::createLayerTilesRenderParameters(std::vector<std::string>& errors) const {
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

  bool layerSetNotReadyFlag = false;
  bool first = true;
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    Layer* layer = _layers[i];

    if (layer->isEnable()) {
      const RenderState layerRenderState = layer->getRenderState();
      const RenderState_Type layerRenderStateType = layerRenderState._type;
      if (layerRenderStateType != RENDER_READY) {
        if (layerRenderStateType == RENDER_ERROR) {
          const std::vector<std::string> layerErrors = layerRenderState.getErrors();
#ifdef C_CODE
          errors.insert(errors.end(),
                      layerErrors.begin(),
                      layerErrors.end());
#endif
#ifdef JAVA_CODE
          errors.addAll(layerErrors);
#endif
        }
        layerSetNotReadyFlag = true;
      }
      else {
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
          if ( mercator != layerParam->_mercator ) {
            errors.push_back("Inconsistency in Layer's Parameters: mercator");
            delete topSector;
            return NULL;
          }

          if (!topSector->isEquals(layerParam->_topSector) ) {
            errors.push_back("Inconsistency in Layer's Parameters: topSector");
            delete topSector;
            return NULL;
          }

          if ( topSectorSplitsByLatitude != layerParam->_topSectorSplitsByLatitude ) {
            errors.push_back("Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
            delete topSector;
            return NULL;
          }

          if ( topSectorSplitsByLongitude != layerParam->_topSectorSplitsByLongitude ) {
            errors.push_back("Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
            delete topSector;
            return NULL;
          }

          if (( tileTextureWidth  != layerParam->_tileTextureResolution._x ) ||
              ( tileTextureHeight != layerParam->_tileTextureResolution._y ) ) {
            errors.push_back("Inconsistency in Layer's Parameters: tileTextureResolution");
            delete topSector;
            return NULL;
          }

          if (( tileMeshWidth  != layerParam->_tileMeshResolution._x ) ||
              ( tileMeshHeight != layerParam->_tileMeshResolution._y ) ) {
            errors.push_back("Inconsistency in Layer's Parameters: tileMeshResolution");
            delete topSector;
            return NULL;
          }

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

        }
      }
    }
  }

  if (layerSetNotReadyFlag) {
    return NULL;
  }
  if (first) {
    errors.push_back("Can't find any enabled Layer");
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

void LayerSet::takeLayersFrom(LayerSet* that) {
  if (that == NULL) {
    return;
  }

  std::vector<Layer*> thatLayers;
  const int thatSize = that->size();
  for (int i = 0; i < thatSize; i++) {
    thatLayers.push_back( that->getLayer(i) );
  }

  that->removeAllLayers(false);

  for (int i = 0; i < thatSize; i++) {
    addLayer( thatLayers[i] );
  }
}
