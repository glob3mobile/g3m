//
//  ProjectedCornersDistanceTileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef ProjectedCornersDistanceTileLoDTester_hpp
#define ProjectedCornersDistanceTileLoDTester_hpp


#include "TileLoDTester.hpp"
#include "Tile.hpp"
#include "Planet.hpp"
#include "Context.hpp"
#include "Camera.hpp"

class ProjectedCornersDistanceTileLoDTester: public TileLoDTester{
protected:
  
  class ProjectedCornersDistanceTileLoDTesterData: public TileLoDTesterData{
    
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
    
    ProjectedCornersDistanceTileLoDTesterData(Tile* tile, double mediumHeight, const Planet* planet):
    TileLoDTesterData(),
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
    tile->setDataForLoDTester(testerLevel, NULL);
  }
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile,
                            const G3MRenderContext& rc) const{
    
    
    ProjectedCornersDistanceTileLoDTesterData* data = (ProjectedCornersDistanceTileLoDTesterData*) tile->getDataForLoDTester(testerLevel);
    if (data == NULL){
      const double mediumHeight = tile->getTessellatorMeshData()->_averageHeight;
      data = new ProjectedCornersDistanceTileLoDTesterData(tile, mediumHeight, rc.getPlanet());
      tile->setDataForLoDTester(testerLevel, data);
    }
    
    return data->evaluate(rc.getCurrentCamera(), _texHeightSquared, _texWidthSquared);
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile,
                  const G3MRenderContext& rc) const{
    return true;
  }
  
  double _texHeightSquared;
  double _texWidthSquared;
  
public:
  
  ProjectedCornersDistanceTileLoDTester(double textureWidth,
                                        double textureHeight,
                                        TileLoDTester* nextTesterRightLoD,
                                        TileLoDTester* nextTesterWrongLoD,
                                        TileLoDTester* nextTesterVisible,
                                        TileLoDTester* nextTesterNotVisible):
  _texHeightSquared(textureHeight * textureHeight),
  _texWidthSquared(textureWidth * textureWidth),
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible){}
  
  
  ~ProjectedCornersDistanceTileLoDTester(){
  }
  
};


#endif /* ProjectedCornersDistanceTileLoDTester_hpp */
