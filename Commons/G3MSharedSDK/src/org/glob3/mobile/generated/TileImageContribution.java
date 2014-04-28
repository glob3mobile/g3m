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



public class TileImageContribution
{
  private static final TileImageContribution FULL_COVERAGE_OPAQUE = new TileImageContribution(false, 1);

  private static TileImageContribution _lastFullCoverageTransparent = null;

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

  public void dispose()
  {
  }

  protected TileImageContribution(TileImageContribution that)
  {
     _isFullCoverage = that._isFullCoverage;
     _sector = new Sector(that._sector);
     _isTransparent = that._isTransparent;
     _alpha = that._alpha;
  }


  public static TileImageContribution fullCoverageOpaque()
  {
    return FULL_COVERAGE_OPAQUE;
  }

  public static TileImageContribution fullCoverageTransparent(float alpha)
  {
    if (alpha <= 0.01)
    {
      return null;
    }
  
    // try to reuse the contribution between calls to avoid too much garbage. Android, in your face!
    if ((_lastFullCoverageTransparent == null) || (_lastFullCoverageTransparent._alpha != alpha))
    {
      if (_lastFullCoverageTransparent != null)
         _lastFullCoverageTransparent.dispose();
  
      _lastFullCoverageTransparent = new TileImageContribution(true, alpha);
    }
    return _lastFullCoverageTransparent;
  }

  public static TileImageContribution partialCoverageOpaque(Sector sector)
  {
    return new TileImageContribution(sector, false, 1);
  }

  public static TileImageContribution partialCoverageTransparent(Sector sector, float alpha)
  {
    return (alpha <= 0.01) ? null : new TileImageContribution(sector, true, alpha);
  }

  public static void deleteContribution(TileImageContribution contribution)
  {
    if ((contribution != null) && (contribution != FULL_COVERAGE_OPAQUE) && (contribution != _lastFullCoverageTransparent))
    {
      if (contribution != null)
         contribution.dispose();
    }
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