package org.glob3.mobile.generated; 
//
//  TilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//



public class TilesRenderParameters
{
  public final Sector _topSector ;
  public final int _splitsByLatitude;
  public final int _splitsByLongitude;
  public final int _topLevel;
  public final int _maxLevel;
  public final Vector2I _tileTextureResolution = new Vector2I();
  public final Vector2I _tileMeshResolution = new Vector2I();
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceTopLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;

  public TilesRenderParameters(Sector topSector, int splitsByLatitude, int splitsByLongitude, int topLevel, int maxLevel, Vector2I tileTextureResolution, Vector2I tileMeshResolution, boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart, boolean incrementalTileQuality)
  {
     _topSector = new Sector(topSector);
     _splitsByLatitude = splitsByLatitude;
     _splitsByLongitude = splitsByLongitude;
     _topLevel = topLevel;
     _maxLevel = maxLevel;
     _tileTextureResolution = new Vector2I(tileTextureResolution);
     _tileMeshResolution = new Vector2I(tileMeshResolution);
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;

  }

  public void dispose()
  {
  }

  public static TilesRenderParameters createDefault(boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart, boolean incrementalTileQuality)
  {
    final int K = 1;
    //const int _TODO_RESET_K_TO_1 = 0;
    final int splitsByLatitude = 1 * K;
    final int splitsByLongitude = 2 * K;
    final int topLevel = 0;
    final int maxLevel = 17;
    final Vector2I tileTextureResolution = new Vector2I(256, 256);
    final Vector2I tileMeshResolution = new Vector2I(20, 20);

    return new TilesRenderParameters(Sector.fullSphere(), splitsByLatitude, splitsByLongitude, topLevel, maxLevel, tileTextureResolution, tileMeshResolution, renderDebug, useTilesSplitBudget, forceTopLevelTilesRenderOnStart, incrementalTileQuality);
  }


  public static TilesRenderParameters createSingleSector(boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart, boolean incrementalQuality)
  {
    final int splitsByLatitude = 1;
    final int splitsByLongitude = 1;
    final int topLevel = 0;
    final int maxLevel = 2;
    final Vector2I tileTextureResolution = new Vector2I(256, 256);
    final Vector2I tileMeshResolution = new Vector2I(20, 20);

    //    Sector sector = Sector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
    //                           Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
    Sector sector = new Sector(new Geodetic2D(Angle.zero(), Angle.zero()), new Geodetic2D(Angle.fromDegrees(90), Angle.fromDegrees(90)));

    return new TilesRenderParameters(sector, splitsByLatitude, splitsByLongitude, topLevel, maxLevel, tileTextureResolution, tileMeshResolution, renderDebug, useTilesSplitBudget, forceTopLevelTilesRenderOnStart, incrementalQuality);
  }

}