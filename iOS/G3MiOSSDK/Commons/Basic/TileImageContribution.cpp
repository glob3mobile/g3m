//
//  TileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "TileImageContribution.hpp"

const TileImageContribution TileImageContribution::NONE                  = TileImageContribution(true, 0);
const TileImageContribution TileImageContribution::FULL_COVERAGE_OPAQUE  = TileImageContribution(false, 1);

//TileImageContribution* TileImageContribution::lastFullCoverageTransparent  = NULL;

const TileImageContribution TileImageContribution::none() {
  return NONE;
}

const TileImageContribution TileImageContribution::fullCoverageOpaque() {
  return FULL_COVERAGE_OPAQUE;
}

const TileImageContribution TileImageContribution::fullCoverageTransparent(float alpha) {
#warning TODO saves the last created alpha-contribution to try to reuse (and avoid barbage)
  return TileImageContribution(true, alpha);

//  if ((lastFullCoverageTransparent == NULL) ||
//      (lastFullCoverageTransparent->_alpha != alpha)) {
//    delete lastFullCoverageTransparent;
//
//    lastFullCoverageTransparent = new TileImageContribution(true, alpha);
//  }
//
//  return *lastFullCoverageTransparent;
}

const TileImageContribution TileImageContribution::partialCoverageOpaque(const Sector& sector) {
  return TileImageContribution(sector, false, 1);
}

const TileImageContribution TileImageContribution::partialCoverageTransparent(const Sector& sector,
                                                                              float alpha) {
  return TileImageContribution(sector, true, alpha);
}
