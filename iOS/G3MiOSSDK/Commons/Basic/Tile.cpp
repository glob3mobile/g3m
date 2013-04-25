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
#include "GLState.hpp"
#include "Box.hpp"
#include "ElevationDataProvider.hpp"
#include "MeshHolder.hpp"
#include "ElevationData.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "IStringBuilder.hpp"
#include "MercatorUtils.hpp"

#include "SubviewElevationData.hpp"

#include "TileElevationDataListener.hpp"

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
_texturizerData(NULL),
_tileExtent(NULL),
_elevationData(NULL),
_elevationDataLevel(-1),
_elevationDataListener(NULL),
_minHeight(0),
_maxHeight(0),
_verticalExaggeration(0),
_mustActualizeMeshDueToNewElevationData(false),
_lastTileMeshResolutionX(-1),
_lastTileMeshResolutionY(-1)
{
  //  int __remove_tile_print;
  //  printf("Created tile=%s\n deltaLat=%s deltaLon=%s\n",
  //         getKey().description().c_str(),
  //         _sector.getDeltaLatitude().description().c_str(),
  //         _sector.getDeltaLongitude().description().c_str()
  //         );
}

Tile::~Tile() {
  prune(NULL, NULL);
  
  delete _debugMesh;
  _debugMesh = NULL;
  
  delete _tessellatorMesh;
  _tessellatorMesh = NULL;
  
  delete _texturizerData;
  _texturizerData = NULL;
  
  delete _texturizedMesh;
  _texturizedMesh = NULL;
  
  delete _tileExtent;
  _tileExtent = NULL;
  
  delete _elevationData;
  _elevationData = NULL;
  
  if (_elevationDataListener != NULL){
    _elevationDataListener->cancelRequest();
    delete _elevationDataListener;
    _elevationDataListener = NULL;
    
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

Mesh* Tile::getTessellatorMesh(const G3MRenderContext* rc,
                               const TileRenderContext* trc) {
  
  const TileTessellator* tessellator = trc->getTessellator();
  const bool renderDebug = trc->getParameters()->_renderDebug;
  ElevationDataProvider* elevationDataProvider = trc->getElevationDataProvider();
  const Planet* planet = rc->getPlanet();
  
  const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
  const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);
  
  if (_elevationData == NULL && elevationDataProvider != NULL){
    initializeElevationData(elevationDataProvider, tessellator, tileMeshResolution, planet, renderDebug);
  }
  
  if ( _tessellatorMesh == NULL || _mustActualizeMeshDueToNewElevationData) {
    _mustActualizeMeshDueToNewElevationData = false;
    
    if (elevationDataProvider == NULL) {
      // no elevation data provider, just create a simple mesh without elevation
      _tessellatorMesh = tessellator->createTileMesh(planet,
                                                     tileMeshResolution,
                                                     this,
                                                     NULL,
                                                     _verticalExaggeration,
                                                     renderDebug);
    }
    else {
      if (_elevationData == NULL) {
        MeshHolder* meshHolder = new MeshHolder( tessellator->createTileMesh(planet,
                                                                             tileMeshResolution,
                                                                             this,
                                                                             NULL,
                                                                             _verticalExaggeration,
                                                                             renderDebug) );
        _tessellatorMesh = meshHolder;
        
      }
      else {
        Mesh* newMesh = tessellator->createTileMesh(planet,
                                                    tileMeshResolution,
                                                    this,
                                                    _elevationData,
                                                    _verticalExaggeration,
                                                    renderDebug);
        
        MeshHolder* meshHolder = (MeshHolder*)_tessellatorMesh;
        if (meshHolder == NULL){
          meshHolder = new MeshHolder(newMesh);
        } else{
          meshHolder->setMesh(newMesh);
        }
        _tessellatorMesh = meshHolder;
      }
    }
  }
  
  
  
  return _tessellatorMesh;
}

Mesh* Tile::getDebugMesh(const G3MRenderContext* rc,
                         const TileRenderContext* trc) {
  if (_debugMesh == NULL) {
    const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
    const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);
    
    //TODO: CHECK
    _debugMesh = trc->getTessellator()->createTileDebugMesh(rc->getPlanet(), tileMeshResolution, this);
  }
  return _debugMesh;
}

