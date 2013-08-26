package org.glob3.mobile.generated; 
//
//  LayerTilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

//
//  LayerTilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//



public class LayerTilesRenderParameters
{
  public final Sector _topSector ;
  public final int _topSectorSplitsByLatitude;
  public final int _topSectorSplitsByLongitude;
  public final int _firstLevel;
  public final int _maxLevel;
  public final int _maxLevelForPoles;
  public final Vector2I _tileTextureResolution;
  public final Vector2I _tileMeshResolution;
  public final boolean _mercator;

  public LayerTilesRenderParameters(Sector topSector, int topSectorSplitsByLatitude, int topSectorSplitsByLongitude, int firstLevel, int maxLevel, Vector2I tileTextureResolution, Vector2I tileMeshResolution, boolean mercator)
  {
     _topSector = new Sector(topSector);
     _topSectorSplitsByLatitude = topSectorSplitsByLatitude;
     _topSectorSplitsByLongitude = topSectorSplitsByLongitude;
     _firstLevel = firstLevel;
     _maxLevel = maxLevel;
     _maxLevelForPoles = 4;
     _tileTextureResolution = tileTextureResolution;
     _tileMeshResolution = tileMeshResolution;
     _mercator = mercator;

  }

  public static Vector2I defaultTileMeshResolution()
  {
    return new Vector2I(16, 16);
  }

  public static Vector2I defaultTileTextureResolution ()
  {
    return new Vector2I(256, 256);
  }

  public static LayerTilesRenderParameters createDefaultNonMercator(Sector topSector)
  {
    final int topSectorSplitsByLatitude = 2;
    final int topSectorSplitsByLongitude = 4;
    final int firstLevel = 0;
    final int maxLevel = 17;
    final boolean mercator = false;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public static LayerTilesRenderParameters createDefaultMercator(int firstLevel, int maxLevel)
  {
    final Sector topSector = Sector.fullSphere();
    final int topSectorSplitsByLatitude = 1;
    final int topSectorSplitsByLongitude = 1;
    final boolean mercator = true;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public void dispose()
  {
  }

}