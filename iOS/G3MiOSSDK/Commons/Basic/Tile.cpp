//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Tile.hpp"

#include "Mesh.hpp"
#include "ITexturizerData.hpp"
#include "ElevationData.hpp"
#include "TileElevationDataRequest.hpp"
#include "PlanetTileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "TilesRenderParameters.hpp"
#include "MeshHolder.hpp"
#include "PlanetRenderer.hpp"
#include "FlatColorMesh.hpp"
#include "MercatorUtils.hpp"
#include "DecimatedSubviewElevationData.hpp"

std::string Tile::createTileId(int level,
                               int row,
                               int column) {
#ifdef C_CODE
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addInt(level);
  isb->addString("/");
  isb->addInt(row);
  isb->addString("/");
  isb->addInt(column);
  std::string s = isb->getString();
  delete isb;
  return s;
#endif
#ifdef JAVA_CODE
  return level + "/" + row + "/" + column;
#endif
}

Tile::Tile(TileTexturizer* texturizer,
           Tile* parent,
           const Sector& sector,
           const bool mercator,
           int level,
           int row,
           int column,
           const PlanetRenderer* planetRenderer):
_texturizer(texturizer),
_parent(parent),
_sector(sector),
_mercator(mercator),
_level(level),
_row(row),
_column(column),
_tessellatorMesh(NULL),
_debugMesh(NULL),
_flatColorMesh(NULL),
_texturizedMesh(NULL),
_textureSolved(false),
_texturizerDirty(true),
_subtiles(NULL),
_justCreatedSubtiles(false),
_isVisible(false),
_texturizerData(NULL),
_elevationData(NULL),
_elevationDataLevel(-1),
_elevationDataRequest(NULL),
_verticalExaggeration(0),
_mustActualizeMeshDueToNewElevationData(false),
_lastTileMeshResolutionX(-1),
_lastTileMeshResolutionY(-1),
_boundingVolume(NULL),
_lastMeetsRenderCriteriaTimeInMS(0),
_planetRenderer(planetRenderer),
_tessellatorData(NULL),
_northWestPoint(NULL),
_northEastPoint(NULL),
_southWestPoint(NULL),
_southEastPoint(NULL),
_northArcSegmentRatioSquared(0),
_southArcSegmentRatioSquared(0),
_eastArcSegmentRatioSquared(0),
_westArcSegmentRatioSquared(0),
_rendered(false),
_id( createTileId(level, row, column) )
{
}

Tile::~Tile() {
//  prune(NULL, NULL);

  delete _debugMesh;
  _debugMesh = NULL;

  delete _flatColorMesh;
  _flatColorMesh = NULL;

  delete _tessellatorMesh;
  _tessellatorMesh = NULL;

  delete _texturizerData;
  _texturizerData = NULL;

  delete _texturizedMesh;
  _texturizedMesh = NULL;

  delete _elevationData;
  _elevationData = NULL;

  if (_elevationDataRequest != NULL) {
    _elevationDataRequest->cancelRequest(); //The listener will auto delete
    delete _elevationDataRequest;
    _elevationDataRequest = NULL;
  }

  delete _tessellatorData;

  delete _northWestPoint;
  delete _northEastPoint;
  delete _southWestPoint;
  delete _southEastPoint;
}

void Tile::setTexturizerData(ITexturizerData* texturizerData) {
  if (texturizerData != _texturizerData) {
    delete _texturizerData;
    _texturizerData = texturizerData;
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
    const size_t subtilesSize = _subtiles->size();
    for (size_t i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      subtile->ancestorTexturedSolvedChanged(ancestor, textureSolved);
    }
  }
}

void Tile::setTextureSolved(bool textureSolved) {
  if (textureSolved != _textureSolved) {
    _textureSolved = textureSolved;

    if (_textureSolved) {
      delete _texturizerData;
      _texturizerData = NULL;
    }

    if (_subtiles != NULL) {
      const size_t subtilesSize = _subtiles->size();
      for (size_t i = 0; i < subtilesSize; i++) {
        Tile* subtile = _subtiles->at(i);
        subtile->ancestorTexturedSolvedChanged(this, _textureSolved);
      }
    }
  }
}

