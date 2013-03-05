//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "TileRenderer.hpp"
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
  _stabilizationIntervalInMS(stabilizationInterval.milliseconds()),
  _lastSector(NULL),
  _timer(NULL),
  _whenNotifyInMS(0)
  {

  }

  void notifyListener(const Sector* visibleSector,
                      const G3MRenderContext* rc) const {
    const Geodetic3D cameraPosition = rc->getPlanet()->toGeodetic3D( rc->getCurrentCamera()->getCartesianPosition() );

    _listener->onVisibleSectorChange(*_lastSector, cameraPosition);
  }

  void tryToNotifyListener(const Sector* visibleSector,
                           const G3MRenderContext* rc) {
    if ( _stabilizationIntervalInMS == 0 ) {
      if ( (_lastSector == NULL) || (!_lastSector->isEqualsTo(*visibleSector)) ) {
        delete _lastSector;
        _lastSector = new Sector(*visibleSector);

        notifyListener(visibleSector, rc);
      }
    }
    else {
      const long long now = getTimer()->now().milliseconds();

      if ( (_lastSector == NULL) || (!_lastSector->isEqualsTo(*visibleSector)) ) {
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


TileRenderer::TileRenderer(const TileTessellator* tessellator,
                           ElevationDataProvider* elevationDataProvider,
                           float verticalExaggeration,
                           TileTexturizer*  texturizer,
                           LayerSet* layerSet,
                           const TilesRenderParameters* parameters,
                           bool showStatistics,
                           long long texturePriority) :
_tessellator(tessellator),
_elevationDataProvider(elevationDataProvider),
_verticalExaggeration(verticalExaggeration),
_texturizer(texturizer),
_layerSet(layerSet),
_parameters(parameters),
_showStatistics(showStatistics),
_topTilesJustCreated(false),
_lastSplitTimer(NULL),
_lastCamera(NULL),
_firstRender(false),
_context(NULL),
_lastVisibleSector(NULL),
_texturePriority(texturePriority)
{
  _layerSet->setChangeListener(this);
}

void TileRenderer::recreateTiles() {
  pruneTopLevelTiles();
  clearTopLevelTiles();
  _firstRender = true;
  createTopLevelTiles(_context);
}

class RecreateTilesTask : public GTask {
private:
  TileRenderer* _tileRenderer;
public:
  RecreateTilesTask(TileRenderer* tileRenderer) :
  _tileRenderer(tileRenderer)
  {
  }

  void run(const G3MContext* context) {
    _tileRenderer->recreateTiles();
  }
};

void TileRenderer::changed(const LayerSet* layerSet) {
  // recreateTiles();

  // recreateTiles() delete tiles, then meshes, and delete textures from the GPU so it has to be executed in the OpenGL thread
  _context->getThreadUtils()->invokeInRendererThread(new RecreateTilesTask(this), true);
}

TileRenderer::~TileRenderer() {
  clearTopLevelTiles();

  delete _tessellator;
  delete _elevationDataProvider;
  delete _texturizer;
  delete _parameters;

  delete _lastSplitTimer;

  delete _lastVisibleSector;

  const int visibleSectorListenersCount = _visibleSectorListeners.size();
  for (int i = 0; i < visibleSectorListenersCount; i++) {
    VisibleSectorListenerEntry* entry = _visibleSectorListeners[i];
    delete entry;
  }
}

void TileRenderer::clearTopLevelTiles() {
  for (int i = 0; i < _topLevelTiles.size(); i++) {
    Tile* tile = _topLevelTiles[i];
    delete tile;
  }

  _topLevelTiles.clear();
}

void TileRenderer::createTopLevelTiles(const G3MContext* context) {

  const LayerTilesRenderParameters* layerParameters = _layerSet->getLayerTilesRenderParameters();
  if (layerParameters == NULL) {
    ILogger::instance()->logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create topTiles");
    return;
  }

  const Angle fromLatitude  = layerParameters->_topSector.lower().latitude();
  const Angle fromLongitude = layerParameters->_topSector.lower().longitude();

  const Angle deltaLan = layerParameters->_topSector.getDeltaLatitude();
  const Angle deltaLon = layerParameters->_topSector.getDeltaLongitude();

  const Angle tileHeight = deltaLan.div(layerParameters->_splitsByLatitude);
  const Angle tileWidth = deltaLon.div(layerParameters->_splitsByLongitude);

  for (int row = 0; row < layerParameters->_splitsByLatitude; row++) {
    const Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
    const Angle tileLatTo = tileLatFrom.add(tileHeight);

    for (int col = 0; col < layerParameters->_splitsByLongitude; col++) {
      const Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
      const Angle tileLonTo = tileLonFrom.add(tileWidth);

      const Geodetic2D tileLower(tileLatFrom, tileLonFrom);
      const Geodetic2D tileUpper(tileLatTo, tileLonTo);
      const Sector sector(tileLower, tileUpper);

//      Tile* tile = new Tile(_texturizer, NULL, sector, _parameters->_topLevel, row, col);
      Tile* tile = new Tile(_texturizer, NULL, sector, 0, row, col);
      _topLevelTiles.push_back(tile);
    }
  }

  context->getLogger()->logInfo("Created %d top level tiles", _topLevelTiles.size());

  _topTilesJustCreated = true;
}

void TileRenderer::initialize(const G3MContext* context) {
  _context = context;

  clearTopLevelTiles();
  createTopLevelTiles(context);

  delete _lastSplitTimer;
  _lastSplitTimer = context->getFactory()->createTimer();

  _layerSet->initialize(context);
  _texturizer->initialize(context, _parameters);
  if (_elevationDataProvider != NULL) {
    _elevationDataProvider->initialize(context);
  }
}

bool TileRenderer::isReadyToRender(const G3MRenderContext *rc) {
  if (_elevationDataProvider != NULL) {
    if (!_elevationDataProvider->isReadyToRender(rc)) {
      return false;
    }
  }

  if (_topTilesJustCreated) {
    _topTilesJustCreated = false;

    const int topLevelTilesCount = _topLevelTiles.size();

    if (_parameters->_forceTopLevelTilesRenderOnStart) {
      TilesStatistics statistics;

      TileRenderContext trc(_tessellator,
                            _elevationDataProvider,
                            _texturizer,
                            _layerSet,
                            _parameters,
                            &statistics,
                            _lastSplitTimer,
                            true,
                            _texturePriority,
                            _verticalExaggeration);

      for (int i = 0; i < topLevelTilesCount; i++) {
        Tile* tile = _topLevelTiles[i];
        tile->prepareForFullRendering(rc, &trc);
      }
    }

    if (_texturizer != NULL) {
      for (int i = 0; i < topLevelTilesCount; i++) {
        Tile* tile = _topLevelTiles[i];
        _texturizer->justCreatedTopTile(rc, tile, _layerSet);
      }
    }
  }

  if (_parameters->_forceTopLevelTilesRenderOnStart) {
    const int topLevelTilesCount = _topLevelTiles.size();
    for (int i = 0; i < topLevelTilesCount; i++) {
      Tile* tile = _topLevelTiles[i];
      if (!tile->isTextureSolved()) {
        return false;
      }
    }

    if (_tessellator != NULL) {
      if (!_tessellator->isReady(rc)) {
        return false;
      }
    }

    if (_texturizer != NULL) {
      if (!_texturizer->isReady(rc, _layerSet)) {
        return false;
      }
    }
  }

  return true;
}

void TileRenderer::render(const G3MRenderContext* rc,
                          const GLState& parentState) {
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();

  TilesStatistics statistics;

  TileRenderContext trc(_tessellator,
                        _elevationDataProvider,
                        _texturizer,
                        _layerSet,
                        _parameters,
                        &statistics,
                        _lastSplitTimer,
                        _firstRender /* if first render, force full render */,
                        _texturePriority,
                        _verticalExaggeration);

  const int topLevelTilesCount = _topLevelTiles.size();

  if (_firstRender && _parameters->_forceTopLevelTilesRenderOnStart) {
    // force one render pass of the topLevel tiles to make the (toplevel) textures loaded
    // as they will be used as last-chance fallback texture for any tile.
    _firstRender = false;

    for (int i = 0; i < topLevelTilesCount; i++) {
      Tile* tile = _topLevelTiles[i];
      tile->render(rc,
                   &trc,
                   parentState,
                   NULL);
    }
  }
  else {
    std::list<Tile*> toVisit;
    for (int i = 0; i < topLevelTilesCount; i++) {
      toVisit.push_back(_topLevelTiles[i]);
    }

    while (toVisit.size() > 0) {
      std::list<Tile*> toVisitInNextIteration;

      for (std::list<Tile*>::iterator iter = toVisit.begin();
           iter != toVisit.end();
           iter++) {
        Tile* tile = *iter;

        tile->render(rc,
                     &trc,
                     parentState,
                     &toVisitInNextIteration);
      }

      toVisit = toVisitInNextIteration;
    }
  }

  if (_showStatistics) {
    statistics.log( rc->getLogger() );
  }


  const Sector* renderedSector = statistics.getRenderedSector();
  if (renderedSector != NULL) {
    if ( (_lastVisibleSector == NULL) || !renderedSector->isEqualsTo(*_lastVisibleSector) ) {
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


bool TileRenderer::onTouchEvent(const G3MEventContext* ec,
                                const TouchEvent* touchEvent) {
  bool handled = false;

  if (touchEvent->getType() == LongPress) {

    if (_lastCamera != NULL) {
      const Vector2I pixel = touchEvent->getTouch(0)->getPos();
      const Vector3D ray = _lastCamera->pixel2Ray(pixel);
      const Vector3D origin = _lastCamera->getCartesianPosition();

      const Planet* planet = ec->getPlanet();

      const Vector3D positionCartesian = planet->closestIntersection(origin, ray);
      if (positionCartesian.isNan()) {
        return false;
      }

      const Geodetic3D position = planet->toGeodetic3D(positionCartesian);

      for (int i = 0; i < _topLevelTiles.size(); i++) {
        const Tile* tile = _topLevelTiles[i]->getDeepestTileContaining(position);
        if (tile != NULL) {
          ILogger::instance()->logInfo("Touched on %s", tile->description().c_str());
          _texturizer->onTerrainTouchEvent(ec, position, tile, _layerSet);
          handled = true;
        }
      }
    }

  }

  return handled;
}


void TileRenderer::pruneTopLevelTiles() {
  for (int i = 0; i < _topLevelTiles.size(); i++) {
    Tile* tile = _topLevelTiles[i];
    tile->prune(_texturizer, _elevationDataProvider);
  }
}

void TileRenderer::addVisibleSectorListener(VisibleSectorListener* listener,
                                            const TimeInterval& stabilizationInterval) {
  _visibleSectorListeners.push_back( new VisibleSectorListenerEntry(listener,
                                                                    stabilizationInterval) );
}
