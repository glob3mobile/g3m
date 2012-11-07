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

TileRenderer::~TileRenderer() {
  clearTopLevelTiles();
  
#ifdef C_CODE
  delete _tessellator;
  delete _texturizer;
  delete _parameters;
  
  delete _lastSplitTimer;
#endif
}

void TileRenderer::clearTopLevelTiles() {
  for (int i = 0; i < _topLevelTiles.size(); i++) {
    Tile* tile = _topLevelTiles[i];
#ifdef C_CODE
    delete tile;
#endif
  }
  
  _topLevelTiles.clear();
}

void TileRenderer::createTopLevelTiles(const InitializationContext* ic) {
  const Angle fromLatitude  = _parameters->_topSector.lower().latitude();
  const Angle fromLongitude = _parameters->_topSector.lower().longitude();
  
  const Angle deltaLan = _parameters->_topSector.getDeltaLatitude();
  const Angle deltaLon = _parameters->_topSector.getDeltaLongitude();
  
  const Angle tileHeight = deltaLan.div(_parameters->_splitsByLatitude);
  const Angle tileWidth = deltaLon.div(_parameters->_splitsByLongitude);
  
  for (int row = 0; row < _parameters->_splitsByLatitude; row++) {
    const Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
    const Angle tileLatTo = tileLatFrom.add(tileHeight);
    
    for (int col = 0; col < _parameters->_splitsByLongitude; col++) {
      const Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
      const Angle tileLonTo = tileLonFrom.add(tileWidth);
      
      const Geodetic2D tileLower(tileLatFrom, tileLonFrom);
      const Geodetic2D tileUpper(tileLatTo, tileLonTo);
      const Sector sector(tileLower, tileUpper);
      
      Tile* tile = new Tile(_texturizer, NULL, sector, _parameters->_topLevel, row, col);
      _topLevelTiles.push_back(tile);
    }
  }
  
  ic->getLogger()->logInfo("Created %d top level tiles", _topLevelTiles.size());
  
  _topTilesJustCreated = true;
}

void TileRenderer::initialize(const InitializationContext* ic) {
  clearTopLevelTiles();
  createTopLevelTiles(ic);
  
  delete _lastSplitTimer;
  _lastSplitTimer      = ic->getFactory()->createTimer();
  
  _layerSet->initialize(ic);
  _texturizer->initialize(ic, _parameters);
}

bool TileRenderer::isReadyToRender(const RenderContext *rc) {
  if (_topTilesJustCreated) {
    if (_texturizer != NULL) {
      const int topLevelTilesSize = _topLevelTiles.size();
      for (int i = 0; i < topLevelTilesSize; i++) {
        Tile* tile = _topLevelTiles[i];
        _texturizer->justCreatedTopTile(rc, tile, _layerSet);
      }
    }
    _topTilesJustCreated = false;
  }
  
  if (_parameters->_forceTopLevelTilesRenderOnStart) {
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

void TileRenderer::render(const RenderContext* rc) {
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();

  TilesStatistics statistics;
  
  TileRenderContext trc(_tessellator,
                        _texturizer,
                        _layerSet,
                        _parameters,
                        &statistics,
                        _lastSplitTimer,
                        _firstRender /* if first render, force full render */);
  
  if (_firstRender && _parameters->_forceTopLevelTilesRenderOnStart) {
    // force one render of the topLevel tiles to make the (toplevel) textures loaded as they
    // will be used as last-chance fallback texture for any tile.
    _firstRender = false;
    
    for (int i = 0; i < _topLevelTiles.size(); i++) {
      Tile* tile = _topLevelTiles[i];
      tile->render(rc,
                   &trc,
                   NULL);
    }
  }
  else {
    std::list<Tile*> toVisit;
    for (int i = 0; i < _topLevelTiles.size(); i++) {
      toVisit.push_back(_topLevelTiles[i]);
    }
    
    //    DistanceToCenterTileComparison predicate = DistanceToCenterTileComparison(rc->getCurrentCamera(),
    //                                                                              rc->getPlanet());
    
    while (toVisit.size() > 0) {
      std::list<Tile*> toVisitInNextIteration;
      
      //      predicate.initialize();
      //      toVisit.sort(predicate);
      
      for (std::list<Tile*>::iterator iter = toVisit.begin();
           iter != toVisit.end();
           iter++) {
        Tile* tile = *iter;
        
        tile->render(rc,
                     &trc,
                     &toVisitInNextIteration);
      }
      
      toVisit = toVisitInNextIteration;
    }
  }
  
  if (_showStatistics) {
    if (!_lastStatistics.equalsTo(statistics)) {
      _lastStatistics  = statistics;
      statistics.log(rc->getLogger());
    }
  }

}


bool TileRenderer::onTouchEvent(const EventContext* ec,
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
    tile->prune(_texturizer);
  }
}
