//
//  PlanetRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#include "PlanetRendererBuilder.hpp"
#include "WMSLayer.hpp"
//#include "MultiLayerTileTexturizer.hpp"
#include "DefaultTileTexturizer.hpp"
#include "PlanetTileTessellator.hpp"
#include "LayerBuilder.hpp"
#include "DownloadPriority.hpp"
#include "ElevationDataProvider.hpp"
//#include "TileRasterizer.hpp"
#include "TileRenderingListener.hpp"
#include "GEOVectorLayer.hpp"
#include "TouchEvent.hpp"


//#include "CompositeTileRasterizer.hpp"

PlanetRendererBuilder::PlanetRendererBuilder() :
_showStatistics(false),
_renderDebug(false),
_useTilesSplitBudget(true),
_forceFirstLevelTilesRenderOnStart(true),
_incrementalTileQuality(false),
_quality(QUALITY_LOW),
_parameters(NULL),
_layerSet(NULL),
_texturizer(NULL),
_tileTessellator(NULL),
_visibleSectorListeners(NULL),
_stabilizationMilliSeconds(NULL),
_tileDownloadPriority(DownloadPriority::HIGHER),
_elevationDataProvider(NULL),
_verticalExaggeration(0),
_renderedSector(NULL),
_terrainTouchListeners(NULL),
_renderTileMeshes(true),
_logTilesPetitions(false),
_tileRenderingListener(NULL),
_changedInfoListener(NULL),
_tileCacheSize(0),
_deleteTexturesOfInvisibleTiles(true),
_touchEventTypeOfTerrainTouchListener(LongPress)
{
}

