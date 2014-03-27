//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Tile.hpp"
#include "Mesh.hpp"
#include "Camera.hpp"
#include "ITimer.hpp"
#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"
#include "PlanetRenderer.hpp"
#include "TilesRenderParameters.hpp"
#include "TileKey.hpp"
#include "Box.hpp"
#include "ElevationDataProvider.hpp"
#include "MeshHolder.hpp"
#include "ElevationData.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "IStringBuilder.hpp"
#include "MercatorUtils.hpp"
#include "DecimatedSubviewElevationData.hpp"
#include "TileElevationDataRequest.hpp"
#include "Vector2F.hpp"
#include "FlatColorMesh.hpp"
#include "PlanetRenderer.hpp"
#include "PlanetTileTessellator.hpp"
#include "TileRenderingListener.hpp"

Tile::Tile(TileTexturizer* texturizer,
           Tile* parent,
           const Sector& sector,
           int level,
           int row,
           int column,
           const PlanetRenderer* planetRenderer):
_texturizer(texturizer),
_parent(parent),
_sector(sector),
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
//_tileBoundingVolume(NULL),
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
//_middleNorthPoint(NULL),
//_middleSouthPoint(NULL),
//_middleEastPoint(NULL),
//_middleWestPoint(NULL),
_northWestPoint(NULL),
_northEastPoint(NULL),
_southWestPoint(NULL),
_southEastPoint(NULL),
//_latitudeArcSegmentRatioSquared(0),
//_longitudeArcSegmentRatioSquared(0),
_northArcSegmentRatioSquared(0),
_southArcSegmentRatioSquared(0),
_eastArcSegmentRatioSquared(0),
_westArcSegmentRatioSquared(0),
_rendered(false)
{
  //  int __remove_tile_print;
  //  printf("Created tile=%s\n deltaLat=%s deltaLon=%s\n",
  //         getKey().description().c_str(),
  //         _sector._deltaLatitude.description().c_str(),
  //         _sector._deltaLongitude.description().c_str()
  //         );
}

Tile::~Tile() {
  prune(NULL, NULL);

  //  delete _boundingVolume;

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

  //  delete _tileBoundingVolume;
  //  _tileBoundingVolume = NULL;

  delete _elevationData;
  _elevationData = NULL;

  if (_elevationDataRequest != NULL) {
    _elevationDataRequest->cancelRequest(); //The listener will auto delete
    delete _elevationDataRequest;
    _elevationDataRequest = NULL;
  }

  delete _tessellatorData;

//  delete _middleEastPoint;
//  delete _middleNorthPoint;
//  delete _middleSouthPoint;
//  delete _middleWestPoint;
  delete _northWestPoint;
  delete _northEastPoint;
  delete _southWestPoint;
  delete _southEastPoint;
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

    if (_textureSolved) {
      delete _texturizerData;
      _texturizerData = NULL;
    }

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
                                                     layerTilesRenderParameters->_mercator,
                                                     tilesRenderParameters->_renderDebug,
                                                     _tileTessellatorMeshData);

      computeTileCorners(rc->getPlanet()); //COMPUTING CORNERS

    }
    else {
      Mesh* tessellatorMesh = tessellator->createTileMesh(rc->getPlanet(),
                                                          layerTilesRenderParameters->_tileMeshResolution,
                                                          this,
                                                          _elevationData,
                                                          _verticalExaggeration,
                                                          layerTilesRenderParameters->_mercator,
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

      computeTileCorners(rc->getPlanet()); //COMPUTING CORNERS
    }

    //Notifying when the tile is first created and every time the elevation data changes
    _planetRenderer->sectorElevationChanged(_elevationData);
  }

  //_tessellatorMesh->showNormals(true);

  return _tessellatorMesh;
}

Mesh* Tile::getDebugMesh(const G3MRenderContext* rc,
                         const TileTessellator* tessellator,
                         const LayerTilesRenderParameters* layerTilesRenderParameters) {
  if (_debugMesh == NULL) {
    const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);

    //TODO: CHECK
    _debugMesh = tessellator->createTileDebugMesh(rc->getPlanet(), tileMeshResolution, this);
  }
  return _debugMesh;
}

