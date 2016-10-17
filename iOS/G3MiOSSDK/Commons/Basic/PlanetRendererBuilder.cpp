//
//  PlanetRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

#include "PlanetRendererBuilder.hpp"

#include "LayerSet.hpp"
#include "DownloadPriority.hpp"
#include "DefaultTileTexturizer.hpp"
#include "GEOVectorLayer.hpp"
#include "TileTessellator.hpp"
#include "LayerSet.hpp"
#include "DefaultChessCanvasImageBuilder.hpp"
#include "PlanetRenderer.hpp"
#include "ProjectedCornersDistanceTileLODTester.hpp"
#include "MaxLevelTileLODTester.hpp"
#include "MaxFrameTimeTileLODTester.hpp"
#include "TimedCacheTileLODTester.hpp"
#include "PlanetTileTessellator.hpp"
#include "LayerBuilder.hpp"
#include "MeshBoundingVolumeTileVisibilityTester.hpp"
#include "TimedCacheTileVisibilityTester.hpp"
#include "OrTileLODTester.hpp"
#include "GradualSplitsTileLODTester.hpp"

#include "ElevationDataProvider.hpp"
#include "TerrainElevationProvider.hpp"


PlanetRendererBuilder::PlanetRendererBuilder() :
_showStatistics(false),
_renderDebug(false),
_incrementalTileQuality(false),
_quality(QUALITY_LOW),
_parameters(NULL),
_layerSet(NULL),
_texturizer(NULL),
_tileTessellator(NULL),
_visibleSectorListeners(NULL),
_stabilizationMilliSeconds(NULL),
_tileTextureDownloadPriority(DownloadPriority::HIGHER),
_elevationDataProvider(NULL),
_terrainElevationProvider(NULL),
_verticalExaggeration(0),
_renderedSector(NULL),
_renderTileMeshes(true),
_logTilesPetitions(false),
_changedInfoListener(NULL),
_touchEventTypeOfTerrainTouchListener(LongPress),
_tileLODTester(NULL),
_tileVisibilityTester(NULL)
{
}

