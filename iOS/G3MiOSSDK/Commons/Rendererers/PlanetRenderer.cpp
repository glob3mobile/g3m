//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//

#include "PlanetRenderer.hpp"
#include "Tile.hpp"
#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "Camera.hpp"
#include "ITimer.hpp"
#include "TilesRenderParameters.hpp"
#include "TouchEvent.hpp"
#include "LayerSet.hpp"
#include "VisibleSectorListener.hpp"
#include "IThreadUtils.hpp"
#include "DownloadPriority.hpp"
#include "ElevationDataProvider.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "MercatorUtils.hpp"
#include "EllipsoidShape.hpp"
#include "Color.hpp"
#include "ElevationData.hpp"
#include "TerrainTouchListener.hpp"
#include "IDeviceInfo.hpp"
#include "Sector.hpp"
#include "IFactory.hpp"
#include "Layer.hpp"
#include <algorithm>
#include "TileLODTester.hpp"
#include "TileVisibilityTester.hpp"
#include "G3MEventContext.hpp"
#include "PlanetRenderContext.hpp"
#include "TerrainElevationProvider.hpp"


class VisibleSectorListenerEntry {
private:
  VisibleSectorListener* _listener;
  const long long        _stabilizationIntervalInMS;

  Sector* _lastSector;
  long long _whenNotifyInMS;

  ITimer*  _timer;

  ITimer* getTimer() {
    if (_timer == NULL) {
      _timer = IFactory::instance()->createTimer();
    }
    return _timer;
  }


public:
  VisibleSectorListenerEntry(VisibleSectorListener* listener,
                             const TimeInterval& stabilizationInterval) :
  _listener(listener),
  _stabilizationIntervalInMS(stabilizationInterval._milliseconds),
  _lastSector(NULL),
  _timer(NULL),
  _whenNotifyInMS(0)
  {

  }

  void notifyListener(const Sector* visibleSector,
                      const G3MRenderContext* rc) const {
    _listener->onVisibleSectorChange(*_lastSector,
                                     rc->getCurrentCamera()->getGeodeticPosition());
  }

  void tryToNotifyListener(const Sector* visibleSector,
                           const G3MRenderContext* rc) {
    if ( _stabilizationIntervalInMS == 0 ) {
      if ( (_lastSector == NULL) || (!_lastSector->isEquals(*visibleSector)) ) {
        delete _lastSector;
        _lastSector = new Sector(*visibleSector);

        notifyListener(visibleSector, rc);
      }
    }
    else {
      const long long now = getTimer()->nowInMilliseconds();

      if ( (_lastSector == NULL) || (!_lastSector->isEquals(*visibleSector)) ) {
        delete _lastSector;
        _lastSector = new Sector(*visibleSector);
        _whenNotifyInMS = now + _stabilizationIntervalInMS;
      }

      if (_whenNotifyInMS != 0) {
        if (now >= _whenNotifyInMS) {
          notifyListener(visibleSector, rc);

          _whenNotifyInMS = 0;
        }
      }
    }

  }

  ~VisibleSectorListenerEntry() {
    delete _listener;
    delete _timer;
    delete _lastSector;
  }
};


PlanetRenderer::PlanetRenderer(TileTessellator*             tessellator,
                               ElevationDataProvider*       elevationDataProvider,
                               bool                         ownsElevationDataProvider,
                               TerrainElevationProvider*    terrainElevationProvider,
                               float                        verticalExaggeration,
                               TileTexturizer*              texturizer,
                               LayerSet*                    layerSet,
                               TilesRenderParameters*       tilesRenderParameters,
                               bool                         showStatistics,
                               long long                    tileTextureDownloadPriority,
                               const Sector&                renderedSector,
                               const bool                   renderTileMeshes,
                               const bool                   logTilesPetitions,
                               ChangedRendererInfoListener* changedInfoListener,
                               TouchEventType               touchEventTypeOfTerrainTouchListener,
                               TileLODTester*               tileLODTester,
                               TileVisibilityTester*        tileVisibilityTester) :
