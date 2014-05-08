//
//  TileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "TileImageContribution.hpp"

const TileImageContribution* TileImageContribution::FULL_COVERAGE_OPAQUE  = new TileImageContribution(false, 1);

TileImageContribution* TileImageContribution::_lastFullCoverageTransparent = NULL;

const TileImageContribution* TileImageContribution::fullCoverageOpaque() {
//  FULL_COVERAGE_OPAQUE->_retain();
//  return FULL_COVERAGE_OPAQUE;

  return new TileImageContribution(false, 1);
}

const TileImageContribution* TileImageContribution::fullCoverageTransparent(float alpha) {
  if (alpha <= 0.01) {
    return NULL;
  }

//  // try to reuse the contribution between calls to avoid too much garbage. Android, in your face!
//  if ((_lastFullCoverageTransparent == NULL) ||
//      (_lastFullCoverageTransparent->_alpha != alpha)) {
//    if (_lastFullCoverageTransparent != NULL) {
//      _lastFullCoverageTransparent->_release();
//    }
//
//    _lastFullCoverageTransparent = new TileImageContribution(true, alpha);
//  }
//  _lastFullCoverageTransparent->_retain();
//  return _lastFullCoverageTransparent;

  return new TileImageContribution(true, alpha);;
}

const TileImageContribution* TileImageContribution::partialCoverageOpaque(const Sector& sector) {
  return new TileImageContribution(sector, false, 1);
}

const TileImageContribution* TileImageContribution::partialCoverageTransparent(const Sector& sector,
                                                                               float alpha) {
  return (alpha <= 0.01) ? NULL : new TileImageContribution(sector, true, alpha);
}

void TileImageContribution::retainContribution(const TileImageContribution* contribution) {
  if (contribution != NULL) {
    contribution->_retain();
  }
}

void TileImageContribution::releaseContribution(const TileImageContribution* contribution) {
  if (contribution != NULL) {
    contribution->_release();
  }
}
