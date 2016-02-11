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

  class PvtData: public TileData {
  public:
    long long _timeoutTimeInMS;

    PvtData(long long timeoutTimeInMS) :
    _timeoutTimeInMS(timeoutTimeInMS)
    {
    }
  };

public:
  TimedCacheTileVisibilityTester(const TimeInterval&   timeout,
                                 TileVisibilityTester* tileVisibilityTester);

  ~TimedCacheTileVisibilityTester();

  bool isVisible(const Tile* tile,
                 const G3MRenderContext* rc,
                 long long nowInMS,
                 const Frustum* frustumInModelCoordinates) const;

  void renderStarted() const;
  
};


#endif
