//
//  MaxFrameTimeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#include "MaxFrameTimeTileLODTester.hpp"

#include "Tile.hpp"
#include "G3MRenderContext.hpp"
#include "ITimer.hpp"


MaxFrameTimeTileLODTester::MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTimeInMs,
                                                     TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_maxFrameTimeInMs(maxFrameTimeInMs.milliseconds()),
_lastElapsedTime(0),
_nSplitsInFrame(0)
{}

MaxFrameTimeTileLODTester::~MaxFrameTimeTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool MaxFrameTimeTileLODTester::meetsRenderCriteria(const Tile* tile,
                                                    const G3MRenderContext* rc,
                                                    const TilesRenderParameters* tilesRenderParameters,
                                                    const ITimer* lastSplitTimer,
                                                    const double texWidthSquared,
                                                    const double texHeightSquared,
                                                    long long nowInMS) const {

  const bool hasSubtiles = tile->areSubtilesCreated();
  long long elapsedTime = rc->getFrameStartTimer()->elapsedTimeInMilliseconds();
  if (elapsedTime < _lastElapsedTime) {
    //New frame
    //      if (_nSplitsInFrame > 0) {
    //        printf("Tile splits on last frame: %d\n", _nSplitsInFrame);
    //      }
    _nSplitsInFrame = 0;
  }
  _lastElapsedTime = elapsedTime;

  if (!hasSubtiles && (elapsedTime > _maxFrameTimeInMs) && (_nSplitsInFrame > 0)) {
    return true;
  }

  const bool res = _tileLODTester->meetsRenderCriteria(tile,
                                                       rc,
                                                       tilesRenderParameters,
                                                       lastSplitTimer,
                                                       texWidthSquared,
                                                       texHeightSquared,
                                                       nowInMS);

  if (!res && !hasSubtiles) {
    _nSplitsInFrame++;
  }

  return res;
}