_tessellator(tessellator),
_elevationDataProvider(elevationDataProvider),
_ownsElevationDataProvider(ownsElevationDataProvider),
_terrainElevationProvider(terrainElevationProvider),
_verticalExaggeration(verticalExaggeration),
_texturizer(texturizer),
_layerSet(layerSet),
_tilesRenderParameters(tilesRenderParameters),
_showStatistics(showStatistics),
_firstLevelTilesJustCreated(false),
_lastSplitTimer(NULL),
_lastCamera(NULL),
_firstRender(false),
_lastVisibleSector(NULL),
_tileTextureDownloadPriority(tileTextureDownloadPriority),
_allFirstLevelTilesAreTextureSolved(false),
_recreateTilesPending(false),
_glState(new GLState()),
_renderedSector(renderedSector.isEquals(Sector::fullSphere())? NULL : new Sector(renderedSector)),
_layerTilesRenderParameters(NULL),
_layerTilesRenderParametersDirty(true),
_renderTileMeshes(renderTileMeshes),
_logTilesPetitions(logTilesPetitions),
_touchEventTypeOfTerrainTouchListener(touchEventTypeOfTerrainTouchListener),
_tileLODTester(tileLODTester),
_tileVisibilityTester(tileVisibilityTester)
{
  _context = NULL;
  _changedInfoListener = changedInfoListener;

  _layerSet->setChangeListener(this);

  _layerSet->setChangedInfoListener(this);

  _rendererIdentifier = -1;

  if (_tileLODTester == NULL) {
    THROW_EXCEPTION("TileLODTester can't be NULL");
  }
  if (_tileVisibilityTester == NULL) {
    THROW_EXCEPTION("TileVisibilityTester can't be NULL");
  }

  _prc = new PlanetRenderContext();
}

void PlanetRenderer::recreateTiles() {
  pruneFirstLevelTiles();
  clearFirstLevelTiles();

  delete _layerTilesRenderParameters;
  _layerTilesRenderParameters = NULL;
  _layerTilesRenderParametersDirty = true;

  _firstRender = true;
  _allFirstLevelTilesAreTextureSolved = false;
  createFirstLevelTiles(_context);

  _recreateTilesPending = false;
}

class RecreateTilesTask : public GTask {
private:
  PlanetRenderer* _planetRenderer;
public:
  RecreateTilesTask(PlanetRenderer* planetRenderer) :
  _planetRenderer(planetRenderer)
  {
  }

  void run(const G3MContext* context) {
    _planetRenderer->recreateTiles();
  }
};

void PlanetRenderer::changed() {
  if (!_recreateTilesPending) {
    _recreateTilesPending = true;
    // recreateTiles() delete tiles, then meshes, and delete textures from the GPU
    //   so it has to be executed in the OpenGL thread
    if (_context == NULL) {
      ILogger::instance()->logError("_context is not initialized");
    }
    else {
      _context->getThreadUtils()->invokeInRendererThread(new RecreateTilesTask(this), true);
    }
  }
}