//Box* Tile::getTileBoundingVolume(const G3MRenderContext *rc) {
//  if (_tileBoundingVolume == NULL) {
//    const Planet* planet = rc->getPlanet();
//
//    const double minHeight = getMinHeight() * _verticalExaggeration;
//    const double maxHeight = getMaxHeight() * _verticalExaggeration;
//
//    const Vector3D v0 = planet->toCartesian( _sector._center, maxHeight );
//    const Vector3D v1 = planet->toCartesian( _sector.getNE(),     minHeight );
//    const Vector3D v2 = planet->toCartesian( _sector.getNW(),     minHeight );
//    const Vector3D v3 = planet->toCartesian( _sector.getSE(),     minHeight );
//    const Vector3D v4 = planet->toCartesian( _sector.getSW(),     minHeight );
//
//    double lowerX = v0._x;
//    if (v1._x < lowerX) { lowerX = v1._x; }
//    if (v2._x < lowerX) { lowerX = v2._x; }
//    if (v3._x < lowerX) { lowerX = v3._x; }
//    if (v4._x < lowerX) { lowerX = v4._x; }
//
//    double upperX = v0._x;
//    if (v1._x > upperX) { upperX = v1._x; }
//    if (v2._x > upperX) { upperX = v2._x; }
//    if (v3._x > upperX) { upperX = v3._x; }
//    if (v4._x > upperX) { upperX = v4._x; }
//
//
//    double lowerY = v0._y;
//    if (v1._y < lowerY) { lowerY = v1._y; }
//    if (v2._y < lowerY) { lowerY = v2._y; }
//    if (v3._y < lowerY) { lowerY = v3._y; }
//    if (v4._y < lowerY) { lowerY = v4._y; }
//
//    double upperY = v0._y;
//    if (v1._y > upperY) { upperY = v1._y; }
//    if (v2._y > upperY) { upperY = v2._y; }
//    if (v3._y > upperY) { upperY = v3._y; }
//    if (v4._y > upperY) { upperY = v4._y; }
//
//
//    double lowerZ = v0._z;
//    if (v1._z < lowerZ) { lowerZ = v1._z; }
//    if (v2._z < lowerZ) { lowerZ = v2._z; }
//    if (v3._z < lowerZ) { lowerZ = v3._z; }
//    if (v4._z < lowerZ) { lowerZ = v4._z; }
//
//    double upperZ = v0._z;
//    if (v1._z > upperZ) { upperZ = v1._z; }
//    if (v2._z > upperZ) { upperZ = v2._z; }
//    if (v3._z > upperZ) { upperZ = v3._z; }
//    if (v4._z > upperZ) { upperZ = v4._z; }
//
//
//    _tileBoundingVolume = new Box(Vector3D(lowerX, lowerY, lowerZ),
//                                  Vector3D(upperX, upperY, upperZ));
//  }
//  return _tileBoundingVolume;
//}


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
      //      _boundingVolume = mesh->getBoundingVolume()->createSphere();
      _boundingVolume = mesh->getBoundingVolume();
    }
  }
  return _boundingVolume;
}

bool Tile::isVisible(const G3MRenderContext* rc,
                     const Planet* planet,
                     const Vector3D& cameraNormalizedPosition,
                     double cameraAngle2HorizonInRadians,
                     const Frustum* cameraFrustumInModelCoordinates,
                     ElevationDataProvider* elevationDataProvider,
                     const Sector* renderedSector,
                     const TileTessellator* tessellator,
                     const LayerTilesRenderParameters* layerTilesRenderParameters,
                     const TilesRenderParameters* tilesRenderParameters) {

  ////  const BoundingVolume* boundingVolume = getTessellatorMesh(rc, trc)->getBoundingVolume();
  //  const BoundingVolume* boundingVolume = getBoundingVolume(rc, trc);
  //  if (boundingVolume == NULL) {
  //    return false;
  //  }
  //
  //  if (!boundingVolume->touchesFrustum(cameraFrustumInModelCoordinates)) {
  //    return false;
  //  }
  //
  //  // test if sector is back oriented with respect to the camera
  //  return !_sector.isBackOriented(rc,
  //                                 getMinHeight(),
  //                                 planet,
  //                                 cameraNormalizedPosition,
  //                                 cameraAngle2HorizonInRadians);


  /* //AGUSTIN:now that zfar is located in the horizon, this test is not needed anymore
   // test if sector is back oriented with respect to the camera
   if (_sector.isBackOriented(rc,
   getMinHeight(),
   planet,
   cameraNormalizedPosition,
   cameraAngle2HorizonInRadians)) {
   return false;
   }*/

  if (renderedSector != NULL && !renderedSector->touchesWith(_sector)) { //Incomplete world
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

//  if (_level == 4 && _column == 7 && _row == 10) {
//    printf("break point!!");
////    return true;
//  }

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
      if (tilesStatistics->getSplitsCountInFrame() > 0) {
        // there are not more splitsCount-budget to spend
        return true;
      }

      if (lastSplitTimer->elapsedTimeInMilliseconds() < 25) {
        // there are not more time-budget to spend
        return true;
      }
    }
  }

  _lastMeetsRenderCriteriaTimeInMS = nowInMS; //Storing time of result


