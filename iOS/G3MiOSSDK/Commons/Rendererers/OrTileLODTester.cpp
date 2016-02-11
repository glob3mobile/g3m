//
//  OrTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#include "OrTileLODTester.hpp"


OrTileLODTester::OrTileLODTester(TileLODTester* left,
                                 TileLODTester* right) :
_left(left),
_right(right)
{

}

OrTileLODTester::~OrTileLODTester() {
  delete _left;
  delete _right;
#ifdef JAVA_CODE
  super.dispose();
#endif
}


bool OrTileLODTester::meetsRenderCriteria(const Tile* tile,
                                          const G3MRenderContext* rc,
                                          const TilesRenderParameters* tilesRenderParameters,
                                          const ITimer* lastSplitTimer,
                                          const double texWidthSquared,
                                          const double texHeightSquared,
                                          long long nowInMS) const {

  if (_left->meetsRenderCriteria(tile,
                                 rc,
                                 tilesRenderParameters,
                                 lastSplitTimer,
                                 texWidthSquared,
                                 texHeightSquared,
                                 nowInMS)) {
    return true;
  }

  return _right->meetsRenderCriteria(tile,
                                     rc,
                                     tilesRenderParameters,
                                     lastSplitTimer,
                                     texWidthSquared,
                                     texHeightSquared,
                                     nowInMS);
}

void OrTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
  _left->onTileHasChangedMesh(tile);
  _right->onTileHasChangedMesh(tile);
}

void OrTileLODTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  _left->onLayerTilesRenderParametersChanged(ltrp);
  _right->onLayerTilesRenderParametersChanged(ltrp);
}

void OrTileLODTester::renderStarted() const {
  _left->renderStarted();
  _right->renderStarted();
}
