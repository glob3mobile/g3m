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

Geodetic3D* LAST_CAMERA_POS = NULL;

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
_lastLodTimeInMS(0),
_planetRenderer(planetRenderer),
_tessellatorData(NULL),
_middleNorthPoint(NULL),
_middleSouthPoint(NULL),
_middleEastPoint(NULL),
_middleWestPoint(NULL),
_latitudeArcSegmentRatioSquared(0),
_longitudeArcSegmentRatioSquared(0)
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

  delete _middleEastPoint;
  delete _middleNorthPoint;
  delete _middleSouthPoint;
  delete _middleWestPoint;
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

    if (_textureSolved){
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

  if (_lastLodTimeInMS != 0 &&
      (nowInMS - _lastLodTimeInMS) < 500 ){
    return _lastLodTest;
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

  _lastLodTimeInMS = nowInMS; //Storing time of result


//  const int texWidth  = layerTilesRenderParameters->_tileTextureResolution._x;
//  const int texHeight = layerTilesRenderParameters->_tileTextureResolution._y;
//
//  double factor = 5;
//  switch (tilesRenderParameters->_quality) {
//    case QUALITY_HIGH:
//      factor = 1.5;
//      break;
//    case QUALITY_MEDIUM:
//      factor = 3;
//      break;
//      //case QUALITY_LOW:
//    default:
//      factor = 5;
//      break;
//  }
//  const Box* boundingVolume = getTileBoundingVolume(rc);
//  if (boundingVolume == NULL) {
//    return true;
//  }
//
//  const Vector2F ex = boundingVolume->projectedExtent(rc);
//  const float t = (ex._x * ex._y);
//  _lastLodTest = t <= ((texWidth * texHeight) * ((factor * deviceQualityFactor) / dpiFactor));


  if ((_latitudeArcSegmentRatioSquared  == 0) ||
      (_longitudeArcSegmentRatioSquared == 0)) {
    prepareTestLODData( rc->getPlanet() );
  }

  
//  if ( _level == 10 && _column == 2119 && _row == 1439 ) {
//    int a = 0;
//    a++;
//  }

  
  int AGUSTIN_AT_WORK;
  
  // instead of project points to pixels, and then compute distance between pixels
  // it's better to compute angle between projected rays of each point
  
  // compute max angle of the tile from the observer
  const Camera* camera = rc->getCurrentCamera();
  const Vector3D cameraPosition = camera->getCartesianPosition();
  const Vector3D rayMiddleNorth = cameraPosition.sub(*_middleNorthPoint);
  const Vector3D rayMiddleSouth = cameraPosition.sub(*_middleSouthPoint);
  const Angle angleNS = rayMiddleNorth.angleBetween(rayMiddleSouth);
  const Vector3D rayMiddleWest = cameraPosition.sub(*_middleWestPoint);
  const Vector3D rayMiddleEast = cameraPosition.sub(*_middleEastPoint);
  const Angle angleWE = rayMiddleWest.angleBetween(rayMiddleEast);
  
  /*
  // compute the angle threshold, that represents 256 pixels on the screen
  double top = camera->getFrustumData()._top;
  double znear = camera->getFrustumData()._znear;
  double X = top * 256 / camera->getHeight();
  double halfAngle = acos(znear/sqrt(znear*znear+X*X));
  ********** IT'S ALWAYS THE SAME VALUE FOR EVERY CAMERA POSITION!! = 
  ********** halfAngle = 5.71 degrees *********
  ********** thresholdAngle = 11.42 degrees *****
   */
  
  // simple test condition --> the max angle represents less than 256 pixels
  // const Angle maxAngle = Angle::max(angleNS, angleWE);
  //_lastLodTest = (maxAngle._degrees < 11.42)? true : false;

  // this condition is better in the horizon --> the sum of the angles represent less than 512 pixels
  _lastLodTest = angleNS.add(angleWE)._degrees < 22.84;

  
  /*const Vector2F pN = camera->point2Pixel(*_middleNorthPoint);
  const Vector2F pS = camera->point2Pixel(*_middleSouthPoint);
  const Vector2F pE = camera->point2Pixel(*_middleEastPoint);
  const Vector2F pW = camera->point2Pixel(*_middleWestPoint);

  const double latitudeMiddleDistSquared  = pN.squaredDistanceTo(pS);
  const double longitudeMiddleDistSquared = pE.squaredDistanceTo(pW);

//  const double latitudeMiddleDist = sqrt( latitudeMiddleDistSquared );
//  const double longitudeMiddleDist = sqrt( longitudeMiddleDistSquared );

  const double latitudeMiddleArcDistSquared  = latitudeMiddleDistSquared  * _latitudeArcSegmentRatioSquared;
  const double longitudeMiddleArcDistSquared = longitudeMiddleDistSquared * _longitudeArcSegmentRatioSquared;

  const double latLonRatio = latitudeMiddleArcDistSquared  / longitudeMiddleArcDistSquared;
  const double lonLatRatio = longitudeMiddleArcDistSquared / latitudeMiddleArcDistSquared;



  if ( _sector.contains(LAST_CAMERA_POS->asGeodetic2D()) ){
    Plane p = camera->getZ0Plane();

    double dN = p.signedDistance(*_middleNorthPoint);
    double dS = p.signedDistance(*_middleSouthPoint);
    double dE = p.signedDistance(*_middleEastPoint);
    double dW = p.signedDistance(*_middleWestPoint);

    ILogger::instance()->logInfo("N: %f %f, S: %f %f, E: %f %f W: %f %f", pN._x, pN._y, pS._x, pS._y, pE._x, pE._y, pW._x, pW._y);
    ILogger::instance()->logInfo("NS: %f, EW: %f", sqrt(pN.squaredDistanceTo(pS)) , sqrt(pW.squaredDistanceTo(pE)));

  }

  if (latLonRatio < 0.15) {
    _lastLodTest = longitudeMiddleArcDistSquared <= texWidthSquared;
  }
  else if (lonLatRatio < 0.15) {
    _lastLodTest = latitudeMiddleArcDistSquared <= texHeightSquared;
  }
  else {
    _lastLodTest = (latitudeMiddleArcDistSquared * longitudeMiddleArcDistSquared) <= (texHeightSquared * texWidthSquared);
  }

  if ( _level == 10 && _column == 2119 && _row == 1439 ) {

    ILogger::instance()->logInfo("N: %f %f, S: %f %f, E: %f %f W: %f %f", pN._x, pN._y, pS._x, pS._y, pE._x, pE._y, pW._x, pW._y);
    ILogger::instance()->logInfo("NS: %f, EW: %f", sqrt(pN.squaredDistanceTo(pS)) , sqrt(pW.squaredDistanceTo(pE)));

  }

#warning Tile-LOD bug
  if (_lastLodTest && _sector.contains(LAST_CAMERA_POS->asGeodetic2D())) {


    printf("break point on me: meetsRenderCriteria at level %d\n   latitudeMiddleDist=%f\n   longitudeMiddleDist=%f\n   latitudeMiddleArcDist=%f\n   longitudeMiddleArcDist=%f\n   latLonRatio=%f\n   lonLonRatio=%f\n",
           _level,
           sqrt(latitudeMiddleDistSquared),
           sqrt(longitudeMiddleDistSquared),
           sqrt(latitudeMiddleArcDistSquared),
           sqrt(longitudeMiddleArcDistSquared),
           latLonRatio,
           lonLatRatio
           );

  //Testing Area
//  _lastLodTest = (latitudeMiddleArcDistSquared * longitudeMiddleArcDistSquared) <= (texHeightSquared * texWidthSquared);

#warning Tile-LOD bug
//  if (_lastLodTest) {
////    printf("break point on me: meetsRenderCriteria at level %d\n   latitudeMiddleDistSquared=%f\n   longitudeMiddleDistSquared=%f\n   latitudeMiddleArcDistSquared=%f\n   longitudeMiddleArcDistSquared=%f\n   latLonRatio=%f\n   lonLonRatio=%f\n",
////           _level,
////           latitudeMiddleDistSquared,
////           longitudeMiddleDistSquared,
////           latitudeMiddleArcDistSquared,
////           longitudeMiddleArcDistSquared,
////           latLonRatio,
////           lonLatRatio
////           );
//    printf(">> meetsRenderCriteria at level %d latLonRatio=%f lonLatRatio=%f\n",
//           _level,
//           latLonRatio,
//           lonLatRatio
//           );
//  }
//
//#warning remove-debug-code
//  if (_level == 10 && _column == 2119 && _row == 1439 ) {
//    printf("dddd");
//  }

    printf(">> meetsRenderCriteria at level %d latLonRatio=%f lonLatRatio=%f\n",
           _level,
           latLonRatio,
           lonLatRatio
           );

  }
*/

  /*
   BAD:
   2014-01-30 11:23:17.885 G3MiOSDemo[8358:60b] Info: Touched on (Tile level=10, row=1439, column=1976, sector=(Sector (lat=36.474609375000007105d, lon=-6.328125d) - (lat=36.5625d, lon=-6.2402343749999991118d)))
   2014-01-30 11:23:17.887 G3MiOSDemo[8358:60b] Info: Camera position=(lat=36.516484364230244353d, lon=-6.2802353330784788099d, height=60.391400095963234662) heading=45.450683 pitch=61.017430
   
   GOOD:
   2014-01-30 11:23:45.400 G3MiOSDemo[8358:60b] Info: Touched on (Tile level=17, row=184253, column=252998, sector=(Sector (lat=36.5164947509765625d, lon=-6.280059814453125d) - (lat=36.517181396484375d, lon=-6.2793731689453116118d)))
   2014-01-30 11:23:45.402 G3MiOSDemo[8358:60b] Info: Camera position=(lat=36.516058816654393127d, lon=-6.2798670606496447277d, height=74.299752202274888191) heading=19.412124 pitch=61.017203
   */

  
//  if ( _level == 10 && _column == 2119 && _row == 1439 ) {
//    int a = 0;
//    a++;
//  }

  return _lastLodTest;
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
                  bool logTilesPetitions) {

  tilesStatistics->computeTileProcessed(this);

  if (verticalExaggeration != _verticalExaggeration) {
    // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
    _verticalExaggeration = verticalExaggeration;
  }

  delete LAST_CAMERA_POS;
  LAST_CAMERA_POS = new Geodetic3D(rc->getCurrentCamera()->getGeodeticPosition() );


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
                  texturePriority,
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
      const Geodetic2D lower = _sector._lower;
      const Geodetic2D upper = _sector._upper;

      const Angle splitLongitude = Angle::midAngle(lower._longitude,
                                                   upper._longitude);

      const Angle splitLatitude = layerTilesRenderParameters->_mercator
      /*                               */ ? MercatorUtils::calculateSplitLatitude(lower._latitude,
                                                                                  upper._latitude)
      /*                               */ : Angle::midAngle(lower._latitude,
                                                            upper._latitude);

      std::vector<Tile*>* subTiles = getSubTiles(splitLatitude, splitLongitude);
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

void Tile::prepareTestLODData(const Planet* planet){

  if (_middleNorthPoint == NULL){
    ILogger::instance()->logError("Error in Tile::prepareTestLODData");
    return;
  }

  const Vector3D nN = planet->centricSurfaceNormal(*_middleNorthPoint);
  const Vector3D nS = planet->centricSurfaceNormal(*_middleSouthPoint);
  const Vector3D nE = planet->centricSurfaceNormal(*_middleEastPoint);
  const Vector3D nW = planet->centricSurfaceNormal(*_middleWestPoint);

  /*
   Arco = ang * Cuerda / (2 * sen(ang/2))
   */

  Angle latitudeAngle = nN.angleBetween(nS);
  double latRad = latitudeAngle._radians;
  const double sin_lat_2 = SIN(latRad / 2);
  const double latitudeArcSegmentRatio = sin_lat_2 == 0? 1 : latRad / (2 * sin_lat_2);

  Angle longitudeAngle = nE.angleBetween(nW);
  const double lonRad = longitudeAngle._radians;
  const double sin_lon_2 = SIN(lonRad / 2);
  const double longitudeArcSegmentRatio = sin_lon_2 == 0? 1 : lonRad / (2 * sin_lon_2);

  _latitudeArcSegmentRatioSquared = latitudeArcSegmentRatio * latitudeArcSegmentRatio;
  _longitudeArcSegmentRatioSquared = longitudeArcSegmentRatio * longitudeArcSegmentRatio;
}

void Tile::computeTileCorners(const Planet* planet){

  if (_tessellatorMesh == NULL){
    ILogger::instance()->logError("Error in Tile::computeTileCorners");
    return;
  }

//    if ( _level == 10 && _column == 2119 && _row == 1439 ) {
//      int a = 0;
//      a++;
//    }

  delete _middleWestPoint;
  delete _middleEastPoint;
  delete _middleNorthPoint;
  delete _middleSouthPoint;

  const double mediumHeight = _tileTessellatorMeshData._averageHeight;

  const Geodetic2D center = _sector.getCenter();
  const Geodetic3D gN( Geodetic2D(_sector.getNorth(), center._longitude), mediumHeight);
  const Geodetic3D gS( Geodetic2D(_sector.getSouth(), center._longitude), mediumHeight);
  const Geodetic3D gW( Geodetic2D(center._latitude, _sector.getWest()), mediumHeight);
  const Geodetic3D gE( Geodetic2D(center._latitude, _sector.getEast()), mediumHeight);

  _middleNorthPoint = new Vector3D(planet->toCartesian(gN));
  _middleSouthPoint = new Vector3D(planet->toCartesian(gS));
  _middleEastPoint = new Vector3D(planet->toCartesian(gE));
  _middleWestPoint = new Vector3D(planet->toCartesian(gW));

#warning remove-debug-code
  if (_level == 10 && _column == 2119 && _row == 1439 ) {
    printf("dddd");
  }
}
