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
_elevationRequestId(-1000)
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

class TileElevationDataListener : public IElevationDataListener {
private:
  Tile*                  _tile;
  MeshHolder*            _meshHolder;
  const TileTessellator* _tessellator;
  const Planet*          _planet;
#ifdef C_CODE
  const Vector2I         _tileMeshResolution;
#endif
#ifdef JAVA_CODE
  private final Vector2I _tileMeshResolution;
#endif
  const bool             _renderDebug;
  const float            _verticalExaggeration;
public:
  TileElevationDataListener(Tile* tile,
                            MeshHolder* meshHolder,
                            const TileTessellator* tessellator,
                            const Planet* planet,
                            const Vector2I& tileMeshResolution,
                            float verticalExaggeration,
                            bool renderDebug) :
  _tile(tile),
  _meshHolder(meshHolder),
  _tessellator(tessellator),
  _planet(planet),
  _tileMeshResolution(tileMeshResolution),
  _verticalExaggeration(verticalExaggeration),
  _renderDebug(renderDebug)
  {
    
  }

  ~TileElevationDataListener() {}

  void onData(const Sector& sector,
              const Vector2I& resolution,
              ElevationData* elevationData) {
    _tile->onElevationData(elevationData,
                           _verticalExaggeration,
                           _meshHolder,
                           _tessellator,
                           _planet,
                           _tileMeshResolution,
                           _renderDebug);
  }

  void onError(const Sector& sector,
               const Vector2I& resolution) {

  }
};

void Tile::onElevationData(ElevationData* elevationData,
                           float verticalExaggeration,
                           MeshHolder* meshHolder,
                           const TileTessellator* tessellator,
                           const Planet* planet,
                           const Vector2I& tileMeshResolution,
                           bool renderDebug) {
  _elevationRequestId = -1000;
  _elevationData = elevationData;
  meshHolder->setMesh( tessellator->createTileMesh(planet,
                                                   tileMeshResolution,
                                                   this,
                                                   _elevationData,
                                                   verticalExaggeration,
                                                   renderDebug) );
}

Mesh* Tile::getTessellatorMesh(const G3MRenderContext* rc,
                               const TileRenderContext* trc) {
  if ( _tessellatorMesh == NULL ) {
    const TileTessellator* tessellator = trc->getTessellator();
    const bool renderDebug = trc->getParameters()->_renderDebug;
    ElevationDataProvider* elevationDataProvider = trc->getElevationDataProvider();
    const Planet* planet = rc->getPlanet();

    const float verticalExaggeration = trc->getVerticalExaggeration();

    const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
    const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);

    if (elevationDataProvider == NULL) {
      // no elevation data provider, just create a simple mesh without elevation
      _tessellatorMesh = tessellator->createTileMesh(planet,
                                                     tileMeshResolution,
                                                     this,
                                                     NULL,
                                                     verticalExaggeration,
                                                     renderDebug);
    }
    else {
      if (_elevationData == NULL) {
        MeshHolder* meshHolder = new MeshHolder( tessellator->createTileMesh(planet,
                                                                             tileMeshResolution,
                                                                             this,
                                                                             NULL,
                                                                             verticalExaggeration,
                                                                             renderDebug) );
        _tessellatorMesh = meshHolder;

        TileElevationDataListener* listener = new TileElevationDataListener(this,
                                                                            meshHolder,
                                                                            tessellator,
                                                                            planet,
                                                                            tileMeshResolution,
                                                                            verticalExaggeration,
                                                                            renderDebug);

        _elevationRequestId = elevationDataProvider->requestElevationData(_sector,
                                                                          tessellator->getTileMeshResolution(planet,
                                                                                                             tileMeshResolution,
                                                                                                             this,
                                                                                                             renderDebug),
                                                                          listener,
                                                                          true);
      }
      else {
        // the elevation data is already available, create a simple "inflated" mesh with
        _tessellatorMesh = tessellator->createTileMesh(planet,
                                                       tileMeshResolution,
                                                       this,
                                                       _elevationData,
                                                       verticalExaggeration,
                                                       renderDebug);
      }
    }

//    Mesh* tessellatorMesh = tessellator->createTileMesh(rc, this, renderDebug);
//
//    if (elevationDataProvider == NULL) {
//      _tessellatorMesh = tessellatorMesh;
//    }
//    else {
//      _tessellatorMesh = elevationDataProvider;
//
//      const long long elevationRequestId = elevationDataProvider->requestElevationData(_sector,
//                                                                                       tessellator->getTileMeshResolution(rc, this, renderDebug),
//                                                                                       new TileElevationDataListener(this),
//                                                                                       true);
//    }

  }
  return _tessellatorMesh;
}

Mesh* Tile::getDebugMesh(const G3MRenderContext* rc,
                         const TileRenderContext* trc) {
  if (_debugMesh == NULL) {
    const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
    const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);

    _debugMesh = trc->getTessellator()->createTileDebugMesh(rc->getPlanet(), tileMeshResolution, this);
  }
  return _debugMesh;
}

Extent* Tile::getTileExtent(const G3MRenderContext *rc) {
  if (_tileExtent == NULL) {
    const Planet* planet = rc->getPlanet();

    const Vector3D v0 = planet->toCartesian( _sector.getCenter() );
    const Vector3D v1 = planet->toCartesian( _sector.getNE() );
    const Vector3D v2 = planet->toCartesian( _sector.getNW() );
    const Vector3D v3 = planet->toCartesian( _sector.getSE() );
    const Vector3D v4 = planet->toCartesian( _sector.getSW() );

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

//  const Extent* extent = getTileExtent(rc);

  return extent->touches( rc->getCurrentCamera()->getFrustumInModelCoordinates() );
  //return extent->touches( rc->getCurrentCamera()->getHalfFrustuminModelCoordinates() );
}

bool Tile::meetsRenderCriteria(const G3MRenderContext *rc,
                               const TileRenderContext* trc) {
//  const TilesRenderParameters* parameters = trc->getParameters();

  const LayerTilesRenderParameters* parameters = trc->getLayerTilesRenderParameters();

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

  //getTileExtent(rc)->render(rc, parentState);

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

std::vector<Tile*>* Tile::getSubTiles(double u, double v) {
  if (_subtiles == NULL) {
    _subtiles = createSubTiles(u, v);
    _justCreatedSubtiles = true;
  }
  return _subtiles;
}

void Tile::cancelElevationDataRequest(ElevationDataProvider* elevationDataProvider) {
  if (_elevationRequestId > 0) {
    elevationDataProvider->cancelRequest(_elevationRequestId);
    _elevationRequestId = -1000;
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

      if (elevationDataProvider != NULL) {
        subtile->cancelElevationDataRequest(elevationDataProvider);
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
    }
    else {
      double u = 0.5;
      double v = 0.5;
      if (trc->getLayerTilesRenderParameters()->_mercator) {
        int TODO_change_V_conforming_to_mercator;
      }

      std::vector<Tile*>* subTiles = getSubTiles(u, v);
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

std::vector<Tile*>* Tile::createSubTiles(double u, double v) {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();

//  const Angle midLat = Angle::midAngle(lower.latitude(), upper.latitude());
//  const Angle midLon = Angle::midAngle(lower.longitude(), upper.longitude());
  const Angle midLat = Angle::linearInterpolation(lower.latitude(),  upper.latitude(),  v);
  const Angle midLon = Angle::linearInterpolation(lower.longitude(), upper.longitude(), u);

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