//  if ((_latitudeArcSegmentRatioSquared  == 0) ||
//      (_longitudeArcSegmentRatioSquared == 0)) {
//#warning CHECK IT
//    prepareTestLODData( rc->getPlanet() );
//  }
  if ((_northArcSegmentRatioSquared == 0) ||
      (_southArcSegmentRatioSquared == 0) ||
      (_eastArcSegmentRatioSquared  == 0) ||
      (_westArcSegmentRatioSquared  == 0)) {
#warning CHECK IT
    prepareTestLODData( rc->getPlanet() );
  }

  const Camera* camera = rc->getCurrentCamera();

//  const double pixelsDistanceNS = camera->getEstimatedPixelDistance(*_middleNorthPoint, *_middleSouthPoint);
//  const double pixelsDistanceWE = camera->getEstimatedPixelDistance(*_middleWestPoint,  *_middleEastPoint);

  const double distanceInPixelsNorth = camera->getEstimatedPixelDistance(*_northWestPoint, *_northEastPoint);
  const double distanceInPixelsSouth = camera->getEstimatedPixelDistance(*_southWestPoint, *_southEastPoint);
  const double distanceInPixelsWest  = camera->getEstimatedPixelDistance(*_northWestPoint, *_southWestPoint);
  const double distanceInPixelsEast  = camera->getEstimatedPixelDistance(*_northEastPoint, *_southEastPoint);

  const double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcWest  = (distanceInPixelsWest  * distanceInPixelsWest)  * _westArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcEast  = (distanceInPixelsEast  * distanceInPixelsEast)  * _eastArcSegmentRatioSquared;

//  const double latitudeMiddleDistSquared  = pixelsDistanceNS * pixelsDistanceNS;
//  const double longitudeMiddleDistSquared = pixelsDistanceWE * pixelsDistanceWE;
//
//  const double latitudeMiddleArcDistSquared  = latitudeMiddleDistSquared  * _latitudeArcSegmentRatioSquared;
//  const double longitudeMiddleArcDistSquared = longitudeMiddleDistSquared * _longitudeArcSegmentRatioSquared;

//  const IMathUtils* mu = IMathUtils::instance();
//
//  const double latitudeMiddleArcDistSquared  = mu->max(distanceInPixelsSquaredArcNorth, distanceInPixelsSquaredArcSouth);
//  const double longitudeMiddleArcDistSquared = mu->max(distanceInPixelsSquaredArcWest,  distanceInPixelsSquaredArcEast);
//
////  const double latLonRatio = latitudeMiddleArcDistSquared  / longitudeMiddleArcDistSquared;
////  const double lonLatRatio = longitudeMiddleArcDistSquared / latitudeMiddleArcDistSquared;
////
////  if (latLonRatio < 0.15) {
////    _lastMeetsRenderCriteriaResult = longitudeMiddleArcDistSquared <= texWidthSquared;
////  }
////  else if (lonLatRatio < 0.15) {
////    _lastMeetsRenderCriteriaResult = latitudeMiddleArcDistSquared <= texHeightSquared;
////  }
////  else {
//    _lastMeetsRenderCriteriaResult = (latitudeMiddleArcDistSquared * longitudeMiddleArcDistSquared) <= (texHeightSquared * texWidthSquared);
////  }

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
                                   TileRasterizer* tileRasterizer,
                                   const LayerTilesRenderParameters* layerTilesRenderParameters,
                                   const LayerSet* layerSet,
                                   const TilesRenderParameters* tilesRenderParameters,
                                   bool isForcedFullRender,
                                   long long texturePriority,
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

  //  TileTexturizer* texturizer = prc->getTexturizer();
  if (texturizer != NULL) {
    const bool needsToCallTexturizer = (_texturizedMesh == NULL) || isTexturizerDirty();

    if (needsToCallTexturizer) {
      _texturizedMesh = texturizer->texturize(rc,
                                              tessellator,
                                              tileRasterizer,
                                              layerTilesRenderParameters,
                                              layerSet,
                                              isForcedFullRender,
                                              texturePriority,
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
                     TileRasterizer* tileRasterizer,
                     const LayerTilesRenderParameters* layerTilesRenderParameters,
                     const LayerSet* layerSet,
                     const TilesRenderParameters* tilesRenderParameters,
                     bool isForcedFullRender,
                     long long texturePriority,
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
                                              tileRasterizer,
                                              layerTilesRenderParameters,
                                              layerSet,
                                              isForcedFullRender,
                                              texturePriority,
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

      //tessellatorMesh->render(rc, glState);
    }
  }


  //  const BoundingVolume* boundingVolume = getBoundingVolume(rc, trc);
  //  boundingVolume->render(rc, parentState);
}