Extent* Tile::getTileExtent(const G3MRenderContext *rc) {
  if (_tileExtent == NULL) {
    const Planet* planet = rc->getPlanet();
    
    const double minHeight = getMinHeight() * _verticalExaggeration;
    const double maxHeight = getMaxHeight() * _verticalExaggeration;
    
    const Vector3D v0 = planet->toCartesian( _sector.getCenter(), maxHeight );
    const Vector3D v1 = planet->toCartesian( _sector.getNE(),     minHeight );
    const Vector3D v2 = planet->toCartesian( _sector.getNW(),     minHeight );
    const Vector3D v3 = planet->toCartesian( _sector.getSE(),     minHeight );
    const Vector3D v4 = planet->toCartesian( _sector.getSW(),     minHeight );
    
    double lowerX = v0._x;
    if (v1._x < lowerX) { lowerX = v1._x; }
    if (v2._x < lowerX) { lowerX = v2._x; }
    if (v3._x < lowerX) { lowerX = v3._x; }
    if (v4._x < lowerX) { lowerX = v4._x; }
    
    double upperX = v0._x;
    if (v1._x > upperX) { upperX = v1._x; }
    if (v2._x > upperX) { upperX = v2._x; }
    if (v3._x > upperX) { upperX = v3._x; }
    if (v4._x > upperX) { upperX = v4._x; }
    
    
    double lowerY = v0._y;
    if (v1._y < lowerY) { lowerY = v1._y; }
    if (v2._y < lowerY) { lowerY = v2._y; }
    if (v3._y < lowerY) { lowerY = v3._y; }
    if (v4._y < lowerY) { lowerY = v4._y; }
    
    double upperY = v0._y;
    if (v1._y > upperY) { upperY = v1._y; }
    if (v2._y > upperY) { upperY = v2._y; }
    if (v3._y > upperY) { upperY = v3._y; }
    if (v4._y > upperY) { upperY = v4._y; }
    
    
    double lowerZ = v0._z;
    if (v1._z < lowerZ) { lowerZ = v1._z; }
    if (v2._z < lowerZ) { lowerZ = v2._z; }
    if (v3._z < lowerZ) { lowerZ = v3._z; }
    if (v4._z < lowerZ) { lowerZ = v4._z; }
    
    double upperZ = v0._z;
    if (v1._z > upperZ) { upperZ = v1._z; }
    if (v2._z > upperZ) { upperZ = v2._z; }
    if (v3._z > upperZ) { upperZ = v3._z; }
    if (v4._z > upperZ) { upperZ = v4._z; }
    
    
    _tileExtent = new Box(Vector3D(lowerX, lowerY, lowerZ),
                          Vector3D(upperX, upperY, upperZ));
  }
  return _tileExtent;
}

bool Tile::isVisible(const G3MRenderContext *rc,
                     const TileRenderContext* trc) {
  // test if sector is back oriented with respect to the camera
  if (_sector.isBackOriented(rc, getMinHeight())) {
    return false;
  }
  
  const Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
  if (extent == NULL) {
    return false;
  }
  
  ////  const Extent* extent = getTileExtent(rc);
  //  const Extent* tileExtent = getTileExtent(rc);
  //  if (!tileExtent->fullContains(extent)) {
  //    printf("break point on me\n");
  //  }
  
  return extent->touches( rc->getCurrentCamera()->getFrustumInModelCoordinates() );
  //return extent->touches( rc->getCurrentCamera()->getHalfFrustuminModelCoordinates() );
}

