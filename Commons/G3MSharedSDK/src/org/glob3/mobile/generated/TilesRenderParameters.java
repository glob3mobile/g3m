

package org.glob3.mobile.generated;

//
// TilesRenderParameters.cpp
// G3MiOSSDK
//
// Created by Diego Gomez Deck on 08/08/12.
//
//

//
// TilesRenderParameters.hpp
// G3MiOSSDK
//
// Created by Diego Gomez Deck on 08/08/12.
//
//


public class TilesRenderParameters {
  public final Sector _topSector;
  public final int _splitsByLatitude;
  public final int _splitsByLongitude;
  public final int _topLevel;
  public final int _maxLevel;
  public final int _tileTextureHeight;
  public final int _tileTextureWidth;
  public final int _tileResolution;
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceTopLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;


  public TilesRenderParameters(final Sector topSector,
                               final int splitsByLatitude,
                               final int splitsByLongitude,
                               final int topLevel,
                               final int maxLevel,
                               final int tileTextureHeight,
                               final int tileTextureWidth,
                               final int tileResolution,
                               final boolean renderDebug,
                               final boolean useTilesSplitBudget,
                               final boolean forceTopLevelTilesRenderOnStart,
                               final boolean incrementalTileQuality) {
    _topSector = new Sector(topSector);
    _splitsByLatitude = splitsByLatitude;
    _splitsByLongitude = splitsByLongitude;
    _topLevel = topLevel;
    _maxLevel = maxLevel;
    _tileTextureHeight = tileTextureHeight;
    _tileTextureWidth = tileTextureWidth;
    _tileResolution = tileResolution;
    _renderDebug = renderDebug;
    _useTilesSplitBudget = useTilesSplitBudget;
    _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
    _incrementalTileQuality = incrementalTileQuality;

  }


  public void dispose() {
  }


  public static TilesRenderParameters createDefault(final boolean renderDebug,
                                                    final boolean useTilesSplitBudget,
                                                    final boolean forceTopLevelTilesRenderOnStart,
                                                    final boolean incrementalTileQuality) {
    final int K = 1;
    // const int _TODO_RESET_K_TO_1 = 0;
    final int splitsByLatitude = 2 * K;
    final int splitsByLongitude = 4 * K;
    final int topLevel = 0;
    final int maxLevel = 17;
    final int tileTextureHeight = 256;
    final int tileTextureWidth = 256;
    // const int tileTextureHeight = 128;
    // const int tileTextureWidth = 128;
    final int tileResolution = 10;

    return new TilesRenderParameters(Sector.fullSphere(), splitsByLatitude,
        splitsByLongitude, topLevel, maxLevel, tileTextureHeight,
        tileTextureWidth, tileResolution, renderDebug, useTilesSplitBudget,
        forceTopLevelTilesRenderOnStart, incrementalTileQuality);
  }


  public static TilesRenderParameters createTileMillSector(final boolean renderDebug,
                                                           final boolean useTilesSplitBudget,
                                                           final boolean forceTopLevelTilesRenderOnStart,
                                                           final boolean incrementalTileQuality) {
    final int K = 1;
    // const int _TODO_RESET_K_TO_1 = 0;
    final int splitsByLatitude = 2 * K;
    final int splitsByLongitude = 2 * K;
    final int topLevel = 0;
    final int maxLevel = 17;
    final int tileTextureHeight = 256;
    final int tileTextureWidth = 256;
    // const int tileTextureHeight = 128;
    // const int tileTextureWidth = 128;
    final int tileResolution = 10;

    return new TilesRenderParameters(Sector.fullSphere(), splitsByLatitude,
        splitsByLongitude, topLevel, maxLevel, tileTextureHeight,
        tileTextureWidth, tileResolution, renderDebug, useTilesSplitBudget,
        forceTopLevelTilesRenderOnStart, incrementalTileQuality);
  }


  public static TilesRenderParameters createSingleSector(final boolean renderDebug,
                                                         final boolean useTilesSplitBudget,
                                                         final boolean forceTopLevelTilesRenderOnStart,
                                                         final boolean incrementalQuality) {
    final int splitsByLatitude = 1;
    final int splitsByLongitude = 1;
    final int topLevel = 0;
    final int maxLevel = 2;
    final int tileTextureHeight = 256;
    final int tileTextureWidth = 256;
    final int tileResolution = 10;

    // Sector sector = Sector(Geodetic2D(Angle::fromDegrees(-90),
    // Angle::fromDegrees(-180)),
    // Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
    final Sector sector = new Sector(
        new Geodetic2D(Angle.zero(), Angle.zero()), new Geodetic2D(
            Angle.fromDegrees(90), Angle.fromDegrees(90)));


    return new TilesRenderParameters(sector, splitsByLatitude,
        splitsByLongitude, topLevel, maxLevel, tileTextureHeight,
        tileTextureWidth, tileResolution, renderDebug, useTilesSplitBudget,
        forceTopLevelTilesRenderOnStart, incrementalQuality);

  }
}
