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
  private static final TileImageContribution NONE = new TileImageContribution(true, 0);
  private static final TileImageContribution FULL_COVERAGE_OPAQUE = new TileImageContribution(false, 1);

  //  static TileImageContribution* lastFullCoverageTransparent;


  private final Sector _sector ;
  private final boolean _transparent;
  private final float _alpha;

  private TileImageContribution(boolean transparent, float alpha)
  {
     _sector = new Sector(Sector.fullSphere());
     _transparent = transparent;
     _alpha = alpha;
  }

  private TileImageContribution(Sector sector, boolean transparent, float alpha)
  {
     _sector = new Sector(sector);
     _transparent = transparent;
     _alpha = alpha;
  }

  public TileImageContribution(TileImageContribution that)
  {
     _sector = new Sector(that._sector);
     _transparent = that._transparent;
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

  public static TileImageContribution partialCoverageOpaque(Sector sector)
  {
    return new TileImageContribution(sector, false, 1);
  }

  public static TileImageContribution partialCoverageTransparent(Sector sector, float alpha)
  {
    return new TileImageContribution(sector, true, alpha);
  }

  public final boolean isNone()
  {
    return (_alpha == 0);
  }

}