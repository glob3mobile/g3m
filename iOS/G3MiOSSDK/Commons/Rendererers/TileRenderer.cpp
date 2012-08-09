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
  
#ifdef C_CODE
  delete _tessellator;
  delete _texturizer;
  delete _parameters;

  delete _lastSplitTimer;
  delete _lastTexturizerTimer;
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
  
  _lastSplitTimer      = ic->getFactory()->createTimer();
  _lastTexturizerTimer = ic->getFactory()->createTimer();
  
  _texturizer->initialize(ic);
}

bool TileRenderer::isReadyToRender(const RenderContext *rc) {
  if (_tessellator != NULL) {
    if (!_tessellator->isReadyToRender(rc)) {
      return false;
    }
  }
  
  if (_texturizer != NULL) {
    if (!_texturizer->isReady(rc)) {
      return false;
    }
  }
  
  return true;
}

int TileRenderer::render(const RenderContext* rc) {
  TilesStatistics statistics;
  
  const int topLevelTilesSize = _topLevelTiles.size();
  
  
  DistanceToCenterTileComparison predicate = DistanceToCenterTileComparison(rc->getCamera(),
                                                                            rc->getPlanet());

  if (_topTilesJustCreated) {
#ifdef C_CODE
    predicate.initialize();
    std::sort(_topLevelTiles.begin(),
              _topLevelTiles.end(),
              predicate);
#endif
    
#ifdef JAVA_CODE
    java.util.Collections.sort(_topLevelTiles, predicate);
#endif
    
    if (_texturizer != NULL) {
      for (int i = 0; i < topLevelTilesSize; i++) {
        Tile* tile = _topLevelTiles[i];
        _texturizer->justCreatedTopTile(tile);
      }
    }
    _topTilesJustCreated = false;
  }
  
//  std::vector<Tile*> toVisit(_topLevelTiles);
  std::list<Tile*> toVisit;

  for (int i = 0; i < _topLevelTiles.size(); i++) {
    toVisit.push_back(_topLevelTiles[i]);
  }
  
  while (toVisit.size() > 0) {
    std::list<Tile*> toVisitInNextIteration;

#ifdef JAVA_CODE
      java.util.Collections.sort(toVisit, predicate);
#endif
    
    for (std::list<Tile *>::const_iterator i = toVisit.begin(); i != toVisit.end(); i++) {
      Tile* tile = *i;
      tile->render(rc,
                   _tessellator,
                   _texturizer,
                   _parameters,
                   &statistics,
                   &toVisitInNextIteration,
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
    
  return Renderer::maxTimeToRender;
}

