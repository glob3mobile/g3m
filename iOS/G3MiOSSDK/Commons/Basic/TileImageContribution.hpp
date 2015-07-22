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
    const Sector _sector;    //Sector within the tile where the contribution will be painted
    const bool   _isTransparent;
    
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
    _sector(Sector::NAN_SECTOR),
    _isTransparent(isTransparent),
    _alpha(alpha)
    {
    }
  
//  TileImageContribution(const Sector& sector,
//                        bool isFullCoverage,
//                        bool isTransparent,
//                        float alpha) :
//  _isFullCoverage(isFullCoverage),
//  _sector(sector),
//  _isTransparent(isTransparent),
//  _alpha(alpha)
//  {
//  }
  
    virtual ~TileImageContribution() {
#ifdef JAVA_CODE
        super.dispose();
#endif
    }
    
    TileImageContribution(const TileImageContribution& that) :
    _isFullCoverage(that._isFullCoverage),
    _sector(that._sector),
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
        return _isFullCoverage && !_isTransparent && isOpaque();
    }
    
    bool isFullCoverage() const {
        return _isFullCoverage;
    }
    
    const Sector* getSector() const {
        return &_sector;
    }
    
    bool isOpaque() const {
        return (_alpha >= 0.99);
    }
    
    bool isTransparent() const {
        return _isTransparent;
    }
  
};

#endif
