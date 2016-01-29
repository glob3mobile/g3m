//
//  MaxLevelTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#include "MaxLevelTileLODTester.hpp"

#include "Tile.hpp"
#include "G3MContext.hpp"
#include "LayerTilesRenderParameters.hpp"


MaxLevelTileLODTester::MaxLevelTileLODTester(int maxLevel,
                                             int maxLevelForPoles,
                                             TileLODTester* nextTesterRightLOD,
                                             TileLODTester* nextTesterWrongLOD):
TileLODTesterResponder(nextTesterRightLOD,
                       nextTesterWrongLOD),
_maxLevelForPoles(maxLevelForPoles),
_maxLevel(maxLevel)
{}


MaxLevelTileLODTester::~MaxLevelTileLODTester() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool MaxLevelTileLODTester::_meetsRenderCriteria(const Tile* tile,
                                                 const G3MRenderContext* rc,
                                                 const TilesRenderParameters* tilesRenderParameters,
                                                 const ITimer* lastSplitTimer,
                                                 const double texWidthSquared,
                                                 const double texHeightSquared,
                                                 long long nowInMS) const {

  if ((tile->_level >= _maxLevel) &&
      (_maxLevel > -1)) {
    return true;
  }

  if (tile->_sector.touchesPoles()) {
    if ((tile->_level >= _maxLevelForPoles) &&
        (_maxLevelForPoles > -1)) {
      return true;
    }
  }

  return false;
}

void MaxLevelTileLODTester::_onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  if (ltrp != NULL) {
    _maxLevel = ltrp->_maxLevel;
    _maxLevelForPoles = ltrp->_maxLevelForPoles;
  }
  else {
    _maxLevel = -1;
    _maxLevelForPoles = -1;
  }
}
