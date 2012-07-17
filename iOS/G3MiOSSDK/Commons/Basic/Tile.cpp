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
  
  prune();

  if (_tessellatorMesh != NULL) {
    delete _tessellatorMesh; 
  }
  
  if (_texturizerMesh != NULL) {
    delete _texturizerMesh; 
  }
  
  if (_bbox != NULL) {
   delete _bbox; 
  }
}


Mesh* Tile::getTessellatorMesh(const RenderContext* rc,
                               const TileTessellator* tessellator) {
  if (_tessellatorMesh == NULL) {
    _tessellatorMesh = tessellator->createMesh(rc, this);
  }
  return _tessellatorMesh;
}

bool Tile::isVisible(const RenderContext *rc, const TileTessellator *tessellator) 
{
  //return getMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->getFrustumInModelCoordinates());
  return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->_halfFrustumInModelCoordinates);
}

bool Tile::meetsRenderCriteria(const RenderContext *rc,
                               const TileParameters* parameters) {
  
  if (_level >= parameters->_maxLevel) {
    return true;
  }
  
//  31890685.000000
//   7083288.848839
  
//  const Vector3D radii = rc->getPlanet()->getRadii();
//  const double rad = (radii.x() + radii.y() + radii.z()) / 3;
//  
//  const double ratio = (distanceToCamera - rad) / rad;
//  
//  rc->getLogger()->logInfo("Distance to camera: %f - %f", distanceToCamera, ratio);
  
//  const Vector3D center = rc->getPlanet()->toVector3D(_sector.getCenter());
//  
//  const double distanceToCamera = rc->getCamera()->getPos().sub(center).length();
//  rc->getLogger()->logInfo("Distance to camera: %f", distanceToCamera);
  
  return _level >= 2;
}

void Tile::rawRender(const RenderContext *rc,
                     const TileTessellator *tessellator,
                     TileTexturizer *texturizer) {
  Mesh* tessellatorMesh = getTessellatorMesh(rc, tessellator);
  
  if (tessellatorMesh != NULL) {
    
    if (!isTextureSolved() || _texturizerMesh == NULL) {
      _texturizerMesh = texturizer->texturize(rc, this, tessellatorMesh, _texturizerMesh);
    }
    
    if (_texturizerMesh != NULL) {
      _texturizerMesh->render(rc);
    }
    else {
      tessellatorMesh->render(rc);
    }
  }
}

std::vector<Tile*>* Tile::getSubTiles(TilesCache *tilesCache) {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles(tilesCache, false);
  }
  return _subtiles;
}

void Tile::prune() {
  if (_subtiles != NULL) {
    for (int i = 0; i < _subtiles->size(); i++) {
      Tile* subtile = _subtiles->at(i);
      
      subtile->prune();
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
                  TilesCache* tilesCache) {
  int ___diego_at_work;
  
  if (isVisible(rc, tessellator)) {
    if (meetsRenderCriteria(rc, parameters)) {
      rawRender(rc, tessellator, texturizer);
    }
    else {
//      std::vector<Tile*> subTiles = createSubTiles(tilesCache);
//      for (int i = 0; i < subTiles.size(); i++) {
//        Tile* subTile = subTiles[i];
//        subTile->render(rc, tessellator, texturizer, parameters, tilesCache);
//      }
      std::vector<Tile*>* subTiles = getSubTiles(tilesCache);
      for (int i = 0; i < subTiles->size(); i++) {
        Tile* subTile = subTiles->at(i);
        subTile->render(rc, tessellator, texturizer, parameters, tilesCache);
      }

    }
  }
  else {
    prune();
  }
}

Tile* Tile::createSubTile(TilesCache* tilesCache,
                          const Angle& lowerLat, const Angle& lowerLon,
                          const Angle& upperLat, const Angle& upperLon,
                          const int level,
                          const int row, const int column,
                          Tile* fallbackTextureTile,
                          bool useCache) {
  
  if (!useCache) {
    return new Tile(Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                    level,
                    row, column,
                    fallbackTextureTile);
  }
  
  Tile* tile = tilesCache->getTile(level, row, column);
  if (tile == NULL) {
    tile = new Tile(Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                    level,
                    row, column,
                    fallbackTextureTile);
    
    tilesCache->putTile(tile);
  }
  else {
    tile->setFallbackTextureTile(fallbackTextureTile);
  }
  
  return tile;
}

std::vector<Tile*>* Tile::createSubTiles(TilesCache* tilesCache,
                                         bool useCache) {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const Angle midLat = Angle::midAngle(lower.latitude(), upper.latitude());
  const Angle midLon = Angle::midAngle(lower.longitude(), upper.longitude());
  
  const int nextLevel = _level + 1;
  
  Tile* fallbackTextureTile = isTextureSolved() ? this : _fallbackTextureTile;
  
//  std::vector<Tile*> subTiles(4);
//  subTiles[0] = createSubTile(tilesCache,
//                              lower.latitude(), lower.longitude(),
//                              midLat, midLon,
//                              nextLevel,
//                              2 * _row,
//                              2 * _column,
//                              fallbackTextureTile,
//                              useCache);
//  
//  subTiles[1] = createSubTile(tilesCache,
//                              lower.latitude(), midLon,
//                              midLat, upper.longitude(),
//                              nextLevel,
//                              2 * _row,
//                              2 * _column + 1,
//                              fallbackTextureTile,
//                              useCache);
//  
//  subTiles[2] = createSubTile(tilesCache,
//                              midLat, lower.longitude(),
//                              upper.latitude(), midLon,
//                              nextLevel,
//                              2 * _row + 1,
//                              2 * _column,
//                              fallbackTextureTile,
//                              useCache);
//  
//  subTiles[3] = createSubTile(tilesCache,
//                              midLat, midLon,
//                              upper.latitude(), upper.longitude(),
//                              nextLevel,
//                              2 * _row + 1,
//                              2 * _column + 1,
//                              fallbackTextureTile,
//                              useCache);
//  
//  return subTiles;
  
  
  std::vector<Tile*>* subTiles = new std::vector<Tile*>();
  subTiles->push_back( createSubTile(tilesCache,
                                     lower.latitude(), lower.longitude(),
                                     midLat, midLon,
                                     nextLevel,
                                     2 * _row,
                                     2 * _column,
                                     fallbackTextureTile,
                                     useCache) );
  
  subTiles->push_back( createSubTile(tilesCache,
                                     lower.latitude(), midLon,
                                     midLat, upper.longitude(),
                                     nextLevel,
                                     2 * _row,
                                     2 * _column + 1,
                                     fallbackTextureTile,
                                     useCache) );
  
  subTiles->push_back( createSubTile(tilesCache,
                                     midLat, lower.longitude(),
                                     upper.latitude(), midLon,
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column,
                                     fallbackTextureTile,
                                     useCache) );
  
  subTiles->push_back( createSubTile(tilesCache,
                                     midLat, midLon,
                                     upper.latitude(), upper.longitude(),
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column + 1,
                                     fallbackTextureTile,
                                     useCache) );
  
  return subTiles;

  
  
}
