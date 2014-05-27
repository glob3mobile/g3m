//
//  LayerTilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

#include "LayerTilesRenderParameters.hpp"

/*
 return ( topSectorSplitsByLatitude, topSectorSplitsByLongitude )
 */
const Vector2I LayerTilesRenderParameters::calculateTopSectorSplitsParametersWGS84(const Sector& topSector) {
//  IMathUtils* math = IMathUtils::instance();
  const double maxTile = 90;
  double sLat;
  double sLon;
  
  const double ratio = topSector._deltaLatitude.div(topSector._deltaLongitude);
  if (ratio > 1) {
    sLat = ratio;
    sLon = 1;
  }
  else {
    sLat = 1;
    sLon = 1 / ratio;
  }
  
  const double tileDeltaLat = topSector._deltaLatitude.div(sLat)._degrees;
  const double factorLat = tileDeltaLat / maxTile;
//  double factor = math->max(factorLat, 1L);
//  return Vector2I((int) math->round(sLat * factor), (int) math->round(sLon * factor));

  const double factor = (factorLat < 1) ? 1 : factorLat;
  return Vector2I((int) ((sLat * factor) + 0.5),
                  (int) ((sLon * factor) + 0.5));
}

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
