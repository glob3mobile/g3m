//
//  PlanetRendererBuilder.cpp
//  G3M
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
#include "DEMProvider.hpp"
#include "ErrorHandling.hpp"


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
_demProvider(NULL),
_verticalExaggeration(0),
_renderedSector(NULL),
_renderTileMeshes(true),
_logTilesPetitions(false),
_changedInfoListener(NULL),
_touchEventTypeOfTerrainTouchListener(LongPress),
_tileLODTester(NULL),
_tileVisibilityTester(NULL),
_verboseTileTexturizerErrors(true),
_defaultTileBackgroundImage(NULL)
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
  if (_demProvider != NULL) {
    _demProvider->_release();
  }

  delete _renderedSector;
}

/**
 * Returns the _tileTessellator.
 *
 * @return _tileTessellator: TileTessellator*
 */
TileTessellator* PlanetRendererBuilder::getTileTessellator() const {
  if (_tileTessellator == NULL) {
    _tileTessellator = createTileTessellator();
  }
  return _tileTessellator;
}

/**
 * Returns the _texturizer.
 *
 * @return _texturizer: TileTexturizer*
 */
TileTexturizer* PlanetRendererBuilder::getTexturizer() const {
  if (_texturizer == NULL) {
    _texturizer = new DefaultTileTexturizer(getDefaultTileBackgroundImageBuilder(),
                                            getVerboseTileTexturizerErrors());
  }
  return _texturizer;
}

/**
 * Returns the _layerSet.
 *
 * @return _layerSet: LayerSet*
 */
LayerSet* PlanetRendererBuilder::getLayerSet() const {
  if (_layerSet == NULL) {
    _layerSet = createLayerSet();
  }
  return _layerSet;
}

/**
 * Returns the _parameters.
 *
 * @return _parameters: TilesRenderParameters*
 */
TilesRenderParameters* PlanetRendererBuilder::getParameters() const {
  if (_parameters == NULL) {
    _parameters = createPlanetRendererParameters();
  }
  return _parameters;
}

/**
 * Returns the showStatistics flag.
 *
 * @return _showStatistics: bool
 */
bool PlanetRendererBuilder::getShowStatistics() const {
  return _showStatistics;
}

