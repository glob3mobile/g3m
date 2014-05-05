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



public class TileImageContribution extends RCObject
{
  private static final TileImageContribution FULL_COVERAGE_OPAQUE = new TileImageContribution(false, 1);

  private static TileImageContribution _lastFullCoverageTransparent = null;

  private final boolean _isFullCoverage;
  private final Sector _sector ;
  private final boolean _isTransparent;

  private TileImageContribution(Sector sector, boolean isTransparent, float alpha)
  {
     _isFullCoverage = false;
     _sector = new Sector(sector);
     _isTransparent = isTransparent;
     _alpha = alpha;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileImageContribution operator =(TileImageContribution that);

  private void _retain()
  {
    super._retain();
  }

  private void _release()
  {
    super._release();
  }


  protected TileImageContribution(boolean isTransparent, float alpha)
  {
     _isFullCoverage = true;
     _sector = new Sector(Sector.fullSphere());
     _isTransparent = isTransparent;
     _alpha = alpha;
  }

  public void dispose()
  {
    System.out.printf("******* deleting contribution %p\n", this);
    super.dispose();
  }

  protected TileImageContribution(TileImageContribution that)
  {
     _isFullCoverage = that._isFullCoverage;
     _sector = new Sector(that._sector);
     _isTransparent = that._isTransparent;
     _alpha = that._alpha;
  }



  public final float _alpha;

  public static TileImageContribution fullCoverageOpaque()
  {
  //  FULL_COVERAGE_OPAQUE->_retain();
  //  return FULL_COVERAGE_OPAQUE;
  
    return new TileImageContribution(false, 1);
  }

  public static TileImageContribution fullCoverageTransparent(float alpha)
  {
    if (alpha <= 0.01)
    {
      return null;
    }
  
  //  // try to reuse the contribution between calls to avoid too much garbage. Android, in your face!
  //  if ((_lastFullCoverageTransparent == NULL) ||
  //      (_lastFullCoverageTransparent->_alpha != alpha)) {
  ////    delete _lastFullCoverageTransparent;
  //    if (_lastFullCoverageTransparent != NULL) {
  //      _lastFullCoverageTransparent->_release();
  //    }
  //
  //    _lastFullCoverageTransparent = new TileImageContribution(true, alpha);
  //  }
  //  _lastFullCoverageTransparent->_retain();
  //  return _lastFullCoverageTransparent;
  
    return new TileImageContribution(true, alpha);
  }

  public static TileImageContribution partialCoverageOpaque(Sector sector)
  {
    return new TileImageContribution(sector, false, 1);
  }

  public static TileImageContribution partialCoverageTransparent(Sector sector, float alpha)
  {
    return (alpha <= 0.01) ? null : new TileImageContribution(sector, true, alpha);
  }

  public static void retainContribution(TileImageContribution contribution)
  {
    if (contribution != null)
    {
      System.out.printf("**** retaining contribution %p\n", contribution);
      contribution._retain();
    }
  }
  public static void releaseContribution(TileImageContribution contribution)
  {
  //  if ((contribution != NULL) &&
  //      (contribution != FULL_COVERAGE_OPAQUE) &&
  //      (contribution != _lastFullCoverageTransparent)) {
  ///#warning remove debug pring
  //    printf("**** deleting contribution %p\n", contribution);
  //    delete contribution;
  //  }
    if (contribution != null)
    {
      System.out.printf("**** releasing contribution %p\n", contribution);
      contribution._release();
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

  public final Sector getSector()
  {
      return _sector;
  }

  public final boolean isOpaque()
  {
    return (_alpha >= 0.99);
  }

  //  bool isTransparent() const {
  //    return _isTransparent;
  //  }

}