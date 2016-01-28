//
//  TileLODTesterResponder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/21/16.
//
//

#include "TileLODTesterResponder.hpp"

#include <stddef.h>


TileLODTesterResponder::~TileLODTesterResponder() {
  delete _nextTesterRightLOD;
  delete _nextTesterWrongLOD;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TileLODTesterResponder::meetsRenderCriteria(const Tile* tile,
                                                 const G3MRenderContext* rc,
                                                 const TilesRenderParameters* tilesRenderParameters,
                                                 const ITimer* lastSplitTimer,
                                                 const double texWidthSquared,
                                                 const double texHeightSquared,
                                                 long long nowInMS) const {
  //Right LOD
  if (_meetsRenderCriteria(tile,
                           rc,
                           tilesRenderParameters,
                           lastSplitTimer,
                           texWidthSquared,
                           texHeightSquared,
                           nowInMS)) {
    if (_nextTesterRightLOD != NULL) {
      return _nextTesterRightLOD->meetsRenderCriteria(tile,
                                                      rc,
                                                      tilesRenderParameters,
                                                      lastSplitTimer,
                                                      texWidthSquared,
                                                      texHeightSquared,
                                                      nowInMS);
    }
    return true;
  }

  //Wrong LOD
  if (_nextTesterWrongLOD != NULL) {
    return _nextTesterWrongLOD->meetsRenderCriteria(tile,
                                                    rc,
                                                    tilesRenderParameters,
                                                    lastSplitTimer,
                                                    texWidthSquared,
                                                    texHeightSquared,
                                                    nowInMS);
  }

  return false;
}

void TileLODTesterResponder::onTileHasChangedMesh(const Tile* tile) const {
  _onTileHasChangedMesh(tile);
  if (_nextTesterRightLOD != NULL) {
    _nextTesterRightLOD->onTileHasChangedMesh(tile);
  }
  if (_nextTesterWrongLOD != NULL) {
    _nextTesterWrongLOD->onTileHasChangedMesh(tile);
  }
}

void TileLODTesterResponder::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters *ltrp) {
  _onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterRightLOD != NULL) {
    _nextTesterRightLOD->onLayerTilesRenderParametersChanged(ltrp);
  }
  if (_nextTesterWrongLOD != NULL) {
    _nextTesterWrongLOD->onLayerTilesRenderParametersChanged(ltrp);
  }
}