PlanetRendererBuilder::~PlanetRendererBuilder() {
  delete _parameters;
  delete _layerSet;
  delete _texturizer;

//  const int tileRasterizersSize = _tileRasterizers.size();
//  for (int i = 0 ; i < tileRasterizersSize; i++) {
//    TileRasterizer* tileRasterizer = _tileRasterizers[i];
//    delete tileRasterizer;
//  }

  const int geoVectorLayersSize = _geoVectorLayers.size();
  for (int i = 0; i < geoVectorLayersSize; i++) {
    GEOVectorLayer* geoVectorLayer = _geoVectorLayers[i];
    delete geoVectorLayer;
  }

  delete _tileTessellator;
  delete _elevationDataProvider;

  delete _renderedSector;

  delete _tileRenderingListener;
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

//TileRasterizer* PlanetRendererBuilder::getTileRasterizer() {
//  const int tileRasterizersSize = _tileRasterizers.size();
//
//  if (tileRasterizersSize == 0) {
//    return NULL;
//  }
//
//  if (tileRasterizersSize == 1) {
//    return _tileRasterizers[0];
//  }
//
//  CompositeTileRasterizer* result = new CompositeTileRasterizer();
//  for (int i = 0; i < tileRasterizersSize; i++) {
//    result->addTileRasterizer(_tileRasterizers[i]);
//  }
//  return result;
//}

/**
 * Returns the _texturizer.
 *
 * @return _texturizer: TileTexturizer*
 */
TileTexturizer* PlanetRendererBuilder::getTexturizer() {
  if (!_texturizer) {
//    _texturizer = new MultiLayerTileTexturizer();
#warning Diego at work!
    _texturizer = new DefaultTileTexturizer();
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

bool PlanetRendererBuilder::getLogTilesPetitions() {
  return _logTilesPetitions;
}


void PlanetRendererBuilder::setLogTilesPetitions(bool logTilesPetitions) {
  _logTilesPetitions = logTilesPetitions;
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

Quality PlanetRendererBuilder::getQuality() const {
  return _quality;
}

void PlanetRendererBuilder::setQuality(Quality quality) {
  _quality = quality;
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
 * Returns the array of TerrainTouchListeners.
 */
std::vector<TerrainTouchListener*>* PlanetRendererBuilder::getTerrainTouchListeners() {
  if (!_terrainTouchListeners) {
    _terrainTouchListeners = new std::vector<TerrainTouchListener*>;
  }
  return _terrainTouchListeners;
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
 * Returns the _tileDownloadPriority.
 *
 * @return _tileDownloadPriority: long long
 */
long long PlanetRendererBuilder::getTileDownloadPriority() {
  return _tileDownloadPriority;
}

void PlanetRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator) {
    ILogger::instance()->logError("LOGIC ERROR: _tileTessellator already initialized");
    return;
  }
  _tileTessellator = tileTessellator;
}

//void PlanetRendererBuilder::addTileRasterizer(TileRasterizer* tileRasterizer) {
//  _tileRasterizers.push_back(tileRasterizer);
//}

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

void PlanetRendererBuilder::setUseTilesSplitBudget(const bool useTilesSplitBudget) {
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
  getStabilizationMilliSeconds()->push_back(stabilizationInterval._milliseconds);
}

void PlanetRendererBuilder::addTerrainTouchListener(TerrainTouchListener* listener) {
  getTerrainTouchListeners()->push_back(listener);
}

/*void PlanetRendererBuilder::setTexturePriority(long long texturePriority) {
  _tile = texturePriority;
}*/

void PlanetRendererBuilder::setTileDownloadPriority(long long tileDownloadPriority) {
  _tileDownloadPriority = tileDownloadPriority;
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

void PlanetRendererBuilder::setTileRenderingListener(TileRenderingListener* tileRenderingListener) {
  if (_tileRenderingListener != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: TileRenderingListener already set");
    return;
  }

  _tileRenderingListener = tileRenderingListener;
}

ChangedRendererInfoListener* PlanetRendererBuilder::getChangedRendererInfoListener() {
  return _changedInfoListener;
}

void PlanetRendererBuilder::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener) {
  if (_changedInfoListener != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: ChangedInfoListener in Planet Render Builder already set");
  }
  else {
    _changedInfoListener = changedInfoListener;
    ILogger::instance()->logError("LOGIC INFO: ChangedInfoListener in Planet Render Builder set OK");
  }
}

void PlanetRendererBuilder::setTouchEventTypeOfTerrainTouchListener(TouchEventType touchEventTypeOfTerrainTouchListener) {
  _touchEventTypeOfTerrainTouchListener = touchEventTypeOfTerrainTouchListener;
}

TouchEventType PlanetRendererBuilder::getTouchEventTypeOfTerrainTouchListener() {
  return _touchEventTypeOfTerrainTouchListener;
}




TileRenderingListener* PlanetRendererBuilder::getTileRenderingListener() {
  return _tileRenderingListener;
}

PlanetRenderer* PlanetRendererBuilder::create() {

  LayerSet* layerSet = getLayerSet();
  const int geoVectorLayersSize = _geoVectorLayers.size();
  for (int i = 0; i < geoVectorLayersSize; i++) {
    GEOVectorLayer* geoVectorLayer = _geoVectorLayers[i];
    layerSet->addLayer(geoVectorLayer);
  }

  PlanetRenderer* planetRenderer = new PlanetRenderer(getTileTessellator(),
                                                      getElevationDataProvider(),
                                                      true,
                                                      getVerticalExaggeration(),
                                                      getTexturizer(),
//                                                      getTileRasterizer(),
                                                      layerSet,
                                                      getParameters(),
                                                      getShowStatistics(),
                                                      getTileDownloadPriority(),
                                                      getRenderedSector(),
                                                      getRenderTileMeshes(),
                                                      getLogTilesPetitions(),
                                                      getTileRenderingListener(),
                                                      getChangedRendererInfoListener(),
                                                      _tileCacheSize,
                                                      _deleteTexturesOfInvisibleTiles,
                                                      getTouchEventTypeOfTerrainTouchListener());

  for (int i = 0; i < getVisibleSectorListeners()->size(); i++) {
    planetRenderer->addVisibleSectorListener(getVisibleSectorListeners()->at(i),
                                             TimeInterval::fromMilliseconds(getStabilizationMilliSeconds()->at(i)));
  }

  for (int i = 0; i < getTerrainTouchListeners()->size(); i++) {
    planetRenderer->addTerrainTouchListener(getTerrainTouchListeners()->at(i));
  }

  _parameters = NULL;
  _layerSet = NULL;
  _texturizer = NULL;
  _tileTessellator = NULL;
  delete _visibleSectorListeners;
  _visibleSectorListeners = NULL;

  delete _terrainTouchListeners;
  _terrainTouchListeners= NULL;
  delete _stabilizationMilliSeconds;
  _stabilizationMilliSeconds = NULL;

  _elevationDataProvider = NULL;

  delete _renderedSector;
  _renderedSector = NULL;

  _tileRenderingListener = NULL;

//  _tileRasterizers.clear();
  _geoVectorLayers.clear();

  return planetRenderer;
}

TilesRenderParameters* PlanetRendererBuilder::createPlanetRendererParameters() {
  return new TilesRenderParameters(getRenderDebug(),
                                   getUseTilesSplitBudget(),
                                   getForceFirstLevelTilesRenderOnStart(),
                                   getIncrementalTileQuality(),
                                   getQuality());
}

void PlanetRendererBuilder::setRenderTileMeshes(bool renderTileMeshes) {
  _renderTileMeshes = renderTileMeshes;
}

bool PlanetRendererBuilder::getRenderTileMeshes() {
  return _renderTileMeshes;
}

TileTessellator* PlanetRendererBuilder::createTileTessellator() {
//#warning Testing Terrain Normals
  const bool skirted = true;
  return new PlanetTileTessellator(skirted, getRenderedSector());
}

LayerSet* PlanetRendererBuilder::createLayerSet() {
  return LayerBuilder::createDefaultSatelliteImagery();
}

void PlanetRendererBuilder::setRenderedSector(const Sector& sector) {
  if (_renderedSector != NULL) {
    ILogger::instance()->logError("LOGIC ERROR: _renderedSector already initialized");
    return;
  }
  _renderedSector = new Sector(sector);
}

Sector PlanetRendererBuilder::getRenderedSector() {
  if (_renderedSector == NULL) {
    return Sector::fullSphere();
  }
  return *_renderedSector;
}

//GEOTileRasterizer* PlanetRendererBuilder::createGEOTileRasterizer() {
//  GEOTileRasterizer* geoTileRasterizer = new GEOTileRasterizer();
//  addTileRasterizer(geoTileRasterizer);
//  return geoTileRasterizer;
//}


GEOVectorLayer* PlanetRendererBuilder::createGEOVectorLayer() {
  GEOVectorLayer* geoVectorLayer = new GEOVectorLayer();
  _geoVectorLayers.push_back(geoVectorLayer);
  return geoVectorLayer;
}

void PlanetRendererBuilder::setTileCacheSize(int x){
  _tileCacheSize = x;
}

void PlanetRendererBuilder::setDeleteTexturesOfInvisibleTiles(bool x){
  _deleteTexturesOfInvisibleTiles = x;
}