void Tile::debugRender(const G3MRenderContext* rc,
                       const GLState* glState,
                       const TileTessellator* tessellator,
                       const LayerTilesRenderParameters* layerTilesRenderParameters) {
  Mesh* debugMesh = getDebugMesh(rc, tessellator, layerTilesRenderParameters);
  if (debugMesh != NULL) {
    //debugMesh->render(rc);
    debugMesh->render(rc,glState);
  }
}


std::vector<Tile*>* Tile::getSubTiles(const bool mercator) {
  if (_subtiles != NULL) {
    // quick check to avoid splitLongitude/splitLatitude calculation
    return _subtiles;
  }

  const Geodetic2D lower = _sector._lower;
  const Geodetic2D upper = _sector._upper;

  const Angle splitLongitude = Angle::midAngle(lower._longitude,
                                               upper._longitude);


  const Angle splitLatitude = mercator
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
                       ElevationDataProvider* elevationDataProvider) {
  if (texturizer != NULL) {
    texturizer->tileToBeDeleted(this, _texturizedMesh);
  }

  if (elevationDataProvider != NULL) {
    //cancelElevationDataRequest(elevationDataProvider);
    if (_elevationDataRequest != NULL) {
      _elevationDataRequest->cancelRequest();
    }
  }
}

void Tile::prune(TileTexturizer* texturizer,
                 ElevationDataProvider* elevationDataProvider) {
  if (_subtiles != NULL) {

    //Notifying elevation event when LOD decreases
    _planetRenderer->sectorElevationChanged(_elevationData);

    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);

      subtile->setIsVisible(false, texturizer);

      subtile->prune(texturizer, elevationDataProvider);
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
                  std::list<Tile*>* toVisitInNextIteration,
                  const Planet* planet,
                  const Vector3D& cameraNormalizedPosition,
                  double cameraAngle2HorizonInRadians,
                  const Frustum* cameraFrustumInModelCoordinates,
                  TilesStatistics* tilesStatistics,
                  const float verticalExaggeration,
                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                  TileTexturizer* texturizer,
                  const TilesRenderParameters* tilesRenderParameters,
                  ITimer* lastSplitTimer,
                  ElevationDataProvider* elevationDataProvider,
                  const TileTessellator* tessellator,
                  TileRasterizer* tileRasterizer,
                  const LayerSet* layerSet,
                  const Sector* renderedSector,
                  bool isForcedFullRender,
                  long long texturePriority,
                  double texWidthSquared,
                  double texHeightSquared,
                  double nowInMS,
                  const bool renderTileMeshes,
                  bool logTilesPetitions,
                  TileRenderingListener* tileRenderingListener) {

  tilesStatistics->computeTileProcessed(this);

  if (verticalExaggeration != _verticalExaggeration) {
    // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
    _verticalExaggeration = verticalExaggeration;
  }

  bool rendered = false;

  if (isVisible(rc,
                planet,
                cameraNormalizedPosition,
                cameraAngle2HorizonInRadians,
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
                                             ? texturePriority + layerTilesRenderParameters->_maxLevel - _level
                                             : texturePriority + _level);

      rendered = true;
      if (renderTileMeshes) {
        rawRender(rc,
                  &parentState,
                  texturizer,
                  elevationDataProvider,
                  tessellator,
                  tileRasterizer,
                  layerTilesRenderParameters,
                  layerSet,
                  tilesRenderParameters,
                  isForcedFullRender,
                  tileTexturePriority,
                  logTilesPetitions);
      }
      if (tilesRenderParameters->_renderDebug) {
        debugRender(rc, &parentState, tessellator, layerTilesRenderParameters);
      }

      tilesStatistics->computePlanetRenderered(this);

      prune(texturizer, elevationDataProvider);
      //TODO: AVISAR CAMBIO DE TERRENO
    }
    else {
      std::vector<Tile*>* subTiles = getSubTiles(layerTilesRenderParameters->_mercator);
      if (_justCreatedSubtiles) {
        lastSplitTimer->start();
        tilesStatistics->computeSplitInFrame();
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
    setIsVisible(false, texturizer);

    prune(texturizer, elevationDataProvider);
    //TODO: AVISAR CAMBIO DE TERRENO
  }

  if (_rendered != rendered) {
    _rendered = rendered;

    if (tileRenderingListener != NULL) {
      if (_rendered) {
        tileRenderingListener->startRendering(this);
      }
      else {
        tileRenderingListener->stopRendering(this);
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

  return subTiles;
}

const TileKey Tile::getKey() const {
  return TileKey(_level, _row, _column);
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
        const int subtilesSize = _subtiles->size();
        for (int i = 0; i < subtilesSize; i++) {
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
  else {
    printf("break point on me\n");
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
    const int subtilesSize = _subtiles->size();
    for (int i = 0; i < subtilesSize; i++) {
      Tile* subtile = _subtiles->at(i);
      subtile->ancestorChangedElevationData(this);
    }
  }
}

ElevationData* Tile::createElevationDataSubviewFromAncestor(Tile* ancestor) const{
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

//  if (_middleNorthPoint == NULL) {
//    ILogger::instance()->logError("Error in Tile::prepareTestLODData");
//    return;
//  }
//
//  const Vector3D nN = planet->centricSurfaceNormal(*_middleNorthPoint);
//  const Vector3D nS = planet->centricSurfaceNormal(*_middleSouthPoint);
//  const Vector3D nE = planet->centricSurfaceNormal(*_middleEastPoint);
//  const Vector3D nW = planet->centricSurfaceNormal(*_middleWestPoint);
//
//  const Angle latitudeAngle = nN.angleBetween(nS);
//  double latRad = latitudeAngle._radians;
//  const double sin_lat_2 = SIN(latRad / 2);
//  const double latitudeArcSegmentRatio = (sin_lat_2 == 0) ? 1 : latRad / (2 * sin_lat_2);
//
//  const Angle longitudeAngle = nE.angleBetween(nW);
//  const double lonRad = longitudeAngle._radians;
//  const double sin_lon_2 = SIN(lonRad / 2);
//  const double longitudeArcSegmentRatio = (sin_lon_2 == 0) ? 1 : lonRad / (2 * sin_lon_2);
//
//  _latitudeArcSegmentRatioSquared  = latitudeArcSegmentRatio * latitudeArcSegmentRatio;
//  _longitudeArcSegmentRatioSquared = longitudeArcSegmentRatio * longitudeArcSegmentRatio;

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

  const double angleInRadians = a.angleInRadiansBetween(b);
  const double halfAngleSin = SIN(angleInRadians / 2);
  const double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
  return (arcSegmentRatio * arcSegmentRatio);
}

void Tile::computeTileCorners(const Planet* planet) {

  if (_tessellatorMesh == NULL) {
    ILogger::instance()->logError("Error in Tile::computeTileCorners");
    return;
  }

//  delete _middleWestPoint;
//  delete _middleEastPoint;
//  delete _middleNorthPoint;
//  delete _middleSouthPoint;
  delete _northWestPoint;
  delete _northEastPoint;
  delete _southWestPoint;
  delete _southEastPoint;


  const double mediumHeight = _tileTessellatorMeshData._averageHeight;

//  const Geodetic2D center = _sector.getCenter();
//  const Geodetic3D gN( Geodetic2D(_sector.getNorth(), center._longitude), mediumHeight);
//  const Geodetic3D gS( Geodetic2D(_sector.getSouth(), center._longitude), mediumHeight);
//  const Geodetic3D gW( Geodetic2D(center._latitude, _sector.getWest()), mediumHeight);
//  const Geodetic3D gE( Geodetic2D(center._latitude, _sector.getEast()), mediumHeight);
//
//  _middleNorthPoint = new Vector3D(planet->toCartesian(gN));
//  _middleSouthPoint = new Vector3D(planet->toCartesian(gS));
//  _middleEastPoint = new Vector3D(planet->toCartesian(gE));
//  _middleWestPoint = new Vector3D(planet->toCartesian(gW));

  _northWestPoint = new Vector3D( planet->toCartesian( _sector.getNW(), mediumHeight ) );
  _northEastPoint = new Vector3D( planet->toCartesian( _sector.getNE(), mediumHeight ) );
  _southWestPoint = new Vector3D( planet->toCartesian( _sector.getSW(), mediumHeight ) );
  _southEastPoint = new Vector3D( planet->toCartesian( _sector.getSE(), mediumHeight ) );
}

Vector2I Tile::getNormalizedPixelsFromPosition(const Geodetic2D& position2D,
                                               const Vector2I& tileDimension) const{
  const IMathUtils* math = IMathUtils::instance();
  const Vector2D uv = _sector.getUVCoordinates(position2D);
  return Vector2I(math->toInt(tileDimension._x * uv._x), math->toInt(tileDimension._y * uv._y));
}
