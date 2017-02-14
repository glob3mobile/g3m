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

  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

  void onTileHasChangedMesh(const Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

  void renderStarted() const;
  
};

#endif