PlanetRenderer::~PlanetRenderer() {
  pruneFirstLevelTiles();
  clearFirstLevelTiles();

  delete _layerTilesRenderParameters;

  delete _tessellator;
  if (_ownsElevationDataProvider) {
    delete _elevationDataProvider;
  }
  if (_terrainElevationProvider != NULL) {
    _terrainElevationProvider->cancel();
    _terrainElevationProvider->_release();
  }
  delete _texturizer;
  delete _tilesRenderParameters;

  delete _lastSplitTimer;

  delete _lastVisibleSector;

  const size_t visibleSectorListenersCount = _visibleSectorListeners.size();
  for (size_t i = 0; i < visibleSectorListenersCount; i++) {
    VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
    delete entry;
  }

  delete _renderedSector;

  delete _tileLODTester;
  delete _tileVisibilityTester;

  delete _prc;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void PlanetRenderer::clearFirstLevelTiles() {
  const size_t firstLevelTilesCount = _firstLevelTiles.size();
  for (size_t i = 0; i < firstLevelTilesCount; i++) {
    Tile* tile = _firstLevelTiles[i];
    tile->toBeDeleted(_texturizer, _elevationDataProvider);
    delete tile;
  }

  _firstLevelTiles.clear();
}

#ifdef C_CODE
class SortTilesClass {
public:
  bool operator() (Tile* i, Tile* j) {
    const int rowI = i->_row;
    const int rowJ = j->_row;

    if (rowI < rowJ) {
      return true;
    }
    if (rowI > rowJ) {
      return false;
    }

    return ( i->_column < j->_column );
  }
} sortTilesObject;
#endif

void PlanetRenderer::sortTiles(std::vector<Tile*>& tiles) const {
#ifdef C_CODE
  std::sort(tiles.begin(),
            tiles.end(),
            sortTilesObject);
#endif
#ifdef JAVA_CODE
  java.util.Collections.sort(tiles, //
                             new java.util.Comparator<Tile>() {
                               @Override
                               public int compare(final Tile i,
                                                  final Tile j) {
                                 final int rowI = i._row;
                                 final int rowJ = j._row;
                                 if (rowI < rowJ) {
                                   return -1;
                                 }
                                 if (rowI > rowJ) {
                                   return 1;
                                 }

                                 final int columnI = i._column;
                                 final int columnJ = j._column;
                                 if (columnI < columnJ) {
                                   return -1;
                                 }
                                 if (columnI > columnJ) {
                                   return 1;
                                 }
                                 return 0;
                               }
                             });
#endif
}


void PlanetRenderer::createFirstLevelTiles(std::vector<Tile*>& firstLevelTiles,
                                           Tile* tile,
                                           int firstLevel) const {

  if (tile->_level == firstLevel) {
    firstLevelTiles.push_back(tile);
  }
  else {
    std::vector<Tile*>* children = tile->createSubTiles(false);

    const size_t childrenSize = children->size();
    for (size_t i = 0; i < childrenSize; i++) {
      Tile* child = children->at(i);
      createFirstLevelTiles(firstLevelTiles, child, firstLevel);
    }

    delete children;
    delete tile;
  }
}


const LayerTilesRenderParameters* PlanetRenderer::getLayerTilesRenderParameters() {
  if (_layerTilesRenderParametersDirty) {
    _errors.clear();
    delete _layerTilesRenderParameters;
    _layerTilesRenderParameters = _layerSet->createLayerTilesRenderParameters(_errors);
    if (_layerTilesRenderParameters == NULL) {
      ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't render planet");
    }
    _layerTilesRenderParametersDirty = false;

    _tileLODTester->onLayerTilesRenderParametersChanged(_layerTilesRenderParameters);
    _tileVisibilityTester->onLayerTilesRenderParametersChanged(_layerTilesRenderParameters);
  }
  return _layerTilesRenderParameters;
}

void PlanetRenderer::createFirstLevelTiles(const G3MContext* context) {

  const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
  if (parameters == NULL) {
    //ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
    return;
  }

  std::vector<Tile*> topLevelTiles;

  const Angle fromLatitude  = parameters->_topSector._lower._latitude;
  const Angle fromLongitude = parameters->_topSector._lower._longitude;

  const Angle deltaLan = parameters->_topSector._deltaLatitude;
  const Angle deltaLon = parameters->_topSector._deltaLongitude;

  const int topSectorSplitsByLatitude  = parameters->_topSectorSplitsByLatitude;
  const int topSectorSplitsByLongitude = parameters->_topSectorSplitsByLongitude;

  const Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
  const Angle tileWidth  = deltaLon.div(topSectorSplitsByLongitude);

  for (int row = 0; row < topSectorSplitsByLatitude; row++) {
    const Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
    const Angle tileLatTo   = tileLatFrom.add(tileHeight);

    for (int col = 0; col < topSectorSplitsByLongitude; col++) {
      const Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
      const Angle tileLonTo   = tileLonFrom.add(tileWidth);

      const Geodetic2D tileLower(tileLatFrom, tileLonFrom);
      const Geodetic2D tileUpper(tileLatTo, tileLonTo);
      const Sector sector(tileLower, tileUpper);

      if (_renderedSector == NULL || sector.touchesWith(*_renderedSector)) { //Do not create innecesary tiles
        Tile* tile = new Tile(_texturizer, NULL, sector, parameters->_mercator, 0, row, col, this);
        if (parameters->_firstLevel == 0) {
          _firstLevelTiles.push_back(tile);
        }
        else {
          topLevelTiles.push_back(tile);
        }
      }
    }
  }

  if (parameters->_firstLevel > 0) {
    const size_t topLevelTilesSize = topLevelTiles.size();
    for (size_t i = 0; i < topLevelTilesSize; i++) {
      Tile* tile = topLevelTiles[i];
      createFirstLevelTiles(_firstLevelTiles, tile, parameters->_firstLevel);
    }
  }

  sortTiles(_firstLevelTiles);

  context->getLogger()->logInfo("Created %d first level tiles", _firstLevelTiles.size());
  if (_firstLevelTiles.size() > 64) {
    context->getLogger()->logWarning("%d tiles are many for the first level. We recommend a number of those less than 64. You can review some parameters (Render Sector and/or First Level) to reduce the number of tiles.", _firstLevelTiles.size());
  }

  _firstLevelTilesJustCreated = true;
}

void PlanetRenderer::initialize(const G3MContext* context) {
  _context = context;

  pruneFirstLevelTiles();
  clearFirstLevelTiles();
  createFirstLevelTiles(context);

  delete _lastSplitTimer;
  _lastSplitTimer = context->getFactory()->createTimer();

  _layerSet->initialize(context);
  _texturizer->initialize(context, _tilesRenderParameters);
  if (_elevationDataProvider != NULL) {
    _elevationDataProvider->initialize(context);
  }
  if (_terrainElevationProvider != NULL) {
    _terrainElevationProvider->initialize(context);
  }
}

RenderState PlanetRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_tessellator == NULL) {
    return RenderState::error("Tessellator is null");
  }

  if (_texturizer == NULL) {
    return RenderState::error("Texturizer is null");
  }

  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
  if (layerTilesRenderParameters == NULL) {
    return _errors.empty() ? RenderState::busy() : RenderState::error(_errors);
  }

  const RenderState layerSetRenderState = _layerSet->getRenderState();
  if (layerSetRenderState._type != RENDER_READY) {
    return layerSetRenderState;
  }

  if (_elevationDataProvider != NULL) {
    if (!_elevationDataProvider->isReadyToRender(rc)) {
      return RenderState::busy();
    }
  }

  if (_terrainElevationProvider != NULL) {
    const RenderState terrainElevationProviderRenderState = _terrainElevationProvider->getRenderState();
    if (terrainElevationProviderRenderState._type != RENDER_READY) {
      return terrainElevationProviderRenderState;
    }
  }

  const RenderState texturizerRenderState = _texturizer->getRenderState(_layerSet);
  if (texturizerRenderState._type != RENDER_READY) {
    return texturizerRenderState;
  }

  if (_firstLevelTilesJustCreated) {
    _firstLevelTilesJustCreated = false;

    const size_t firstLevelTilesCount = _firstLevelTiles.size();

    _statistics.clear();

    _prc->_tileLODTester               = _tileLODTester;
    _prc->_tileVisibilityTester        = _tileVisibilityTester;
    _prc->_frustumInModelCoordinates   = NULL;
    _prc->_verticalExaggeration        = _verticalExaggeration;
    _prc->_layerTilesRenderParameters  = layerTilesRenderParameters;
    _prc->_texturizer                  = _texturizer;
    _prc->_tilesRenderParameters       = _tilesRenderParameters;
    _prc->_lastSplitTimer              = _lastSplitTimer;
    _prc->_elevationDataProvider       = _elevationDataProvider;
    _prc->_tessellator                 = _tessellator;
    _prc->_layerSet                    = _layerSet;
    _prc->_tileTextureDownloadPriority = _tileTextureDownloadPriority;
    _prc->_texWidthSquared             = -1;
    _prc->_texHeightSquared            = -1;
    _prc->_nowInMS                     = -1;
    _prc->_renderTileMeshes            = _renderTileMeshes;
    _prc->_logTilesPetitions           = _logTilesPetitions;

    for (size_t i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      tile->prepareForFullRendering(rc, _prc);
    }

    for (int i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      _texturizer->justCreatedTopTile(rc, tile, _layerSet);
    }
  }

  if (!_allFirstLevelTilesAreTextureSolved) {
    const size_t firstLevelTilesCount = _firstLevelTiles.size();
    for (size_t i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      if (!tile->isTextureSolved()) {
        return RenderState::busy();
      }
    }

    _allFirstLevelTilesAreTextureSolved = true;
  }

  return RenderState::ready();
}

