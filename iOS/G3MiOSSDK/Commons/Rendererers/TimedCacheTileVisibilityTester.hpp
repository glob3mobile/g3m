//
//  TimedCacheTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#ifndef TimedCacheTileVisibilityTester_hpp
#define TimedCacheTileVisibilityTester_hpp


#include "DecoratorTileVisibilityTester.hpp"

#include "TileData.hpp"
class TimeInterval;


class TimedCacheTileVisibilityTester : public DecoratorTileVisibilityTester {
private:
  long long _timeoutInMS;

#define TimedCacheTVTDataID 0
  class PvtData: public TileData {
  public:
    long long _timeoutTimeInMS;

    PvtData(long long timeoutTimeInMS) :
    TileData(TimedCacheTVTDataID),
    _timeoutTimeInMS(timeoutTimeInMS)
    {
    }
  };

public:
  TimedCacheTileVisibilityTester(const TimeInterval&   timeout,
                                 TileVisibilityTester* tileVisibilityTester);

  ~TimedCacheTileVisibilityTester();

  bool isVisible(const G3MRenderContext* rc,
                 const PlanetRenderContext* prc,
                 Tile* tile) const;

  void renderStarted() const;
  
};


#endif
