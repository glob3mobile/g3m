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


TimedTileLODTester::TimedTileLODTester(const TimeInterval& timeout,
                                       TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_timeoutInMS(timeout.milliseconds())
{
}

TimedTileLODTester::~TimedTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TimedTileLODTester::meetsRenderCriteria(Tile* tile,
                                             const G3MRenderContext* rc,
                                             const TilesRenderParameters* tilesRenderParameters,
                                             const ITimer* lastSplitTimer,
                                             const double texWidthSquared,
                                             const double texHeightSquared,
                                             long long nowInMS) const {

  TimedTileLODTesterData* data = (TimedTileLODTesterData*) tile->getDataForLODTester(_id);
  if (data == NULL) {
    data = new TimedTileLODTesterData(nowInMS);
    tile->setDataForLODTester(_id, data);
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

bool TimedTileLODTester::isVisible(Tile* tile,
                                   const G3MRenderContext* rc) const {
  return _tileLODTester->isVisible(tile, rc);
}

void TimedTileLODTester::onTileHasChangedMesh(Tile* tile) const {
  _tileLODTester->onTileHasChangedMesh(tile);
}

void TimedTileLODTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  _tileLODTester->onLayerTilesRenderParametersChanged(ltrp);
}