void PlanetRenderer::visitTilesTouchesWith(const Sector& sector,
                                           const int firstLevel,
                                           const int maxLevel) {
  if (_tileVisitor != NULL) {
    const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
    if (parameters == NULL) {
      ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
      return;
    }

    const int firstLevelToVisit = (firstLevel < parameters-> _firstLevel) ? parameters->_firstLevel : firstLevel;
    if (firstLevel < firstLevelToVisit) {
      ILogger::instance()->logError("Can only visit from level %d", firstLevelToVisit);
      return;
    }

    const int maxLevelToVisit = (maxLevel > parameters->_maxLevel) ? parameters->_maxLevel : maxLevel;
    if (maxLevel > maxLevelToVisit) {
      ILogger::instance()->logError("Can only visit to level %d", maxLevelToVisit);
      return;
    }

    if (firstLevelToVisit > maxLevelToVisit) {
      ILogger::instance()->logError("Can't visit, first level is gratter than max level");
      return;
    }

    std::vector<Layer*> layers;
    const size_t layersCount = _layerSet->size();
    for (size_t i = 0; i < layersCount; i++) {
      Layer* layer = _layerSet->getLayer(i);
      if (layer->isEnable() && layer->getRenderState()._type == RENDER_READY) {
        layers.push_back(layer);
      }
    }

    const size_t firstLevelTilesCount = _firstLevelTiles.size();
    for (size_t i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      if (tile->_sector.touchesWith(sector)) {
        _tileVisitor->visitTile(layers, tile);
        visitSubTilesTouchesWith(layers,
                                 tile,
                                 sector,
                                 firstLevelToVisit,
                                 maxLevelToVisit);
      }
    }
  }
  else {
    ILogger::instance()->logError("TileVisitor is NULL");
  }
}

