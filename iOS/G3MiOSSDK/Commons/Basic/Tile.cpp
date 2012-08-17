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

//static long visibleCounter = 0;

Tile::~Tile() {
  if (_isVisible) {
    deleteTexturizerMesh();
  }
  
  prune(NULL);
  
  //  if (_isVisible) {
  //    visibleCounter--;
  //    printf("**** Tile %s is DESTROYED (visibles=%ld)\n",
  //           getKey().description().c_str(),
  //           visibleCounter);
  //  }
  
  if (_texturizerData != NULL) {
    delete _texturizerData;
    _texturizerData = NULL;
  }
  
  if (_texturizerTimer != NULL) {
    delete _texturizerTimer;
  }
  
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
  
  /*
   // test if sector is back oriented with respect to the camera
   if (_sector.isBackOriented(rc)) {
   return false;
   }
   */
  
  return getTessellatorMesh(rc, trc)->getExtent()->touches(rc->getNextCamera()->getFrustumInModelCoordinates());
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
  
  
  //  const double projectedSize = getTessellatorMesh(rc, trc)->getExtent()->squaredProjectedArea(rc);
  //  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
  //    return true;
  //  }
  const Vector2D extent = getTessellatorMesh(rc, trc)->getExtent()->projectedExtent(rc);
  //const double t = extent.maxAxis() * 2;
  const double t = (extent.x() + extent.y());
  if ( t <= ((parameters->_tileTextureWidth + parameters->_tileTextureHeight) * 1.75) ) {
    return true;
  }
  
  
  int __TODO_tune_render_budget;
  if (_subtiles == NULL) { // the tile needs to create the subtiles
    if (trc->getStatistics()->getSplitsCountInFrame() > 1) {
      // there are not more splitsCount-budget to spend
      return true;
    }
    
    if (trc->getLastSplitTimer()->elapsedTime().milliseconds() < 50) {
      // there are not more time-budget to spend
      return true;
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
      
      const bool needsToCallTexturizer = (!isTextureSolved() || (_texturizerMesh == NULL)) && isTexturizerDirty();
      
      if (needsToCallTexturizer) {
        int __TODO_tune_render_budget;
        
        //                               (_texturizerTimer->elapsedTime().milliseconds() > 125 &&
        //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
        //                               (_texturizerTimer->elapsedTime().milliseconds() > 125 &&
        //                                lastTexturizerTimer->elapsedTime().milliseconds() > 50));
        //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
        //                                     (_texturizerTimer->elapsedTime().milliseconds() > 100 &&
        //                                      lastTexturizerTimer->elapsedTime().milliseconds() > 10));
        //        const bool callTexturizer = ((_texturizerTimer == NULL) ||
        //                                     (_texturizerTimer->elapsedTime().milliseconds() > 100));
        const bool callTexturizer = ((_texturizerTimer == NULL) ||
                                     (_texturizerTimer->elapsedTime().milliseconds() > 50)) && isTexturizerDirty();
        
        if (callTexturizer) {
          _texturizerMesh = texturizer->texturize(rc,
                                                  trc,
                                                  this,
                                                  tessellatorMesh,
                                                  _texturizerMesh);
          trc->getLastTexturizerTimer()->start();
          
          if (_texturizerTimer == NULL) {
            _texturizerTimer = rc->getFactory()->createTimer();
          }
          else {
            _texturizerTimer->start();
          }
        }
      }
      
      if ((_texturizerTimer != NULL) && isTextureSolved()) {
        delete _texturizerTimer;
        _texturizerTimer = NULL;
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

void Tile::prune(const TileRenderContext* trc) {
  if (_subtiles != NULL) {
    
    TileTexturizer* texturizer = (trc == NULL) ? NULL : trc->getTexturizer();
    
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      
      int ___TESTING;
      subtile->setIsVisible(false);
      
      subtile->prune(trc);
      if (texturizer != NULL) {
        texturizer->tileToBeDeleted(subtile, subtile->_texturizerMesh);
      }
      delete subtile;
    }
    
    delete _subtiles;
    _subtiles = NULL;
    
  }
}

void Tile::setIsVisible(bool isVisible) {
  
  
  if (_isVisible != isVisible) {
    _isVisible = isVisible;
    
    if (_isVisible) {
      //      visibleCounter++;
      //      printf("**** Tile %s becomed Visible (visibles=%ld)\n",
      //             getKey().description().c_str(),
      //             visibleCounter);
    }
    else {
      //      visibleCounter--;
      //      printf("**** Tile %s becomed INVisible (visibles=%ld)\n",
      //             getKey().description().c_str(),
      //             visibleCounter);
      deleteTexturizerMesh();
    }
  }
}

void Tile::deleteTexturizerMesh() {
  if ((_level > 0) && (_texturizerMesh != NULL)) {
    int _BIG_BANG;
    _texturizer->tileMeshToBeDeleted(this, _texturizerMesh);
    
    delete _texturizerMesh;
    _texturizerMesh = NULL;
    
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
    setIsVisible(true);
    
    statistics->computeVisibleTile(this);
    
    if (meetsRenderCriteria(rc, trc)) {
      rawRender(rc, trc);
      if (trc->getParameters()->_renderDebug) {
        debugRender(rc, trc);
      }
      
      statistics->computeTileRendered(this);
      
      prune(trc);
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles();
      if (_justCreatedSubtiles) {
        trc->getLastSplitTimer()->start();
        statistics->computeSplit();
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
    setIsVisible(false);
    
    prune(trc);
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

Geodetic3D Tile::intersection(const Vector3D& origin,
                              const Vector3D& ray,
                              const Planet* planet) const {
    //As our tiles are still flat our onPlanet vector is calculated directly from Planet
  std::vector<double> ts = planet->intersections(origin, ray);
  
  if (ts.size() > 0) {
    const Vector3D onPlanet = origin.add(ray.times(ts[0]));
    const Geodetic3D g = planet->toGeodetic3D(onPlanet);
    
    if (_sector.contains(g)) {
      //If this tile is not a leaf
      if (_subtiles != NULL) {
        for (int i = 0; i < _subtiles->size(); i++) {
          const Geodetic3D g3d = _subtiles->at(i)->intersection(origin, ray, planet);
          if (!g3d.isNan()) {
            return g3d;
          }
        }
      }
      else {
        //printf("TOUCH TILE %d\n", _level);
        _texturizer->onTerrainTouchEvent(g, this);
        return g;
      }
    }
  }

  return Geodetic3D::nan();
}
