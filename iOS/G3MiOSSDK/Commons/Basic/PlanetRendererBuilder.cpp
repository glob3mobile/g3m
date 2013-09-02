//
//  PlanetRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#include "PlanetRendererBuilder.hpp"
#include "WMSLayer.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "PlanetTileTessellator.hpp"
#include "LayerBuilder.hpp"
#include "DownloadPriority.hpp"
#include "ElevationDataProvider.hpp"
#include "TileRasterizer.hpp"


PlanetRendererBuilder::PlanetRendererBuilder() {
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

PlanetRendererBuilder::~PlanetRendererBuilder() {
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
TileTessellator* PlanetRendererBuilder::getTileTessellator() {
  if (!_tileTessellator) {
    _tileTessellator = createTileTessellator();
  }

  return _tileTessellator;
}

TileRasterizer* PlanetRendererBuilder::getTileRasterizer() {
  return _tileRasterizer;
}

/**
 * Returns the _texturizer.
 *
 * @return _texturizer: TileTexturizer*
 */
TileTexturizer* PlanetRendererBuilder::getTexturizer() {
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
LayerSet* PlanetRendererBuilder::getLayerSet() {
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
TilesRenderParameters* PlanetRendererBuilder::getParameters() {
  if (!_parameters) {
    _parameters = createPlanetRendererParameters();
  }

  return _parameters;
}

/**
 * Returns the showStatistics flag.
 *
 * @return _showStatistics: bool
 */
bool PlanetRendererBuilder::getShowStatistics() {
  return _showStatistics;
}

/**
 * Returns the renderDebug flag.
 *
 * @return _renderDebug: bool
 */
bool PlanetRendererBuilder::getRenderDebug() {
  return _renderDebug;
}

/**
 * Returns the useTilesSplitBudget flag.
 *
 * @return _useTilesSplitBudget: bool
 */
bool PlanetRendererBuilder::getUseTilesSplitBudget() {
  return _useTilesSplitBudget;
}

/**
 * Returns the forceFirstLevelTilesRenderOnStart flag.
 *
 * @return _forceFirstLevelTilesRenderOnStart: bool
 */
bool PlanetRendererBuilder::getForceFirstLevelTilesRenderOnStart() {
  return _forceFirstLevelTilesRenderOnStart;
}

/**
 * Returns the incrementalTileQuality flag.
 *
 * @return _incrementalTileQuality: bool
 */
bool PlanetRendererBuilder::getIncrementalTileQuality() {
  return _incrementalTileQuality;
}

/**
 * Returns the array of visibleSectorListeners.
 */
std::vector<VisibleSectorListener*>* PlanetRendererBuilder::getVisibleSectorListeners() {
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
std::vector<long long>* PlanetRendererBuilder::getStabilizationMilliSeconds() {
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
long long PlanetRendererBuilder::getTexturePriority() {
  return _texturePriority;
}

void PlanetRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator) {
    ILogger::instance()->logError("LOGIC ERROR: _tileTessellator already initialized");
    return;
  }
  _tileTessellator = tileTessellator;
}

void PlanetRendererBuilder::setTileRasterizer(TileRasterizer* tileRasterizer) {
  if (_tileRasterizer) {
    ILogger::instance()->logError("LOGIC ERROR: _tileRasterizer already initialized");
    return;
  }
  _tileRasterizer = tileRasterizer;
}

void PlanetRendererBuilder::setTileTexturizer(TileTexturizer *tileTexturizer) {
  if (_texturizer) {
    ILogger::instance()->logError("LOGIC ERROR: _texturizer already initialized");
    return;
  }
  _texturizer = tileTexturizer;
}

void PlanetRendererBuilder::setLayerSet(LayerSet *layerSet) {
  if (_layerSet) {
    ILogger::instance()->logError("LOGIC ERROR: _layerSet already initialized");
    return;
  }
  _layerSet = layerSet;
}

void PlanetRendererBuilder::setPlanetRendererParameters(TilesRenderParameters *parameters) {
  if (_parameters) {
    ILogger::instance()->logError("LOGIC ERROR: _parameters already initialized");
    return;
  }
  _parameters = parameters;
}

void PlanetRendererBuilder::setShowStatistics(const bool showStatistics) {
  _showStatistics = showStatistics;
}

void PlanetRendererBuilder::setRenderDebug(const bool renderDebug) {
  _renderDebug = renderDebug;
}

void PlanetRendererBuilder::setUseTilesSplitBuget(const bool useTilesSplitBudget) {
  _useTilesSplitBudget = useTilesSplitBudget;
}

void PlanetRendererBuilder::setForceFirstLevelTilesRenderOnStart(const bool forceFirstLevelTilesRenderOnStart) {
  _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
}

void PlanetRendererBuilder::setIncrementalTileQuality(const bool incrementalTileQuality) {
  _incrementalTileQuality = incrementalTileQuality;
}

void PlanetRendererBuilder::addVisibleSectorListener(VisibleSectorListener* listener,
                                                     const TimeInterval& stabilizationInterval) {
  getVisibleSectorListeners()->push_back(listener);
  getStabilizationMilliSeconds()->push_back(stabilizationInterval.milliseconds());
}

void PlanetRendererBuilder::setTexturePriority(long long texturePriority) {
  _texturePriority = texturePriority;
}

void PlanetRendererBuilder::setElevationDataProvider(ElevationDataProvider* elevationDataProvider) {
  if (_elevationDataProvider != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: _elevationDataProvider already initialized");
    return;
  }
  _elevationDataProvider = elevationDataProvider;
}

void PlanetRendererBuilder::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration > 0.0f) {
    ILogger::instance()->logError("LOGIC ERROR: _verticalExaggeration already initialized");
    return;
  }
  _verticalExaggeration = verticalExaggeration;
}

ElevationDataProvider* PlanetRendererBuilder::getElevationDataProvider() {
  return _elevationDataProvider;
}

float PlanetRendererBuilder::getVerticalExaggeration() {
  if (_verticalExaggeration <= 0.0f) {
    _verticalExaggeration = 1.0f;
  }
  return _verticalExaggeration;
}


PlanetRenderer* PlanetRendererBuilder::create() {
  PlanetRenderer* planetRenderer = new PlanetRenderer(getTileTessellator(),
                                                      getElevationDataProvider(),
                                                      getVerticalExaggeration(),
                                                      getTexturizer(),
                                                      getTileRasterizer(),
                                                      getLayerSet(),
                                                      getParameters(),
                                                      getShowStatistics(),
                                                      getTexturePriority());

  for (int i = 0; i < getVisibleSectorListeners()->size(); i++) {
    planetRenderer->addVisibleSectorListener(getVisibleSectorListeners()->at(i),
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

  return planetRenderer;
}

TilesRenderParameters* PlanetRendererBuilder::createPlanetRendererParameters() {
  return new TilesRenderParameters(getRenderDebug(),
                                   getUseTilesSplitBudget(),
                                   getForceFirstLevelTilesRenderOnStart(),
                                   getIncrementalTileQuality());
}

TileTessellator* PlanetRendererBuilder::createTileTessellator() {
  return new PlanetTileTessellator(true);
}

LayerSet* PlanetRendererBuilder::createLayerSet() {
  return LayerBuilder::createDefaultSatelliteImagery();
}