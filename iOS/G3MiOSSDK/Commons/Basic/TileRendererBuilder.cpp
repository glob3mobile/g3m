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
//#include "WMSBillElevationDataProvider.hpp"
#include "SingleBillElevationDataProvider.hpp"

TileRendererBuilder::TileRendererBuilder() {  
  _showStatistics = false;
  _renderDebug = false;
  _useTilesSplitBudget = true;
  _forceFirstLevelTilesRenderOnStart = true;
  _incrementalTileQuality = false;

  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileTessellator = NULL;
  _visibleSectorListeners = NULL;
  _stabilizationMilliSeconds = NULL;
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

TileRenderer* TileRendererBuilder::create() {
  int _TODO_make_configurable_1;
  
  ElevationDataProvider* elevationDataProvider = NULL;

//  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
  
//  ElevationDataProvider* elevationDataProvider;
//  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
//                                                              Sector::fullSphere(),
//                                                              Vector2I(2048, 1024),
//                                                              0);

//  ElevationDataProvider* elevationDataProvider;
//  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
//                                                              Sector::fromDegrees(35, -6, 38, -2),
//                                                              Vector2I(4096, 2048),
//                                                              0);

//  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-4096x2048.bil", false),
//                                                              Sector::fullSphere(),
//                                                              Vector2I(4096, 2048),
//                                                              0);

//  ElevationDataProvider* elevationDataProvider;
//  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///caceres-2008x2032.bil", false),
//                                                              Sector::fromDegrees(
//                                                                                  39.4642996294239623,
//                                                                                  -6.3829977122432933,
//                                                                                  39.4829891936013553,
//                                                                                  -6.3645288909498845
//                                                                                  ),
//                                                              Vector2I(2008, 2032),
//                                                              0);

  int _TODO_make_configurable_2;
  float verticalExaggeration = 5;
  
  TileRenderer* tileRenderer = new TileRenderer(getTileTessellator(),
                                                elevationDataProvider,
                                                verticalExaggeration,
                                                getTexturizer(),
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
  _tileTessellator = NULL;
  delete _visibleSectorListeners;
  _visibleSectorListeners = NULL;
  delete _stabilizationMilliSeconds;
  _stabilizationMilliSeconds = NULL;
  
  return tileRenderer;
}

TilesRenderParameters* TileRendererBuilder::createTileRendererParameters() {
  return new TilesRenderParameters(getRenderDebug(),
                                   getUseTilesSplitBudget(),
                                   getForceFirstLevelTilesRenderOnStart(),
                                   getIncrementalTileQuality());
}

TileTessellator* TileRendererBuilder::createTileTessellator() {
  //return new EllipsoidalTileTessellator(getParameters()->_tileMeshResolution, true);
  return new EllipsoidalTileTessellator(true);
}

LayerSet* TileRendererBuilder::createLayerSet() {
  return LayerBuilder::createDefaultSatelliteImagery();
}