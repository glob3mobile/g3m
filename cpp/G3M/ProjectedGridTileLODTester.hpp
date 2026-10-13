//
//  ProjectedGridTileLODTester.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/10/26.
//
//

#ifndef ProjectedGridTileLODTester_hpp
#define ProjectedGridTileLODTester_hpp

#include "TileLODTester.hpp"

#include "TileData.hpp"
#include "Vector3D.hpp"

class Planet;
class Camera;
class Angle;


class ProjectedGridTileLODTester : public TileLODTester {
private:

  class PvtData: public TileData {
  private:
    const Vector3D _northWest;
    const Vector3D _north;
    const Vector3D _northEast;
    const Vector3D _west;
    const Vector3D _center;
    const Vector3D _east;
    const Vector3D _southWest;
    const Vector3D _south;
    const Vector3D _southEast;

    static const Angle splitLatitude(const Tile* tile);

    static const Angle splitLongitude(const Tile* tile);

    static bool isBehind(const Vector3D& point,
                         const Vector3D& cameraPosition,
                         const Vector3D& viewDirection);

    static double squaredPixelDistance(const Camera* camera,
                                       const Vector3D& point0,
                                       const Vector3D& point1);

  public:
    PvtData(const Tile* tile,
            double averageHeight,
            const Planet* planet);

    bool evaluate(const Camera* camera,
                  double texHeightSquared,
                  double texWidthSquared) const;
  };

  const double _thresholdFactorSquared;

  PvtData* getData(const Tile* tile,
                   const G3MRenderContext* rc) const;

public:

  // thresholdFactor scales the allowed projected size: 1 is the texture resolution, greater asks for fewer tiles
  ProjectedGridTileLODTester(double thresholdFactor);

  ~ProjectedGridTileLODTester();

  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

  void onTileHasChangedMesh(const Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {

  }

  void renderStarted() const {

  }

};

#endif
