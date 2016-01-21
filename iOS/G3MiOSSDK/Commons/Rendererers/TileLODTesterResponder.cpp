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
}

bool TileLODTesterResponder::meetsRenderCriteria(int testerLevel,
                                                 Tile* tile,
                                                 const G3MRenderContext& rc) const {

  //Right LOD
  if (_meetsRenderCriteria(testerLevel, tile, rc)) {
    if (_nextTesterRightLOD != NULL) {
      return _nextTesterRightLOD->meetsRenderCriteria(testerLevel + 1, tile, rc);
    }
    return true;
  }

  //Wrong LOD
  if (_nextTesterWrongLOD != NULL) {
    return _nextTesterWrongLOD->meetsRenderCriteria(testerLevel + 1, tile, rc);
  }
  return false;
}


bool TileLODTesterResponder::isVisible(int testerLevel,
                                       Tile* tile,
                                       const G3MRenderContext& rc) const {

  //Visible
  if (_isVisible(testerLevel, tile, rc)) {
    if (_nextTesterVisible != NULL) {
      return _nextTesterVisible->isVisible(testerLevel+1, tile, rc);
    }
    return true;
  }

  //Not visible
  if (_nextTesterNotVisible != NULL) {
    return _nextTesterNotVisible->isVisible(testerLevel+1, tile, rc);
  }
  return false;

}

void TileLODTesterResponder::onTileHasChangedMesh(int testerLevel, Tile* tile) const {
  _onTileHasChangedMesh(testerLevel, tile);
  if (_nextTesterNotVisible != NULL)
    _nextTesterNotVisible->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterVisible != NULL)
    _nextTesterVisible->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterRightLOD != NULL)
    _nextTesterRightLOD->onTileHasChangedMesh(testerLevel+1, tile);
  if (_nextTesterWrongLOD != NULL)
    _nextTesterWrongLOD->onTileHasChangedMesh(testerLevel+1, tile);
}

void TileLODTesterResponder::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters *ltrp) {
  _onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterNotVisible != NULL)
    _nextTesterNotVisible->onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterVisible != NULL)
    _nextTesterVisible->onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterRightLOD != NULL)
    _nextTesterRightLOD->onLayerTilesRenderParametersChanged(ltrp);
  if (_nextTesterWrongLOD != NULL)
    _nextTesterWrongLOD->onLayerTilesRenderParametersChanged(ltrp);
}
