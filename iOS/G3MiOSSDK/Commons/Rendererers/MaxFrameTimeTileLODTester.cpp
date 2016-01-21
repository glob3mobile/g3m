//
//  MaxFrameTimeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#include "MaxFrameTimeTileLODTester.hpp"

#include "Tile.hpp"
#include "Context.hpp"


MaxFrameTimeTileLODTester::MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTimeInMs,
                                                     TileLODTester* nextTester):
_maxFrameTimeInMs(maxFrameTimeInMs.milliseconds()),
_nextTester(nextTester),
_lastElapsedTime(0),
_nSplitsInFrame(0)
{}

MaxFrameTimeTileLODTester::~MaxFrameTimeTileLODTester() {
  delete _nextTester;
}

bool MaxFrameTimeTileLODTester::meetsRenderCriteria(Tile* tile,
                                                    const G3MRenderContext& rc) const {

  const bool hasSubtiles = tile->areSubtilesCreated();
  long long elapsedTime = rc.getFrameStartTimer()->elapsedTimeInMilliseconds();
  if (elapsedTime < _lastElapsedTime) {
    //New frame
    //      if (_nSplitsInFrame > 0) {
    //        printf("Tile splits on last frame: %d\n", _nSplitsInFrame);
    //      }
    _nSplitsInFrame = 0;
  }
  _lastElapsedTime = elapsedTime;

  if (!hasSubtiles && elapsedTime > _maxFrameTimeInMs && _nSplitsInFrame > 0) {
    return true;
  }

  bool res = (_nextTester == NULL)? true : _nextTester->meetsRenderCriteria(tile, rc);

  if (!res && !hasSubtiles) {
    _nSplitsInFrame++;
  }

  return res;
}

bool MaxFrameTimeTileLODTester::isVisible(Tile* tile,
                                          const G3MRenderContext& rc) const {
  if (_nextTester == NULL) {
    return true;
  }
  return _nextTester->isVisible(tile, rc);
}

void MaxFrameTimeTileLODTester::onTileHasChangedMesh(Tile* tile) const {
  if (_nextTester != NULL) {
    _nextTester->onTileHasChangedMesh(tile);
  }
}

void MaxFrameTimeTileLODTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  if (_nextTester != NULL) {
    _nextTester->onLayerTilesRenderParametersChanged(ltrp);
  }
}
