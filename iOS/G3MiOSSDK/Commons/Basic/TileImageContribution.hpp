//
//  TileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#ifndef __G3MiOSSDK__TileImageContribution__
#define __G3MiOSSDK__TileImageContribution__

//enum TileImageContribution {
//  NONE,
//  FULL_COVERAGE_OPAQUE,
//  FULL_COVERAGE_TRANSPARENT,
//  PARTIAL_COVERAGE_OPAQUE,
//  PARTIAL_COVERAGE_TRANSPARENT
//};

#include "Sector.hpp"

class TileImageContribution {
private:
  static const TileImageContribution NONE;
  static const TileImageContribution FULL_COVERAGE_OPAQUE;

  //  static TileImageContribution* lastFullCoverageTransparent;


  const Sector _sector;
  const bool   _transparent;
  const float  _alpha;

  TileImageContribution(bool transparent,
                        float alpha) :
  _sector(Sector::fullSphere()),
  _transparent(transparent),
  _alpha(alpha)
  {
  }

  TileImageContribution(const Sector& sector,
                        bool transparent,
                        float alpha) :
  _sector(sector),
  _transparent(transparent),
  _alpha(alpha)
  {
  }

public:
  TileImageContribution(const TileImageContribution& that) :
  _sector(that._sector),
  _transparent(that._transparent),
  _alpha(that._alpha)
  {
  }

  static const TileImageContribution none();

  static const TileImageContribution fullCoverageOpaque();

  static const TileImageContribution fullCoverageTransparent(float alpha);

  static const TileImageContribution partialCoverageOpaque(const Sector& sector);

  static const TileImageContribution partialCoverageTransparent(const Sector& sector,
                                                                float alpha);

  bool isNone() const {
    return (_alpha == 0);
  }
  
};

#endif
