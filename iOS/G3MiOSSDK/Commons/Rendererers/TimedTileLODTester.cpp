//
//  TimedLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#include "TimedTileLODTester.hpp"

#include "Tile.hpp"
#include "Context.hpp"


TimedTileLODTester::TimedTileLODTester(const TimeInterval& time,
                                       TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_timeInMs(time.milliseconds())
{
}

TimedTileLODTester::~TimedTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TimedTileLODTester::meetsRenderCriteria(Tile* tile,
                                             const G3MRenderContext& rc) const {
#warning TODO: move now up in the chain
  long long now = rc.getFrameStartTimer()->nowInMilliseconds();

  TimedTileLODTesterData* data = (TimedTileLODTesterData*) tile->getDataForLODTester(_id);
  if (data == NULL) {
    data = new TimedTileLODTesterData(now);
    tile->setDataForLODTester(_id, data);
    data->_lastMeetsRenderCriteriaResult = _tileLODTester->meetsRenderCriteria(tile, rc);
  }
  else if ((now - data->_lastMeetsRenderCriteriaTimeInMS) > _timeInMs) {
#warning TODO: talk with JM
    data->_lastMeetsRenderCriteriaTimeInMS = now;
    data->_lastMeetsRenderCriteriaResult = _tileLODTester->meetsRenderCriteria(tile, rc);
  }

  return data->_lastMeetsRenderCriteriaResult;
}

bool TimedTileLODTester::isVisible(Tile* tile,
                                   const G3MRenderContext& rc) const {
  return _tileLODTester->isVisible(tile, rc);
}

void TimedTileLODTester::onTileHasChangedMesh(Tile* tile) const {
  _tileLODTester->onTileHasChangedMesh(tile);
}

void TimedTileLODTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  _tileLODTester->onLayerTilesRenderParametersChanged(ltrp);
}