void PlanetRenderer::visitSubTilesTouchesWith(std::vector<Layer*> layers,
                                              Tile* tile,
                                              const Sector& sectorToVisit,
                                              const int topLevel,
                                              const int maxLevel) {
  if (tile->_level < maxLevel) {
    std::vector<Tile*>* subTiles = tile->getSubTiles();

    const size_t subTilesCount = subTiles->size();
    for (size_t i = 0; i < subTilesCount; i++) {
      Tile* tl = subTiles->at(i);
      if (tl->_sector.touchesWith(sectorToVisit)) {
        if ((tile->_level >= topLevel)) {
          _tileVisitor->visitTile(layers, tl);
        }
        visitSubTilesTouchesWith(layers, tl, sectorToVisit, topLevel, maxLevel);
      }
    }
  }
}

void PlanetRenderer::updateGLState(const G3MRenderContext* rc) {

  const Camera* camera = rc->getCurrentCamera();
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(camera), true);
  }
  else {
    f->setMatrix(camera->getModelViewMatrix44D());
  }
}

void PlanetRenderer::render(const G3MRenderContext* rc,
                            GLState* glState) {

  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();
  if (layerTilesRenderParameters == NULL) {
    return;
  }

  updateGLState(rc);
  //#warning Testing Terrain Normals
  _glState->setParent(glState);

  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();

  _statistics.clear();

  const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
  const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
  const double correctionFactor = deviceInfo->getDPI() / factor;

  const double texWidth  = correctionFactor * layerTilesRenderParameters->_tileTextureResolution._x;
  const double texHeight = correctionFactor * layerTilesRenderParameters->_tileTextureResolution._y;

  const double texWidthSquared  = texWidth  * texWidth;
  const double texHeightSquared = texHeight * texHeight;

  const size_t firstLevelTilesCount = _firstLevelTiles.size();

  const Frustum* frustumInModelCoordinates = _lastCamera->getFrustumInModelCoordinates();

  const long long nowInMS = _lastSplitTimer->nowInMilliseconds();

  _prc->_tileLODTester               = _tileLODTester;
  _prc->_tileVisibilityTester        = _tileVisibilityTester;
  _prc->_frustumInModelCoordinates   = frustumInModelCoordinates;
  _prc->_verticalExaggeration        = _verticalExaggeration;
  _prc->_layerTilesRenderParameters  = layerTilesRenderParameters;
  _prc->_texturizer                  = _texturizer;
  _prc->_tilesRenderParameters       = _tilesRenderParameters;
  _prc->_lastSplitTimer              = _lastSplitTimer;
  _prc->_elevationDataProvider       = _elevationDataProvider;
  _prc->_tessellator                 = _tessellator;
  _prc->_layerSet                    = _layerSet;
  _prc->_tileTextureDownloadPriority = _tileTextureDownloadPriority;
  _prc->_texWidthSquared             = texWidthSquared;
  _prc->_texHeightSquared            = texHeightSquared;
  _prc->_nowInMS                     = nowInMS;
  _prc->_renderTileMeshes            = _renderTileMeshes;
  _prc->_logTilesPetitions           = _logTilesPetitions;


  _tileLODTester->renderStarted();
  _tileVisibilityTester->renderStarted();


  if (_firstRender) {
    // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
    // loaded as they will be used as last-chance fallback texture for any tile.

    for (int i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      tile->render(rc,
                   _prc,
                   _glState,
                   &_statistics,
                   NULL /* toVisitInNextIteration */);
    }
  }
  else {
#ifdef C_CODE
    _toVisit = _firstLevelTiles;
#endif
#ifdef JAVA_CODE
    _toVisit.clear();
    // hand made for to avoid garbage (like an interator)
    for (int i = 0; i < firstLevelTilesCount; i++) {
      _toVisit.add( _firstLevelTiles.get(i) );
    }
#endif

    while (!_toVisit.empty()) {
      _toVisitInNextIteration.clear();

      const size_t toVisitSize = _toVisit.size();
      for (size_t i = 0; i < toVisitSize; i++) {
        Tile* tile = _toVisit[i];
        tile->render(rc,
                     _prc,
                     _glState,
                     &_statistics,
                     &_toVisitInNextIteration);
      }

#ifdef C_CODE
      _toVisit = _toVisitInNextIteration;
#endif
#ifdef JAVA_CODE
      _toVisit.clear();
      // hand made for to avoid garbage (like an interator)
      final int toVisitInNextIterationSize = _toVisitInNextIteration.size();
      for (int i = 0; i < toVisitInNextIterationSize; i++) {
        _toVisit.add( _toVisitInNextIteration.get(i) );
      }
#endif
    }
  }

  _firstRender = false;

  if (_showStatistics) {
    _statistics.log( rc->getLogger() );
  }

  const Sector* previousLastVisibleSector = _lastVisibleSector;
  _lastVisibleSector = _statistics.updateVisibleSector(_lastVisibleSector);
  if (previousLastVisibleSector != _lastVisibleSector) {
    // ILogger::instance()->logInfo("=> visibleSector: %s", _lastVisibleSector->description().c_str());
    if (_lastVisibleSector != NULL) {
      const size_t visibleSectorListenersCount = _visibleSectorListeners.size();
      for (size_t i = 0; i < visibleSectorListenersCount; i++) {
        VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
        entry->tryToNotifyListener(_lastVisibleSector, rc);
      }
    }
  }

}

