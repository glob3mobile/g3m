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

#include "TileData.hpp"
#include "Vector3D.hpp"

class Planet;
class Camera;


class ProjectedCornersDistanceTileLODTester : public TileLODTester {
private:

  class PvtData: public TileData {
  private:
    static double getSquaredArcSegmentRatio(const Vector3D& a,
                                            const Vector3D& b);

    double _northArcSegmentRatioSquared;
    double _southArcSegmentRatioSquared;
    double _eastArcSegmentRatioSquared;
    double _westArcSegmentRatioSquared;

    const Vector3D _northWestPoint;
    const Vector3D _northEastPoint;
    const Vector3D _southWestPoint;
    const Vector3D _southEastPoint;


  public:
    PvtData(const Tile* tile,
            double mediumHeight,
            const Planet* planet);

    bool evaluate(const Camera* camera,
                  double texHeightSquared,
                  double texWidthSquared);
  };


  PvtData* getData(const Tile* tile,
                   const G3MRenderContext* rc) const;

public:

  ProjectedCornersDistanceTileLODTester();

  ~ProjectedCornersDistanceTileLODTester();

  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

  void onTileHasChangedMesh(const Tile* tile) const {

  }

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {

  }

  void renderStarted() const {
    
  }
  
};

#endif
