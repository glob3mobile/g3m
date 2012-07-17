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


Tile* TilesCache::getTile(const int level,
                          const int row, const int column) {
  
  const int entriesSize = _entries.size();
  for (int i = 0; i < entriesSize; i++) {
    TileCacheEntry* entry = _entries[i];
    Tile* tile = entry->_tile;
    
    if ((tile->getLevel()  == level ) &&
        (tile->getRow()    == row   ) &&
        (tile->getColumn() == column)) {
      entry->_timestamp = _tsCounter++;
      
      return tile;
    }
  }
  
  return NULL;
}


void TilesCache::putTile(Tile* tile) {
  TileCacheEntry* newEntry = new TileCacheEntry(tile, _tsCounter++);
  
  const int entriesSize = _entries.size();
  if (entriesSize < _maxElements) {
    _entries.push_back(newEntry);
  }
  else {
    int loserI = 0;
    TileCacheEntry* loserEntry = _entries[0];
    
    for (int i = 1; i < entriesSize; i++) {
      TileCacheEntry* entry = _entries[i];
      if (entry->_timestamp < loserEntry->_timestamp) {
        loserEntry = entry;
        loserI = i;
      }
    }
    
    _entries[loserI] = newEntry;
    
    _tileRenderer->tileDeleted(loserEntry->_tile);
    
    delete loserEntry;
  }
}



TileRenderer::~TileRenderer() {
  clearTopLevelTiles();
  
  delete _tessellator;
  delete _texturizer;
  delete _parameters;
  delete _tilesCache;
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
      
      Tile* tile = new Tile(sector, _parameters->_topLevel, row, col, NULL);
      _topLevelTiles.push_back(tile);
    }
  }
  
  ic->getLogger()->logInfo("Created %i top level tiles", _topLevelTiles.size());
}

void TileRenderer::initialize(const InitializationContext* ic) {
  clearTopLevelTiles();
  createTopLevelTiles(ic);
}


int TileRenderer::render(const RenderContext* rc) {
  IGL *gl = rc->getGL();
  
  gl->enablePolygonOffset(5, 5);

  int ___dgd_at_work;
  
  const int topLevelTilesSize = _topLevelTiles.size();
  for (int i = 0; i < topLevelTilesSize; i++) {
    Tile* tile = _topLevelTiles[i];
    tile->render(rc, _tessellator, _texturizer, _parameters, _tilesCache);
  }
  
  gl->disablePolygonOffset();
  
  return MAX_TIME_TO_RENDER;
}

void TileRenderer::tileDeleted(Tile* tile) {
  
}
