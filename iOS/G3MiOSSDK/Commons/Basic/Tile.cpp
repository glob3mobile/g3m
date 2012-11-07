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
#include "ITimer.hpp"
#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "TileRenderer.hpp"
#include "TilesRenderParameters.hpp"
#include "TileKey.hpp"

Tile::Tile(TileTexturizer* texturizer,
           Tile* parent,
           const Sector& sector,
           int level,
           int row,
           int column):
_texturizer(texturizer),
_parent(parent),
_sector(sector),
_level(level),
_row(row),
_column(column),
_tessellatorMesh(NULL),
_debugMesh(NULL),
_texturizedMesh(NULL),
_textureSolved(false),
_texturizerDirty(true),
_subtiles(NULL),
_justCreatedSubtiles(false),
_isVisible(false),
_texturizerData(NULL)
{
  //  int __remove_tile_print;
  //  printf("Created tile=%s\n deltaLat=%s deltaLon=%s\n",
  //         getKey().description().c_str(),
  //         _sector.getDeltaLatitude().description().c_str(),
  //         _sector.getDeltaLongitude().description().c_str()
  //         );
}

Tile::~Tile() {
//  if (_isVisible) {
//    deleteTexturizedMesh();
//  }
  
  prune(NULL);
  
  delete _debugMesh;
  
  delete _tessellatorMesh;
  
#ifdef C_CODE
    delete _texturizerData;
#endif
    _texturizerData = NULL;
  
  delete _texturizedMesh;
}

void Tile::ancestorTexturedSolvedChanged(Tile* ancestor,
                                         bool textureSolved) {
  if (textureSolved && isTextureSolved()) {
    return;
  }
  
  if (_texturizer != NULL) {
    _texturizer->ancestorTexturedSolvedChanged(this, ancestor, textureSolved);
  }
  
  if (_subtiles != NULL) {
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      subtile->ancestorTexturedSolvedChanged(ancestor, textureSolved);
    }
  }
}

void Tile::setTextureSolved(bool textureSolved) {
  if (textureSolved != _textureSolved) {
    _textureSolved = textureSolved;
    
    if (_subtiles != NULL) {
      const int subtilesSize = _subtiles->size();
      for (int i = 0; i < subtilesSize; i++) {
        Tile* subtile = _subtiles->at(i);
        subtile->ancestorTexturedSolvedChanged(this, _textureSolved);
      }
    }
  }
}

Mesh* Tile::getTessellatorMesh(const RenderContext* rc,
                               const TileRenderContext* trc) {
  if (_tessellatorMesh == NULL) {
    _tessellatorMesh = trc->getTessellator()->createMesh(rc, this);
  }
  return _tessellatorMesh;
}

Mesh* Tile::getDebugMesh(const RenderContext* rc,
                         const TileRenderContext* trc) {
  if (_debugMesh == NULL) {
    _debugMesh = trc->getTessellator()->createDebugMesh(rc, this);
  }
  return _debugMesh;
}

bool Tile::isVisible(const RenderContext *rc,
                     const TileRenderContext* trc) {
  // test if sector is back oriented with respect to the camera
  //  if (_sector.isBackOriented(rc)) {
  //    return false;
  //  }
  
  Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
  if (extent == NULL) {
    return false;
  }
  return extent->touches( rc->getCurrentCamera()->getFrustumInModelCoordinates() );
  //return extent->touches( rc->getCurrentCamera()->getHalfFrustuminModelCoordinates() );
}

bool Tile::meetsRenderCriteria(const RenderContext *rc,
                               const TileRenderContext* trc) {
  const TilesRenderParameters* parameters = trc->getParameters();
  
  if (_level >= parameters->_maxLevel) {
    return true;
  }
  
  //  if (timer != NULL) {
  //    if ( timer->elapsedTime().milliseconds() > 50 ) {
  //      return true;
  //    }
  //  }
  
  TileTexturizer* texturizer = trc->getTexturizer();
  if (texturizer != NULL) {
    if (texturizer->tileMeetsRenderCriteria(this)) {
      return true;
    }
  }
  
  Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
  if (extent == NULL) {
    return true;
  }
  //  const double projectedSize = extent->squaredProjectedArea(rc);
  //  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
  //    return true;
  //  }
  const Vector2I ex = extent->projectedExtent(rc);
  //const double t = extent.maxAxis() * 2;
  const int t = (ex._x + ex._y);
  if ( t <= ((parameters->_tileTextureWidth + parameters->_tileTextureHeight) * 1.75) ) {
    return true;
  }
  
  
  if (trc->getParameters()->_useTilesSplitBudget) {
    if (_subtiles == NULL) { // the tile needs to create the subtiles
      if (trc->getStatistics()->getSplitsCountInFrame() > 1) {
        // there are not more splitsCount-budget to spend
        return true;
      }
      
      if (trc->getLastSplitTimer()->elapsedTime().milliseconds() < 25) {
        // there are not more time-budget to spend
        return true;
      }
    }
  }
  
  return false;
}