Mesh* Tile::getTessellatorMesh(const G3MRenderContext* rc,
                               ElevationDataProvider* elevationDataProvider,
                               const TileTessellator* tessellator,
                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                               const TilesRenderParameters* tilesRenderParameters) {


  if ( (_elevationData == NULL) && (elevationDataProvider != NULL) && (elevationDataProvider->isEnabled()) ) {
    initializeElevationData(elevationDataProvider,
                            tessellator,
                            layerTilesRenderParameters->_tileMeshResolution,
                            rc->getPlanet(),
                            tilesRenderParameters->_renderDebug);
  }

  if ( (_tessellatorMesh == NULL) || _mustActualizeMeshDueToNewElevationData ) {
    _mustActualizeMeshDueToNewElevationData = false;

    if (elevationDataProvider == NULL) {
      // no elevation data provider, just create a simple mesh without elevation
      _tessellatorMesh = tessellator->createTileMesh(rc->getPlanet(),
                                                     layerTilesRenderParameters->_tileMeshResolution,
                                                     this,
                                                     NULL,
                                                     _verticalExaggeration,
                                                     tilesRenderParameters->_renderDebug,
                                                     _tileTessellatorMeshData);

      computeTileCorners(rc->getPlanet());
    }
    else {
      Mesh* tessellatorMesh = tessellator->createTileMesh(rc->getPlanet(),
                                                          layerTilesRenderParameters->_tileMeshResolution,
                                                          this,
                                                          _elevationData,
                                                          _verticalExaggeration,
                                                          tilesRenderParameters->_renderDebug,
                                                          _tileTessellatorMeshData);

      MeshHolder* meshHolder = (MeshHolder*) _tessellatorMesh;
      if (meshHolder == NULL) {
        meshHolder = new MeshHolder(tessellatorMesh);
        _tessellatorMesh = meshHolder;
      }
      else {
        meshHolder->setMesh(tessellatorMesh);
      }

      computeTileCorners(rc->getPlanet());
    }

    //Notifying when the tile is first created and every time the elevation data changes
    _planetRenderer->sectorElevationChanged(_elevationData);
  }

  return _tessellatorMesh;
}

Mesh* Tile::getDebugMesh(const G3MRenderContext* rc,
                         const TileTessellator* tessellator,
                         const LayerTilesRenderParameters* layerTilesRenderParameters) {
  if (_debugMesh == NULL) {
    const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);

    _debugMesh = tessellator->createTileDebugMesh(rc->getPlanet(), tileMeshResolution, this);
  }
  return _debugMesh;
}


const BoundingVolume* Tile::getBoundingVolume(const G3MRenderContext* rc,
                                              ElevationDataProvider* elevationDataProvider,
                                              const TileTessellator* tessellator,
                                              const LayerTilesRenderParameters* layerTilesRenderParameters,
                                              const TilesRenderParameters* tilesRenderParameters) {
  if (_boundingVolume == NULL) {
    Mesh* mesh = getTessellatorMesh(rc,
                                    elevationDataProvider,
                                    tessellator,
                                    layerTilesRenderParameters,
                                    tilesRenderParameters);
    if (mesh != NULL) {
      _boundingVolume = mesh->getBoundingVolume();
    }
  }
  return _boundingVolume;
}

bool Tile::isVisible(const G3MRenderContext* rc,
                     const Frustum* cameraFrustumInModelCoordinates,
                     ElevationDataProvider* elevationDataProvider,
                     const Sector* renderedSector,
                     const TileTessellator* tessellator,
                     const LayerTilesRenderParameters* layerTilesRenderParameters,
                     const TilesRenderParameters* tilesRenderParameters) {
  if ((renderedSector != NULL) &&
      !renderedSector->touchesWith(_sector)) { //Incomplete world
    return false;
  }

  const BoundingVolume* boundingVolume = getBoundingVolume(rc,
                                                           elevationDataProvider,
                                                           tessellator,
                                                           layerTilesRenderParameters,
                                                           tilesRenderParameters);

  return ((boundingVolume != NULL)  &&
          boundingVolume->touchesFrustum(cameraFrustumInModelCoordinates));
}

