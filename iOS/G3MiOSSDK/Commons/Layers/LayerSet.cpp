//
//  LayerSet.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "LayerSet.hpp"
#include "Layer.hpp"
#include "LayerTouchEventListener.hpp"
#include "Tile.hpp"
#include "RenderState.hpp"
#include "ChangedListener.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Context.hpp"
#include "TileImageProvider.hpp"
#include "CompositeTileImageProvider.hpp"
#include "Color.hpp"
#include "Info.hpp"

LayerSet::~LayerSet() {
  for (unsigned int i = 0; i < _layers.size(); i++) {
    delete _layers[i];
  }
  
  if (_tileImageProvider != NULL) {
    _tileImageProvider->_release();
  }
}

bool LayerSet::onTerrainTouchEvent(const G3MEventContext* ec,
                                   const Geodetic3D& position,
                                   const Tile* tile) const {

  for (int i = _layers.size()-1; i >= 0; i--) {
    Layer* layer = _layers[i];
    if (layer->isAvailable(tile)) {
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

  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    _layers[i]->initialize(context);
  }
}

void LayerSet::setChangeListener(ChangedListener* listener) {
  if (_listener != NULL) {
    ILogger::instance()->logError("Listener already set");
  }
  _listener = listener;
}

void LayerSet::setTileImageProvider(TileImageProvider* tileImageProvider) {
  if (_tileImageProvider != NULL) {
    ILogger::instance()->logError("TileImageProvider already set");
  }
  _tileImageProvider = tileImageProvider;
}


RenderState LayerSet::getRenderState() {
  _errors.clear();
  bool busyFlag  = false;
  bool errorFlag = false;
  const size_t layersCount = _layers.size();
  
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

Layer* LayerSet::getLayer(size_t index) const {
  if (index < _layers.size()) {
    return _layers[index];
  }

  return NULL;
}

void LayerSet::disableAllLayers() {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    _layers[i]->setEnable(false);
  }
}

Layer* LayerSet::getLayerByTitle(const std::string& title) const {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
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
  changedInfo(layer->getInfo());
}

void LayerSet::removeAllLayers(const bool deleteLayers) {
  const size_t layersSize = _layers.size();
  if (layersSize > 0) {
    for (size_t i = 0; i < layersSize; i++) {
      Layer* layer = _layers[i];
      layer->removeLayerSet(this);
      if (deleteLayers) {
        delete layer;
      }
    }
    _layers.clear();

    layersChanged();
    changedInfo(getInfo());
  }
}

void LayerSet::layerChanged(const Layer* layer) const {
  layersChanged();
}

void LayerSet::layersChanged() const {
  if (_tileImageProvider != NULL) {
    _tileImageProvider->_release();
    _tileImageProvider = NULL;
  }
  if (_listener != NULL) {
    _listener->changed();
  }
}

bool LayerSet::isEquals(const LayerSet* that) const {
  if (that == NULL) {
    return false;
  }

  const size_t thisSize = size();
  const size_t thatSize = that->size();

  if (thisSize != thatSize) {
    return false;
  }

  for (size_t i = 0; i < thisSize; i++) {
    Layer* thisLayer = getLayer(i);
    Layer* thatLayer = that->getLayer(i);

    if (!thisLayer->isEquals(thatLayer)) {
      return false;
    }
  }

  return true;
}

bool LayerSet::checkLayersDataSector(const bool forceFirstLevelTilesRenderOnStart,
                                     std::vector<std::string>& errors) const {

  if (forceFirstLevelTilesRenderOnStart) {
    Sector* biggestDataSector = NULL;

    const size_t layersCount = _layers.size();
    double biggestArea = 0;
    for (size_t i = 0; i < layersCount; i++) {
      Layer* layer = _layers[i];
      if (layer->isEnable()) {
        const double layerArea = layer->getDataSector().getAngularAreaInSquaredDegrees();
        if (layerArea > biggestArea) {
          delete biggestDataSector;
          biggestDataSector = new Sector(layer->getDataSector());
          biggestArea = layerArea;
        }
      }
    }

    if (biggestDataSector != NULL) {
      bool dataSectorsInconsistency = false;
      for (size_t i = 0; i < layersCount; i++) {
        Layer* layer = _layers[i];
        if (layer->isEnable()) {
          if (!biggestDataSector->fullContains(layer->getDataSector())) {
            dataSectorsInconsistency = true;
            break;
          }
        }
      }

      delete biggestDataSector;

      if (dataSectorsInconsistency) {
        errors.push_back("Inconsistency in layers data sectors");
        return false;
      }
    }
  }

  return true;
}

bool LayerSet::checkLayersRenderState(std::vector<std::string>& errors,
                                      std::vector<Layer*>& enableLayers) const {
  bool layerSetNotReadyFlag = false;
  for (size_t i = 0; i < _layers.size(); i++) {
    Layer* layer = _layers[i];

    if (layer->isEnable()) {
      enableLayers.push_back(layer);

      const RenderState layerRenderState = layer->getRenderState();
      const RenderState_Type layerRenderStateType = layerRenderState._type;
      if (layerRenderStateType != RENDER_READY) {
        if (layerRenderStateType == RENDER_ERROR) {
          const std::vector<std::string> layerErrors = layerRenderState.getErrors();
#ifdef C_CODE
          errors.insert(errors.end(), layerErrors.begin(), layerErrors.end());
#endif
#ifdef JAVA_CODE
          errors.addAll(layerErrors);
#endif
        }
        layerSetNotReadyFlag = true;
      }
    }
  }

  return !layerSetNotReadyFlag;
}


class MutableLayerTilesRenderParameters {
private:
  Sector* _topSector;
  int     _topSectorSplitsByLatitude;
  int     _topSectorSplitsByLongitude;
  int     _firstLevel;
  int     _maxLevel;
  int     _tileTextureWidth;
  int     _tileTextureHeight;
  int     _tileMeshWidth;
  int     _tileMeshHeight;
  bool    _mercator;

public:
  MutableLayerTilesRenderParameters() :
  _topSector(NULL),
  _topSectorSplitsByLatitude(0),
  _topSectorSplitsByLongitude(0),
  _firstLevel(0),
  _maxLevel(0),
  _tileTextureWidth(0),
  _tileTextureHeight(0),
  _tileMeshWidth(0),
  _tileMeshHeight(0),
  _mercator(false)
  {
  }

  ~MutableLayerTilesRenderParameters() {
    delete _topSector;
  }

  bool update(const LayerTilesRenderParameters* parameters,
              std::vector<std::string>& errors) {
    if (_topSector == NULL) {
      _topSector                  = new Sector( parameters->_topSector );
      _topSectorSplitsByLatitude  = parameters->_topSectorSplitsByLatitude;
      _topSectorSplitsByLongitude = parameters->_topSectorSplitsByLongitude;
      _firstLevel                 = parameters->_firstLevel;
      _maxLevel                   = parameters->_maxLevel;
      _tileTextureWidth           = parameters->_tileTextureResolution._x;
      _tileTextureHeight          = parameters->_tileTextureResolution._y;
      _tileMeshWidth              = parameters->_tileMeshResolution._x;
      _tileMeshHeight             = parameters->_tileMeshResolution._y;
      _mercator                   = parameters->_mercator;
      return true;
    }

    if ( _mercator != parameters->_mercator ) {
      errors.push_back("Inconsistency in Layer's Parameters: mercator");
      return false;
    }

    if ( !_topSector->isEquals(parameters->_topSector) ) {
      errors.push_back("Inconsistency in Layer's Parameters: topSector");
      return false;
    }

    if ( _topSectorSplitsByLatitude != parameters->_topSectorSplitsByLatitude ) {
      errors.push_back("Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
      return false;
    }

    if ( _topSectorSplitsByLongitude != parameters->_topSectorSplitsByLongitude ) {
      errors.push_back("Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
      return false;
    }

    if (( _tileTextureWidth  != parameters->_tileTextureResolution._x ) ||
        ( _tileTextureHeight != parameters->_tileTextureResolution._y ) ) {
      errors.push_back("Inconsistency in Layer's Parameters: tileTextureResolution");
      return false;
    }

    if (( _tileMeshWidth  != parameters->_tileMeshResolution._x ) ||
        ( _tileMeshHeight != parameters->_tileMeshResolution._y ) ) {
      errors.push_back("Inconsistency in Layer's Parameters: tileMeshResolution");
      return false;
    }

    if ( _maxLevel < parameters->_maxLevel ) {
      ILogger::instance()->logWarning("Inconsistency in Layer's Parameters: maxLevel (upgrading from %d to %d)",
                                      _maxLevel,
                                      parameters->_maxLevel);
      _maxLevel = parameters->_maxLevel;
    }

    if ( _firstLevel < parameters->_firstLevel ) {
      ILogger::instance()->logWarning("Inconsistency in Layer's Parameters: firstLevel (upgrading from %d to %d)",
                                      _firstLevel,
                                      parameters->_firstLevel);
      _firstLevel = parameters->_firstLevel;
    }

    return true;
  }

  bool update(Layer* layer,
              std::vector<std::string>& errors) {
    const std::vector<const LayerTilesRenderParameters*> layerParametersVector = layer->getLayerTilesRenderParametersVector();

    if (_topSector == NULL) {
      return update(layerParametersVector[0], errors);
    }

    int foundI = -1;
    for (int i = 0; i < layerParametersVector.size(); i++) {
      const LayerTilesRenderParameters* parameters = layerParametersVector[i];
      if (parameters->_mercator == _mercator) {
        foundI = i;
        break;
      }
    }

    if (foundI < 0) {
      errors.push_back("Can't find a compatible LayerTilesRenderParameters in layer " + layer->description());
      return false;
    }

    layer->selectLayerTilesRenderParameters(foundI);

    return update(layerParametersVector[foundI], errors);
  }

  LayerTilesRenderParameters* create(std::vector<std::string>& errors) {
    if (_topSector == NULL) {
      errors.push_back("Can't find any enabled Layer");
      return NULL;
    }

    return new LayerTilesRenderParameters(*_topSector,
                                          _topSectorSplitsByLatitude,
                                          _topSectorSplitsByLongitude,
                                          _firstLevel,
                                          _maxLevel,
                                          Vector2I(_tileTextureWidth, _tileTextureHeight),
                                          Vector2I(_tileMeshWidth,    _tileMeshHeight),
                                          _mercator);
  }
};


LayerTilesRenderParameters* LayerSet::checkAndComposeLayerTilesRenderParameters(const bool forceFirstLevelTilesRenderOnStart,
                                                                                const std::vector<Layer*>& enableLayers,
                                                                                std::vector<std::string>& errors) const {

  MutableLayerTilesRenderParameters mutableLayerTilesRenderParameters;

  std::vector<Layer*> multiProjectionLayers;

  for (size_t i = 0; i < enableLayers.size(); i++) {
    Layer* layer = enableLayers[i];

    const std::vector<const LayerTilesRenderParameters*> layerParametersVector = layer->getLayerTilesRenderParametersVector();

    const size_t layerParametersVectorSize = layerParametersVector.size();
    if (layerParametersVectorSize == 0) {
      continue;
    }
    else if (layerParametersVectorSize == 1) {
      if (!mutableLayerTilesRenderParameters.update(layerParametersVector[0], errors)) {
        return NULL;
      }
    }
    else {
      multiProjectionLayers.push_back(layer);
    }
  }

  for (size_t i = 0; i < multiProjectionLayers.size(); i++) {
    Layer* layer = multiProjectionLayers[i];
    if (!mutableLayerTilesRenderParameters.update(layer, errors)) {
      return NULL;
    }
  }
  
  return mutableLayerTilesRenderParameters.create(errors);
}


LayerTilesRenderParameters* LayerSet::createLayerTilesRenderParameters(const bool forceFirstLevelTilesRenderOnStart,
                                                                       std::vector<std::string>& errors) const {

  if (!checkLayersDataSector(forceFirstLevelTilesRenderOnStart, errors)) {
    return NULL;
  }

  std::vector<Layer*> enableLayers;
  if (!checkLayersRenderState(errors, enableLayers)) {
    return NULL;
  }

  return checkAndComposeLayerTilesRenderParameters(forceFirstLevelTilesRenderOnStart, enableLayers, errors);
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

TileImageProvider* LayerSet::createTileImageProvider(const G3MRenderContext* rc,
                                                     const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  TileImageProvider*          singleTileImageProvider    = NULL;
  CompositeTileImageProvider* compositeTileImageProvider = NULL;

  const int layersSize = _layers.size();
  for (int i = 0; i < layersSize; i++) {
    Layer* layer = _layers[i];
    if (layer->isEnable()) {
      TileImageProvider* layerTileImageProvider = layer->createTileImageProvider(rc, layerTilesRenderParameters);
      if (layerTileImageProvider != NULL) {
        if (compositeTileImageProvider != NULL) {
          compositeTileImageProvider->addProvider(layerTileImageProvider);
        }
        else if (singleTileImageProvider == NULL) {
          singleTileImageProvider = layerTileImageProvider;
        }
        else {
          compositeTileImageProvider = new CompositeTileImageProvider();
          compositeTileImageProvider->addProvider(singleTileImageProvider);
          compositeTileImageProvider->addProvider(layerTileImageProvider);
        }
      }
    }
  }
  
  return (compositeTileImageProvider == NULL) ? singleTileImageProvider : compositeTileImageProvider;
}

TileImageProvider* LayerSet::getTileImageProvider(const G3MRenderContext* rc,
                                                  const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  if (_tileImageProvider == NULL) {
    _tileImageProvider = createTileImageProvider(rc, layerTilesRenderParameters);
  }
  return _tileImageProvider;
}

const std::vector<const Info*> LayerSet::getInfo() {
  _infos.clear();
  const int layersCount = _layers.size();
  for (int i = 0; i < layersCount; i++) {
    Layer* layer = _layers[i];
    if (layer->isEnable()) {
      const std::vector<const Info*> layerInfo = layer->getInfo();
      const int infoSize = layerInfo.size();
      for (int j = 0; j < infoSize; j++) {
        _infos.push_back(layerInfo[j]);
      }
    }
  }
  return _infos;
}

void LayerSet::changedInfo(const std::vector<const Info*>& info) {
  if (_changedInfoListener != NULL) {
    _changedInfoListener->changedInfo(getInfo());
  }
}

void LayerSet::setChangedInfoListener(ChangedInfoListener* changedInfoListener) {
  if (_changedInfoListener != NULL) {
    ILogger::instance()->logError("Changed Info Listener of LayerSet already set");
    return;
  }
  _changedInfoListener = changedInfoListener;
  if (_changedInfoListener != NULL) {
    _changedInfoListener->changedInfo(getInfo());
  }
}
