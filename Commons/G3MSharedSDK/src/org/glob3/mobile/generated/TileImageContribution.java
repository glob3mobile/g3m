package org.glob3.mobile.generated; 
//
//  TileImageContribution.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

//
//  TileImageContribution.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//


//enum TileImageContribution {
//  NONE,
//  FULL_COVERAGE_OPAQUE,
//  FULL_COVERAGE_TRANSPARENT,
//  PARTIAL_COVERAGE_OPAQUE,
//  PARTIAL_COVERAGE_TRANSPARENT
//};


public class TileImageContribution
{
  private static final TileImageContribution NONE = new TileImageContribution(false, 0);
  private static final TileImageContribution FULL_COVERAGE_OPAQUE = new TileImageContribution(false, 1);

//  static TileImageContribution* lastFullCoverageTransparent;

  private final boolean _isFullCoverage;
  private final Sector _sector ;
  private final boolean _isTransparent;
  private final float _alpha;

  private TileImageContribution(Sector sector, boolean isTransparent, float alpha)
  {
     _isFullCoverage = false;
     _sector = new Sector(sector);
     _isTransparent = isTransparent;
     _alpha = alpha;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileImageContribution operator =(TileImageContribution that);

  protected TileImageContribution(boolean isTransparent, float alpha)
  {
     _isFullCoverage = true;
     _sector = new Sector(Sector.fullSphere());
     _isTransparent = isTransparent;
     _alpha = alpha;
  }

  public TileImageContribution(TileImageContribution that)
  {
     _isFullCoverage = that._isFullCoverage;
     _sector = new Sector(that._sector);
     _isTransparent = that._isTransparent;
     _alpha = that._alpha;
  }


  //TileImageContribution* TileImageContribution::lastFullCoverageTransparent  = NULL;
  
  public static TileImageContribution none()
  {
    return NONE;
  }

  public static TileImageContribution fullCoverageOpaque()
  {
    return FULL_COVERAGE_OPAQUE;
  }

  public static TileImageContribution fullCoverageTransparent(float alpha)
  {
    if (alpha <= 0.01)
    {
      return NONE;
    }
    else
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO saves the last created alpha-contribution to try to reuse (&& avoid barbage)
      return new TileImageContribution(true, alpha);
  
      //  if ((lastFullCoverageTransparent == NULL) ||
      //      (lastFullCoverageTransparent->_alpha != alpha)) {
      //    delete lastFullCoverageTransparent;
      //
      //    lastFullCoverageTransparent = new TileImageContribution(true, alpha);
      //  }
      //
      //  return *lastFullCoverageTransparent;
    }
  }

  public static TileImageContribution partialCoverageOpaque(Sector sector)
  {
    return new TileImageContribution(sector, false, 1);
  }

  public static TileImageContribution partialCoverageTransparent(Sector sector, float alpha)
  {
    if (alpha <= 0.01)
    {
      return NONE;
    }
    else
    {
      return new TileImageContribution(sector, true, alpha);
    }
  }

  public final boolean isNone()
  {
    return (_alpha <= 0.01);
  }

  public final boolean isFullCoverageAndOpaque()
  {
    return _isFullCoverage && !_isTransparent && (_alpha >= 0.99);
  }

  public final boolean isFullCoverage()
  {
    return _isFullCoverage;
  }

//  const Sector getSector() const {
//    return _sector;
//  }

  public final boolean isOpaque()
  {
    return (_alpha >= 0.99);
  }

//  bool isTransparent() const {
//    return _isTransparent;
//  }

}