PlanetRendererBuilder::~PlanetRendererBuilder() {
  delete _parameters;
  delete _layerSet;
  delete _texturizer;

  const size_t geoVectorLayersSize = _geoVectorLayers.size();
  for (size_t i = 0; i < geoVectorLayersSize; i++) {
    GEOVectorLayer* geoVectorLayer = _geoVectorLayers[i];
    delete geoVectorLayer;
  }

  delete _tileTessellator;
  delete _elevationDataProvider;
  if (_terrainElevationProvider != NULL) {
    _terrainElevationProvider->_release();
  }

  delete _renderedSector;
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

/**
 * Returns the _texturizer.
 *
 * @return _texturizer: TileTexturizer*
 */
TileTexturizer* PlanetRendererBuilder::getTexturizer() {
  if (!_texturizer) {
    _texturizer = new DefaultTileTexturizer(PlanetRendererBuilder::getDefaultTileBackgroundImageBuilder());
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
 * Returns the _tileTextureDownloadPriority.
 *
 * @return _tileTextureDownloadPriority: long long
 */
long long PlanetRendererBuilder::getTileTextureDownloadPriority() {
  return _tileTextureDownloadPriority;
}

void PlanetRendererBuilder::setTileTessellator(TileTessellator *tileTessellator) {
  if (_tileTessellator) {
    THROW_EXCEPTION("LOGIC ERROR: _tileTessellator already initialized");
  }
  _tileTessellator = tileTessellator;
}

void PlanetRendererBuilder::setTileTexturizer(TileTexturizer *tileTexturizer) {
  if (_texturizer) {
    THROW_EXCEPTION("LOGIC ERROR: _texturizer already initialized");
  }
  _texturizer = tileTexturizer;
}

void PlanetRendererBuilder::setLayerSet(LayerSet *layerSet) {
  if (_layerSet) {
    THROW_EXCEPTION("LOGIC ERROR: _layerSet already initialized");
  }
  _layerSet = layerSet;
}

void PlanetRendererBuilder::setPlanetRendererParameters(TilesRenderParameters *parameters) {
  if (_parameters) {
    THROW_EXCEPTION("LOGIC ERROR: _parameters already initialized");
  }
  _parameters = parameters;
}

void PlanetRendererBuilder::setShowStatistics(const bool showStatistics) {
  _showStatistics = showStatistics;
}

void PlanetRendererBuilder::setRenderDebug(const bool renderDebug) {
  _renderDebug = renderDebug;
}

void PlanetRendererBuilder::setIncrementalTileQuality(const bool incrementalTileQuality) {
  _incrementalTileQuality = incrementalTileQuality;
}

void PlanetRendererBuilder::addVisibleSectorListener(VisibleSectorListener* listener,
                                                     const TimeInterval& stabilizationInterval) {
  getVisibleSectorListeners()->push_back(listener);
  getStabilizationMilliSeconds()->push_back(stabilizationInterval._milliseconds);
}

void PlanetRendererBuilder::setTileTextureDownloadPriority(long long tileTextureDownloadPriority) {
  _tileTextureDownloadPriority = tileTextureDownloadPriority;
}

void PlanetRendererBuilder::setElevationDataProvider(ElevationDataProvider* elevationDataProvider) {
  if (_elevationDataProvider != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: _elevationDataProvider already initialized");
  }
  _elevationDataProvider = elevationDataProvider;
}

void PlanetRendererBuilder::setTerrainElevationProvider(TerrainElevationProvider* terrainElevationProvider) {
  if (_terrainElevationProvider != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: _terrainElevationProvider already initialized");
  }
  _terrainElevationProvider = terrainElevationProvider;
}

void PlanetRendererBuilder::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration > 0.0f) {
    THROW_EXCEPTION("LOGIC ERROR: _verticalExaggeration already initialized");
  }
  _verticalExaggeration = verticalExaggeration;
}

ElevationDataProvider* PlanetRendererBuilder::getElevationDataProvider() {
  return _elevationDataProvider;
}

TerrainElevationProvider* PlanetRendererBuilder::getTerrainElevationProvider() {
  return _terrainElevationProvider;
}

float PlanetRendererBuilder::getVerticalExaggeration() {
  if (_verticalExaggeration <= 0.0f) {
    _verticalExaggeration = 1.0f;
  }
  return _verticalExaggeration;
}

ChangedRendererInfoListener* PlanetRendererBuilder::getChangedRendererInfoListener() {
  return _changedInfoListener;
}

void PlanetRendererBuilder::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener) {
  if (_changedInfoListener != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: ChangedInfoListener in Planet Render Builder already set");
  }
  _changedInfoListener = changedInfoListener;
}

void PlanetRendererBuilder::setTouchEventTypeOfTerrainTouchListener(TouchEventType touchEventTypeOfTerrainTouchListener) {
  _touchEventTypeOfTerrainTouchListener = touchEventTypeOfTerrainTouchListener;
}

TouchEventType PlanetRendererBuilder::getTouchEventTypeOfTerrainTouchListener() {
  return _touchEventTypeOfTerrainTouchListener;
}

void PlanetRendererBuilder::setDefaultTileBackgroundImage(IImageBuilder* defaultTileBackgroundImage) {
  _defaultTileBackgroundImage = defaultTileBackgroundImage;
}

IImageBuilder* PlanetRendererBuilder::getDefaultTileBackgroundImageBuilder() const {
  if (_defaultTileBackgroundImage == NULL) {
    return new DefaultChessCanvasImageBuilder(256, 256, Color::black(), Color::white(), 4);
  }
  return _defaultTileBackgroundImage;
}

