//
//  TimedLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef TimedLODTester_hpp
#define TimedLODTester_hpp

#include "TileLODTester.hpp"
#include "Tile.hpp"
#include "Context.hpp"
#include "TileLODTesterData.hpp"


class TimedTileLODTester: public TileLODTester {
private:
  TileLODTester* _nextTester;
  long long _timeInMs;

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

  TimedTileLODTester(const TimeInterval& time,
                     TileLODTester* nextTester):
  _timeInMs(time.milliseconds()),
  _nextTester(nextTester)
  {}

  virtual ~TimedTileLODTester() {
    delete _nextTester;
  }

  virtual bool meetsRenderCriteria(Tile* tile,
                                   const G3MRenderContext& rc) const {

    long long now = rc.getFrameStartTimer()->nowInMilliseconds();

    TimedTileLODTesterData* data = (TimedTileLODTesterData*) tile->getDataForLODTester(_id);
    if (data == NULL) {
      data = new TimedTileLODTesterData(now);
      tile->setDataForLODTester(_id, data);
      data->_lastMeetsRenderCriteriaResult = (_nextTester == NULL)? true : _nextTester->meetsRenderCriteria(tile, rc);
    }

    if ((now - data->_lastMeetsRenderCriteriaTimeInMS) > _timeInMs) {
      data->_lastMeetsRenderCriteriaResult = (_nextTester == NULL)? true : _nextTester->meetsRenderCriteria(tile, rc);
    }

    return data->_lastMeetsRenderCriteriaResult;
  }

  virtual bool isVisible(Tile* tile,
                         const G3MRenderContext& rc) const {
    if (_nextTester == NULL) {
      return true;
    }
    return _nextTester->isVisible(tile, rc);
  }

  virtual void onTileHasChangedMesh(Tile* tile) const {
    if (_nextTester != NULL) {
      _nextTester->onTileHasChangedMesh(tile);
    }
  }

  void onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
    if (_nextTester != NULL) {
      _nextTester->onLayerTilesRenderParametersChanged(ltrp);
    }
  }

};

#endif