bool Tile::meetsRenderCriteria(const G3MRenderContext* rc,
                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                               TileTexturizer* texturizer,
                               const TilesRenderParameters* tilesRenderParameters,
                               const TilesStatistics* tilesStatistics,
                               const ITimer* lastSplitTimer,
                               double texWidthSquared,
                               double texHeightSquared,
                               double nowInMS) {

  if ((_level >= layerTilesRenderParameters->_maxLevelForPoles) &&
      (_sector.touchesPoles())) {
    return true;
  }

  if (_level >= layerTilesRenderParameters->_maxLevel) {
    return true;
  }

  if (texturizer != NULL) {
    if (texturizer->tileMeetsRenderCriteria(this)) {
      return true;
    }
  }

  if (_lastMeetsRenderCriteriaTimeInMS != 0 &&
      (nowInMS - _lastMeetsRenderCriteriaTimeInMS) < 250 /*500*/ ) {
    return _lastMeetsRenderCriteriaResult;
  }

  if (tilesRenderParameters->_useTilesSplitBudget) {
    if (_subtiles == NULL) { // the tile needs to create the subtiles
      if (lastSplitTimer->elapsedTimeInMilliseconds() < 67) {
        // there are not more time-budget to spend
        return true;
      }
    }
  }

  _lastMeetsRenderCriteriaTimeInMS = nowInMS; //Storing time of result
#warning store camera-timestamp to avoid recalculation when the camera isn't moving

  if ((_northArcSegmentRatioSquared == 0) ||
      (_southArcSegmentRatioSquared == 0) ||
      (_eastArcSegmentRatioSquared  == 0) ||
      (_westArcSegmentRatioSquared  == 0)) {
    prepareTestLODData( rc->getPlanet() );
  }

  const Camera* camera = rc->getCurrentCamera();

  const double distanceInPixelsNorth = camera->getEstimatedPixelDistance(*_northWestPoint, *_northEastPoint);
  const double distanceInPixelsSouth = camera->getEstimatedPixelDistance(*_southWestPoint, *_southEastPoint);
  const double distanceInPixelsWest  = camera->getEstimatedPixelDistance(*_northWestPoint, *_southWestPoint);
  const double distanceInPixelsEast  = camera->getEstimatedPixelDistance(*_northEastPoint, *_southEastPoint);

  const double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcWest  = (distanceInPixelsWest  * distanceInPixelsWest)  * _westArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcEast  = (distanceInPixelsEast  * distanceInPixelsEast)  * _eastArcSegmentRatioSquared;

  _lastMeetsRenderCriteriaResult = ((distanceInPixelsSquaredArcNorth <= texHeightSquared) &&
                                    (distanceInPixelsSquaredArcSouth <= texHeightSquared) &&
                                    (distanceInPixelsSquaredArcWest  <= texWidthSquared ) &&
                                    (distanceInPixelsSquaredArcEast  <= texWidthSquared ));
  
  return _lastMeetsRenderCriteriaResult;
}

void Tile::prepareForFullRendering(const G3MRenderContext* rc,
                                   TileTexturizer* texturizer,
                                   ElevationDataProvider* elevationDataProvider,
                                   const TileTessellator* tessellator,
                                   const LayerTilesRenderParameters* layerTilesRenderParameters,
                                   const LayerSet* layerSet,
                                   const TilesRenderParameters* tilesRenderParameters,
                                   bool forceFullRender,
                                   long long tileDownloadPriority,
                                   float verticalExaggeration,
                                   bool logTilesPetitions) {

  //You have to set _verticalExaggertion
  if (verticalExaggeration != _verticalExaggeration) {
    // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
    _verticalExaggeration = verticalExaggeration;
  }


  Mesh* tessellatorMesh = getTessellatorMesh(rc,
                                             elevationDataProvider,
                                             tessellator,
                                             layerTilesRenderParameters,
                                             tilesRenderParameters);
  if (tessellatorMesh == NULL) {
    return;
  }

  if (texturizer != NULL) {
    const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();

    if (needsToCallTexturizer) {
      _texturizedMesh = texturizer->texturize(rc,
                                              tessellator,
                                              layerTilesRenderParameters,
                                              layerSet,
                                              forceFullRender,
                                              tileDownloadPriority,
                                              this,
                                              tessellatorMesh,
                                              _texturizedMesh,
                                              logTilesPetitions);
    }
  }
}

