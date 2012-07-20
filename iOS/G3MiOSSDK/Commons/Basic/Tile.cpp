//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Tile.hpp"
#include "Mesh.hpp"
#include "Camera.hpp"

#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "TileRenderer.hpp"

Tile::~Tile() {
  prune(NULL);
  
  if (_debugMesh != NULL) {
    delete _debugMesh;
  }
  
  if (_tessellatorMesh != NULL) {
    delete _tessellatorMesh; 
  }
  
  if (_texturizerMesh != NULL) {
    delete _texturizerMesh; 
  }
}

void Tile::setTextureSolved(bool textureSolved) {
  _textureSolved = textureSolved;
}

Mesh* Tile::getTessellatorMesh(const RenderContext* rc,
                               const TileTessellator* tessellator) {
  if (_tessellatorMesh == NULL) {
    _tessellatorMesh = tessellator->createMesh(rc, this);
  }
  return _tessellatorMesh;
}


Mesh* Tile::getDebugMesh(const RenderContext* rc,
                         const TileTessellator* tessellator) {
  if (_debugMesh == NULL) {
    _debugMesh = tessellator->createDebugMesh(rc, this);
  }
  return _debugMesh;
}


bool Tile::isVisible(const RenderContext *rc,
                     const TileTessellator *tessellator) {
  
  if (_sector.isBackOriented(rc)) {
    return false; 
  }
  
  return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->getFrustumInModelCoordinates());
//  return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->_halfFrustumInModelCoordinates);
}

bool Tile::meetsRenderCriteria(const RenderContext *rc,
                               const TileTessellator *tessellator,
                               TileTexturizer *texturizer,
                               const TileParameters* parameters) {
  
  
  if (_level >= parameters->_maxLevel) {
    return true;
  }
  
//  if ( timer->elapsedTime().milliseconds() > 100 ) {
//    return true;
//  }

  if (texturizer != NULL) {
    if (texturizer->tileMeetsRenderCriteria(this)) {
      return true;
    }
  }
  
//  int projectedSize = getTessellatorMesh(rc, tessellator)->getExtent()->squaredProjectedArea(rc);
//  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
//    return true;
//  }
  const Vector2D extent = getTessellatorMesh(rc, tessellator)->getExtent()->projectedExtent(rc);
//  const double t = extent.maxAxis();
  const double t = (extent.x() + extent.y()) / 2;
  if (t <= ((parameters->_tileTextureWidth + parameters->_tileTextureHeight) / 2 * 1.5)) {
    return true;
  }

  
  return false;
}

void Tile::rawRender(const RenderContext *rc,
                     const TileTessellator *tessellator,
                     TileTexturizer *texturizer) {
  
  Mesh* tessellatorMesh = getTessellatorMesh(rc, tessellator);
  
  if (tessellatorMesh != NULL) {
    if (texturizer == NULL) {
      tessellatorMesh->render(rc);
    }
    else {
      if (!isTextureSolved() || _texturizerMesh == NULL) {
        if ((_texturedCounter++ % 10) == 0) {
          _texturizerMesh = texturizer->texturize(rc, this, tessellatorMesh, _texturizerMesh);
          _texturedCounter = 0;
        }
      }
      
      if (_texturizerMesh != NULL) {
        _texturizerMesh->render(rc);
      }
      else {
        tessellatorMesh->render(rc);
      }
    }
  }
  
}

void Tile::debugRender(const RenderContext* rc,
                       const TileTessellator* tessellator) {
  Mesh* debugMesh = getDebugMesh(rc, tessellator);
  if (debugMesh != NULL) {
    debugMesh->render(rc);
  }
}

std::vector<Tile*>* Tile::getSubTiles() {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles();
  }
  return _subtiles;
}

void Tile::prune(TileTexturizer* texturizer) {
  if (_subtiles != NULL) {
    
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      
      subtile->prune(texturizer);
      if (texturizer != NULL) {
        texturizer->tileToBeDeleted(subtile);
      }
      delete subtile;
    }
    
    delete _subtiles;
    _subtiles = NULL;
    
  }
}

void Tile::render(const RenderContext* rc,
                  const TileTessellator* tessellator,
                  TileTexturizer* texturizer,
                  const TileParameters* parameters,
                  TilesStatistics* statistics) {
  if (isVisible(rc, tessellator)) {
    if (meetsRenderCriteria(rc, tessellator, texturizer, parameters)) {
      rawRender(rc, tessellator, texturizer);
      if (parameters->_renderDebug) {
        debugRender(rc, tessellator);
      }
      
      prune(texturizer);
      
      statistics->computeTile(this);
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles();
      const int subTilesSize = subTiles->size();
      for (int i = 0; i < subTilesSize; i++) {
        Tile* subTile = subTiles->at(i);
        subTile->render(rc, tessellator, texturizer, parameters, statistics);
      }
    }
  }
  else {
    prune(texturizer);
  }
}

Tile* Tile::createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                          const Angle& upperLat, const Angle& upperLon,
                          const int level,
                          const int row, const int column) {
  return new Tile(this,
                  Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                  level,
                  row, column);
}

std::vector<Tile*>* Tile::createSubTiles() {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const Angle midLat = Angle::midAngle(lower.latitude(), upper.latitude());
  const Angle midLon = Angle::midAngle(lower.longitude(), upper.longitude());
  
  const int nextLevel = _level + 1;
  
//  Tile* fallbackTextureTile = isTextureSolved() ? this : _fallbackTextureTile;
  
  std::vector<Tile*>* subTiles = new std::vector<Tile*>();
  subTiles->push_back( createSubTile(lower.latitude(), lower.longitude(),
                                     midLat, midLon,
                                     nextLevel,
                                     2 * _row,
                                     2 * _column ) );
  
  subTiles->push_back( createSubTile(lower.latitude(), midLon,
                                     midLat, upper.longitude(),
                                     nextLevel,
                                     2 * _row,
                                     2 * _column + 1 ) );
  
  subTiles->push_back( createSubTile(midLat, lower.longitude(),
                                     upper.latitude(), midLon,
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column ) );
  
  subTiles->push_back( createSubTile(midLat, midLon,
                                     upper.latitude(), upper.longitude(),
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column + 1) );
  
  return subTiles;
}
