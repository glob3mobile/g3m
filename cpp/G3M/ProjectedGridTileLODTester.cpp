//
//  ProjectedGridTileLODTester.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/10/26.
//
//

#include "ProjectedGridTileLODTester.hpp"

#include "Tile.hpp"
#include "Camera.hpp"
#include "PlanetRenderContext.hpp"
#include "G3MRenderContext.hpp"
#include "Planet.hpp"
#include "Angle.hpp"
#include "Geodetic2D.hpp"
#include "Sector.hpp"
#include "Vector2F.hpp"
#include "MercatorUtils.hpp"


ProjectedGridTileLODTester::ProjectedGridTileLODTester(double thresholdFactor) :
_thresholdFactorSquared(thresholdFactor * thresholdFactor)
{
}

ProjectedGridTileLODTester::~ProjectedGridTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

ProjectedGridTileLODTester::PvtData* ProjectedGridTileLODTester::getData(const Tile* tile,
                                                                         const G3MRenderContext* rc) const {
  PvtData* data = (PvtData*) tile->getData(ProjectedGridTLTDataID);
  if (data == NULL) {
    const double averageHeight = tile->getTileTessellatorMeshData()->_averageHeight;
    data = new PvtData(tile, averageHeight, rc->getPlanet());
    tile->setData(data);
  }
  return data;
}

void ProjectedGridTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
  // the points were placed at the average height of the previous mesh
  tile->clearDataWithID(ProjectedGridTLTDataID);
}

bool ProjectedGridTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                     const PlanetRenderContext* prc,
                                                     const Tile* tile) const {
  return getData(tile, rc)->evaluate(rc->getCurrentCamera(),
                                     prc->_texHeightSquared * _thresholdFactorSquared,
                                     prc->_texWidthSquared  * _thresholdFactorSquared);
}

const Angle ProjectedGridTileLODTester::PvtData::splitLatitude(const Tile* tile) {
  const Sector& sector = tile->_sector;
  return tile->_mercator
  /*  */ ? MercatorUtils::calculateSplitLatitude(sector._lower._latitude, sector._upper._latitude)
  /*  */ : Angle::midAngle(sector._lower._latitude, sector._upper._latitude);
}

const Angle ProjectedGridTileLODTester::PvtData::splitLongitude(const Tile* tile) {
  const Sector& sector = tile->_sector;
  return Angle::midAngle(sector._lower._longitude, sector._upper._longitude);
}

ProjectedGridTileLODTester::PvtData::PvtData(const Tile* tile,
                                             double averageHeight,
                                             const Planet* planet):
TileData(ProjectedGridTLTDataID),
_northWest( planet->toCartesian( Geodetic2D(tile->_sector._upper._latitude, tile->_sector._lower._longitude), averageHeight ) ),
_north(     planet->toCartesian( Geodetic2D(tile->_sector._upper._latitude, splitLongitude(tile)),           averageHeight ) ),
_northEast( planet->toCartesian( Geodetic2D(tile->_sector._upper._latitude, tile->_sector._upper._longitude), averageHeight ) ),
_west(      planet->toCartesian( Geodetic2D(splitLatitude(tile),            tile->_sector._lower._longitude), averageHeight ) ),
_center(    planet->toCartesian( Geodetic2D(splitLatitude(tile),            splitLongitude(tile)),           averageHeight ) ),
_east(      planet->toCartesian( Geodetic2D(splitLatitude(tile),            tile->_sector._upper._longitude), averageHeight ) ),
_southWest( planet->toCartesian( Geodetic2D(tile->_sector._lower._latitude, tile->_sector._lower._longitude), averageHeight ) ),
_south(     planet->toCartesian( Geodetic2D(tile->_sector._lower._latitude, splitLongitude(tile)),           averageHeight ) ),
_southEast( planet->toCartesian( Geodetic2D(tile->_sector._lower._latitude, tile->_sector._upper._longitude), averageHeight ) )
{
}

bool ProjectedGridTileLODTester::PvtData::isBehind(const Vector3D& point,
                                                   const Vector3D& cameraPosition,
                                                   const Vector3D& viewDirection) {
  return point.sub(cameraPosition).dot(viewDirection) <= 0;
}

double ProjectedGridTileLODTester::PvtData::squaredPixelDistance(const Camera* camera,
                                                                 const Vector3D& point0,
                                                                 const Vector3D& point1) {
  const Vector2F pixel0 = camera->point2Pixel(point0);
  const Vector2F pixel1 = camera->point2Pixel(point1);
  const double dx = pixel1._x - pixel0._x;
  const double dy = pixel1._y - pixel0._y;
  return (dx * dx) + (dy * dy);
}

bool ProjectedGridTileLODTester::PvtData::evaluate(const Camera* camera,
                                                   double texHeightSquared,
                                                   double texWidthSquared) const {
  const Vector3D cameraPosition = camera->getCartesianPosition();
  const Vector3D viewDirection  = camera->getViewDirection();

  // a tile reaching behind the eye is as close as a tile can be
  if (isBehind(_northWest, cameraPosition, viewDirection) ||
      isBehind(_north,     cameraPosition, viewDirection) ||
      isBehind(_northEast, cameraPosition, viewDirection) ||
      isBehind(_west,      cameraPosition, viewDirection) ||
      isBehind(_center,    cameraPosition, viewDirection) ||
      isBehind(_east,      cameraPosition, viewDirection) ||
      isBehind(_southWest, cameraPosition, viewDirection) ||
      isBehind(_south,     cameraPosition, viewDirection) ||
      isBehind(_southEast, cameraPosition, viewDirection)) {
    return false;
  }

  // every segment spans half the tile, so half the texture
  const double halfTexWidthSquared  = texWidthSquared  / 4;
  const double halfTexHeightSquared = texHeightSquared / 4;

  if ((squaredPixelDistance(camera, _northWest, _north)     > halfTexWidthSquared) ||
      (squaredPixelDistance(camera, _north,     _northEast) > halfTexWidthSquared) ||
      (squaredPixelDistance(camera, _west,      _center)    > halfTexWidthSquared) ||
      (squaredPixelDistance(camera, _center,    _east)      > halfTexWidthSquared) ||
      (squaredPixelDistance(camera, _southWest, _south)     > halfTexWidthSquared) ||
      (squaredPixelDistance(camera, _south,     _southEast) > halfTexWidthSquared)) {
    return false;
  }

  if ((squaredPixelDistance(camera, _northWest, _west)      > halfTexHeightSquared) ||
      (squaredPixelDistance(camera, _west,      _southWest) > halfTexHeightSquared) ||
      (squaredPixelDistance(camera, _north,     _center)    > halfTexHeightSquared) ||
      (squaredPixelDistance(camera, _center,    _south)     > halfTexHeightSquared) ||
      (squaredPixelDistance(camera, _northEast, _east)      > halfTexHeightSquared) ||
      (squaredPixelDistance(camera, _east,      _southEast) > halfTexHeightSquared)) {
    return false;
  }

  return true;
}
