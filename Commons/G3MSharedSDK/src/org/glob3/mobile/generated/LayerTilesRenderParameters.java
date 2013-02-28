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
  public final int _splitsByLatitude;
  public final int _splitsByLongitude;
  public final int _maxLevel;
  public final Vector2I _tileTextureResolution;
  public final Vector2I _tileMeshResolution;
  public final boolean _mercator;

  public LayerTilesRenderParameters(Sector topSector, int splitsByLatitude, int splitsByLongitude, int maxLevel, Vector2I tileTextureResolution, Vector2I tileMeshResolution, boolean mercator)
  {
     _topSector = new Sector(topSector);
     _splitsByLatitude = splitsByLatitude;
     _splitsByLongitude = splitsByLongitude;
     _maxLevel = maxLevel;
     _tileTextureResolution = tileTextureResolution;
     _tileMeshResolution = tileMeshResolution;
     _mercator = mercator;

  }

  public static LayerTilesRenderParameters createDefaultNonMercator(Sector topSector)
  {
    final int splitsByLatitude = 4;
    final int splitsByLongitude = 8;
    final int maxLevel = 17;
    final Vector2I tileTextureResolution = new Vector2I(256, 256);
    final Vector2I tileMeshResolution = new Vector2I(16, 16);
    final boolean mercator = false;

    return new LayerTilesRenderParameters(topSector, splitsByLatitude, splitsByLongitude, maxLevel, tileTextureResolution, tileMeshResolution, mercator);
  }


  public void dispose()
  {
  }

}