//
//  TimedLODTester.hpp
//  G3M
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef TimedLODTester_hpp
#define TimedLODTester_hpp

#include "DecoratorTileLODTester.hpp"

#include "TileData.hpp"
class TimeInterval;


class TimedCacheTileLODTester : public DecoratorTileLODTester {
private:
  long long _timeoutInMS;

  class PvtData: public TileData {
  public:
    bool _lastMeetsRenderCriteriaResult;
    long long _lastMeetsRenderCriteriaTimeInMS;

    PvtData(long long now):
    TileData(TimedCacheTLTDataID) {
      _lastMeetsRenderCriteriaTimeInMS = now;
      _lastMeetsRenderCriteriaResult = false;
    }
  };

public:

  TimedCacheTileLODTester(const TimeInterval& timeout,
                          TileLODTester* tileLODTester);

  ~TimedCacheTileLODTester();

  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

};

#endif
