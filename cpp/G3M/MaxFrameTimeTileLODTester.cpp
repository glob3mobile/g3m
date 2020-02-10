//
//  MaxFrameTimeTileLODTester.cpp
//  G3M
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#include "MaxFrameTimeTileLODTester.hpp"

#include "Tile.hpp"
#include "G3MRenderContext.hpp"
#include "ITimer.hpp"


MaxFrameTimeTileLODTester::MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTime,
                                                     TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_maxFrameTimeInMs(maxFrameTime.milliseconds()),
_splitsInFrameCounter(0)
{
}

MaxFrameTimeTileLODTester::~MaxFrameTimeTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool MaxFrameTimeTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                    const PlanetRenderContext* prc,
                                                    const Tile* tile) const {
  const bool hasSubtiles = tile->hasSubtiles();

  if (!hasSubtiles) {
    if (_splitsInFrameCounter > 0) {
      long long elapsedTime = rc->getFrameStartTimer()->elapsedTimeInMilliseconds();
      if (elapsedTime > _maxFrameTimeInMs) {
        return true;
      }
    }
  }

  const bool result = _tileLODTester->meetsRenderCriteria(rc, prc, tile);

  if (!result && !hasSubtiles) {
    _splitsInFrameCounter++;
  }

  return result;
}

void MaxFrameTimeTileLODTester::renderStarted() const {
  _splitsInFrameCounter = 0;
  DecoratorTileLODTester::renderStarted();
}
