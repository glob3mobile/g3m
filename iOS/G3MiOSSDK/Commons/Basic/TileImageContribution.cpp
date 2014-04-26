//
//  TileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "TileImageContribution.hpp"

const TileImageContribution* TileImageContribution::NONE                  = new TileImageContribution(false, 0);
const TileImageContribution* TileImageContribution::FULL_COVERAGE_OPAQUE  = new TileImageContribution(false, 1);

TileImageContribution* TileImageContribution::_lastFullCoverageTransparent = NULL;

const TileImageContribution* TileImageContribution::none() {
  return NONE;
}

const TileImageContribution* TileImageContribution::fullCoverageOpaque() {
  return FULL_COVERAGE_OPAQUE;
}

const TileImageContribution* TileImageContribution::fullCoverageTransparent(float alpha) {
  if (alpha <= 0.01) {
    return NONE;
  }

  // return TileImageContribution(true, alpha);
  if ((_lastFullCoverageTransparent == NULL) ||
      (_lastFullCoverageTransparent->_alpha != alpha)) {
    delete _lastFullCoverageTransparent;

    _lastFullCoverageTransparent = new TileImageContribution(true, alpha);
  }
  return _lastFullCoverageTransparent;
}

const TileImageContribution* TileImageContribution::partialCoverageOpaque(const Sector& sector) {
  return new TileImageContribution(sector, false, 1);
}

const TileImageContribution* TileImageContribution::partialCoverageTransparent(const Sector& sector,
                                                                               float alpha) {
  return (alpha <= 0.01) ? NONE : new TileImageContribution(sector, true, alpha);
}

void TileImageContribution::deleteContribution(const TileImageContribution* contribution) {
  if ((contribution != NULL) &&
      (contribution != NONE) &&
      (contribution != FULL_COVERAGE_OPAQUE) &&
      (contribution != _lastFullCoverageTransparent)) {
    delete contribution;
  }
}
