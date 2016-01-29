//
//  OrTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#ifndef OrTileLODTester_hpp
#define OrTileLODTester_hpp

#include "TileLODTester.hpp"


class OrTileLODTester : public TileLODTester {
private:
  TileLODTester* _left;
  TileLODTester* _right;

public:
  OrTileLODTester(TileLODTester* left,
                  TileLODTester* right);

  ~OrTileLODTester();

  bool meetsRenderCriteria(const Tile* tile,
                           const G3MRenderContext* rc,
                           const TilesRenderParameters* tilesRenderParameters,
                           const ITimer* lastSplitTimer,
                           const double texWidthSquared,
                           const double texHeightSquared,
                           long long nowInMS) const;

  void onTileHasChangedMesh(const Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

  void renderStarted() const;
  
};

#endif
