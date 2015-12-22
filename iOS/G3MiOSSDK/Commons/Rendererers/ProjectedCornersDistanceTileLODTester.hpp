//
//  ProjectedCornersDistanceTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef ProjectedCornersDistanceTileLODTester_hpp
#define ProjectedCornersDistanceTileLODTester_hpp


#include "TileLODTester.hpp"
#include "Tile.hpp"
#include "Planet.hpp"
#include "Context.hpp"
#include "Camera.hpp"
#include "BoundingVolume.hpp"
#include "Mesh.hpp"

class ProjectedCornersDistanceTileLODTester: public TileLODTesterResponder{
protected:
  
  class ProjectedCornersDistanceTileLODTesterData: public TileLODTesterData{
    
    double getSquaredArcSegmentRatio(const Vector3D& a,
                                     const Vector3D& b) {
      /*
       Arco = ang * Cuerda / (2 * sen(ang/2))
       */
      
      const double angleInRadians = Vector3D::angleInRadiansBetween(a, b);
      const double halfAngleSin = SIN(angleInRadians / 2);
      const double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
      return (arcSegmentRatio * arcSegmentRatio);
    }
    
  public:
    double _northArcSegmentRatioSquared;
    double _southArcSegmentRatioSquared;
    double _eastArcSegmentRatioSquared;
    double _westArcSegmentRatioSquared;
    
    Vector3D _northWestPoint;
    Vector3D _northEastPoint;
    Vector3D _southWestPoint;
    Vector3D _southEastPoint;
    
    BoundingVolume* _bvol;
    
    ProjectedCornersDistanceTileLODTesterData(Tile* tile, double mediumHeight, const Planet* planet):
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
      } else{
        _bvol = mesh->getBoundingVolume(); //BV is deleted by mesh
      }
      
    }
    
    bool evaluate(const Camera* camera, double texHeightSquared, double texWidthSquared){
      
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
    
  };
  
  void _onTileHasChangedMesh(int testerLevel, Tile* tile) const{
    //Recomputing data when tile changes tessellator mesh
    tile->setDataForLoDTester(testerLevel, NULL);
  }
  
  ProjectedCornersDistanceTileLODTesterData* getData(Tile* tile, int testerLevel, const G3MRenderContext& rc) const{
    ProjectedCornersDistanceTileLODTesterData* data = (ProjectedCornersDistanceTileLODTesterData*) tile->getDataForLoDTester(testerLevel);
    if (data == NULL){
      const double mediumHeight = tile->getTessellatorMeshData()->_averageHeight;
      data = new ProjectedCornersDistanceTileLODTesterData(tile, mediumHeight, rc.getPlanet());
      tile->setDataForLoDTester(testerLevel, data);
    }
    return data;
  }
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile,
                            const G3MRenderContext& rc) const{
    
    
    ProjectedCornersDistanceTileLODTesterData* data = getData(tile, testerLevel, rc);
    
    return data->evaluate(rc.getCurrentCamera(), _texHeightSquared, _texWidthSquared);
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile,
                  const G3MRenderContext& rc) const{
    ProjectedCornersDistanceTileLODTesterData* data = getData(tile, testerLevel, rc);
    return data->_bvol->touchesFrustum(rc.getCurrentCamera()->getFrustumInModelCoordinates());
  }
  
  double _texHeightSquared;
  double _texWidthSquared;
  
public:
  
  ProjectedCornersDistanceTileLODTester(double textureWidth,
                                        double textureHeight,
                                        TileLODTester* nextTesterRightLoD,
                                        TileLODTester* nextTesterWrongLoD,
                                        TileLODTester* nextTesterVisible,
                                        TileLODTester* nextTesterNotVisible):
  TileLODTesterResponder(nextTesterRightLoD,
                         nextTesterWrongLoD,
                         nextTesterVisible,
                         nextTesterNotVisible),
  _texHeightSquared(textureHeight * textureHeight),
  _texWidthSquared(textureWidth * textureWidth)
  {}
  
  
  ~ProjectedCornersDistanceTileLODTester(){
  }
  
};


#endif /* ProjectedCornersDistanceTileLODTester_hpp */
