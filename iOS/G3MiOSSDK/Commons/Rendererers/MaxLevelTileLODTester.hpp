//
//  MaxLevelTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef MaxLevelTileLODTester_hpp
#define MaxLevelTileLODTester_hpp

#include "TileLODTester.hpp"


class MaxLevelTileLODTester : public TileLODTester {
private:
  int _maxLevel;
  int _maxLevelForPoles;


public:

  MaxLevelTileLODTester();

  ~MaxLevelTileLODTester();

  bool meetsRenderCriteria(const Tile* tile,
                           const G3MRenderContext* rc,
                           const TilesRenderParameters* tilesRenderParameters,
                           const ITimer* lastSplitTimer,
                           const double texWidthSquared,
                           const double texHeightSquared,
                           long long nowInMS) const;

  void onTileHasChangedMesh(const Tile* tile) const {

  }

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

  void renderStarted() const {
    
  }
  
};

#endif
