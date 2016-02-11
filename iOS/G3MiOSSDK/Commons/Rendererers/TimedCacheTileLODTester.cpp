//
//  TimedLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#include "TimedCacheTileLODTester.hpp"

#include "Tile.hpp"
#include "G3MContext.hpp"
#include "TimeInterval.hpp"


TimedCacheTileLODTester::TimedCacheTileLODTester(const TimeInterval& timeout,
                                                 TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_timeoutInMS(timeout.milliseconds())
{
}

TimedCacheTileLODTester::~TimedCacheTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TimedCacheTileLODTester::meetsRenderCriteria(const Tile* tile,
                                                  const G3MRenderContext* rc,
                                                  const TilesRenderParameters* tilesRenderParameters,
                                                  const ITimer* lastSplitTimer,
                                                  const double texWidthSquared,
                                                  const double texHeightSquared,
                                                  long long nowInMS) const {

  PvtData* data = (PvtData*) tile->getData(_id);
  if (data == NULL) {
    data = new PvtData(nowInMS);
    tile->setData(_id, data);
    data->_lastMeetsRenderCriteriaResult = _tileLODTester->meetsRenderCriteria(tile,
                                                                               rc,
                                                                               tilesRenderParameters,
                                                                               lastSplitTimer,
                                                                               texWidthSquared,
                                                                               texHeightSquared,
                                                                               nowInMS);
  }
  else if ((nowInMS - data->_lastMeetsRenderCriteriaTimeInMS) > _timeoutInMS) {
    data->_lastMeetsRenderCriteriaTimeInMS = nowInMS;
    data->_lastMeetsRenderCriteriaResult = _tileLODTester->meetsRenderCriteria(tile,
                                                                               rc,
                                                                               tilesRenderParameters,
                                                                               lastSplitTimer,
                                                                               texWidthSquared,
                                                                               texHeightSquared,
                                                                               nowInMS);
  }

  return data->_lastMeetsRenderCriteriaResult;
}
