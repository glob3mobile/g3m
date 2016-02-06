//
//  GradualSplitsTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/6/16.
//
//

#include "GradualSplitsTileLODTester.hpp"

#include "TimeInterval.hpp"
#include "PlanetRenderContext.hpp"
#include "Tile.hpp"
#include "ITimer.hpp"


GradualSplitsTileLODTester::GradualSplitsTileLODTester(const TimeInterval& delay,
                                                       TileLODTester* tileLODTester) :
DecoratorTileLODTester(tileLODTester),
_delayInMs(delay.milliseconds())
{
}

GradualSplitsTileLODTester::~GradualSplitsTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool GradualSplitsTileLODTester::meetsRenderCriteria(const G3MRenderContext* rc,
                                                     const PlanetRenderContext* prc,
                                                     const Tile* tile) const {

  const bool result = _tileLODTester->meetsRenderCriteria(rc, prc, tile);

  if (!result) {
    const bool hasSubtiles = tile->areSubtilesCreated();

    if (!hasSubtiles) { // the tile needs to create the subtiles
      if (prc->_lastSplitTimer->elapsedTimeInMilliseconds() <= _delayInMs) {
        // there are not more time-budget to spend
        return true;
      }
    }
  }

  return result;
}
