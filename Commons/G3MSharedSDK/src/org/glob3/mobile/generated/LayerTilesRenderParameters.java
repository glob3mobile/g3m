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


  public static LayerTilesRenderParameters createDefaultWGS84(Sector topSector, int firstLevel, int maxLevel)
  {
    final int topSectorSplitsByLatitude = 2;
    final int topSectorSplitsByLongitude = 4;
    final boolean mercator = false;

    return new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, LayerTilesRenderParameters.defaultTileTextureResolution(), LayerTilesRenderParameters.defaultTileMeshResolution(), mercator);
  }

  public static LayerTilesRenderParameters createDefaultWGS84(Sector topSector)
  {
    final int firstLevel = 0;
    final int maxLevel = 17;

    return createDefaultWGS84(topSector, firstLevel, maxLevel);
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