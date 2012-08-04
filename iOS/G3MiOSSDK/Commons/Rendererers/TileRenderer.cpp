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


TileRenderer::~TileRenderer() {
  clearTopLevelTiles();
  
  delete _tessellator;
  delete _texturizer;
  delete _parameters;

  delete _frameTimer;
  delete _lastSplitTimer;
  delete _lastTexturizerTimer;
}

void TileRenderer::clearTopLevelTiles() {
  for (int i = 0; i < _topLevelTiles.size(); i++) {
    Tile* tile = _topLevelTiles[i];
    delete tile;
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
      
      Tile* tile = new Tile(NULL, sector, _parameters->_topLevel, row, col);
      _topLevelTiles.push_back(tile);
    }
  }
  
  ic->getLogger()->logInfo("Created %i top level tiles", _topLevelTiles.size());
  
  _topTilesJustCreated = true;
}

void TileRenderer::initialize(const InitializationContext* ic) {
  clearTopLevelTiles();
  createTopLevelTiles(ic);
  
  _frameTimer          = ic->getFactory()->createTimer();
  _lastSplitTimer      = ic->getFactory()->createTimer();
  _lastTexturizerTimer = ic->getFactory()->createTimer();
}

bool TileRenderer::isReadyToRender(const RenderContext *rc) {
  if (_tessellator != NULL) {
    if (!_tessellator->isReadyToRender(rc)) {
      return false;
    }
  }
  
  if (_texturizer != NULL) {
    if (!_texturizer->isReadyToRender(rc)) {
      return false;
    }
  }
  
  return true;
}

int TileRenderer::render(const RenderContext* rc) {
  _frameTimer->start();

  TilesStatistics statistics(_parameters);
  
  const int topLevelTilesSize = _topLevelTiles.size();

  if (_topTilesJustCreated) {
    if (_texturizer != NULL) {
      for (int i = 0; i < topLevelTilesSize; i++) {
        Tile* tile = _topLevelTiles[i];
        _texturizer->justCreatedTopTile(tile);
      }
    }
    _topTilesJustCreated = false;
  }
  
  const DistanceToCenterTileComparison predicate = DistanceToCenterTileComparison(rc->getCamera(),
                                                                                  rc->getPlanet());
  
//  std::vector<Tile*> toVisit(_topLevelTiles);
  std::list<Tile*> toVisit;

  for (int i = 0; i < _topLevelTiles.size(); i++) {
    toVisit.push_back(_topLevelTiles[i]);
  }
  
  while (toVisit.size() > 0) {
    std::list<Tile*> toVisitInNextIteration;
    
//    std::sort(toVisit.begin(),
//              toVisit.end(),
//              predicate);

//    const int toVisitSize = toVisit.size();
//    for (int i = 0; i < toVisitSize; i++) {
//      Tile* tile = toVisit[i];
//      tile->render(rc,
//                   _tessellator,
//                   _texturizer,
//                   _parameters,
//                   &statistics,
//                   &toVisitInNextIteration,
//                   _frameTimer,
//                   _lastSplitTimer,
//                   _lastTexturizerTimer);
//    }
    
    for (std::list<Tile*>::iterator iter = toVisit.begin();
         iter != toVisit.end();
         iter++) {
      Tile* tile = *iter;
      
      tile->render(rc,
                   _tessellator,
                   _texturizer,
                   _parameters,
                   &statistics,
                   &toVisitInNextIteration,
                   _frameTimer,
                   _lastSplitTimer,
                   _lastTexturizerTimer);
    }

    toVisit = toVisitInNextIteration;
//    toVisitInNextIteration.clear();
  }
  
  
  if (_showStatistics) {
    if (!_lastStatistics.equalsTo(statistics)) {
      _lastStatistics  = statistics;
      statistics.log(rc->getLogger());
    }
  }
    
  return MAX_TIME_TO_RENDER;
}