bool PlanetRendererBuilder::getLogTilesPetitions() const {
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
bool PlanetRendererBuilder::getRenderDebug() const {
  return _renderDebug;
}

/**
 * Returns the incrementalTileQuality flag.
 *
 * @return _incrementalTileQuality: bool
 */
bool PlanetRendererBuilder::getIncrementalTileQuality() const {
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
std::vector<VisibleSectorListener*>* PlanetRendererBuilder::getVisibleSectorListeners() const {
  if (_visibleSectorListeners == NULL) {
    _visibleSectorListeners = new std::vector<VisibleSectorListener*>;
  }
  return _visibleSectorListeners;
}

/**
 * Returns the array of stabilization milliseconds related to visible-sector listeners.
 *
 * @return _stabilizationMilliSeconds: std::vector<long long>
 */
std::vector<long long>* PlanetRendererBuilder::getStabilizationMilliSeconds() const {
  if (_stabilizationMilliSeconds == NULL) {
    _stabilizationMilliSeconds = new std::vector<long long>;
  }
  return _stabilizationMilliSeconds;
}

/**
 * Returns the _tileTextureDownloadPriority.
 *
 * @return _tileTextureDownloadPriority: long long
 */
long long PlanetRendererBuilder::getTileTextureDownloadPriority() const {
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

void PlanetRendererBuilder::setVerboseTileTexturizerErrors(const bool verboseTileTexturizerErrors) {
  _verboseTileTexturizerErrors = verboseTileTexturizerErrors;
}

bool PlanetRendererBuilder::getVerboseTileTexturizerErrors() const {
  return _verboseTileTexturizerErrors;
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

void PlanetRendererBuilder::setDEMProvider(DEMProvider* demProvider) {
  if (_demProvider != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: _demProvider already initialized");
  }
  _demProvider = demProvider;
}

void PlanetRendererBuilder::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration > 0.0f) {
    THROW_EXCEPTION("LOGIC ERROR: _verticalExaggeration already initialized");
  }
  _verticalExaggeration = verticalExaggeration;
}

ElevationDataProvider* PlanetRendererBuilder::getElevationDataProvider() const {
  return _elevationDataProvider;
}

DEMProvider* PlanetRendererBuilder::getDEMProvider() const {
  return _demProvider;
}

float PlanetRendererBuilder::getVerticalExaggeration() const {
  if (_verticalExaggeration <= 0.0f) {
    _verticalExaggeration = 1.0f;
  }
  return _verticalExaggeration;
}

ChangedRendererInfoListener* PlanetRendererBuilder::getChangedRendererInfoListener() const {
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

TouchEventType PlanetRendererBuilder::getTouchEventTypeOfTerrainTouchListener() const {
  return _touchEventTypeOfTerrainTouchListener;
}

void PlanetRendererBuilder::setDefaultTileBackgroundImage(IImageBuilder* defaultTileBackgroundImage) {
  _defaultTileBackgroundImage = defaultTileBackgroundImage;
}

IImageBuilder* PlanetRendererBuilder::getDefaultTileBackgroundImageBuilder() const {
  if (_defaultTileBackgroundImage == NULL) {
    // _defaultTileBackgroundImage = new DefaultChessCanvasImageBuilder(256, 256, Color::BLACK, Color::WHITE, 4);
    _defaultTileBackgroundImage = new DefaultChessCanvasImageBuilder(256, 256, Color::WHITE, Color::TRANSPARENT, 4);
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
                                                      getDEMProvider(),
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
  _demProvider = NULL;

  delete _renderedSector;
  _renderedSector = NULL;

  _geoVectorLayers.clear();

  return planetRenderer;
}

TilesRenderParameters* PlanetRendererBuilder::createPlanetRendererParameters() const {
  return new TilesRenderParameters(getRenderDebug(),
                                   getIncrementalTileQuality(),
                                   getQuality());
}

void PlanetRendererBuilder::setRenderTileMeshes(bool renderTileMeshes) {
  _renderTileMeshes = renderTileMeshes;
}

bool PlanetRendererBuilder::getRenderTileMeshes() const {
  return _renderTileMeshes;
}

TileTessellator* PlanetRendererBuilder::createTileTessellator() const {
  //#warning Testing Terrain Normals
  const bool skirted = true;
  return new PlanetTileTessellator(skirted, getRenderedSector());
}

LayerSet* PlanetRendererBuilder::createLayerSet() const {
  return LayerBuilder::createDefault();
}

void PlanetRendererBuilder::setRenderedSector(const Sector& sector) {
  if (_renderedSector != NULL) {
    THROW_EXCEPTION("LOGIC ERROR: _renderedSector already initialized");
  }
  _renderedSector = new Sector(sector);
}

Sector PlanetRendererBuilder::getRenderedSector() const {
  if (_renderedSector == NULL) {
    return Sector::FULL_SPHERE;
  }
  return *_renderedSector;
}


GEOVectorLayer* PlanetRendererBuilder::createGEOVectorLayer() const {
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

TileLODTester* PlanetRendererBuilder::getTileLODTester() const {
  if (_tileLODTester == NULL) {
    _tileLODTester = createDefaultTileLODTester();
  }
  return _tileLODTester;
}

TileVisibilityTester* PlanetRendererBuilder::createDefaultTileVisibilityTester() const {
  return new TimedCacheTileVisibilityTester(TimeInterval::fromMilliseconds(1000),
                                            new MeshBoundingVolumeTileVisibilityTester());
}

TileVisibilityTester* PlanetRendererBuilder::getTileVisibilityTester() const {
  if (_tileVisibilityTester == NULL) {
    _tileVisibilityTester = createDefaultTileVisibilityTester();
  }
  return _tileVisibilityTester;
}
