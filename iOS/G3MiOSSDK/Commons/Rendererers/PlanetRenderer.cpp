//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
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
#include "TileRasterizer.hpp"
#include "ElevationData.hpp"
#include "TerrainTouchListener.hpp"
#include "IDeviceInfo.hpp"
#include "Sector.hpp"
#include "TileRenderingListener.hpp"

#include <algorithm>

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
      const long long now = getTimer()->now()._milliseconds;

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

    if (_timer != NULL) {
      IFactory::instance()->deleteTimer(_timer);
    }

    delete _lastSector;
  }
};


PlanetRenderer::PlanetRenderer(TileTessellator*             tessellator,
                               ElevationDataProvider*       elevationDataProvider,
                               bool                         ownsElevationDataProvider,
                               float                        verticalExaggeration,
                               TileTexturizer*              texturizer,
                               TileRasterizer*              tileRasterizer,
                               LayerSet*                    layerSet,
                               const TilesRenderParameters* tilesRenderParameters,
                               bool                         showStatistics,
                               long long                    texturePriority,
                               const Sector&                renderedSector,
                               const bool                   renderTileMeshes,
                               const bool                   logTilesPetitions,
                               TileRenderingListener*       tileRenderingListener) :
_tessellator(tessellator),
_elevationDataProvider(elevationDataProvider),
_ownsElevationDataProvider(ownsElevationDataProvider),
_verticalExaggeration(verticalExaggeration),
_texturizer(texturizer),
_tileRasterizer(tileRasterizer),
_layerSet(layerSet),
_tilesRenderParameters(tilesRenderParameters),
_showStatistics(showStatistics),
_firstLevelTilesJustCreated(false),
_lastSplitTimer(NULL),
_lastCamera(NULL),
_firstRender(false),
_lastVisibleSector(NULL),
_texturePriority(texturePriority),
_allFirstLevelTilesAreTextureSolved(false),
_recreateTilesPending(false),
_glState(new GLState()),
_renderedSector(renderedSector.isEquals(Sector::fullSphere())? NULL : new Sector(renderedSector)),
_layerTilesRenderParameters(NULL),
_layerTilesRenderParametersDirty(true),
_renderTileMeshes(renderTileMeshes),
_logTilesPetitions(logTilesPetitions),
_tileRenderingListener(tileRenderingListener)
{
  _context = NULL;
  _layerSet->setChangeListener(this);
  if (_tileRasterizer != NULL) {
    _tileRasterizer->setChangeListener(this);
  }
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
      ILogger::instance()->logError("_context if not initialized");
    }
    else {
      _context->getThreadUtils()->invokeInRendererThread(new RecreateTilesTask(this), true);
    }
  }
}

