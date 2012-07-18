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
  
  if (_tessellatorMesh != NULL) {
    delete _tessellatorMesh; 
  }
  
  if (_texturizerMesh != NULL) {
    delete _texturizerMesh; 
  }
}

void Tile::setTextureSolved(bool textureSolved) {
  _textureSolved = textureSolved;
  
  if (textureSolved) {
    if (_subtiles != NULL) {
      for (int i = 0; i < _subtiles->size(); i++) {
        Tile* subtile = _subtiles->at(i);
        subtile->setFallbackTextureTile(this);
      }
    }
  }
}

void Tile::setFallbackTextureTile(Tile* fallbackTextureTile) {
  _fallbackTextureTile = fallbackTextureTile;
  
  if (_subtiles != NULL) {
    if (!isTextureSolved()) {
      for (int i = 0; i < _subtiles->size(); i++) {
        Tile* subtile = _subtiles->at(i);
        subtile->setFallbackTextureTile(fallbackTextureTile);
      }
    }
  }
}



Mesh* Tile::getTessellatorMesh(const RenderContext* rc,
                               const TileTessellator* tessellator) {
  if (_tessellatorMesh == NULL) {
    _tessellatorMesh = tessellator->createMesh(rc, this);
  }
  return _tessellatorMesh;
}

bool Tile::isVisible(const RenderContext *rc,
                     const TileTessellator *tessellator) {
  //  return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->getFrustumInModelCoordinates());
  return getTessellatorMesh(rc, tessellator)->getExtent()->touches(rc->getCamera()->_halfFrustumInModelCoordinates);
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
  
  Mesh* tessellatorMesh = getTessellatorMesh(rc, tessellator);
  
  if (tessellatorMesh != NULL) {
    if (texturizer == NULL) {
      tessellatorMesh->render(rc);
    }
    else {
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
  
}

std::vector<Tile*>* Tile::getSubTiles() {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles();
  }
  return _subtiles;
}

void Tile::prune(TileTexturizer* texturizer) {
  if (_subtiles != NULL) {
    for (int i = 0; i < _subtiles->size(); i++) {
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
                  const TileParameters* parameters) {
  int ___diego_at_work;
  
  if (isVisible(rc, tessellator)) {
    if (meetsRenderCriteria(rc, parameters)) {
      rawRender(rc, tessellator, texturizer);
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles();
      for (int i = 0; i < 4 /* subTiles->size() */; i++) {
        Tile* subTile = subTiles->at(i);
        subTile->render(rc, tessellator, texturizer, parameters);
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
                          const int row, const int column,
                          Tile* fallbackTextureTile) {
  return new Tile(Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                  level,
                  row, column,
                  fallbackTextureTile);
}

std::vector<Tile*>* Tile::createSubTiles() {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const Angle midLat = Angle::midAngle(lower.latitude(), upper.latitude());
  const Angle midLon = Angle::midAngle(lower.longitude(), upper.longitude());
  
  const int nextLevel = _level + 1;
  
  Tile* fallbackTextureTile = isTextureSolved() ? this : _fallbackTextureTile;
  
  std::vector<Tile*>* subTiles = new std::vector<Tile*>();
  subTiles->push_back( createSubTile(lower.latitude(), lower.longitude(),
                                     midLat, midLon,
                                     nextLevel,
                                     2 * _row,
                                     2 * _column,
                                     fallbackTextureTile) );
  
  subTiles->push_back( createSubTile(lower.latitude(), midLon,
                                     midLat, upper.longitude(),
                                     nextLevel,
                                     2 * _row,
                                     2 * _column + 1,
                                     fallbackTextureTile) );
  
  subTiles->push_back( createSubTile(midLat, lower.longitude(),
                                     upper.latitude(), midLon,
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column,
                                     fallbackTextureTile) );
  
  subTiles->push_back( createSubTile(midLat, midLon,
                                     upper.latitude(), upper.longitude(),
                                     nextLevel,
                                     2 * _row + 1,
                                     2 * _column + 1,
                                     fallbackTextureTile) );
  
  return subTiles;
}