void Tile::rawRender(const G3MRenderContext* rc,
                     const GLState* glState,
                     TileTexturizer* texturizer,
                     ElevationDataProvider* elevationDataProvider,
                     const TileTessellator* tessellator,
                     const LayerTilesRenderParameters* layerTilesRenderParameters,
                     const LayerSet* layerSet,
                     const TilesRenderParameters* tilesRenderParameters,
                     bool forceFullRender,
                     long long tileDownloadPriority,
                     bool logTilesPetitions) {

  Mesh* tessellatorMesh = getTessellatorMesh(rc,
                                             elevationDataProvider,
                                             tessellator,
                                             layerTilesRenderParameters,
                                             tilesRenderParameters);
  if (tessellatorMesh == NULL) {
    return;
  }

  if (texturizer == NULL) {
    tessellatorMesh->render(rc, glState);
  }
  else {
    const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();

    if (needsToCallTexturizer) {
      _texturizedMesh = texturizer->texturize(rc,
                                              tessellator,
                                              layerTilesRenderParameters,
                                              layerSet,
                                              forceFullRender,
                                              tileDownloadPriority,
                                              this,
                                              tessellatorMesh,
                                              _texturizedMesh,
                                              logTilesPetitions);
    }

    if (_texturizedMesh != NULL) {
      _texturizedMesh->render(rc, glState);
    }
    else {
      //Adding flat color if no texture set on the mesh
      if (_flatColorMesh == NULL) {
        _flatColorMesh = new FlatColorMesh(tessellatorMesh, false,
                                           Color::newFromRGBA((float) 1.0, (float) 1.0, (float) 1.0, (float) 1.0), true);
      }
      _flatColorMesh->render(rc, glState);
    }
  }
}

void Tile::debugRender(const G3MRenderContext* rc,
                       const GLState* glState,
                       const TileTessellator* tessellator,
                       const LayerTilesRenderParameters* layerTilesRenderParameters) {
  Mesh* debugMesh = getDebugMesh(rc, tessellator, layerTilesRenderParameters);
  if (debugMesh != NULL) {
    debugMesh->render(rc, glState);
  }
}


std::vector<Tile*>* Tile::getSubTiles() {
  if (_subtiles != NULL) {
    // quick check to avoid splitLongitude/splitLatitude calculation
    return _subtiles;
  }

  const Geodetic2D lower = _sector._lower;
  const Geodetic2D upper = _sector._upper;

  const Angle splitLongitude = Angle::midAngle(lower._longitude,
                                               upper._longitude);


  const Angle splitLatitude = _mercator
  /*                               */ ? MercatorUtils::calculateSplitLatitude(lower._latitude,
                                                                              upper._latitude)
  /*                               */ : Angle::midAngle(lower._latitude,
                                                        upper._latitude);

  return getSubTiles(splitLatitude, splitLongitude);
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
                       ElevationDataProvider* elevationDataProvider,
                       std::vector<std::string>* tilesStoppedRendering) {
  if (_rendered) {
    if (tilesStoppedRendering != NULL) {
      tilesStoppedRendering->push_back(_id);
    }
  }

  prune(texturizer, elevationDataProvider, tilesStoppedRendering);

  if (texturizer != NULL) {
    texturizer->tileToBeDeleted(this, _texturizedMesh);
  }

  if (elevationDataProvider != NULL) {
    if (_elevationDataRequest != NULL) {
      _elevationDataRequest->cancelRequest();
    }
  }
}

