package org.glob3.mobile.generated;
//
//  LayerTilesRenderParameters.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//

//
//  LayerTilesRenderParameters.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/25/13.
//
//




public class LayerTilesRenderParameters
{

  /*
   return ( topSectorSplitsByLatitude, topSectorSplitsByLongitude )
   */
  private static Vector2S calculateTopSectorSplitsParametersWGS84(Sector topSector)
  {
    final double maxTile = 90;
    double sLat;
    double sLon;
  
    final double ratio = topSector._deltaLatitude.div(topSector._deltaLongitude);
    if (ratio > 1)
    {
      sLat = ratio;
      sLon = 1;
    }
    else
    {
      sLat = 1;
      sLon = 1 / ratio;
    }
  
    final double tileDeltaLat = topSector._deltaLatitude.div(sLat)._degrees;
    final double factorLat = tileDeltaLat / maxTile;
  
    final double factor = (factorLat < 1) ? 1 : factorLat;
    return new Vector2S((short)((sLat * factor) + 0.5), (short)((sLon * factor) + 0.5));
  }
  public final Sector _topSector ;
  public final int _topSectorSplitsByLatitude;
  public final int _topSectorSplitsByLongitude;
  public final int _firstLevel;
  public final int _maxLevel;
  public final int _maxLevelForPoles;
  public final Vector2S _tileTextureResolution;
  public final Vector2S _tileMeshResolution;
  public final boolean _mercator;

  public LayerTilesRenderParameters(Sector topSector, int topSectorSplitsByLatitude, int topSectorSplitsByLongitude, int firstLevel, int maxLevel, Vector2S tileTextureResolution, Vector2S tileMeshResolution, boolean mercator)
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

  public static Vector2S defaultTileMeshResolution()
  {
    //return Vector2S((short)16, (short)16);
    return new Vector2S((short)32, (short)32);
  }

  public static Vector2S defaultTileTextureResolution ()
  {
    return new Vector2S((short)256, (short)256);
  }


  public static LayerTilesRenderParameters createDefaultWGS84(int firstLevel, int maxLevel)
  {
    return createDefaultWGS84(Sector.FULL_SPHERE, firstLevel, maxLevel);
  }

  public static LayerTilesRenderParameters createDefaultWGS84(Sector topSector, int firstLevel, int maxLevel)
  {
    final Vector2S splitsParameters = calculateTopSectorSplitsParametersWGS84(topSector);
    final int topSectorSplitsByLatitude = splitsParameters._x;
    final int topSectorSplitsByLongitude = splitsParameters._y;
    final boolean mercator = false;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public static LayerTilesRenderParameters createDefaultWGS84(Sector topSector, int topSectorSplitsByLatitude, int topSectorSplitsByLongitude, int firstLevel, int maxLevel)
  {
    final boolean mercator = false;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public static LayerTilesRenderParameters createDefaultMercator(int firstLevel, int maxLevel)
  {
    final Sector topSector = Sector.FULL_SPHERE;
    final int topSectorSplitsByLatitude = 1;
    final int topSectorSplitsByLongitude = 1;
    final boolean mercator = true;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public static java.util.ArrayList<LayerTilesRenderParameters> createDefaultMultiProjection(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel)
  {
     return createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, wgs84firstLevel, 18);
  }
  public static java.util.ArrayList<LayerTilesRenderParameters> createDefaultMultiProjection(int mercatorFirstLevel, int mercatorMaxLevel)
  {
     return createDefaultMultiProjection(mercatorFirstLevel, mercatorMaxLevel, 0, 18);
  }
  public static java.util.ArrayList<LayerTilesRenderParameters> createDefaultMultiProjection(int mercatorFirstLevel)
  {
     return createDefaultMultiProjection(mercatorFirstLevel, 18, 0, 18);
  }
  public static java.util.ArrayList<LayerTilesRenderParameters> createDefaultMultiProjection()
  {
     return createDefaultMultiProjection(2, 18, 0, 18);
  }
  public static java.util.ArrayList<LayerTilesRenderParameters> createDefaultMultiProjection(int mercatorFirstLevel, int mercatorMaxLevel, int wgs84firstLevel, int wgs84maxLevel)
  {
    final java.util.ArrayList<LayerTilesRenderParameters> result = new java.util.ArrayList<LayerTilesRenderParameters>();
    result.add(LayerTilesRenderParameters.createDefaultWGS84(wgs84firstLevel, wgs84maxLevel)); // WGS84 tiles-pyramid layout is preferred
    result.add(LayerTilesRenderParameters.createDefaultMercator(mercatorFirstLevel, mercatorMaxLevel));
    return result;
  }

  public void dispose()
  {
  }


  public final boolean isEquals(LayerTilesRenderParameters that)
  {
    if (that == null)
    {
      return false;
    }
  
    if (!(_topSector.isEquals(that._topSector)))
    {
      return false;
    }
  
    if (_topSectorSplitsByLatitude != that._topSectorSplitsByLatitude)
    {
      return false;
    }
  
    if (_topSectorSplitsByLongitude != that._topSectorSplitsByLongitude)
    {
      return false;
    }
  
    if (_firstLevel != that._firstLevel)
    {
      return false;
    }
  
    if (_maxLevel != that._maxLevel)
    {
      return false;
    }
  
    if (_maxLevelForPoles != that._maxLevelForPoles)
    {
      return false;
    }
  
    if (!_tileTextureResolution.isEquals(that._tileTextureResolution))
    {
      return false;
    }
  
    if (!_tileMeshResolution.isEquals(that._tileMeshResolution))
    {
      return false;
    }
  
    if (_mercator != that._mercator)
    {
      return false;
    }
  
    return true;
  }

  public final LayerTilesRenderParameters copy()
  {
    return new LayerTilesRenderParameters(_topSector, _topSectorSplitsByLatitude, _topSectorSplitsByLongitude, _firstLevel, _maxLevel, _tileTextureResolution, _tileMeshResolution, _mercator);
  }

}