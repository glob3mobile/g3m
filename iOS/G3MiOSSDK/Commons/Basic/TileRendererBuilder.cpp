//
//  TileRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#include "TileRendererBuilder.hpp"
#include "WMSLayer.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "EllipsoidalTileTessellator.hpp"
#include "LayerBuilder.hpp"
#include "DownloadPriority.hpp"
#include "WMSBillElevationDataProvider.hpp"

TileRendererBuilder::TileRendererBuilder() {  
  _showStatistics = false;
  _renderDebug = false;
  _useTilesSplitBudget = true;
  _forceTopLevelTilesRenderOnStart = true;
  _incrementalTileQuality = false;

  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileTessellator = NULL;
  _texturePriority = DownloadPriority::HIGHER;
}

TileRendererBuilder::~TileRendererBuilder() {
  delete _parameters;
  delete _layerSet;
  delete _texturizer;
  delete _tileTessellator;
}

/**
 * Returns the _tileTessellator.
 * Lazy initialization.
 *
 * @return _tileTessellator: TileTessellator*
 */
TileTessellator* TileRendererBuilder::getTileTessellator() {
  if (!_tileTessellator) {
    _tileTessellator = createTileTessellator();
  }
  
  return _tileTessellator;
}

/**
 * Returns the _texturizer.
 * Lazy initialization.
 *
 * @return _texturizer: TileTexturizer*
 */
TileTexturizer* TileRendererBuilder::getTexturizer() {
  if (!_texturizer) {
    _texturizer = new MultiLayerTileTexturizer();
  }
  
  return _texturizer;
}

/**
 * Returns the _layerSet.
 * Lazy initialization.
 *
 * @return _layerSet: LayerSet*
 */
LayerSet* TileRendererBuilder::getLayerSet() {
  if (!_layerSet) {
    _layerSet = createLayerSet();
  }
  
  return _layerSet;
}

/**
 * Returns the _parameters.
 * Lazy initialization.
 *
 * @return _parameters: TilesRenderParameters*
 */
TilesRenderParameters* TileRendererBuilder::getParameters() {
  if (!_parameters) {
    _parameters = createTileRendererParameters();
  }
  
  return _parameters;
}

/**
 * Returns the showStatistics flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _showStatistics: bool
 */
bool TileRendererBuilder::getShowStatistics() {
  return _showStatistics;
}

/**
 * Returns the renderDebug flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _renderDebug: bool
 */
bool TileRendererBuilder::getRenderDebug() {
  return _renderDebug;
}

/**
 * Returns the useTilesSplitBudget flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _useTilesSplitBudget: bool
 */
bool TileRendererBuilder::getUseTilesSplitBudget() {
  return _useTilesSplitBudget;
}

/**
 * Returns the forceTopLevelTilesRenderOnStart flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _forceTopLevelTilesRenderOnStart: bool
 */
bool TileRendererBuilder::getForceTopLevelTilesRenderOnStart() {
  return _forceTopLevelTilesRenderOnStart;
}

/**
 * Returns the incrementalTileQuality flag.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _incrementalTileQuality: bool
 */
bool TileRendererBuilder::getIncrementalTileQuality() {
  return _incrementalTileQuality;
}

/**
 * Returns the array of visibleSectorListeners.
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _forceTopLevelTilesRenderOnStart: std::vector<VisibleSectorListener*>
 */
std::vector<VisibleSectorListener*> TileRendererBuilder::getVisibleSectorListeners() {
  return _visibleSectorListeners;
}

/**
  * Returns the array of stabilization milliseconds related to visible-sector listeners.
  * Method created to keep convention. It is not needed as it does not have to create a default value.
  *
  * @return _stabilizationMilliSeconds: std::vector<long long>
  */
std::vector<long long> TileRendererBuilder::getStabilizationMilliSeconds() {
  return _stabilizationMilliSeconds;
}

/**
 * Returns the _texturePriority
 * Method created to keep convention. It is not needed as it does not have to create a default value.
 *
 * @return _texturePriority: long long
 */
