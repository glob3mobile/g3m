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


MaxLevelTileLODTester::MaxLevelTileLODTester():
TileLODTesterResponder(NULL,
                       NULL),
_maxLevel(-1),
_maxLevelForPoles(-1)
{
}


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

  if (_maxLevel < 0) {
    return true;
  }

  if (tile->_level >= _maxLevel) {
    return true;
  }

  if ((tile->_level >= _maxLevelForPoles) && (tile->_sector.touchesPoles())) {
    return true;
  }

  return false;
}

void MaxLevelTileLODTester::_onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  if (ltrp == NULL) {
    _maxLevel         = -1;
    _maxLevelForPoles = -1;
  }
  else {
    _maxLevel         = ltrp->_maxLevel;
    _maxLevelForPoles = ltrp->_maxLevelForPoles;
  }
}