bool Tile::meetsRenderCriteria(const G3MRenderContext *rc,
                               const TileRenderContext* trc) {
  //  const TilesRenderParameters* parameters = trc->getParameters();
  
  const LayerTilesRenderParameters* parameters = trc->getLayerTilesRenderParameters();
  
  if (_level >= parameters->_maxLevelForPoles) {
    if (_sector.touchesNorthPole() || _sector.touchesSouthPole()) {
      return true;
    }
  }
  
  if (_level >= parameters->_maxLevel) {
    return true;
  }
  
  TileTexturizer* texturizer = trc->getTexturizer();
  if (texturizer != NULL) {
    if (texturizer->tileMeetsRenderCriteria(this)) {
      return true;
    }
  }
  
  //const Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
  const Extent* extent = getTileExtent(rc);
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
  if ( t <= ((parameters->_tileTextureResolution._x + parameters->_tileTextureResolution._y) * 1.75) ) {
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

void Tile::prepareForFullRendering(const G3MRenderContext* rc,
                                   const TileRenderContext* trc) {
  Mesh* tessellatorMesh = getTessellatorMesh(rc, trc);
  if (tessellatorMesh == NULL) {
    return;
  }
  
  TileTexturizer* texturizer = trc->getTexturizer();
  if (texturizer != NULL) {
    const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();
    
    if (needsToCallTexturizer) {
      _texturizedMesh = texturizer->texturize(rc,
                                              trc,
                                              this,
                                              tessellatorMesh,
                                              _texturizedMesh);
    }
  }
}

void Tile::rawRender(const G3MRenderContext *rc,
                     const TileRenderContext* trc,
                     const GLState& parentState) {
  
  Mesh* tessellatorMesh = getTessellatorMesh(rc, trc);
  if (tessellatorMesh == NULL) {
    return;
  }
  
  TileTexturizer* texturizer = trc->getTexturizer();
  if (texturizer == NULL) {
    tessellatorMesh->render(rc, parentState);
  }
  else {
    const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();
    
    if (needsToCallTexturizer) {
      _texturizedMesh = texturizer->texturize(rc,
                                              trc,
                                              this,
                                              tessellatorMesh,
                                              _texturizedMesh);
    }
    
    if (_texturizedMesh != NULL) {
      _texturizedMesh->render(rc, parentState);
    }
    else {
      tessellatorMesh->render(rc, parentState);
    }
  }
  
}

void Tile::debugRender(const G3MRenderContext* rc,
                       const TileRenderContext* trc,
                       const GLState& parentState) {
  Mesh* debugMesh = getDebugMesh(rc, trc);
  if (debugMesh != NULL) {
    debugMesh->render(rc, parentState);
  }
}

std::vector<Tile*>* Tile::getSubTiles(const Angle& splitLatitude,
                                      const Angle& splitLongitude) {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles(splitLatitude, splitLongitude, true);
    _justCreatedSubtiles = true;
  }
  return _subtiles;
}

void Tile::toBeDeleted(TileTexturizer*        texturizer,
                       ElevationDataProvider* elevationDataProvider) {
  if (texturizer != NULL) {
    texturizer->tileToBeDeleted(this, _texturizedMesh);
  }
  
  if (elevationDataProvider != NULL) {
    //cancelElevationDataRequest(elevationDataProvider);
    
    if (_elevationDataListener != NULL){
      _elevationDataListener->cancelRequest();
    }
  }
}

void Tile::prune(TileTexturizer* texturizer,
                 ElevationDataProvider* elevationDataProvider) {
  if (_subtiles != NULL) {
    
    //    printf("= pruned tile %s\n", getKey().description().c_str());
    
    //    TileTexturizer* texturizer = (trc == NULL) ? NULL : trc->getTexturizer();
    
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      
      subtile->setIsVisible(false, texturizer);
      
      subtile->prune(texturizer, elevationDataProvider);
      if (texturizer != NULL) {
        texturizer->tileToBeDeleted(subtile, subtile->_texturizedMesh);
      }
      
      //      if (elevationDataProvider != NULL) {
      //        //subtile->cancelElevationDataRequest(elevationDataProvider);
      //      }
      
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
  // check for (_parent != NULL) to avoid deleting the firstLevel tiles.
  // in this case, the mesh is always loaded (as well as its texture) to be the last option
  // falback texture for any tile
  if ((_parent != NULL) && (_texturizedMesh != NULL)) {
    
    if (texturizer != NULL) {
      texturizer->tileMeshToBeDeleted(this, _texturizedMesh);
    }
    
    delete _texturizedMesh;
    _texturizedMesh = NULL;
    
    delete _texturizerData;
    _texturizerData = NULL;
    
    setTexturizerDirty(true);
    setTextureSolved(false);
  }
}

void Tile::render(const G3MRenderContext* rc,
                  const TileRenderContext* trc,
                  const GLState& parentState,
                  std::list<Tile*>* toVisitInNextIteration) {
  
  const float verticalExaggeration =  trc->getVerticalExaggeration();
  if (verticalExaggeration != _verticalExaggeration) {
    // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
    
    _verticalExaggeration = trc->getVerticalExaggeration();
  }
  
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
      rawRender(rc, trc, parentState);
      if (trc->getParameters()->_renderDebug) {
        debugRender(rc, trc, parentState);
      }
      
      statistics->computeTileRendered(this);
      
      prune(trc->getTexturizer(),
            trc->getElevationDataProvider());
      //TODO: AVISAR CAMBIO DE TERRENO
    }
    else {
      const Geodetic2D lower = _sector.lower();
      const Geodetic2D upper = _sector.upper();
      
      const Angle splitLongitude = Angle::midAngle(lower.longitude(),
                                                   upper.longitude());
      
      const Angle splitLatitude = trc->getLayerTilesRenderParameters()->_mercator
      /*                               */ ? MercatorUtils::calculateSplitLatitude(lower.latitude(),
                                                                                  upper.latitude())
      /*                               */ : Angle::midAngle(lower.latitude(),
                                                            upper.latitude());
      
      std::vector<Tile*>* subTiles = getSubTiles(splitLatitude, splitLongitude);
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
    
    prune(trc->getTexturizer(),
          trc->getElevationDataProvider());
    //TODO: AVISAR CAMBIO DE TERRENO
  }
}

Tile* Tile::createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                          const Angle& upperLat, const Angle& upperLon,
                          const int level,
                          const int row, const int column,
                          bool setParent) {
  Tile* parent = setParent ? this : NULL;
  return new Tile(_texturizer,
                  parent,
                  Sector(Geodetic2D(lowerLat, lowerLon), Geodetic2D(upperLat, upperLon)),
                  level,
                  row, column);
}

