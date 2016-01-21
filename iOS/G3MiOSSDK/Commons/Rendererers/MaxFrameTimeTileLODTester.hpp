//
//  MaxFrameTimeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef MaxFrameTimeTileLODTester_hpp
#define MaxFrameTimeTileLODTester_hpp


#include "TileLODTester.hpp"

class TimeInterval;


class MaxFrameTimeTileLODTester: public TileLODTester {
private:
  TileLODTester* _nextTester;
  long long _maxFrameTimeInMs;

  mutable long long _lastElapsedTime;
  mutable int _nSplitsInFrame;

public:

  MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTimeInMs,
                            TileLODTester* nextTester);

  virtual ~MaxFrameTimeTileLODTester();

  virtual bool meetsRenderCriteria(Tile* tile,
                                   const G3MRenderContext& rc) const;

  virtual bool isVisible(Tile* tile,
                         const G3MRenderContext& rc) const;

  virtual void onTileHasChangedMesh(Tile* tile) const;

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp);
  
};

#endif
