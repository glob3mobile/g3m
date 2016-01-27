//
//  MaxFrameTimeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef MaxFrameTimeTileLODTester_hpp
#define MaxFrameTimeTileLODTester_hpp


#include "DecoratorTileLODTester.hpp"

class TimeInterval;


class MaxFrameTimeTileLODTester : public DecoratorTileLODTester {
private:
  long long _maxFrameTimeInMs;

  mutable long long _lastElapsedTime;
  mutable int _nSplitsInFrame;

public:

  MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTimeInMs,
                            TileLODTester* tileLODTester);

  ~MaxFrameTimeTileLODTester();
  
  bool meetsRenderCriteria(Tile* tile,
                           const G3MRenderContext* rc,
                           const TilesRenderParameters* tilesRenderParameters,
                           const ITimer* lastSplitTimer,
                           const double texWidthSquared,
                           const double texHeightSquared,
                           long long nowInMS) const;

  bool isVisible(Tile* tile,
                 const G3MRenderContext* rc) const;

  void onTileHasChangedMesh(Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);

};

#endif