std::vector<Tile*>* Tile::createSubTiles(const Angle& splitLatitude,
                                         const Angle& splitLongitude,
                                         bool setParent) {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const int nextLevel = _level + 1;
  
  const int row2    = 2 * _row;
  const int column2 = 2 * _column;
  
  std::vector<Tile*>* subTiles = new std::vector<Tile*>();
  
  subTiles->push_back( createSubTile(lower.latitude(), lower.longitude(),
                                     splitLatitude, splitLongitude,
                                     nextLevel,
                                     row2,
                                     column2,
                                     setParent) );
  
  subTiles->push_back( createSubTile(lower.latitude(), splitLongitude,
                                     splitLatitude, upper.longitude(),
                                     nextLevel,
                                     row2,
                                     column2 + 1,
                                     setParent) );
  
  subTiles->push_back( createSubTile(splitLatitude, lower.longitude(),
                                     upper.latitude(), splitLongitude,
                                     nextLevel,
                                     row2 + 1,
                                     column2,
                                     setParent) );
  
  subTiles->push_back( createSubTile(splitLatitude, splitLongitude,
                                     upper.latitude(), upper.longitude(),
                                     nextLevel,
                                     row2 + 1,
                                     column2 + 1,
                                     setParent) );
  
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
    
    for (int i = 0; i < _subtiles->size(); i++) {
      const Tile* subtile = _subtiles->at(i);
      const Tile* subtileResult = subtile->getDeepestTileContaining(position);
      if (subtileResult != NULL) {
        return subtileResult;
      }
    }
  }
  
  return NULL;
}

