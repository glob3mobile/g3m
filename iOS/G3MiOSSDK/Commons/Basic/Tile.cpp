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
  if (_tessellatorMesh != NULL) {
    delete _tessellatorMesh; 
  }
  
  if (_texturizerMesh != NULL) {
    delete _texturizerMesh; 
  }
}


Mesh* Tile::getMesh(const RenderContext* rc,
                    const TileTessellator* tessellator) {
  if (_tessellatorMesh == NULL) {
    _tessellatorMesh = tessellator->createMesh(rc, this);
  }
  return _tessellatorMesh;
}

bool Tile::isVisible(const RenderContext *rc, const TileTessellator *tessellator) 
{
  return getMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->getFrustumInModelCoordinates());
  //return getMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->_halfFrustumInModelCoordinates);
}

bool Tile::meetsRenderCriteria(const RenderContext *rc,
                               const TileParameters* parameters) {
  
  int __agustin_at_work;
  
  if (_level==2 && _sector.contains(Geodetic2D(Angle::fromDegrees(0.1), Angle::fromDegrees(0.1)))) {
    unsigned int projectedSize = _tessellatorMesh->getExtent()->projectedSize(rc);
  }
  
  
  
  
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
  Mesh* mesh = getMesh(rc, tessellator);
  
  if (mesh != NULL) {
//    if (!isTextureSolved()) {
//      mesh = texturizer->texturize(rc, this, mesh);
//    }
    
    if (!isTextureSolved() || _texturizerMesh == NULL) {
      _texturizerMesh = texturizer->texturize(rc, this, mesh, _texturizerMesh);
    }
    
    if (_texturizerMesh != NULL) {
      _texturizerMesh->render(rc);
    }
    else {
      if (mesh != NULL) {
        mesh->render(rc);
      }
    }
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
      std::vector<Tile*> subTiles = createSubTiles(tilesCache);
      for (int i = 0; i < subTiles.size(); i++) {
        Tile* subTile = subTiles[i];
        subTile->render(rc, tessellator, texturizer, parameters, tilesCache);
      }
    }
  }
}

Tile* Tile::createSubTile(TilesCache* tilesCache,
                          const Angle& lowerLat, const Angle& lowerLon,
                          const Angle& upperLat, const Angle& upperLon,
                          const int level,
                          const int row, const int column,
                          Tile* fallbackTextureTile) {
  
  Tile* tile = tilesCache->getTile(level, row, column);
  if (tile != NULL) {
    return tile;
  }
  
  tile = new Tile(Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                  level,
                  row, column,
                  fallbackTextureTile);
  
  tilesCache->putTile(tile);
  
  return tile;
}

std::vector<Tile*> Tile::createSubTiles(TilesCache* tilesCache) {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const Angle lat0 = lower.latitude();
  const Angle lat2 = upper.latitude();
  const Angle lat1 = Angle::midAngle(lat0, lat2);
  
  const Angle lon0 = lower.longitude();
  const Angle lon2 = upper.longitude();
  const Angle lon1 = Angle::midAngle(lon0, lon2);
  
  const int nextLevel = _level + 1;
  Tile* fallbackTextureTile = isTextureSolved() ? this : _fallbackTextureTile;
  
  std::vector<Tile*> subTiles(4);
  subTiles[0] = createSubTile(tilesCache, lat0, lon0, lat1, lon1, nextLevel, 2 * _row    , 2 * _column    , fallbackTextureTile);
  subTiles[1] = createSubTile(tilesCache, lat0, lon1, lat1, lon2, nextLevel, 2 * _row    , 2 * _column + 1, fallbackTextureTile);
  subTiles[2] = createSubTile(tilesCache, lat1, lon0, lat2, lon1, nextLevel, 2 * _row + 1, 2 * _column    , fallbackTextureTile);
  subTiles[3] = createSubTile(tilesCache, lat1, lon1, lat2, lon2, nextLevel, 2 * _row + 1, 2 * _column + 1, fallbackTextureTile);
  
  return subTiles;
}