long long TileRendererBuilder::getTexturePriority() {
  return _texturePriority;
}

/**
 * Returns an array with the names of the layers that make up the default layerSet
 *
 * @return layersNames: std::vector<std::string>
 */
std::vector<std::string> TileRendererBuilder::getDefaultLayersNames() {
  std::vector<std::string> layersNames;
  
  for (int i = 0; i < getLayerSet()->size(); i++) {
    layersNames.push_back(getLayerSet()->get(i)->getName());
  }
  
  return layersNames;
}

void TileRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator) {
    ILogger::instance()->logError("LOGIC ERROR: _tileTessellator already initialized");
    return;
  }
  _tileTessellator = tileTessellator;
}

void TileRendererBuilder::setTileTexturizer(TileTexturizer *tileTexturizer) {
  if (_texturizer) {
    ILogger::instance()->logError("LOGIC ERROR: _texturizer already initialized");
    return;
  }
  _texturizer = tileTexturizer;
}

void TileRendererBuilder::setLayerSet(LayerSet *layerSet) {
  if (_layerSet) {
    ILogger::instance()->logError("LOGIC ERROR: _layerSet already initialized");
    return;
  }
  _layerSet = layerSet;
}

void TileRendererBuilder::setTileRendererParameters(TilesRenderParameters *parameters) {
  if (_parameters) {
    ILogger::instance()->logError("LOGIC ERROR: _parameters already initialized");
    return;
  }
  _parameters = parameters;
}

void TileRendererBuilder::setShowStatistics(const bool showStatistics) {
  _showStatistics = showStatistics;
}

void TileRendererBuilder::setRenderDebug(const bool renderDebug) {
  _renderDebug = renderDebug;
}

void TileRendererBuilder::setUseTilesSplitBuget(const bool useTilesSplitBudget) {
  _useTilesSplitBudget = useTilesSplitBudget;
}

void TileRendererBuilder::setForceTopLevelTilesRenderOnStart(const bool forceTopLevelTilesRenderOnStart) {
  _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
}

void TileRendererBuilder::setIncrementalTileQuality(const bool incrementalTileQuality) {
  _incrementalTileQuality = incrementalTileQuality;
}

void TileRendererBuilder::addVisibleSectorListener(VisibleSectorListener* listener,
                                                   const TimeInterval& stabilizationInterval) {
  getVisibleSectorListeners().push_back(listener);
  getStabilizationMilliSeconds().push_back(stabilizationInterval.milliseconds());
}

void TileRendererBuilder::setTexturePriority(long long texturePriority) {
  _texturePriority = texturePriority;
}

TileRenderer* TileRendererBuilder::create() {
  int __TODO_make_inflator_configurable;
  //  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
  ElevationDataProvider* elevationDataProvider = NULL;
  TileRenderer* tileRenderer = new TileRenderer(getTileTessellator(),
                                                elevationDataProvider,
                                                getTexturizer(),
                                                getLayerSet(),
                                                getParameters(),
                                                getShowStatistics(),
                                                getTexturePriority());
  
  for (int i = 0; i < getVisibleSectorListeners().size(); i++) {
    tileRenderer->addVisibleSectorListener(getVisibleSectorListeners()[i],
                                           TimeInterval::fromMilliseconds(getStabilizationMilliSeconds()[i]));
  }
  
  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileTessellator = NULL;
  
  return tileRenderer;
}

TilesRenderParameters* TileRendererBuilder::createTileRendererParameters() {
  return TilesRenderParameters::createDefault(getRenderDebug(),
                                              getUseTilesSplitBudget(),
                                              getForceTopLevelTilesRenderOnStart(),
                                              getIncrementalTileQuality());
}

TileTessellator* TileRendererBuilder::createTileTessellator() {
  TileTessellator* tileTessellator = new EllipsoidalTileTessellator(getParameters()->_tileResolution, true);

  return tileTessellator;
}

LayerSet* TileRendererBuilder::createLayerSet() {
  LayerSet* layerSet = LayerBuilder::createDefaultSatelliteImagery();
  
  return layerSet;
}