void Tile::prune(TileTexturizer*           texturizer,
                 ElevationDataProvider*    elevationDataProvider,
                 std::vector<std::string>* tilesStoppedRendering) {

  if (_subtiles != NULL) {
    //Notifying elevation event when LOD decreases
    _planetRenderer->sectorElevationChanged(_elevationData);

    const size_t subtilesSize = _subtiles->size();
    for (size_t i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);

      subtile->setIsVisible(false, texturizer);

      subtile->prune(texturizer, elevationDataProvider, tilesStoppedRendering);
      if (texturizer != NULL) {
        texturizer->tileToBeDeleted(subtile, subtile->_texturizedMesh);
      }

//      if (_rendered) {
//        if (tilesStoppedRendering != NULL) {
//          tilesStoppedRendering->push_back(subtile);
//        }
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
                  const GLState& parentState,
                  std::vector<Tile*>* toVisitInNextIteration,
                  const Frustum* cameraFrustumInModelCoordinates,
                  TilesStatistics* tilesStatistics,
                  const float verticalExaggeration,
                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                  TileTexturizer* texturizer,
                  const TilesRenderParameters* tilesRenderParameters,
                  ITimer* lastSplitTimer,
                  ElevationDataProvider* elevationDataProvider,
                  const TileTessellator* tessellator,
                  const LayerSet* layerSet,
                  const Sector* renderedSector,
                  bool forceFullRender,
                  long long tileDownloadPriority,
                  double texWidthSquared,
                  double texHeightSquared,
                  double nowInMS,
                  const bool renderTileMeshes,
                  bool logTilesPetitions,
                  std::vector<const Tile*>* tilesStartedRendering,
                  std::vector<std::string>* tilesStoppedRendering) {

  tilesStatistics->computeTileProcessed(this);

  if (verticalExaggeration != _verticalExaggeration) {
    // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
    _verticalExaggeration = verticalExaggeration;
  }

  bool rendered = false;

  if (isVisible(rc,
                cameraFrustumInModelCoordinates,
                elevationDataProvider,
                renderedSector,
                tessellator,
                layerTilesRenderParameters,
                tilesRenderParameters)) {
    setIsVisible(true, texturizer);

    tilesStatistics->computeVisibleTile(this);

    const bool isRawRender = (
                              (toVisitInNextIteration == NULL) ||
                              meetsRenderCriteria(rc,
                                                  layerTilesRenderParameters,
                                                  texturizer,
                                                  tilesRenderParameters,
                                                  tilesStatistics,
                                                  lastSplitTimer,
                                                  texWidthSquared,
                                                  texHeightSquared,
                                                  nowInMS) ||
                              (tilesRenderParameters->_incrementalTileQuality && !_textureSolved)
                              );

    if (isRawRender) {

      const long long tileTexturePriority = (tilesRenderParameters->_incrementalTileQuality
                                             ? tileDownloadPriority + layerTilesRenderParameters->_maxLevel - _level
                                             : tileDownloadPriority + _level);

      rendered = true;
      if (renderTileMeshes) {
        rawRender(rc,
                  &parentState,
                  texturizer,
                  elevationDataProvider,
                  tessellator,
                  layerTilesRenderParameters,
                  layerSet,
                  tilesRenderParameters,
                  forceFullRender,
                  tileTexturePriority,
                  logTilesPetitions);
      }
      if (tilesRenderParameters->_renderDebug) {
        debugRender(rc, &parentState, tessellator, layerTilesRenderParameters);
      }

      tilesStatistics->computeTileRenderered(this);

      prune(texturizer, elevationDataProvider, tilesStoppedRendering);
      //TODO: AVISAR CAMBIO DE TERRENO
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles();
      if (_justCreatedSubtiles) {
        lastSplitTimer->start();
        _justCreatedSubtiles = false;
      }

      const size_t subTilesSize = subTiles->size();
      for (size_t i = 0; i < subTilesSize; i++) {
        Tile* subTile = subTiles->at(i);
        toVisitInNextIteration->push_back(subTile);
      }
    }
  }
  else {
    setIsVisible(false, texturizer);

    prune(texturizer, elevationDataProvider, tilesStoppedRendering);
    //TODO: AVISAR CAMBIO DE TERRENO
  }

  if (_rendered != rendered) {
    _rendered = rendered;

    if (_rendered) {
      if (tilesStartedRendering != NULL) {
        tilesStartedRendering->push_back(this);
      }
    }
    else {
      if (tilesStoppedRendering != NULL) {
        tilesStoppedRendering->push_back(_id);
      }
    }
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
                  _mercator,
                  level,
                  row, column,
                  _planetRenderer);
}

std::vector<Tile*>* Tile::createSubTiles(const Angle& splitLatitude,
                                         const Angle& splitLongitude,
                                         bool setParent) {
  const Geodetic2D lower = _sector._lower;
  const Geodetic2D upper = _sector._upper;

  const int nextLevel = _level + 1;

  const int row2    = 2 * _row;
  const int column2 = 2 * _column;

  std::vector<Tile*>* subTiles = new std::vector<Tile*>();

  const Sector* renderedSector = _planetRenderer->getRenderedSector();

  Sector s1(Geodetic2D(lower._latitude, lower._longitude), Geodetic2D(splitLatitude, splitLongitude));
  if (renderedSector == NULL || renderedSector->touchesWith(s1)) {
    subTiles->push_back( createSubTile(lower._latitude, lower._longitude,
                                       splitLatitude, splitLongitude,
                                       nextLevel,
                                       row2,
                                       column2,
                                       setParent) );
  }

  Sector s2(Geodetic2D(lower._latitude, splitLongitude), Geodetic2D(splitLatitude, upper._longitude));
  if (renderedSector == NULL || renderedSector->touchesWith(s2)) {
    subTiles->push_back( createSubTile(lower._latitude, splitLongitude,
                                       splitLatitude, upper._longitude,
                                       nextLevel,
                                       row2,
                                       column2 + 1,
                                       setParent) );
  }

  Sector s3(Geodetic2D(splitLatitude, lower._longitude), Geodetic2D(upper._latitude, splitLongitude));
  if (renderedSector == NULL || renderedSector->touchesWith(s3)) {
    subTiles->push_back( createSubTile(splitLatitude, lower._longitude,
                                       upper._latitude, splitLongitude,
                                       nextLevel,
                                       row2 + 1,
                                       column2,
                                       setParent) );
  }

  Sector s4(Geodetic2D(splitLatitude, splitLongitude), Geodetic2D(upper._latitude, upper._longitude));
  if (renderedSector == NULL || renderedSector->touchesWith(s4)) {
    subTiles->push_back( createSubTile(splitLatitude, splitLongitude,
                                       upper._latitude, upper._longitude,
                                       nextLevel,
                                       row2 + 1,
                                       column2 + 1,
                                       setParent) );
  }

#ifdef JAVA_CODE
  subTiles.trimToSize();
#endif

  return subTiles;
}

const Tile* Tile::getDeepestTileContaining(const Geodetic3D& position) const {
  if (_sector.contains(position._latitude, position._longitude)) {
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
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
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

#pragma mark ElevationData methods

void Tile::setElevationData(ElevationData* ed, int level) {
  if (_elevationDataLevel < level) {

    if (_elevationData != NULL) {
      delete _elevationData;
    }

    _elevationData = ed;
    _elevationDataLevel = level;
    _mustActualizeMeshDueToNewElevationData = true;

    //If the elevation belongs to tile's level, we notify the sub-tree
    if (isElevationDataSolved()) {
      if (_subtiles != NULL) {
        const size_t subtilesSize = _subtiles->size();
        for (size_t i = 0; i < subtilesSize; i++) {
          Tile* subtile = _subtiles->at(i);
          subtile->ancestorChangedElevationData(this);
        }
      }
    }

  }
}

void Tile::getElevationDataFromAncestor(const Vector2I& extent) {
  if (_elevationData == NULL) {
    Tile* ancestor = getParent();
    while ((ancestor != NULL) &&
           !ancestor->isElevationDataSolved()) {
      ancestor = ancestor->getParent();
    }

    if (ancestor != NULL) {
      ElevationData* subView = createElevationDataSubviewFromAncestor(ancestor);
      setElevationData(subView, ancestor->_level);
    }
  }
}

void Tile::initializeElevationData(ElevationDataProvider* elevationDataProvider,
                                   const TileTessellator* tessellator,
                                   const Vector2I& tileMeshResolution,
                                   const Planet* planet,
                                   bool renderDebug) {
  //Storing for subviewing
  _lastElevationDataProvider = elevationDataProvider;
  _lastTileMeshResolutionX = tileMeshResolution._x;
  _lastTileMeshResolutionY = tileMeshResolution._y;
  if (_elevationDataRequest == NULL) {

    const Vector2I res = tessellator->getTileMeshResolution(planet,
                                                            tileMeshResolution,
                                                            this,
                                                            renderDebug);
    _elevationDataRequest = new TileElevationDataRequest(this, res, elevationDataProvider);
    _elevationDataRequest->sendRequest();
  }

  //If after petition we still have no data we request from ancestor (provider asynchronous)
  if (_elevationData == NULL) {
    getElevationDataFromAncestor(tileMeshResolution);
  }

}

void Tile::ancestorChangedElevationData(Tile* ancestor) {

  if (ancestor->_level > _elevationDataLevel) {
    ElevationData* subView = createElevationDataSubviewFromAncestor(ancestor);
    if (subView != NULL) {
      setElevationData(subView, ancestor->_level);
    }
  }

  if (_subtiles != NULL) {
    const size_t subtilesSize = _subtiles->size();
    for (size_t i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      subtile->ancestorChangedElevationData(this);
    }
  }
}

ElevationData* Tile::createElevationDataSubviewFromAncestor(Tile* ancestor) const {
  ElevationData* ed = ancestor->getElevationData();

  if (ed == NULL) {
    ILogger::instance()->logError("Ancestor can't have undefined Elevation Data.");
    return NULL;
  }

  if (ed->getExtentWidth() < 1 || ed->getExtentHeight() < 1) {
    ILogger::instance()->logWarning("Tile too small for ancestor elevation data.");
    return NULL;
  }

  if ((_lastElevationDataProvider != NULL) &&
      (_lastTileMeshResolutionX > 0) &&
      (_lastTileMeshResolutionY > 0)) {
    return new DecimatedSubviewElevationData(ed,
                                             _sector,
                                             Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
  }

  ILogger::instance()->logError("Can't create subview of elevation data from ancestor");
  return NULL;

}

void Tile::setTessellatorData(PlanetTileTessellatorData* tessellatorData) {
  if (tessellatorData != _tessellatorData) {
    delete _tessellatorData;
    _tessellatorData = tessellatorData;
  }
}

void Tile::prepareTestLODData(const Planet* planet) {
  if ((_northWestPoint == NULL) ||
      (_northEastPoint == NULL) ||
      (_southWestPoint == NULL) ||
      (_southEastPoint == NULL)) {
    ILogger::instance()->logError("Error in Tile::prepareTestLODData");
    return;
  }

  const Vector3D normalNW = planet->centricSurfaceNormal(*_northWestPoint);
  const Vector3D normalNE = planet->centricSurfaceNormal(*_northEastPoint);
  const Vector3D normalSW = planet->centricSurfaceNormal(*_southWestPoint);
  const Vector3D normalSE = planet->centricSurfaceNormal(*_southEastPoint);

  _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
  _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
  _eastArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNE, normalSE);
  _westArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNW, normalSW);
}

double Tile::getSquaredArcSegmentRatio(const Vector3D& a,
                                       const Vector3D& b) {
  /*
   Arco = ang * Cuerda / (2 * sen(ang/2))
   */

  const double angleInRadians = Vector3D::angleInRadiansBetween(a, b);
  const double halfAngleSin = SIN(angleInRadians / 2);
  const double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
  return (arcSegmentRatio * arcSegmentRatio);
}

void Tile::computeTileCorners(const Planet* planet) {

  if (_tessellatorMesh == NULL) {
    ILogger::instance()->logError("Error in Tile::computeTileCorners");
    return;
  }

  delete _northWestPoint;
  delete _northEastPoint;
  delete _southWestPoint;
  delete _southEastPoint;


  const double mediumHeight = _tileTessellatorMeshData._averageHeight;

  _northWestPoint = new Vector3D( planet->toCartesian( _sector.getNW(), mediumHeight ) );
  _northEastPoint = new Vector3D( planet->toCartesian( _sector.getNE(), mediumHeight ) );
  _southWestPoint = new Vector3D( planet->toCartesian( _sector.getSW(), mediumHeight ) );
  _southEastPoint = new Vector3D( planet->toCartesian( _sector.getSE(), mediumHeight ) );
}

Vector2I Tile::getNormalizedPixelsFromPosition(const Geodetic2D& position2D,
                                               const Vector2I& tileDimension) const {
  const IMathUtils* math = IMathUtils::instance();
  const Vector2D uv = _sector.getUVCoordinates(position2D);
  return Vector2I(math->toInt(tileDimension._x * uv._x), math->toInt(tileDimension._y * uv._y));
}
