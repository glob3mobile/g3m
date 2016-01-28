//
//  TileLODTesterResponder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#ifndef TileLODTesterResponder_hpp
#define TileLODTesterResponder_hpp

#include "TileLODTester.hpp"

class TileLODTesterResponder : public TileLODTester {

  TileLODTester* _nextTesterRightLOD;
  TileLODTester* _nextTesterWrongLOD;

protected:

  virtual bool _meetsRenderCriteria(const Tile* tile,
                                    const G3MRenderContext* rc,
                                    const TilesRenderParameters* tilesRenderParameters,
                                    const ITimer* lastSplitTimer,
                                    const double texWidthSquared,
                                    const double texHeightSquared,
                                    long long nowInMS) const = 0;

  virtual void _onTileHasChangedMesh(const Tile* tile) const {}

  virtual void _onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) = 0;

public:

  TileLODTesterResponder(TileLODTester* nextTesterRightLOD,
                         TileLODTester* nextTesterWrongLOD):
  _nextTesterRightLOD(nextTesterRightLOD),
  _nextTesterWrongLOD(nextTesterWrongLOD)
  {
  }

  virtual ~TileLODTesterResponder();

  bool meetsRenderCriteria(const Tile* tile,
                           const G3MRenderContext* rc,
                           const TilesRenderParameters* tilesRenderParameters,
                           const ITimer* lastSplitTimer,
                           const double texWidthSquared,
                           const double texHeightSquared,
                           long long nowInMS) const;

  void onTileHasChangedMesh(const Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);
};

#endif
