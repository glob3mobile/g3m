//
//  TimedLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef TimedLODTester_hpp
#define TimedLODTester_hpp

#include "DecoratorTileLODTester.hpp"

#include "TileLODTesterData.hpp"
class TimeInterval;


class TimedTileLODTester : public DecoratorTileLODTester {
private:
  long long _timeoutInMS;

  class TimedTileLODTesterData: public TileLODTesterData{
  public:
    bool _lastMeetsRenderCriteriaResult;
    long long _lastMeetsRenderCriteriaTimeInMS;

    TimedTileLODTesterData(long long now) {
      _lastMeetsRenderCriteriaTimeInMS = now;
      _lastMeetsRenderCriteriaResult = false;
    }
  };

public:

  TimedTileLODTester(const TimeInterval& timeout,
                     TileLODTester* tileLODTester);

  virtual ~TimedTileLODTester();

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
