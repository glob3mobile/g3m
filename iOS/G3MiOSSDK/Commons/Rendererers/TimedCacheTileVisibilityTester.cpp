//
//  TimedCacheTileVisibilityTester.cpp
//  G3MiOSSDK
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
  
#warning Calculate ID;
  const int id = 32;

  bool result;
  PvtData* data = (PvtData*) tile->getData(id);

  if (data == NULL) {
    result = _tileVisibilityTester->isVisible(rc, prc, tile);
    if (result) {
      data = new PvtData(nowInMS + _timeoutInMS);
      tile->setData(id, data);
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
        tile->setData(id, NULL);
      }
    }
  }

  return result;
}
