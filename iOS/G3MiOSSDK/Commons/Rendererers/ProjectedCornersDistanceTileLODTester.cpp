//
//  ProjectedCornersDistanceTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "ProjectedCornersDistanceTileLODTester.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "BoundingVolume.hpp"
#include "Camera.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "Mesh.hpp"


ProjectedCornersDistanceTileLODTester::ProjectedCornersDistanceTileLODTester(TileLODTester* nextTesterRightLOD,
                                                                             TileLODTester* nextTesterWrongLOD,
                                                                             TileLODTester* nextTesterVisible,
                                                                             TileLODTester* nextTesterNotVisible):
TileLODTesterResponder(nextTesterRightLOD,
                       nextTesterWrongLOD,
                       nextTesterVisible,
                       nextTesterNotVisible)
{
}


ProjectedCornersDistanceTileLODTester::~ProjectedCornersDistanceTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void ProjectedCornersDistanceTileLODTester::_onTileHasChangedMesh(Tile* tile) const {
  //Recomputing data when tile changes tessellator mesh
  tile->setDataForLODTester(_id, NULL);
}

ProjectedCornersDistanceTileLODTester::PCDTesterData* ProjectedCornersDistanceTileLODTester::getData(Tile* tile,
                                                                                                     const G3MRenderContext* rc) const {
  PCDTesterData* data = (PCDTesterData*) tile->getDataForLODTester(_id);
  if (data == NULL) {
    const double mediumHeight = tile->getTessellatorMeshData()->_averageHeight;
    data = new PCDTesterData(tile, mediumHeight, rc->getPlanet());
    tile->setDataForLODTester(_id, data);
  }
  return data;
}

bool ProjectedCornersDistanceTileLODTester::_meetsRenderCriteria(Tile* tile,
                                                                 const G3MRenderContext* rc,
                                                                 const TilesRenderParameters* tilesRenderParameters,
                                                                 const ITimer* lastSplitTimer,
                                                                 const double texWidthSquared,
                                                                 const double texHeightSquared,
                                                                 long long nowInMS) const {
  PCDTesterData* data = getData(tile, rc);

  return data->evaluate(rc->getCurrentCamera(), texHeightSquared, texWidthSquared);
}

bool ProjectedCornersDistanceTileLODTester::_isVisible(Tile* tile,
                                                       const G3MRenderContext* rc) const {
  PCDTesterData* data = getData(tile, rc);
  return data->_bvol->touchesFrustum(rc->getCurrentCamera()->getFrustumInModelCoordinates());
}

void ProjectedCornersDistanceTileLODTester::_onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
}

double ProjectedCornersDistanceTileLODTester::PCDTesterData::getSquaredArcSegmentRatio(const Vector3D& a,
                                                                                       const Vector3D& b) {
  /*
   Arco = ang * Cuerda / (2 * sen(ang/2))
   */

  const double angleInRadians = Vector3D::angleInRadiansBetween(a, b);
  const double halfAngleSin = SIN(angleInRadians / 2);
  const double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
  return (arcSegmentRatio * arcSegmentRatio);
}


ProjectedCornersDistanceTileLODTester::PCDTesterData::PCDTesterData(Tile* tile,
                                                                    double mediumHeight,
                                                                    const Planet* planet):
TileLODTesterData(),
_northWestPoint(planet->toCartesian( tile->_sector.getNW(), mediumHeight )),
_northEastPoint(planet->toCartesian( tile->_sector.getNE(), mediumHeight )),
_southWestPoint(planet->toCartesian( tile->_sector.getSW(), mediumHeight )),
_southEastPoint(planet->toCartesian( tile->_sector.getSE(), mediumHeight ))
{
  const Vector3D normalNW = planet->centricSurfaceNormal(_northWestPoint);
  const Vector3D normalNE = planet->centricSurfaceNormal(_northEastPoint);
  const Vector3D normalSW = planet->centricSurfaceNormal(_southWestPoint);
  const Vector3D normalSE = planet->centricSurfaceNormal(_southEastPoint);

  _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
  _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
  _eastArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNE, normalSE);
  _westArcSegmentRatioSquared  = getSquaredArcSegmentRatio(normalNW, normalSW);

  //Computing Bounding Volume

  const Mesh* mesh = tile->getCurrentTessellatorMesh();
  if (mesh == NULL) {
    ILogger::instance()->logError("Problem computing BVolume in ProjectedCornersDistanceTileLODTesterData");
    _bvol = NULL;
  }
  else {
    _bvol = mesh->getBoundingVolume(); //BV is deleted by mesh
  }

}

bool ProjectedCornersDistanceTileLODTester::PCDTesterData::evaluate(const Camera* camera,
                                                                    double texHeightSquared,
                                                                    double texWidthSquared) {

  const double distanceInPixelsNorth = camera->getEstimatedPixelDistance(_northWestPoint, _northEastPoint);
  const double distanceInPixelsSouth = camera->getEstimatedPixelDistance(_southWestPoint, _southEastPoint);
  const double distanceInPixelsWest  = camera->getEstimatedPixelDistance(_northWestPoint, _southWestPoint);
  const double distanceInPixelsEast  = camera->getEstimatedPixelDistance(_northEastPoint, _southEastPoint);

  const double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcWest  = (distanceInPixelsWest  * distanceInPixelsWest)  * _westArcSegmentRatioSquared;
  const double distanceInPixelsSquaredArcEast  = (distanceInPixelsEast  * distanceInPixelsEast)  * _eastArcSegmentRatioSquared;

  return ((distanceInPixelsSquaredArcNorth <= texHeightSquared) &&
          (distanceInPixelsSquaredArcSouth <= texHeightSquared) &&
          (distanceInPixelsSquaredArcWest  <= texWidthSquared ) &&
          (distanceInPixelsSquaredArcEast  <= texWidthSquared ));
}