void PlanetRenderer::addTerrainTouchListener(TerrainTouchListener* listener) {
  if (listener != NULL) {
    _terrainTouchListeners.push_back(listener);
  }
}

bool PlanetRenderer::onTouchEvent(const G3MEventContext* ec,
                                  const TouchEvent* touchEvent) {
  if (_lastCamera == NULL) {
    return false;
  }

  if ( touchEvent->getType() == _touchEventTypeOfTerrainTouchListener ) {
    const Vector2F pixel = touchEvent->getTouch(0)->getPos();
    const Vector3D ray = _lastCamera->pixel2Ray(pixel);
    const Vector3D origin = _lastCamera->getCartesianPosition();

    const Planet* planet = ec->getPlanet();

    const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
    if (positionCartesian.isNan()) {
      ILogger::instance()->logWarning("PlanetRenderer::onTouchEvent: positionCartesian ( - planet->closestIntersection(origin, ray) - ) is NaN");
      return false;
    }

    const Geodetic3D position = planet->toGeodetic3D(positionCartesian);

    const size_t firstLevelTilesCount = _firstLevelTiles.size();
    for (size_t i = 0; i < firstLevelTilesCount; i++) {
      const Tile* tile = _firstLevelTiles[i]->getDeepestTileContaining(position);
      if (tile != NULL) {

        const Vector2I tileDimension = Vector2I(256, 256);
        const Vector2I normalizedPixel = tile->getNormalizedPixelFromPosition(position.asGeodetic2D(), tileDimension);
        ILogger::instance()->logInfo("Touched on %s", tile->description().c_str());
        ILogger::instance()->logInfo("Touched on position %s", position.description().c_str());
        ILogger::instance()->logInfo("Touched on pixels %s", normalizedPixel.description().c_str());
        ILogger::instance()->logInfo("Camera position=%s heading=%f pitch=%f",
                                     _lastCamera->getGeodeticPosition().description().c_str(),
                                     _lastCamera->getHeading()._degrees,
                                     _lastCamera->getPitch()._degrees);

        if (_texturizer->onTerrainTouchEvent(ec, position, tile, _layerSet)) {
          return true;
        }

        const int terrainTouchListenersSize = _terrainTouchListeners.size();
        for (int j = terrainTouchListenersSize-1; j >= 0; j--) {
          TerrainTouchListener* listener = _terrainTouchListeners[j];
          if (listener->onTerrainTouch(ec, pixel, _lastCamera, position, tile)) {
            return true;
          }
        }

        return false;
      }
    }
  }

  return false;
}


