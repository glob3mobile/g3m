//
//  ProjectedCornersDistanceTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef ProjectedCornersDistanceTileLODTester_hpp
#define ProjectedCornersDistanceTileLODTester_hpp


#include "TileLODTesterResponder.hpp"

#include "TileData.hpp"
#include "Vector3D.hpp"
class BoundingVolume;
class Planet;
class Camera;


class ProjectedCornersDistanceTileLODTester : public TileLODTesterResponder {
protected:

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

  void _onTileHasChangedMesh(const Tile* tile) const;

  PvtData* getData(const Tile* tile,
                   const G3MRenderContext* rc) const;

  bool _meetsRenderCriteria(const Tile* tile,
                            const G3MRenderContext* rc,
                            const TilesRenderParameters* tilesRenderParameters,
                            const ITimer* lastSplitTimer,
                            const double texWidthSquared,
                            const double texHeightSquared,
                            long long nowInMS) const;

  void _onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

public:

  ProjectedCornersDistanceTileLODTester(TileLODTester* nextTesterRightLOD,
                                        TileLODTester* nextTesterWrongLOD);

  ~ProjectedCornersDistanceTileLODTester();
  
};

#endif
