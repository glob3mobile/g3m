//
//  LayerTilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

#include "LayerTilesRenderParameters.hpp"

bool LayerTilesRenderParameters::isEquals(const LayerTilesRenderParameters* that) const {
  if (that == NULL) {
    return false;
  }

  if (!(_topSector.isEquals(that->_topSector))) {
    return false;
  }

  if (_topSectorSplitsByLatitude != that->_topSectorSplitsByLatitude) {
    return false;
  }

  if (_topSectorSplitsByLongitude != that->_topSectorSplitsByLongitude) {
    return false;
  }

  if (_firstLevel != that->_firstLevel) {
    return false;
  }

  if (_maxLevel != that->_maxLevel) {
    return false;
  }

  if (_maxLevelForPoles != that->_maxLevelForPoles) {
    return false;
  }

  if (!_tileTextureResolution.isEquals(that->_tileTextureResolution)) {
    return false;
  }

  if (!_tileMeshResolution.isEquals(that->_tileMeshResolution)) {
    return false;
  }

  if (_mercator != that->_mercator) {
    return false;
  }

  return true;
}

LayerTilesRenderParameters* LayerTilesRenderParameters::copy() const {
  return new LayerTilesRenderParameters(_topSector,
                                        _topSectorSplitsByLatitude,
                                        _topSectorSplitsByLongitude,
                                        _firstLevel,
                                        _maxLevel,
                                        _tileTextureResolution,
                                        _tileMeshResolution,
                                        _mercator);
}
