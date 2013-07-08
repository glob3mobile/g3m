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
#include "ElevationDataProvider.hpp"
#include "TileRasterizer.hpp"


TileRendererBuilder::TileRendererBuilder() {  
  _showStatistics = false;
  _renderDebug = false;
  _useTilesSplitBudget = true;
  _forceFirstLevelTilesRenderOnStart = true;
  _incrementalTileQuality = false;

  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileRasterizer = NULL;
  _tileTessellator = NULL;
  _visibleSectorListeners = NULL;
  _stabilizationMilliSeconds = NULL;
  _texturePriority = DownloadPriority::HIGHER;

  _elevationDataProvider = NULL;
  _verticalExaggeration = 0.0f;
}

TileRendererBuilder::~TileRendererBuilder() {
  delete _parameters;
  delete _layerSet;
  delete _texturizer;
  delete _tileRasterizer;
  delete _tileTessellator;
  delete _elevationDataProvider;
}

/**
 * Returns the _tileTessellator.
 *
 * @return _tileTessellator: TileTessellator*
 */
TileTessellator* TileRendererBuilder::getTileTessellator() {
  if (!_tileTessellator) {
    _tileTessellator = createTileTessellator();
  }
  
  return _tileTessellator;
}

TileRasterizer* TileRendererBuilder::getTileRasterizer() {
  return _tileRasterizer;
}

/**
 * Returns the _texturizer.
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
 *
 * @return _showStatistics: bool
 */
bool TileRendererBuilder::getShowStatistics() {
  return _showStatistics;
}

/**
 * Returns the renderDebug flag.
 *
 * @return _renderDebug: bool
 */
bool TileRendererBuilder::getRenderDebug() {
  return _renderDebug;
}

/**
 * Returns the useTilesSplitBudget flag.
 *
 * @return _useTilesSplitBudget: bool
 */
bool TileRendererBuilder::getUseTilesSplitBudget() {
  return _useTilesSplitBudget;
}

/**
 * Returns the forceFirstLevelTilesRenderOnStart flag.
 *
 * @return _forceFirstLevelTilesRenderOnStart: bool
 */
bool TileRendererBuilder::getForceFirstLevelTilesRenderOnStart() {
  return _forceFirstLevelTilesRenderOnStart;
}

/**
 * Returns the incrementalTileQuality flag.
 *
 * @return _incrementalTileQuality: bool
 */
bool TileRendererBuilder::getIncrementalTileQuality() {
  return _incrementalTileQuality;
}

/**
 * Returns the array of visibleSectorListeners.
 */
std::vector<VisibleSectorListener*>* TileRendererBuilder::getVisibleSectorListeners() {
  if (!_visibleSectorListeners) {
    _visibleSectorListeners = new std::vector<VisibleSectorListener*>;
  }
  return _visibleSectorListeners;
}

/**
  * Returns the array of stabilization milliseconds related to visible-sector listeners.
  *
  * @return _stabilizationMilliSeconds: std::vector<long long>
  */
std::vector<long long>* TileRendererBuilder::getStabilizationMilliSeconds() {
  if (!_stabilizationMilliSeconds) {
    _stabilizationMilliSeconds = new std::vector<long long>;
  }
  return _stabilizationMilliSeconds;
}

/**
 * Returns the _texturePriority.
 *
 * @return _texturePriority: long long
 */
long long TileRendererBuilder::getTexturePriority() {
  return _texturePriority;
}

void TileRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator) {
    ILogger::instance()->logError("LOGIC ERROR: _tileTessellator already initialized");
    return;
  }
  _tileTessellator = tileTessellator;
}

void TileRendererBuilder::setTileRasterizer(TileRasterizer* tileRasterizer) {
  if (_tileRasterizer) {
    ILogger::instance()->logError("LOGIC ERROR: _tileRasterizer already initialized");
    return;
  }
  _tileRasterizer = tileRasterizer;
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

void TileRendererBuilder::setForceFirstLevelTilesRenderOnStart(const bool forceFirstLevelTilesRenderOnStart) {
  _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
}

void TileRendererBuilder::setIncrementalTileQuality(const bool incrementalTileQuality) {
  _incrementalTileQuality = incrementalTileQuality;
}

void TileRendererBuilder::addVisibleSectorListener(VisibleSectorListener* listener,
                                                   const TimeInterval& stabilizationInterval) {
  getVisibleSectorListeners()->push_back(listener);
  getStabilizationMilliSeconds()->push_back(stabilizationInterval.milliseconds());
}

void TileRendererBuilder::setTexturePriority(long long texturePriority) {
  _texturePriority = texturePriority;
}

void TileRendererBuilder::setElevationDataProvider(ElevationDataProvider* elevationDataProvider) {
  if (_elevationDataProvider != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: _elevationDataProvider already initialized");
    return;
  }
  _elevationDataProvider = elevationDataProvider;
}

void TileRendererBuilder::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration > 0.0f) {
    ILogger::instance()->logError("LOGIC ERROR: _verticalExaggeration already initialized");
    return;
  }
  _verticalExaggeration = verticalExaggeration;
}

ElevationDataProvider* TileRendererBuilder::getElevationDataProvider() {
  return _elevationDataProvider;
}

float TileRendererBuilder::getVerticalExaggeration() {
  if (_verticalExaggeration <= 0.0f) {
    _verticalExaggeration = 1.0f;
  }
  return _verticalExaggeration;
}


TileRenderer* TileRendererBuilder::create() {
  TileRenderer* tileRenderer = new TileRenderer(getTileTessellator(),
                                                getElevationDataProvider(),
                                                getVerticalExaggeration(),
                                                getTexturizer(),
                                                getTileRasterizer(),
                                                getLayerSet(),
                                                getParameters(),
                                                getShowStatistics(),
                                                getTexturePriority());
  
  for (int i = 0; i < getVisibleSectorListeners()->size(); i++) {
    tileRenderer->addVisibleSectorListener(getVisibleSectorListeners()->at(i),
                                           TimeInterval::fromMilliseconds(getStabilizationMilliSeconds()->at(i)));
  }
  
  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileRasterizer = NULL;
  _tileTessellator = NULL;
  delete _visibleSectorListeners;
  _visibleSectorListeners = NULL;
  delete _stabilizationMilliSeconds;
  _stabilizationMilliSeconds = NULL;

  _elevationDataProvider = NULL;
  
  return tileRenderer;
}

TilesRenderParameters* TileRendererBuilder::createTileRendererParameters() {
  int __TODO_MakeConfigurable_renderIncompletePlanet;
  const bool renderIncompletePlanet = false;
  const URL incompletePlanetTexureURL("", false);
  return new TilesRenderParameters(getRenderDebug(),
                                   getUseTilesSplitBudget(),
                                   getForceFirstLevelTilesRenderOnStart(),
                                   getIncrementalTileQuality(),
                                   renderIncompletePlanet,
                                   incompletePlanetTexureURL);
}

TileTessellator* TileRendererBuilder::createTileTessellator() {
  return new EllipsoidalTileTessellator(true);
}

LayerSet* TileRendererBuilder::createLayerSet() {
  return LayerBuilder::createDefaultSatelliteImagery();
}