//
//  TimedCacheTileVisibilityTester.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#include "TimedCacheTileVisibilityTester.hpp"

#include "TimeInterval.hpp"
#include "Tile.hpp"
#include "PlanetRenderContext.hpp"


TimedCacheTileVisibilityTester::TimedCacheTileVisibilityTester(const TimeInterval&   timeout,
                                                               TileVisibilityTester* tileVisibilityTester) :
DecoratorTileVisibilityTester(tileVisibilityTester),
_timeoutInMS(timeout.milliseconds())
{

}

TimedCacheTileVisibilityTester::~TimedCacheTileVisibilityTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TimedCacheTileVisibilityTester::renderStarted() const {

}

bool TimedCacheTileVisibilityTester::isVisible(const G3MRenderContext* rc,
                                               const PlanetRenderContext* prc,
                                               Tile* tile) const {

  const long long nowInMS = prc->_nowInMS;

  bool result;
  PvtData* data = (PvtData*) tile->getData(TimedCacheTVTDataID);

  if (data == NULL) {
    result = _tileVisibilityTester->isVisible(rc, prc, tile);
    if (result) {
      data = new PvtData(nowInMS + _timeoutInMS);
      tile->setData(data);
    }
  }
  else {
    if (data->_timeoutTimeInMS > nowInMS) {
      result = true;
    }
    else {
      result = _tileVisibilityTester->isVisible(rc, prc, tile);
      if (result) {
        data->_timeoutTimeInMS = nowInMS + _timeoutInMS;
      }
      else {
        tile->setData(data);
      }
    }
  }

  return result;
}
