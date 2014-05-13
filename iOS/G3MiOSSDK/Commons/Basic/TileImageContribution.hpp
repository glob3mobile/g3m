//
//  TileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#ifndef __G3MiOSSDK__TileImageContribution__
#define __G3MiOSSDK__TileImageContribution__

#include "Sector.hpp"
#include "RCObject.hpp"

class TileImageContribution : public RCObject {
private:
  static const TileImageContribution* FULL_COVERAGE_OPAQUE;

  static TileImageContribution* _lastFullCoverageTransparent;

  const bool   _isFullCoverage;
  const Sector _destinationSector;
  const bool   _isTransparent;

  TileImageContribution(const Sector& destinationSector,
                        bool isTransparent,
                        float alpha) :
  _isFullCoverage(false),
  _destinationSector(destinationSector),
  _isTransparent(isTransparent),
  _alpha(alpha)
  {
  }

  TileImageContribution& operator=(const TileImageContribution& that);

//  void _retain() const {
//    RCObject::_retain();
//  }
//
//  void _release() const {
//    RCObject::_release();
//  }


protected:
  TileImageContribution(bool isTransparent,
                        float alpha) :
  _isFullCoverage(true),
  _destinationSector(Sector::NAN_SECTOR),
  _isTransparent(isTransparent),
  _alpha(alpha)
  {
  }

  virtual ~TileImageContribution() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  TileImageContribution(const TileImageContribution& that) :
  _isFullCoverage(that._isFullCoverage),
  _destinationSector(that._destinationSector),
  _isTransparent(that._isTransparent),
  _alpha(that._alpha)
  {
  }


public:

  const float  _alpha;

  static const TileImageContribution* fullCoverageOpaque();

  static const TileImageContribution* fullCoverageTransparent(float alpha);

  static const TileImageContribution* partialCoverageOpaque(const Sector& sector);

  static const TileImageContribution* partialCoverageTransparent(const Sector& sector,
                                                                 float alpha);

  static void retainContribution(const TileImageContribution* contribution);
  static void releaseContribution(const TileImageContribution* contribution);

  bool isFullCoverageAndOpaque() const {
    return _isFullCoverage && !_isTransparent && (_alpha >= 0.99);
  }

  bool isFullCoverage() const {
    return _isFullCoverage;
  }

  const Sector* getDestinationSector() const {
      return &_destinationSector;
  }

  bool isOpaque() const {
    return (_alpha >= 0.99);
  }

  //  bool isTransparent() const {
  //    return _isTransparent;
  //  }
  
};

#endif
