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
  delete _nextTesterNotVisible;
  delete _nextTesterRightLOD;
  delete _nextTesterVisible;
  delete _nextTesterWrongLOD;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool TileLODTesterResponder::meetsRenderCriteria(Tile* tile,
                                                 const G3MRenderContext& rc) const {
  //Right LOD
  if (_meetsRenderCriteria(tile, rc)) {
    if (_nextTesterRightLOD != NULL) {
      return _nextTesterRightLOD->meetsRenderCriteria(tile, rc);
    }
    return true;
  }

  //Wrong LOD
  if (_nextTesterWrongLOD != NULL) {
    return _nextTesterWrongLOD->meetsRenderCriteria(tile, rc);
  }

  return false;
}

bool TileLODTesterResponder::isVisible(Tile* tile,
                                       const G3MRenderContext& rc) const {
  //Visible
  if (_isVisible(tile, rc)) {
    if (_nextTesterVisible != NULL) {
      return _nextTesterVisible->isVisible(tile, rc);
    }
    return true;
  }

  //Not visible
  if (_nextTesterNotVisible != NULL) {
    return _nextTesterNotVisible->isVisible(tile, rc);
  }

  return false;
}

void TileLODTesterResponder::onTileHasChangedMesh(Tile* tile) const {
  _onTileHasChangedMesh(tile);
  if (_nextTesterNotVisible != NULL) {
    _nextTesterNotVisible->onTileHasChangedMesh(tile);
  }
  if (_nextTesterVisible != NULL) {
    _nextTesterVisible->onTileHasChangedMesh(tile);
  }
  if (_nextTesterRightLOD != NULL) {
    _nextTesterRightLOD->onTileHasChangedMesh(tile);
  }
  if (_nextTesterWrongLOD != NULL) {
    _nextTesterWrongLOD->onTileHasChangedMesh(tile);
  }
}

void TileLODTesterResponder::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters *ltrp) {
  _onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterNotVisible != NULL) {
    _nextTesterNotVisible->onLayerTilesRenderParametersChanged(ltrp);
  }
  if (_nextTesterVisible != NULL) {
    _nextTesterVisible->onLayerTilesRenderParametersChanged(ltrp);
  }
  if (_nextTesterRightLOD != NULL) {
    _nextTesterRightLOD->onLayerTilesRenderParametersChanged(ltrp);
  }
  if (_nextTesterWrongLOD != NULL) {
    _nextTesterWrongLOD->onLayerTilesRenderParametersChanged(ltrp);
  }
}
