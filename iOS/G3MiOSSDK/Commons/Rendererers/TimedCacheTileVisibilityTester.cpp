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

bool TimedCacheTileVisibilityTester::isVisible(const Tile* tile,
                                               const G3MRenderContext* rc,
                                               long long nowInMS) const {
  // return _tileVisibilityTester->isVisible(tile, rc, nowInMS);

#warning Calculate ID;
  int _id = 32;

  bool result;
  PvtData* data = (PvtData*) tile->getData(_id);

  if (data == NULL) {
    result = _tileVisibilityTester->isVisible(tile, rc, nowInMS);
    if (result) {
      data = new PvtData(nowInMS + _timeoutInMS);
      tile->setData(_id, data);
    }
  }
  else {
    if (data->_timeoutTimeInMS <= nowInMS) {
      result = true;
    }
    else {
      result = _tileVisibilityTester->isVisible(tile, rc, nowInMS);
      if (result) {
        data->_timeoutTimeInMS = nowInMS + _timeoutInMS;
      }
      else {
        tile->setData(_id, NULL);
      }
    }
  }

  return result;
}