void Tile::rawRender(const RenderContext *rc,
                     const TileRenderContext* trc) {
  
  Mesh* tessellatorMesh = getTessellatorMesh(rc, trc);
  
  TileTexturizer* texturizer = trc->getTexturizer();
  
  if (tessellatorMesh != NULL) {
    if (texturizer == NULL) {
      tessellatorMesh->render(rc);
    }
    else {
//      const bool needsToCallTexturizer = (!isTextureSolved() || (_texturizedMesh == NULL)) && isTexturizerDirty();
      const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();
      
      if (needsToCallTexturizer) {
        _texturizedMesh = texturizer->texturize(rc,
                                                trc,
                                                this,
                                                tessellatorMesh,
                                                _texturizedMesh);
      }
      
      if (_texturizedMesh != NULL) {
        _texturizedMesh->render(rc);
      }
      else {
        tessellatorMesh->render(rc);
      }
    }
  }
  
}

void Tile::debugRender(const RenderContext* rc,
                       const TileRenderContext* trc) {
  Mesh* debugMesh = getDebugMesh(rc, trc);
  if (debugMesh != NULL) {
    debugMesh->render(rc);
  }
}

std::vector<Tile*>* Tile::getSubTiles() {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles();
    _justCreatedSubtiles = true;
  }
  return _subtiles;
}

void Tile::prune(TileTexturizer* texturizer) {
  if (_subtiles != NULL) {
    
    //    printf("= pruned tile %s\n", getKey().description().c_str());
    
//    TileTexturizer* texturizer = (trc == NULL) ? NULL : trc->getTexturizer();

    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      
      subtile->setIsVisible(false, texturizer);
      
      subtile->prune(texturizer);
      if (texturizer != NULL) {
        texturizer->tileToBeDeleted(subtile, subtile->_texturizedMesh);
      }
      delete subtile;
    }
    
    delete _subtiles;
    _subtiles = NULL;
    
  }
}

void Tile::setIsVisible(bool isVisible,
                        TileTexturizer* texturizer) {
  if (_isVisible != isVisible) {
    _isVisible = isVisible;
    
    if (!_isVisible) {
      deleteTexturizedMesh(texturizer);
    }
  }
}

void Tile::deleteTexturizedMesh(TileTexturizer* texturizer) {
  if ((_level > 0) && (_texturizedMesh != NULL)) {
    
    if (texturizer != NULL) {
      texturizer->tileMeshToBeDeleted(this, _texturizedMesh);
    }
    
    delete _texturizedMesh;
    _texturizedMesh = NULL;
    
#ifdef C_CODE
    delete _texturizerData;
#endif
    _texturizerData = NULL;
    
    setTexturizerDirty(true);
    setTextureSolved(false);
  }
}

void Tile::render(const RenderContext* rc,
                  const TileRenderContext* trc,
                  std::list<Tile*>* toVisitInNextIteration) {
  TilesStatistics* statistics = trc->getStatistics();
  
  statistics->computeTileProcessed(this);
  if (isVisible(rc, trc)) {
    setIsVisible(true, trc->getTexturizer());
    
    statistics->computeVisibleTile(this);

    const bool isRawRender = (
                              (toVisitInNextIteration == NULL) ||
                              meetsRenderCriteria(rc, trc)     ||
                              (trc->getParameters()->_incrementalTileQuality && !_textureSolved)
                              );

    if (isRawRender) {
      rawRender(rc, trc);
      if (trc->getParameters()->_renderDebug) {
        debugRender(rc, trc);
      }
      
      statistics->computeTileRendered(this);
      
      prune(trc->getTexturizer());
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles();
      if (_justCreatedSubtiles) {
        trc->getLastSplitTimer()->start();
        statistics->computeSplitInFrame();
        _justCreatedSubtiles = false;
      }
      
      const int subTilesSize = subTiles->size();
      for (int i = 0; i < subTilesSize; i++) {
        Tile* subTile = subTiles->at(i);
        toVisitInNextIteration->push_back(subTile);
      }
    }
  }
  else {
    setIsVisible(false, trc->getTexturizer());
    
    prune(trc->getTexturizer());
  }
}

Tile* Tile::createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                          const Angle& upperLat, const Angle& upperLon,
                          const int level,
                          const int row, const int column) {
  return new Tile(_texturizer,
                  this,
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

const TileKey Tile::getKey() const {
  return TileKey(_level, _row, _column);
}

const Tile* Tile::getDeepestTileContaining(const Geodetic3D& position) const {
  if (_sector.contains(position)) {
    if (_subtiles == NULL) {
      return this;
    }
    else {
      for (int i = 0; i < _subtiles->size(); i++) {
        const Tile* subtile = _subtiles->at(i);
        const Tile* subtileResult = subtile->getDeepestTileContaining(position);
        if (subtileResult != NULL) {
          return subtileResult;
        }
      }
    }
  }
  
  return NULL;
}
