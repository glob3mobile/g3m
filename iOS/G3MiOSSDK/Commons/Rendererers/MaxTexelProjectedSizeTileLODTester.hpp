//
//  MaxTexelProjectedSizeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

#ifndef MaxTexelProjectedSizeTileLODTester_hpp
#define MaxTexelProjectedSizeTileLODTester_hpp

#include "TileLODTester.hpp"

#include "TileData.hpp"
#include "Vector3D.hpp"
#include "Box.hpp"

class Planet;
class Camera;


class MaxTexelProjectedSizeTileLODTester : public TileLODTester {
  
  class PvtData: public TileData {
  private:
    Box* _boundingBox;
  public:
    PvtData(const Tile* tile);
    
    bool evaluate(const Camera* camera,
                  double texHeightSquared,
                  double texWidthSquared);
  };
  
  
public:
  
  MaxTexelProjectedSizeTileLODTester();
  
  ~MaxTexelProjectedSizeTileLODTester();
  
  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;
  
  void onTileHasChangedMesh(const Tile* tile) const;
  
  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
    
  }
  
  void renderStarted() const {
    
  }
  

};

#endif /* MaxTexelProjectedSizeTileLODTester_hpp */
