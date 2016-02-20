//
//  ProjectedCornersDistanceTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "ProjectedCornersDistanceTileLODTester.hpp"

#include "Tile.hpp"
#include "G3MContext.hpp"
#include "Camera.hpp"
#include "PlanetRenderContext.hpp"


ProjectedCornersDistanceTileLODTester::ProjectedCornersDistanceTileLODTester()
{
}


ProjectedCornersDistanceTileLODTester::~ProjectedCornersDistanceTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

ProjectedCornersDistanceTileLODTester::PvtData* ProjectedCornersDistanceTileLODTester::getData(const Tile* tile,
                                                                                               const G3MRenderContext* rc) const {
  PvtData* data = (PvtData*) tile->getData(ProjectedCornersDistanceTLTDataID);
  if (data == NULL) {
    const double mediumHeight = tile->getTessellatorMeshData()->_averageHeight;
    data = new PvtData(tile, mediumHeight, rc->getPlanet());
    tile->setData(data);
  }
  return data;
}

bool ProjectedCornersDistanceTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                                const PlanetRenderContext* prc,
                                                                const Tile* tile) const {
  return getData(tile, rc)->evaluate(rc->getCurrentCamera(),
                                     prc->_texHeightSquared,
                                     prc->_texWidthSquared);
}

double ProjectedCornersDistanceTileLODTester::PvtData::getSquaredArcSegmentRatio(const Vector3D& a,
                                                                                 const Vector3D& b) {
  /*
   Arco = ang * Cuerda / (2 * sen(ang/2))
   */

  const double angleInRadians = Vector3D::angleInRadiansBetween(a, b);
  const double halfAngleSin = SIN(angleInRadians / 2);
  const double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
  return (arcSegmentRatio * arcSegmentRatio);
}

ProjectedCornersDistanceTileLODTester::PvtData::PvtData(const Tile* tile,
                                                        double mediumHeight,
                                                        const Planet* planet):
TileData(ProjectedCornersDistanceTLTDataID),
_northWestPoint( planet->toCartesian( tile->_sector.getNW(), mediumHeight ) ),
_northEastPoint( planet->toCartesian( tile->_sector.getNE(), mediumHeight ) ),
_southWestPoint( planet->toCartesian( tile->_sector.getSW(), mediumHeight ) ),
_southEastPoint( planet->toCartesian( tile->_sector.getSE(), mediumHeight ) )
{
  const Vector3D normalNW = planet->centricSurfaceNormal(_northWestPoint);
  const Vector3D normalNE = planet->centricSurfaceNormal(_northEastPoint);
  const Vector3D normalSW = planet->centricSurfaceNormal(_southWestPoint);
  const Vector3D normalSE = planet->centricSurfaceNormal(_southEastPoint);

  _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
  _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
  _eastArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNE, normalSE);
  _westArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNW, normalSW);
}

bool ProjectedCornersDistanceTileLODTester::PvtData::evaluate(const Camera* camera,
                                                              double texHeightSquared,
                                                              double texWidthSquared) {
  
  const double distanceInPixelsNorth = camera->getEstimatedPixelDistance(_northWestPoint, _northEastPoint);
  const double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
  if (distanceInPixelsSquaredArcNorth > texWidthSquared){
    return false;
  }
  
  const double distanceInPixelsSouth = camera->getEstimatedPixelDistance(_southWestPoint, _southEastPoint);
  const double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
  if (distanceInPixelsSquaredArcSouth > texWidthSquared){
    return false;
  }
  
  const double distanceInPixelsWest  = camera->getEstimatedPixelDistance(_northWestPoint, _southWestPoint);
  const double distanceInPixelsSquaredArcWest  = (distanceInPixelsWest  * distanceInPixelsWest)  * _westArcSegmentRatioSquared;
  if (distanceInPixelsSquaredArcWest > texHeightSquared){
    return false;
  }
  
  const double distanceInPixelsEast  = camera->getEstimatedPixelDistance(_northEastPoint, _southEastPoint);
  const double distanceInPixelsSquaredArcEast  = (distanceInPixelsEast  * distanceInPixelsEast)  * _eastArcSegmentRatioSquared;
  if (distanceInPixelsSquaredArcEast > texHeightSquared){
    return false;
  }
  
  return true;
}