void PlanetRenderer::pruneFirstLevelTiles() {
  const size_t firstLevelTilesCount = _firstLevelTiles.size();
  for (size_t i = 0; i < firstLevelTilesCount; i++) {
    Tile* tile = _firstLevelTiles[i];
    tile->prune(_texturizer, _elevationDataProvider);
  }
}

void PlanetRenderer::addVisibleSectorListener(VisibleSectorListener* listener,
                                              const TimeInterval& stabilizationInterval) {
  _visibleSectorListeners.push_back( new VisibleSectorListenerEntry(listener,
                                                                    stabilizationInterval) );
}

void PlanetRenderer::addListener(const Angle& latitude,
                                 const Angle& longitude,
                                 SurfaceElevationListener* listener) {
  _elevationListenersTree.add(Geodetic2D(latitude, longitude), listener);
}

void PlanetRenderer::addListener(const Geodetic2D& position,
                                 SurfaceElevationListener* listener) {
  _elevationListenersTree.add(position, listener);
}

bool PlanetRenderer::removeListener(SurfaceElevationListener* listener) {
  return _elevationListenersTree.remove(listener);
}

void PlanetRenderer::sectorElevationChanged(ElevationData* elevationData) const {
  if (elevationData != NULL) {
    _elevationListenersTree.notifyListeners(elevationData, _verticalExaggeration);
  }
}