PlanetRenderer::~PlanetRenderer() {
  clearFirstLevelTiles();

  delete _layerTilesRenderParameters;

  delete _tessellator;
  delete _elevationDataProvider;
  delete _texturizer;
  delete _tilesRenderParameters;

  delete _lastSplitTimer;

  delete _lastVisibleSector;

  const int visibleSectorListenersCount = _visibleSectorListeners.size();
  for (int i = 0; i < visibleSectorListenersCount; i++) {
    VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
    delete entry;
  }

  delete _renderedSector;

  delete _tileRenderingListener;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void PlanetRenderer::clearFirstLevelTiles() {
  const int firstLevelTilesCount = _firstLevelTiles.size();
  for (int i = 0; i < firstLevelTilesCount; i++) {
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
                                           int firstLevel,
                                           bool mercator) const {
  if (tile->_level == firstLevel) {
    firstLevelTiles.push_back(tile);
  }
  else {
    const Sector sector = tile->_sector;
    const Geodetic2D lower = sector._lower;
    const Geodetic2D upper = sector._upper;

    const Angle splitLongitude = Angle::midAngle(lower._longitude,
                                                 upper._longitude);

    const Angle splitLatitude = mercator
    /*                               */ ? MercatorUtils::calculateSplitLatitude(lower._latitude,
                                                                                upper._latitude)
    /*                               */ : Angle::midAngle(lower._latitude,
                                                          upper._latitude);


    std::vector<Tile*>* children = tile->createSubTiles(splitLatitude,
                                                        splitLongitude,
                                                        false);

    const int childrenSize = children->size();
    for (int i = 0; i < childrenSize; i++) {
      Tile* child = children->at(i);
      createFirstLevelTiles(firstLevelTiles, child, firstLevel, mercator);
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
  }
  return _layerTilesRenderParameters;
}

void PlanetRenderer::createFirstLevelTiles(const G3MContext* context) {

  const LayerTilesRenderParameters* parameters = getLayerTilesRenderParameters();
  if (!parameters) {
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
        Tile* tile = new Tile(_texturizer, NULL, sector, 0, row, col, this);
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
    const int topLevelTilesSize = topLevelTiles.size();
    for (int i = 0; i < topLevelTilesSize; i++) {
      Tile* tile = topLevelTiles[i];
      createFirstLevelTiles(_firstLevelTiles, tile, parameters->_firstLevel, parameters->_mercator);
    }
  }

  sortTiles(_firstLevelTiles);

  context->getLogger()->logInfo("Created %d first level tiles", _firstLevelTiles.size());

  _firstLevelTilesJustCreated = true;
}

void PlanetRenderer::initialize(const G3MContext* context) {
  _context = context;

  clearFirstLevelTiles();
  createFirstLevelTiles(context);

  delete _lastSplitTimer;
  _lastSplitTimer = context->getFactory()->createTimer();

  _layerSet->initialize(context);
  _texturizer->initialize(context, _tilesRenderParameters);
  if (_elevationDataProvider != NULL) {
    _elevationDataProvider->initialize(context);
  }
  if (_tileRasterizer != NULL) {
    _tileRasterizer->initialize(context);
  }
}

RenderState PlanetRenderer::getRenderState(const G3MRenderContext* rc) {
  const LayerTilesRenderParameters* layerTilesRenderParameters = getLayerTilesRenderParameters();

  if (layerTilesRenderParameters == NULL) {
    return RenderState::error(_errors);
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

  if (_firstLevelTilesJustCreated) {
    _firstLevelTilesJustCreated = false;

    const int firstLevelTilesCount = _firstLevelTiles.size();

    if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
      _statistics.clear();

      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        tile->prepareForFullRendering(rc,
                                      _texturizer,
                                      _elevationDataProvider,
                                      _tessellator,
                                      _tileRasterizer,
                                      layerTilesRenderParameters,
                                      _layerSet,
                                      _tilesRenderParameters,
                                      true,
                                      _texturePriority,
                                      _verticalExaggeration,
                                      _logTilesPetitions);
      }
    }

    if (_texturizer != NULL) {
      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        _texturizer->justCreatedTopTile(rc, tile, _layerSet);
      }
    }
  }

  if (_tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
    if (!_allFirstLevelTilesAreTextureSolved) {
      const int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++) {
        Tile* tile = _firstLevelTiles[i];
        if (!tile->isTextureSolved()) {
          return RenderState::busy();
        }
      }

      if (_tessellator != NULL) {
        if (!_tessellator->isReady(rc)) {
          return RenderState::busy();
        }
      }

      if (_texturizer != NULL) {
        const RenderState texturizerRenderState = _texturizer->getRenderState(_layerSet);
        if (texturizerRenderState._type != RENDER_READY) {
          return texturizerRenderState;
        }
      }

      _allFirstLevelTilesAreTextureSolved = true;
    }
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
    const int layersCount = _layerSet->size();
    for (int i = 0; i < layersCount; i++) {
      Layer* layer = _layerSet->getLayer(i);
      if (layer->isEnable() && layer->getRenderState()._type == RENDER_READY) {
        layers.push_back(layer);
      }
    }

    const int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++) {
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
    std::vector<Tile*>* subTiles = tile->getSubTiles(getLayerTilesRenderParameters()->_mercator);

    const int subTilesCount = subTiles->size();
    for (int i = 0; i < subTilesCount; i++) {
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

  const Camera* cam = rc->getCurrentCamera();
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  } else{
    f->setMatrix(cam->getModelViewMatrix44D());
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
//  const float dpiFactor = deviceInfo->getPixelsInMM(0.1f);
  const float deviceQualityFactor = deviceInfo->getQualityFactor();

  const int firstLevelTilesCount = _firstLevelTiles.size();

  const Planet* planet = rc->getPlanet();
  const Vector3D& cameraNormalizedPosition       = _lastCamera->getNormalizedPosition();
  double cameraAngle2HorizonInRadians            = _lastCamera->getAngle2HorizonInRadians();
  const Frustum* cameraFrustumInModelCoordinates = _lastCamera->getFrustumInModelCoordinates();

  //Texture Size for every tile
  int texWidth  = layerTilesRenderParameters->_tileTextureResolution._x;
  int texHeight = layerTilesRenderParameters->_tileTextureResolution._y;

  const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
  const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / factor;

  texWidth *= correctionFactor;
  texHeight *= correctionFactor;

  const double texWidthSquared = texWidth * texWidth;
  const double texHeightSquared = texHeight * texHeight;

  const double nowInMS = _lastSplitTimer->now().milliseconds(); //Getting now from _lastSplitTimer

  if (_firstRender && _tilesRenderParameters->_forceFirstLevelTilesRenderOnStart) {
    // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
    // loaded as they will be used as last-chance fallback texture for any tile.
    _firstRender = false;

    for (int i = 0; i < firstLevelTilesCount; i++) {
      Tile* tile = _firstLevelTiles[i];
      tile->render(rc,
                   *_glState,
                   NULL,
                   planet,
                   cameraNormalizedPosition,
                   cameraAngle2HorizonInRadians,
                   cameraFrustumInModelCoordinates,
                   &_statistics,
                   _verticalExaggeration,
                   layerTilesRenderParameters,
                   _texturizer,
                   _tilesRenderParameters,
                   _lastSplitTimer,
                   _elevationDataProvider,
                   _tessellator,
                   _tileRasterizer,
                   _layerSet,
                   _renderedSector,
                   _firstRender, /* if first render, force full render */
                   _texturePriority,
                   texWidthSquared,
                   texHeightSquared,
                   nowInMS,
                   _renderTileMeshes,
                   _logTilesPetitions,
                   _tileRenderingListener);
    }
  }
  else {
    std::list<Tile*> toVisit;
    for (int i = 0; i < firstLevelTilesCount; i++) {
      toVisit.push_back(_firstLevelTiles[i]);
    }

    while (toVisit.size() > 0) {
      std::list<Tile*> toVisitInNextIteration;

      for (std::list<Tile*>::iterator iter = toVisit.begin();
           iter != toVisit.end();
           iter++) {
        Tile* tile = *iter;

        tile->render(rc,
                     *_glState,
                     &toVisitInNextIteration,
                     planet,
                     cameraNormalizedPosition,
                     cameraAngle2HorizonInRadians,
                     cameraFrustumInModelCoordinates,
                     &_statistics,
                     _verticalExaggeration,
                     layerTilesRenderParameters,
                     _texturizer,
                     _tilesRenderParameters,
                     _lastSplitTimer,
                     _elevationDataProvider,
                     _tessellator,
                     _tileRasterizer,
                     _layerSet,
                     _renderedSector,
                     _firstRender, /* if first render, force full render */
                     _texturePriority,
                     texWidthSquared,     //SENDING SQUARED TEX SIZE
                     texHeightSquared,
                     nowInMS,
                     _renderTileMeshes,
                     _logTilesPetitions,
                     _tileRenderingListener);
      }

      toVisit = toVisitInNextIteration;
    }
  }

  if (_showStatistics) {
    _statistics.log( rc->getLogger() );
  }


  const Sector* renderedSector = _statistics.getRenderedSector();
  if (renderedSector != NULL) {
    if ( (_lastVisibleSector == NULL) || !renderedSector->isEquals(*_lastVisibleSector) ) {
      delete _lastVisibleSector;
      _lastVisibleSector = new Sector(*renderedSector);
    }
  }

  if (_lastVisibleSector != NULL) {
    const int visibleSectorListenersCount = _visibleSectorListeners.size();
    for (int i = 0; i < visibleSectorListenersCount; i++) {
      VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];

      entry->tryToNotifyListener(_lastVisibleSector, rc);
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

  if (touchEvent->getType() == LongPress) {
    const Vector2I pixel = touchEvent->getTouch(0)->getPos();
    const Vector3D ray = _lastCamera->pixel2Ray(pixel);
    const Vector3D origin = _lastCamera->getCartesianPosition();

    const Planet* planet = ec->getPlanet();

    const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
    if (positionCartesian.isNan()) {
      return false;
    }

    const Geodetic3D position = planet->toGeodetic3D(positionCartesian);

    const int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++) {
      const Tile* tile = _firstLevelTiles[i]->getDeepestTileContaining(position);
      if (tile != NULL) {
        ILogger::instance()->logInfo("Touched on %s", tile->description().c_str());
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
  const int firstLevelTilesCount = _firstLevelTiles.size();
  for (int i = 0; i < firstLevelTilesCount; i++) {
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

void PlanetRenderer::sectorElevationChanged(ElevationData* elevationData) const{
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
    } else{
      _renderedSector = new Sector(sector);
    }
    
    _tessellator->setRenderedSector(sector);
    
    changed();
    
    return true;
  }
  return false;
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