const std::string Tile::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("(Tile");
  isb->addString(" level=");
  isb->addInt(_level);
  isb->addString(", row=");
  isb->addInt(_row);
  isb->addString(", column=");
  isb->addInt(_column);
  isb->addString(", sector=");
  isb->addString(_sector.description());
  isb->addString(")");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

double Tile::getMinHeight() const {
  return _minHeight;
}

double Tile::getMaxHeight() const {
  return _maxHeight;
}

#pragma mark ElevationData methods

void Tile::setElevationData(ElevationData* ed, int level){
  if (_elevationDataLevel < level){
    
    if (_elevationData != NULL){
      delete _elevationData;
    }
    
    _elevationData = ed;
    _elevationDataLevel = level;
    _mustActualizeMeshDueToNewElevationData = true;
    
    //If the elevation belongs to tile's level, we notify the sub-tree
    if (isElevationDataSolved()){
      if (_subtiles != NULL) {
        const int subtilesSize = _subtiles->size();
        for (int i = 0; i < subtilesSize; i++) {
          Tile* subtile = _subtiles->at(i);
          subtile->ancestorChangedElevationData(this);
        }
      }
    }
    
  }
}

void Tile::getElevationDataFromAncestor(const Vector2I& resolution){
  
  if (_elevationData == NULL){
    
    Tile* ancestor = this;
    while (ancestor != NULL && !ancestor->isElevationDataSolved()) {
      ancestor = ancestor->getParent();
    }
    
    if (ancestor != NULL){
      ElevationData* subView = createElevationDataSubviewFromAncestor(ancestor); ancestor->getElevationData();
      setElevationData(subView, ancestor->getLevel());
    }
    
  }
  
}

void Tile::initializeElevationData(ElevationDataProvider* elevationDataProvider,
                                   const TileTessellator* tessellator,
                                   const Vector2I& tileMeshResolution,
                                   const Planet* planet,
                                   bool renderDebug){
  //Storing for subviewing
  _lastElevationDataProvider = elevationDataProvider;
  _lastTileMeshResolutionX = tileMeshResolution._x;
  _lastTileMeshResolutionY = tileMeshResolution._y;
  
  if (_elevationDataListener == NULL){
    
    Vector2I res = tessellator->getTileMeshResolution(planet,
                                                      tileMeshResolution,
                                                      this,
                                                      renderDebug);
    
    _elevationDataListener = new TileElevationDataListener(this, res, elevationDataProvider);
    _elevationDataListener->sendRequest();
  }
  
  //If after petition we still have no data we request from ancestor
  if (_elevationData == NULL){
    getElevationDataFromAncestor(tileMeshResolution);
  }
  
}

void Tile::ancestorChangedElevationData(Tile* ancestor){
  
  if (ancestor->getLevel() > _elevationDataLevel){
    ElevationData* subView = createElevationDataSubviewFromAncestor(ancestor);
    setElevationData(subView, ancestor->getLevel());
  }
  
  if (_subtiles != NULL) {
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      subtile->ancestorChangedElevationData(this);
    }
  }
}

ElevationData* Tile::createElevationDataSubviewFromAncestor(Tile* ancestor) const{
  ElevationData* ed = ancestor->getElevationData();
  
  if (_lastElevationDataProvider != NULL && _lastTileMeshResolutionX > 0 && _lastTileMeshResolutionY > 0 ){
    ElevationData* subView = _lastElevationDataProvider->createSubviewOfElevationData(ed,
                                                                                      getSector(),
                                                                                      Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
    return subView;
  } else{
    ILogger::instance()->logError("Can't create subview of elevation data from ancestor");
    return NULL;
  }
  
  
}