PlanetRenderer* PlanetRendererBuilder::create() {

  LayerSet* layerSet = getLayerSet();
  const size_t geoVectorLayersSize = _geoVectorLayers.size();
  for (size_t i = 0; i < geoVectorLayersSize; i++) {
    GEOVectorLayer* geoVectorLayer = _geoVectorLayers[i];
    layerSet->addLayer(geoVectorLayer);
  }

  PlanetRenderer* planetRenderer = new PlanetRenderer(getTileTessellator(),
                                                      getElevationDataProvider(),
                                                      true,
                                                      getTerrainElevationProvider(),
                                                      getVerticalExaggeration(),
                                                      getTexturizer(),
                                                      layerSet,
                                                      getParameters(),
                                                      getShowStatistics(),
                                                      getTileTextureDownloadPriority(),
                                                      getRenderedSector(),
                                                      getRenderTileMeshes(),
                                                      getLogTilesPetitions(),
                                                      getChangedRendererInfoListener(),
                                                      getTouchEventTypeOfTerrainTouchListener(),
                                                      getTileLODTester(),
                                                      getTileVisibilityTester());

  for (int i = 0; i < getVisibleSectorListeners()->size(); i++) {
    planetRenderer->addVisibleSectorListener(getVisibleSectorListeners()->at(i),
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

  _elevationDataProvider = NULL;
  _terrainElevationProvider = NULL;

  delete _renderedSector;
  _renderedSector = NULL;

  _geoVectorLayers.clear();

  return planetRenderer;
}

TilesRenderParameters* PlanetRendererBuilder::createPlanetRendererParameters() {
  return new TilesRenderParameters(getRenderDebug(),
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
  return LayerBuilder::createDefault();
}

void PlanetRendererBuilder::setRenderedSector(const Sector& sector) {
  if (_renderedSector != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: _renderedSector already initialized");
  }
  _renderedSector = new Sector(sector);
}

Sector PlanetRendererBuilder::getRenderedSector() {
  if (_renderedSector == NULL) {
    return Sector::FULL_SPHERE;
  }
  return *_renderedSector;
}


GEOVectorLayer* PlanetRendererBuilder::createGEOVectorLayer() {
  GEOVectorLayer* geoVectorLayer = new GEOVectorLayer();
  _geoVectorLayers.push_back(geoVectorLayer);
  return geoVectorLayer;
}

void PlanetRendererBuilder::setTileLODTester(TileLODTester* tlt) {
  _tileLODTester = tlt;
}

TileLODTester* PlanetRendererBuilder::createDefaultTileLODTester() const {
  TileLODTester* proj = new ProjectedCornersDistanceTileLODTester();

  TileLODTester* timed = new TimedCacheTileLODTester(TimeInterval::fromMilliseconds(500),
                                                     proj);

  TileLODTester* maxLevel = new MaxLevelTileLODTester();

  TileLODTester* gradual = new GradualSplitsTileLODTester(TimeInterval::fromMilliseconds(10),
                                                          timed);

  TileLODTester* composite = new OrTileLODTester(maxLevel, gradual);

  return new MaxFrameTimeTileLODTester(TimeInterval::fromMilliseconds(25),
                                       composite);
}

TileLODTester* PlanetRendererBuilder::getTileLODTester() {
  if (_tileLODTester == NULL) {
    _tileLODTester = createDefaultTileLODTester();
  }
  return _tileLODTester;
}

TileVisibilityTester* PlanetRendererBuilder::createDefaultTileVisibilityTester() const {
  return new TimedCacheTileVisibilityTester(TimeInterval::fromMilliseconds(1000),
                                            new MeshBoundingVolumeTileVisibilityTester());
}

TileVisibilityTester* PlanetRendererBuilder::getTileVisibilityTester() {
  if (_tileVisibilityTester == NULL) {
    _tileVisibilityTester = createDefaultTileVisibilityTester();
  }
  return _tileVisibilityTester;
}
