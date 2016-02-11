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
_splitsInFrameCounter(0)
{
}

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

#warning Diego at work!

  const bool hasSubtiles = tile->areSubtilesCreated();

  if (!hasSubtiles) {
    if (_splitsInFrameCounter > 0) {
      long long elapsedTime = rc->getFrameStartTimer()->elapsedTimeInMilliseconds();
      if (elapsedTime > _maxFrameTimeInMs) {
        return true;
      }
    }
  }

  const bool result = _tileLODTester->meetsRenderCriteria(tile,
                                                          rc,
                                                          tilesRenderParameters,
                                                          lastSplitTimer,
                                                          texWidthSquared,
                                                          texHeightSquared,
                                                          nowInMS);

  if (!result && !hasSubtiles) {
    _splitsInFrameCounter++;
  }

  return result;
}

void MaxFrameTimeTileLODTester::renderStarted() const {
  _splitsInFrameCounter = 0;
  DecoratorTileLODTester::renderStarted();
}
