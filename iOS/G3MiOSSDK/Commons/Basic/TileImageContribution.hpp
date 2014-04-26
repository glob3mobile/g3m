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
  static const TileImageContribution* NONE;
  static const TileImageContribution* FULL_COVERAGE_OPAQUE;

  static TileImageContribution* _lastFullCoverageTransparent;

  const bool   _isFullCoverage;
  const Sector _sector;
  const bool   _isTransparent;
  const float  _alpha;

  TileImageContribution(const Sector& sector,
                        bool isTransparent,
                        float alpha) :
  _isFullCoverage(false),
  _sector(sector),
  _isTransparent(isTransparent),
  _alpha(alpha)
  {
  }

  TileImageContribution& operator=(const TileImageContribution& that);

protected:
  TileImageContribution(bool isTransparent,
                        float alpha) :
  _isFullCoverage(true),
  _sector(Sector::fullSphere()),
  _isTransparent(isTransparent),
  _alpha(alpha)
  {
  }

  virtual ~TileImageContribution() {
  }

public:
  TileImageContribution(const TileImageContribution& that) :
  _isFullCoverage(that._isFullCoverage),
  _sector(that._sector),
  _isTransparent(that._isTransparent),
  _alpha(that._alpha)
  {
  }


  static const TileImageContribution* none();

  static const TileImageContribution* fullCoverageOpaque();

  static const TileImageContribution* fullCoverageTransparent(float alpha);

  static const TileImageContribution* partialCoverageOpaque(const Sector& sector);

  static const TileImageContribution* partialCoverageTransparent(const Sector& sector,
                                                                 float alpha);

  static void deleteContribution(const TileImageContribution* contribution);

  bool isNone() const {
    return (_alpha <= 0.01);
  }

  bool isFullCoverageAndOpaque() const {
    return _isFullCoverage && !_isTransparent && (_alpha >= 0.99);
  }

  bool isFullCoverage() const {
    return _isFullCoverage;
  }

  //  const Sector getSector() const {
  //    return _sector;
  //  }

  bool isOpaque() const {
    return (_alpha >= 0.99);
  }

  //  bool isTransparent() const {
  //    return _isTransparent;
  //  }
  
};

#endif