bool PlanetRenderer::setRenderedSector(const Sector& sector) {
  if ((_renderedSector != NULL && !_renderedSector->isEquals(sector)) ||
      (_renderedSector == NULL && !sector.isEquals(Sector::fullSphere()))) {
    delete _renderedSector;

    if (sector.isEquals(Sector::fullSphere())) {
      _renderedSector = NULL;
    }
    else {
      _renderedSector = new Sector(sector);
    }

    _tessellator->setRenderedSector(sector);

    changed();

    return true;
  }
  return false;
}

void PlanetRenderer::setTerrainElevationProvider(TerrainElevationProvider* terrainElevationProvider) {
  if (_terrainElevationProvider != terrainElevationProvider) {
    if (_terrainElevationProvider != NULL) {
      _terrainElevationProvider->cancel();
      _terrainElevationProvider->_release();
    }

    _terrainElevationProvider = terrainElevationProvider;

    if (_terrainElevationProvider != NULL) {
      //_terrainElevationProvider->setChangedListener(this);
      if (_context != NULL) {
        _terrainElevationProvider->initialize(_context); // initializing TerrainElevationProvider in case it wasn't
      }
    }

    changed();
  }
}

void PlanetRenderer::setElevationDataProvider(ElevationDataProvider* elevationDataProvider,
                                              bool owned) {
  if (_elevationDataProvider != elevationDataProvider) {
    if (_ownsElevationDataProvider) {
      delete _elevationDataProvider;
    }

    _ownsElevationDataProvider = owned;
    _elevationDataProvider = elevationDataProvider;

    if (_elevationDataProvider != NULL) {
      _elevationDataProvider->setChangedListener(this);
      if (_context != NULL) {
        _elevationDataProvider->initialize(_context); //Initializing EDP in case it wasn't
      }
    }

    changed();
  }
}

void PlanetRenderer::setVerticalExaggeration(float verticalExaggeration) {
  if (_verticalExaggeration != verticalExaggeration) {
    _verticalExaggeration = verticalExaggeration;
    changed();
  }
}

void PlanetRenderer::setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener,
                                                    const size_t rendererIdentifier) {
  if (_changedInfoListener != NULL) {
    ILogger::instance()->logWarning("Changed Renderer Info Listener of PlanetRenderer already set");
  }

  _rendererIdentifier = rendererIdentifier;
  _changedInfoListener = changedInfoListener;

  if(_changedInfoListener != NULL) {
    _changedInfoListener->changedRendererInfo(rendererIdentifier, _layerSet->getInfo());
  }
}

//std::vector<std::string> PlanetRenderer::getInfo() {
//  _info.clear();
//  std::vector<std::string> info = _layerSet->getInfo();
//
//#ifdef C_CODE
//      _info.insert(_info.end(),info.begin(), info.end());
//#endif
//#ifdef JAVA_CODE
//      _infos.add(info);
//#endif
//
//  return _info;
//}

void PlanetRenderer::onTileHasChangedMesh(const Tile *tile) const {
  _tileLODTester->onTileHasChangedMesh(tile);
  _tileVisibilityTester->onTileHasChangedMesh(tile);
}

void PlanetRenderer::setIncrementalTileQuality(bool incrementalTileQuality) {
  _tilesRenderParameters->_incrementalTileQuality = incrementalTileQuality;
}
