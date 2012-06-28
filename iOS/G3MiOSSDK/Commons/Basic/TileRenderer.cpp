//
//  TileRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "TileRenderer.hpp"
#include "Tile.hpp"


TileRenderer::~TileRenderer() {
  clearTopTiles();
  
  delete _tessellator;
}

void TileRenderer::clearTopTiles() {
  for (int i = 0; i < _topTiles.size(); i++) {
    Tile* tile = _topTiles[i];
    delete tile;
  }
  
  _topTiles.clear();
}

void TileRenderer::createTopTiles(const InitializationContext* ic) {
  const Sector topSector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
                         Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
  const int splitsByLatitude = 2;
  const int splitsByLongitude = 4;
  const int topLevel = 0;
  
  
  const Angle fromLatitude = topSector.lower().latitude();
  const Angle fromLongitude = topSector.lower().longitude();
  
  const Angle deltaLan = topSector.getDeltaLatitude();
  const Angle deltaLon = topSector.getDeltaLongitude();
  
  const Angle tileHeight = deltaLan.div(splitsByLatitude);
  const Angle tileWidth = deltaLon.div(splitsByLongitude);
  
  for (int row = 0; row < splitsByLatitude; row++) {
    const Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
    const Angle tileLatTo = tileLatFrom.add(tileHeight);
    
    for (int col = 0; col < splitsByLongitude; col++) {
      const Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
      const Angle tileLonTo = tileLonFrom.add(tileWidth);
      
      //      ic->getLogger()->logInfo("row=%i, col=%i, from=(%f,%f) to=(%f, %f)",
      //                               row, col,
      //                               tileLatFrom.degrees(), tileLonFrom.degrees(),
      //                               tileLatTo.degrees(), tileLonTo.degrees()
      //                               );
      
      const Geodetic2D tileLower(tileLatFrom, tileLonFrom);
      const Geodetic2D tileUpper(tileLatTo, tileLonTo);
      const Sector sector(tileLower, tileUpper);
      
      Tile* tile = new Tile(sector, topLevel, row, col);
      _topTiles.push_back(tile);
    }
  }
  
}

void TileRenderer::initialize(const InitializationContext* ic)
{
  clearTopTiles();
  
  createTopTiles(ic);
}  




int TileRenderer::render(const RenderContext* rc)
{
  IGL *gl = rc->getGL();
  gl->enableVertices();
  
  for (int i = 0; i < _topTiles.size(); i++) {
    Tile* tile = _topTiles[i];
    tile->render(rc, _tessellator);
  }
  
  gl->disableVertices();
  
  return MAX_TIME_TO_RENDER;
}
